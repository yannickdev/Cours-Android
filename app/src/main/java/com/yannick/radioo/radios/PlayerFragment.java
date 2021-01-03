package com.yannick.radioo.radios;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yannick.radioo.Favourite;
import com.yannick.radioo.Podcast;
import com.yannick.radioo.PodcastDAO;
import com.yannick.radioo.R;
import com.yannick.radioo.Station;
import com.yannick.radioo.StationDAO;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class PlayerFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{

    private TextView mTextViewRecordingState;
    private InputStream input;
    private long startTime;
    private SpeechRecognizer sr;
    private static final String TAG = "SpeechListener playing";
    private String saveFilePath;
    private String saveFaviconPath;
    private boolean isRecording = false;

    private Menu menu;

    private OutputStream outputStream;

    private static final int BUFFER_SIZE = 4096;

    private Context context;

    private MediaPlayer mPlayer;

    private Station station;
    private Podcast podcast;
    private Favourite favourite;

    private OnFragmentInteractionListener mListener;

    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    public PlayerFragment() {
    }

    public static PlayerFragment newInstance(Station station) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putParcelable("STATION", station);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        context=this.getActivity();


//        if(mPlayer!=null&&mPlayer.isPlaying()){
//            mPlayer.stop();
//        }

        if (getArguments() != null) {
            stop();
            mPlayer = new MediaPlayer();
            station = getArguments().getParcelable("STATION");
            System.out.println("STATION ON CREATE: "+station);
            podcast = getArguments().getParcelable("PODCAST");
            System.out.println("PODCAST ON CREATE: "+station);
            favourite = getArguments().getParcelable("FAVOURITE");
            System.out.println("FAVOURITE ON CREATE: "+station);

            if(station!=null){

                System.out.println("ok entre station");
                play();
            }

            else if(podcast!=null){
                System.out.println("ok entre podcast");
                play();
            }

            else if(favourite!=null){
                System.out.println("ok entre favourite");
                play();
            }
        }

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                switch(what){

                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        // handle MEDIA_ERROR_UNKNOWN, optionally handle extras
                        handleExtras(extra);
                        break;

                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        // handle MEDIA_ERROR_SERVER_DIED, optionally handle extras
                        handleExtras(extra);
                        break;
                }

                return true;
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        this.menu = menu;
        inflater.inflate(R.menu.bottom_nav_menu, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        BottomNavigationView toolbar = rootView.findViewById(R.id.bottom_navigation);
        toolbar.setOnNavigationItemSelectedListener(this);

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mute:
                try {
                    togglePlayPause(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.record:
                try {
                    toggleRecordStop(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.favourite:

                System.out.println("ok entre favourite");
                System.out.println(station);
                PlayerFragment.DownloadImageStreamTask downloadTask = new PlayerFragment.DownloadImageStreamTask();
                //bug: quand on tente de mettre un station podcastée en favori

                downloadTask.execute(new String[] { station.getFavicon() });
                Log.v("AddFavouriteTask",station.getStationuuid());

                StationDAO datasource = new StationDAO(context);
                datasource.open();
                Log.v("saveFaviconPath: ",""+saveFaviconPath);
                // PlayingActivity.this.station.setFavicon(saveFaviconPath);


                Favourite f = new Favourite(station);
                if(!datasource.existInDb(f)){
                    datasource.create(f);
                }
                return true;
        }
        return false;
    }

    private class RecordAudioStreamTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            startTime= System.currentTimeMillis();

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        File directory = new File(context.getFilesDir()+File.separator+"MyPodcasts");

                        if(!directory.exists()){
                            directory.mkdir();
                        }

                        try {

                            String path=context.getFilesDir()+File.separator+"MyPodcasts";//+File.separator+station.getName()+(new Date()).toString()+"."+station.getCodec().toLowerCase()

                            String streamURL=station.getUrl();
                            URL url = new URL(streamURL);
                            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                            int responseCode = httpConn.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                String fileName = "";
                                String disposition = httpConn.getHeaderField("Content-Disposition");

                                if (disposition != null) {
                                    int index = disposition.indexOf("filename=");
                                    if (index > 0) {
                                        fileName = disposition.substring(index + 10, disposition.length() - 1);
                                    }
                                } else {
                                    fileName = streamURL.substring(streamURL.lastIndexOf("/") + 1, streamURL.length());
                                }

                                saveFilePath = path + File.separator + fileName+getNow()+"."+station.getCodec();
                                if(input!=null) input.close();
                                input = httpConn.getInputStream();

                                outputStream = new FileOutputStream(saveFilePath);

                                int bytesRead = -1;
                                byte[] buffer = new byte[BUFFER_SIZE];
                                while ((input!=null)&&(bytesRead = input.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                                System.out.println("File downloaded");
                            } else {
                                System.out.println("No stream. Server replied HTTP code: " + responseCode);
                            }
                            httpConn.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            return "recording...";

        }
        @Override
        protected void onPostExecute(String result) {
           // mTextViewRecordingState.setText(result);
            isRecording = true;
        }
    }

    private class StopRecordAudioStreamTask extends AsyncTask<String, Void, String> {
        long duration =  System.currentTimeMillis() - startTime;
        @Override
        protected String doInBackground(String... urls) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {

                        Log.v("duration",""+duration);
                        PodcastDAO datasource = new PodcastDAO(context);
                        datasource.open();

                        podcast= new Podcast();
                        podcast.setId(0);
                        podcast.setTitle(station.getName()+(new Date()).toString()+"."+station.getCodec().toLowerCase());
                        podcast.setStation(null);
                        podcast.setDate(new Date().toString());
                        podcast.setFileUrl(saveFilePath);
                        if(saveFaviconPath==null)saveFaviconPath ="";
                        podcast.setFaviconUrl(saveFaviconPath);
                        podcast.setDuration(duration);
                        datasource.create(podcast);

                        try {
                            outputStream.close();
                          //  input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("File downloaded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            return "stop recording";
        }
        @Override
        protected void onPostExecute(String result) {
            Log.v("RESULT",result);
            //mTextViewRecordingState.setText(result);
        }
    }

    private class DownloadImageStreamTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        File directory = new File(context.getFilesDir()+File.separator+"MyStationImages");

                        if(!directory.exists()){
                            directory.mkdir();
                        }

                        Log.v("url",directory.getAbsolutePath());

                        try {
                            String path=context.getFilesDir()+File.separator+"MyStationImages";
                            Log.v("station",""+station.getFavicon());
                            String[] parts =station.getFavicon().split("/");
                            String fileName = parts[parts.length-1];

                            BufferedInputStream in;
                           // if(station.getFavicon()==null)
                               // in = new BufferedInputStream(new URL("http://www."+station.getFavicon()).openStream());
                            //else
                                in = new BufferedInputStream(new URL(station.getFavicon()).openStream());


                            saveFaviconPath = path + File.separator + fileName;
                            FileOutputStream fileOutputStream = new FileOutputStream(saveFaviconPath);
                            Log.v("filename",fileName);
                            byte dataBuffer[] = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                                fileOutputStream.write(dataBuffer, 0, bytesRead);
                            }
                            //in.close();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            return "image downloaded...";

        }
        @Override
        protected void onPostExecute(String result) {
            Log.v("result",result);
            //mTextViewRecordingState.setText(result);
        }
    }

    private class AddFavouriteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {

                    PlayerFragment.DownloadImageStreamTask task = new PlayerFragment.DownloadImageStreamTask();
                    task.execute(new String[] { station.getFavicon() });

                }
            });

            thread.start();

            return "stop recording";
        }

        @Override
        protected void onPostExecute(String result) {

            Station s= new Station();
            s.setStationuuid("0");
            s.setName(station.getName()+(new Date()).toString()+"."+station.getCodec().toLowerCase());
            s.setFavicon(saveFilePath);
            s.setCountry(station.getCountry());
            s.setUrl(station.getUrl());
            s.setCountrycode(station.getCountrycode());
            s.setName(station.getName());
            s.setState(station.getState());
            s.setCodec(station.getCodec());
            s.setVotes(station.getVotes());

            Favourite f = new Favourite(s);

            StationDAO datasource = new StationDAO(context);
            datasource.open();
            Log.v("saveFaviconPath: ",""+saveFaviconPath);

            datasource.create(f);

           // mTextViewRecordingState.setText(result);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+ " must implement OnFragmentInteractionListener");
        }
    }

    public static String getNow() {
        String formattedDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
                Locale.US);
        formattedDate = sdf.format(new Date());
        return formattedDate;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stop();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void stop(){
        if(mPlayer!=null&&mPlayer.isPlaying()){
            mPlayer.stop();
        }
    }
    public void play(){
        try {
            String audioUrl =null;
            if(station!=null){
                audioUrl = station.getUrl();
            }
            else if(favourite!=null){
                audioUrl = favourite.getUrl();
            }
            else if(podcast!=null){
                audioUrl =podcast.getFileUrl();
                Log.v("podcast",audioUrl);
            }

           // if(mPlayer==null) mPlayer = new MediaPlayer();
        if(audioUrl!=null){
            mPlayer.reset();
            mPlayer.setDataSource(audioUrl);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayer.start();
                }
            });
        }


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void togglePlayPause(MenuItem item) throws IOException {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                stop();
                item.setIcon(R.drawable.ic_play_circle_outline_black_24dp);
            } else {
                play();
                item.setIcon(R.drawable.ic_pause_circle_outline_black_24dp);
            }
        }
    }

    private void toggleRecordStop(MenuItem item) throws IOException {
        Log.v("togglePlayPause ","entre");

        if (!isRecording) {
            item.setIcon(R.drawable.ic_stop_black_24dp);
            //System.out.println("station.getFavicon()"+station.getFavicon());
            if(station!=null){
                PlayerFragment.RecordAudioStreamTask audiotask = new PlayerFragment.RecordAudioStreamTask();
                audiotask.execute(new String[] { station.getUrl() });

                if(!station.getFavicon().isEmpty()){
                    PlayerFragment.DownloadImageStreamTask task = new PlayerFragment.DownloadImageStreamTask();
                    task.execute(new String[] { station.getFavicon() });
                }

            }


        } else {
            item.setIcon(R.drawable.ic_fiber_manual_record_red_24dp);
            PlayerFragment.StopRecordAudioStreamTask audiotask = new PlayerFragment.StopRecordAudioStreamTask();
            audiotask.execute(new String[] { station.getUrl() });

        }
    }



    private void handleExtras(int extra){
        switch(extra){
            case MediaPlayer.MEDIA_ERROR_IO:
                // handle MEDIA_ERROR_IO
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                // handle MEDIA_ERROR_MALFORMED
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                // handle MEDIA_ERROR_UNSPECIFIED
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                // handle MEDIA_ERROR_TIMED_OUT
                break;

        }
    }

    public void initializeTimerTask() {

         timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //get the current timeStamp
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());

                        //show the toast
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, strDate, duration);
                        toast.show();
                    }
                });
            }
        };
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 10000); //
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
