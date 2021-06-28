package com.yannick.radioo.radios;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yannick.radioo.Favourite;
import com.yannick.radioo.Podcast;
import com.yannick.radioo.PodcastDAO;
import com.yannick.radioo.R;
import com.yannick.radioo.ScheduledStation;
import com.yannick.radioo.ScheduledStationDAO;
import com.yannick.radioo.Station;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.yannick.radioo.radios.PlayerFragment.getNow;


public class ScheduledStationsFragment extends Fragment {

    PendingIntent myPendingIntent;
    PendingIntent myStopingIntent;
    Intent intent;
    AlarmManager alarmManager;
    AlarmManager alarmManagerStop;
    BroadcastReceiver myBroadcastReceiver;
    BroadcastReceiver myStopinBroadcastReceiver;
    Calendar firingCal;
    Calendar stopingCal;

    //  List<ScheduledStation>

    private boolean isRecording = false;
    private InputStream input;
    private OutputStream outputStream;

    private static final int BUFFER_SIZE = 4096;
    private long startTime;

    //private Station station;
    private Podcast podcast;
    private Favourite favourite;

    private String saveFilePath;
    private String saveFaviconPath;


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Context context;

    ScheduledStation scheduledStation;

    private ScheduledStationDAO datasource;

    List<ScheduledStation> lp;

    ScheduledStationsRecyclerViewAdapter adapter;


    public ScheduledStationsFragment() {
    }

    public static ScheduledStationsFragment newInstance(int columnCount) {
        ScheduledStationsFragment fragment = new ScheduledStationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        scheduledStation = new ScheduledStation();
//        scheduledStation.setUrl("http://icecast4.play.cz/crojazz256.mp3");
//        scheduledStation.setName("jazz scheduled");
//        scheduledStation.setCodec("MP3");
//        scheduledStation.setFavicon("http://www.0nradio.com/images/favicon/mstile-144x144.png");
        //scheduledStation.setStartDate();


//        scheduledStation = new ScheduledStation();
//        scheduledStation.setUrl("http://ibadat.out.airtime.pro:8000/ibadat_a");
//        scheduledStation.setName("hindi scheduled");
//        scheduledStation.setCodec("MP3");
//        scheduledStation.setFavicon("http://www.2bradioindia.com/wp-content/uploads/edd/2018/02/maa.png");

//        scheduledStation = new ScheduledStation();
//        scheduledStation.setUrl("http://radio2bindia.out.airtime.pro:8000/radio2bindia_a");
//        scheduledStation.setName("sangam radio");
//        scheduledStation.setCodec("MP3");
//        scheduledStation.setFavicon("http://www.2bradioindia.com/wp-content/uploads/edd/2018/02/ganesha.png");

        scheduledStation = new ScheduledStation();
        scheduledStation.setUrl("http://icecast.radiofrance.fr/franceculture-midfi.mp3");
        scheduledStation.setName("france culture scheduled");
        scheduledStation.setCodec("MP3");
        scheduledStation.setFavicon("https://www.franceculture.fr//favicons/android-icon-192x192.png");

        context=this.getContext();
        datasource = new ScheduledStationDAO(context);
        datasource.open();

        //intent = new Intent(this.context, BroadcastReceiver.class);
        //Intent broadcast_intent = new Intent(this, AlarmBroadcastReceiver.class);

        podcast= new Podcast();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

//        firingCal= Calendar.getInstance();
//        firingCal.set(Calendar.HOUR, 2); // At the hour you want to fire the alarm
//        firingCal.set(Calendar.MINUTE, 17); // alarm minute
//        firingCal.set(Calendar.SECOND, 0); // and alarm second
//        long intendedTime = firingCal.getTimeInMillis();
//
//        stopingCal= Calendar.getInstance();
//        stopingCal.set(Calendar.HOUR, 2); // At the hour you want to fire the alarm
//        stopingCal.set(Calendar.MINUTE,18); // alarm minute
//        stopingCal.set(Calendar.SECOND, 0); // and alarm second
//        long intendedStopTime = stopingCal.getTimeInMillis();
//
//        registerMyAlarmBroadcast();
//        registerMyStopingAlarmBroadcast();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, intendedTime, myPendingIntent);
//            alarmManagerStop.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, intendedStopTime, myStopingIntent);        }
//        else{
//            alarmManager.set(AlarmManager.RTC_WAKEUP, intendedTime, myPendingIntent);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, intendedStopTime, myStopingIntent);
//        }
    }

//    private void registerMyAlarmBroadcast()
//    {
//        Log.i(TAG, "Going to register Intent.RegisterAlramBroadcast");
//
//        //This is the call back function(BroadcastReceiver) which will be call when your
//        //alarm time will reached.
//        myBroadcastReceiver = new BroadcastReceiver()
//        {
//            @Override
//            public void onReceive(Context context, Intent intent)
//            {
//                Log.i(TAG,"BroadcastReceiver::OnReceive() start");
//                Toast.makeText(context, "Your Alarm is there on start", Toast.LENGTH_LONG).show();
//
//                ScheduledStationsFragment.RecordAudioStreamTask startudiotask = new ScheduledStationsFragment.RecordAudioStreamTask();
//                startudiotask.execute(new String[] { scheduledStation.getUrl() });
//
//            }
//        };
//
//        intent =new Intent("com.yannick.radioo");
//        this.getActivity().registerReceiver(myBroadcastReceiver, new IntentFilter("com.yannick.radioo") );
//        myPendingIntent = PendingIntent.getBroadcast( this.getActivity(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT );
//
//        alarmManager = (AlarmManager)(context.getSystemService( Context.ALARM_SERVICE ));
//    }
//
//    private void registerMyStopingAlarmBroadcast()
//    {
//        Log.i(TAG, "Going to register Intent.RegisterAlramBroadcast");
//
//        myStopinBroadcastReceiver = new BroadcastReceiver()
//        {
//            @Override
//            public void onReceive(Context context, Intent intent)
//            {
//                Log.i(TAG,"BroadcastReceiver::OnReceive() stops");
//                Toast.makeText(context, "Your Alarm is there on stop", Toast.LENGTH_LONG).show();
//
//
//
//                ScheduledStationsFragment.StopRecordAudioStreamTask stopaudiotask = new ScheduledStationsFragment.StopRecordAudioStreamTask();
//                stopaudiotask.execute(new String[] { scheduledStation.getUrl() });
//
//
//
//            }
//        };
//
//        this.getActivity().registerReceiver(myStopinBroadcastReceiver, new IntentFilter("com.yannick.radioo") );
//        myStopingIntent = PendingIntent.getBroadcast( this.getActivity(), 1, intent,0 );
//        // myStopingIntent = PendingIntent.getBroadcast( this.getActivity(), 0, new Intent("com.yannick.radioo"),0 );
//
//        alarmManagerStop = (AlarmManager)(this.getActivity().getSystemService( Context.ALARM_SERVICE ));
//    }
//
//    private void unregisterAlarmBroadcast()
//    {
//        alarmManager.cancel(myPendingIntent);
//        this.getActivity().getBaseContext().unregisterReceiver(myBroadcastReceiver);
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_list, container, false);

        lp=datasource.getAllStations();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if(lp!=null){
                adapter = new ScheduledStationsRecyclerViewAdapter(lp, mListener);
                adapter.notifyItemInserted(lp.size());
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        }
        return view;
    }

//    private class RecordAudioStreamTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            startTime = System.currentTimeMillis();
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    Log.i(TAG,"BroadcastReceiver::ok records!");
//
//                    //if(station ==null) station = favourite;
//                    try {
//                        File directory = new File(context.getFilesDir() + File.separator + "MyPodcasts");
//
//                        if (!directory.exists()) {
//                            directory.mkdir();
//                        }
//
//                        try {
//
//                            String path = context.getFilesDir() + File.separator + "MyPodcasts";//+File.separator+station.getName()+(new Date()).toString()+"."+station.getCodec().toLowerCase()
//
//                            String streamURL = null;
//                            if (scheduledStation != null)
//                                streamURL = scheduledStation.getUrl();
//                            else
//                                streamURL = favourite.getUrl();
//
//                            URL url = new URL(streamURL);
//                            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//                            int responseCode = httpConn.getResponseCode();
//                            if (responseCode == HttpURLConnection.HTTP_OK) {
//                                String fileName = "";
//                                String disposition = httpConn.getHeaderField("Content-Disposition");
//
//                                if (disposition != null) {
//                                    int index = disposition.indexOf("filename=");
//                                    if (index > 0) {
//                                        fileName = disposition.substring(index + 10, disposition.length() - 1);
//                                    }
//                                } else {
//                                    fileName = streamURL.substring(streamURL.lastIndexOf("/") + 1, streamURL.length());
//                                }
//
//                                if (scheduledStation != null)
//                                    saveFilePath = path + File.separator + fileName + getNow() + "." + scheduledStation.getCodec();
//
//
//                                if (input != null) input.close();
//                                input = httpConn.getInputStream();
//
//                                outputStream = new FileOutputStream(saveFilePath);
//
//                                int bytesRead = -1;
//                                byte[] buffer = new byte[BUFFER_SIZE];
//                                try {
//                                    while ((input != null) && (bytesRead = input.read(buffer)) != -1) {
//                                        outputStream.write(buffer, 0, bytesRead);
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                Log.i(TAG,"BroadcastReceiver::ok files downloaded!");
//                                System.out.println("File downloaded");
//                            } else {
//                                System.out.println("No stream. Server replied HTTP code: " + responseCode);
//                            }
//                            httpConn.disconnect();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            thread.start();
//
//            return "recording...";
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // mTextViewRecordingState.setText(result);
//            DownloadImageStreamTask downloadTask = new DownloadImageStreamTask();
//            downloadTask.execute(new String[] { scheduledStation.getFavicon() });
//            podcast.setFileUrl(saveFilePath);
//            System.out.println("saveFaviconPath: "+saveFaviconPath);
//            podcast.setFaviconUrl(saveFaviconPath);
//            podcast.setFaviconUrl("http://www.0nradio.com/images/favicon/mstile-144x144.png");
//            intent.putExtra("podcast", podcast);
//            intent.putExtra("startTime",startTime);
//           // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(saveFilePath)));
//            //isRecording = true;
//        }
//    }
//
//    private class StopRecordAudioStreamTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            //Log.i(TAG,"BroadcastReceiver::ok stops recording!");
//            //Log.v("BroadcastReceiver ", "stops :fileUrl"+podcast.getFileUrl());
//
//            long duration =  System.currentTimeMillis() - intent.getLongExtra("startTime",0);
//
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    try  {
//
//                        Log.v("duration",""+duration);
//                        PodcastDAO datasource = new PodcastDAO(context);
//                        datasource.open();
//
//                        podcast.setId(0);
//
//                        if(scheduledStation !=null){
//                            podcast.setTitle(scheduledStation.getName()+(new Date()).toString());
//                            //+"."+station.getCodec().toLowerCase());
//                        }
//
//                        Podcast p= intent.getParcelableExtra("podcast");
//                        System.out.println("podcast pathfile: "+podcast.getFileUrl());
//                        podcast.setDate(new Date().toString());
//                        if(saveFaviconPath==null)saveFaviconPath ="";
//                        podcast.setFaviconUrl(saveFaviconPath);
//                        podcast.setDuration(duration);
//                        //Log.i(TAG,"BroadcastReceiver::saveFilePath 3"+podcast);
//                        //System.out.println("podcast dan stop avant create: "+podcast);
//                        if(podcast.getFileUrl()!=null)
//                            datasource.create(podcast);
//                        //String pathfile = intent.getStringExtra("podcast");
//
//                     //   Uri file = intent.getParcelableExtra("Intent.EXTRA_STREAM");
//
//
//                        try {
//                            if(outputStream!=null){
//                                outputStream.close();
//                            }
//
//                            //  input.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        Log.i(TAG,"BroadcastReceiver::ok stops  : File downloaded!");
//                        System.out.println("File downloaded");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            thread.start();
//
//            return "stop recording";
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            Log.v("RESULT",result);
//
//
//            //isRecording = false;
//            //mTextViewRecordingState.setText(result);
//        }
//    }

//    public void setAdapter(List<Podcast> results, RecyclerView recyclerView){
//        lp=datasource.getAllPodcasts();
//
//        adapter = new PodcastRecyclerViewAdapter(lp, mListener);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

//    private class DownloadImageStreamTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    try  {
//                        File directory = new File(context.getFilesDir()+File.separator+"MyStationImages");
//
//                        if(!directory.exists()){
//                            directory.mkdir();
//                        }
//
//                        Log.v("url",directory.getAbsolutePath());
//
//                        try {
//                            String path=context.getFilesDir()+File.separator+"MyStationImages";
//                            Log.v("scheduledStation",""+scheduledStation.getFavicon());
//                            String[] parts =scheduledStation.getFavicon().split("/");
//                            String fileName = parts[parts.length-1];
//
//                            BufferedInputStream in;
//                            // if(station.getFavicon()==null)
//                            // in = new BufferedInputStream(new URL("http://www."+station.getFavicon()).openStream());
//                            //else
//                            in = new BufferedInputStream(new URL(scheduledStation.getFavicon()).openStream());
//
//
//                            saveFaviconPath = path + File.separator + fileName;
//                            Log.v("saveFaviconPath dim",saveFaviconPath);
//                            FileOutputStream fileOutputStream = new FileOutputStream(saveFaviconPath);
//                            Log.v("filename",fileName);
//                            byte dataBuffer[] = new byte[1024];
//                            int bytesRead;
//                            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
//                                fileOutputStream.write(dataBuffer, 0, bytesRead);
//                            }
//                            //in.close();
//                            fileOutputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            thread.start();
//
//            return saveFaviconPath;
//
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            Log.v("result image",""+result);
//            podcast= intent.getParcelableExtra("podcast");
//            podcast.setFaviconUrl(saveFaviconPath);
//            //mTextViewRecordingState.setText(result);
//        }
//    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+ " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        datasource.close();

    }

    @Override
    public void onDestroy() {
        this.getActivity().unregisterReceiver(myBroadcastReceiver);
        super.onDestroy();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Station podcast);
    }
}
