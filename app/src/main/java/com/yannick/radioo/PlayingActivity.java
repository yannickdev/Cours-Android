//package com.yannick.radioo;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaRecorder;
//import android.net.ConnectivityManager;
//import android.net.Network;
//import android.net.NetworkCapabilities;
//import android.net.NetworkRequest;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.speech.RecognitionListener;
//import android.speech.RecognizerIntent;
//import android.speech.SpeechRecognizer;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.RequiresApi;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Locale;
//
//import javax.xml.transform.Result;
//
////import javax.sound;
//
//
//
//
//public class PlayingActivity extends Activity implements RecognitionListener {
//    private Context mContext;
//    private Activity mActivity;
//    private ImageView mPlayerControl;
//    private LinearLayout mRootLayout;
//    private Button mButtonPlay;
//    private Button mButtonStopPlaying;
//    private Button mButtonRecord;
//    private TextView mTextViewRecordingState;
//    private Button mButtonStopRecording;
//    private Button mButtonAddFavourite;
//    private Button mButtonGoToFavourite;
//    private Button mButtonMute;
//    private Button mButtonUnmute;
//    private Button mButtonGoToPodcasts;
//    private InputStream input;
//    private long startTime;
//    private SpeechRecognizer sr;
//    private static final String TAG = "SpeechListener playing";
//    private String saveFilePath;
//    private String saveFaviconPath;
//
//    private OutputStream outputStream;
//
//    private static final int BUFFER_SIZE = 4096;
//
//
//
//    private MediaPlayer mPlayer;
//
//    Station station;
//    Podcast podcast;
//
//    MediaRecorder recorder;
//
//    PlayingActivity context=PlayingActivity.this;
//
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_playing);
//
//        mContext = getApplicationContext();
//        mActivity = PlayingActivity.this;
//
//        mRootLayout = findViewById(R.id.root_layout);
//        mButtonPlay = findViewById(R.id.btn_play);
//        mButtonMute =findViewById(R.id.btn_mute);
//        mButtonUnmute = findViewById(R.id.btn_unmute);
//        mButtonRecord = findViewById(R.id.btn_record);
//        mButtonStopRecording = findViewById(R.id.btn_stop_record);
//        mButtonAddFavourite= findViewById(R.id.btn_add_favourite);
//        mButtonGoToFavourite= findViewById(R.id.btn_go_to_favourite);
//        mTextViewRecordingState = findViewById(R.id.recording_state);
//        mButtonGoToPodcasts = findViewById(R.id.btn_go_to_podcasts);
//
//        mButtonStopPlaying= findViewById(R.id.btn_stop_playing);
//
//        Log.v("network",""+getNetworkType(this));
//
//
//
//        //getSpeechRecognizer();
//        //startListening();
//
//        listenNetworkConnectivity();
//
//
//        mPlayerControl = (ImageView) findViewById(R.id.player_control0);
//
//        station=getIntent().getExtras().getParcelable("STATION");
//        podcast = getIntent().getExtras().getParcelable("PODCAST");
//
//        play();
//
//        mButtonPlay.setOnClickListener(new View.OnClickListener() {
//            Activity _this=PlayingActivity.this;
//            @Override
//            public void onClick(View v) {
//                play();
//            }
//        });
//
//       /* mNetworkCallback = new ConnectivityManager.NetworkCallback() {
//
//            @Override
//            public void onLost(Network lostNetwork) {
//                if (mNetwork.equals(lostNetwork))
//                    done(Result.UNWANTED);
//            }
//        };*/
//
//
//
//        mButtonStopPlaying.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPlayer.stop();
//                //.pause();
//            }
//        });
//
//        mButtonMute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPlayer.setVolume(0,0);
//            }
//        });
//
//        mButtonUnmute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPlayer.setVolume(1,1);
//            }
//        });
//
//
//        mButtonRecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecordAudioStreamTask audiotask = new RecordAudioStreamTask();
//                audiotask.execute(new String[] { station.getUrl() });
//
//                //new DownloadFileFromURL().execute(station.getFavicon());
//
//                DownloadImageStreamTask task = new DownloadImageStreamTask();
//                task.execute(new String[] { station.getFavicon() });
//
//            }
//        });
//
//        mButtonStopRecording.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                StopRecordAudioStreamTask audiotask = new StopRecordAudioStreamTask();
//                audiotask.execute(new String[] { station.getUrl() });
//            }
//        });
//
//        mButtonAddFavourite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DownloadImageStreamTask task = new DownloadImageStreamTask();
//                task.execute(new String[] { station.getFavicon() });
//
//                Log.v("AddFavouriteTask",station.getStationuuid());
//
//                StationDAO datasource = new StationDAO(PlayingActivity.this);
//                datasource.open();
//                Log.v("saveFaviconPath: ",""+saveFaviconPath);
//                // PlayingActivity.this.station.setFavicon(saveFaviconPath);
//
//
//                if(!datasource.existInDb(station)){
//                    datasource.create(station);
//                }
//
//            }
//        });
//
//
//        mButtonGoToFavourite.setOnClickListener(new View.OnClickListener() {
//            Intent intent;
//            @Override
//            public void onClick(View v) {
//                intent = new Intent(PlayingActivity.this, FavouriteActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        mButtonGoToPodcasts.setOnClickListener(new View.OnClickListener() {
//            Intent intent;
//            @Override
//            public void onClick(View v) {
//                intent = new Intent(PlayingActivity.this, PodcastActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void togglePlayPause() throws IOException {
//        Log.v("togglePlayPause ","entre");
//
//        if (mPlayer.isPlaying()) {
//            Log.v("playing","yes");
//            mPlayer.pause();
//            //mPlayer.pause();
//            // mMediaPlayer.seekTo(0);
//            mPlayerControl.setImageResource(R.drawable.btn_play);
//        } else {
//            play();
//            mPlayerControl.setImageResource(R.drawable.btn_pause);
//        }
//    }
//
//    private class RecordAudioStreamTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            startTime= System.currentTimeMillis();
//
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    try  {
//                        File directory = new File(context.getFilesDir()+File.separator+"MyPodcasts");
//
//                        if(!directory.exists()){
//                            directory.mkdir();
//                        }
//
//                        try {
//
//                            String path=context.getFilesDir()+File.separator+"MyPodcasts";//+File.separator+station.getName()+(new Date()).toString()+"."+station.getCodec().toLowerCase()
//
//                            String streamURL=station.getUrl();
//                            URL url = new URL(streamURL);
//                            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//                            int responseCode = httpConn.getResponseCode();
//
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
//                                saveFilePath = path + File.separator + fileName+getNow()+"."+station.getCodec();
//                                input = httpConn.getInputStream();
//
//                                outputStream = new FileOutputStream(saveFilePath);
//
//                                int bytesRead = -1;
//                                byte[] buffer = new byte[BUFFER_SIZE];
//                                while ((input!=null)&&(bytesRead = input.read(buffer)) != -1) {
//                                    outputStream.write(buffer, 0, bytesRead);
//                                }
//                                System.out.println("File downloaded");
//                            } else {
//                                System.out.println("No stream. Server replied HTTP code: " + responseCode);
//                            }
//                            httpConn.disconnect();
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
//            return "recording...";
//
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            mTextViewRecordingState.setText(result);
//        }
//    }
//
//    private class StopRecordAudioStreamTask extends AsyncTask<String, Void, String> {
//        long duration =  System.currentTimeMillis() - startTime;
//        @Override
//        protected String doInBackground(String... urls) {
//
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    try  {
//
//                        Log.v("duration",""+duration);
//                        PodcastDAO datasource = new PodcastDAO(PlayingActivity.this);
//                        datasource.open();
//
//                        podcast= new Podcast();
//                        podcast.setId(0);
//                        podcast.setTitle(station.getName()+(new Date()).toString()+"."+station.getCodec().toLowerCase());
//                        podcast.setStation(null);
//                        podcast.setDate(new Date().toString());
//                        podcast.setFileUrl(saveFilePath);
//                        podcast.setFaviconUrl(saveFaviconPath);
//                        podcast.setDuration(duration);
//                        datasource.create(podcast);
//
//                        try {
//                            outputStream.close();
//                            input.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
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
//            mTextViewRecordingState.setText(result);
//        }
//    }
//
//    private class DownloadImageStreamTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
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
//                            Log.v("station",""+station.getFavicon());
//                            String[] parts =station.getFavicon().split("/");
//                            String fileName = parts[parts.length-1];
//
//
//                            BufferedInputStream in = new BufferedInputStream(new URL(station.getFavicon()).openStream());
//                            saveFaviconPath = path + File.separator + fileName;
//                            FileOutputStream fileOutputStream = new FileOutputStream(saveFaviconPath);
//                            Log.v("filename",fileName);
//                            byte dataBuffer[] = new byte[1024];
//                            int bytesRead;
//                            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
//                                fileOutputStream.write(dataBuffer, 0, bytesRead);
//                            }
//                            in.close();
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
//            return "image downloaded...";
//
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            mTextViewRecordingState.setText(result);
//        }
//    }
//
//    private class AddFavouriteTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    DownloadImageStreamTask task = new DownloadImageStreamTask();
//                    task.execute(new String[] { station.getFavicon() });
//
//                }
//            });
//
//            thread.start();
//
//            return "stop recording";
//        }
//        @Override
//        protected void onPostExecute(String result) {
//
//
//
//            Station s= new Station();
//            s.setStationuuid("0");
//            s.setName(station.getName()+(new Date()).toString()+"."+station.getCodec().toLowerCase());
//            s.setFavicon(saveFilePath);
//            s.setCountry(station.getCountry());
//            s.setUrl(station.getUrl());
//            s.setCountrycode(station.getCountrycode());
//            s.setName(station.getName());
//            s.setState(station.getState());
//            s.setCodec(station.getCodec());
//            s.setVotes(station.getVotes());
//
//            StationDAO datasource = new StationDAO(PlayingActivity.this);
//            datasource.open();
//            Log.v("saveFaviconPath: ",""+saveFaviconPath);
//
//            datasource.create(s);
//
//            mTextViewRecordingState.setText(result);
//        }
//    }
//
//
//    public void play(){
//        try {
//            String audioUrl =null;
//            if(station!=null){
//                audioUrl = station.getUrl();
//            }
//            else{
//                audioUrl =podcast.getFileUrl();
//                Log.v("podcast",audioUrl);
//            }
//
//            if(mPlayer!=null&&mPlayer.isPlaying()){
//                mPlayer.stop();
//            }
//            mPlayer = new MediaPlayer();
//            mPlayer.reset();
//            mPlayer.setDataSource(audioUrl);
//            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mPlayer.prepareAsync();
//
//            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.start();
//                }
//            });
//
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mPlayer.stop();
//    }
//
//    public void scanFiles(){
//        String path = context.getFilesDir()+File.separator+"MyPodcasts";
//        Log.d("Files", "Path: " + path);
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
//        for (int i = 0; i < files.length; i++)
//        {
//            Log.d("Files", "FileName:" + files[i].getName());
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (mPlayer != null) {
//            if (mPlayer.isPlaying()) {
//                mPlayer.stop();
//            }
//            mPlayer.reset();
//            mPlayer = null;
//        }
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public static int getNetworkType(Context context)
//    {
//        NetworkCapabilities capabilities =null;
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            Network network = connectivityManager.getActiveNetwork();
//            capabilities = connectivityManager.getNetworkCapabilities(network);
//        }
//
//        if(capabilities != null&& (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))){
//            return NetworkCapabilities.TRANSPORT_WIFI;
//        }
//        else if(capabilities != null&& (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))){
//            return NetworkCapabilities.TRANSPORT_CELLULAR;
//        }
//        else{
//            return -1;
//        }
//    }
//
//    private void listenNetworkConnectivity() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (connectivityManager != null) {
//                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
//                    @Override
//                    public void onAvailable(Network network) {
//                        super.onAvailable(network);
//                        Log.d(TAG, "onAvailable()");
//                    }
//                    @Override
//                    public void onUnavailable() {
//                        super.onUnavailable();
//                        Log.d(TAG, "onUnavailable()");
//                    }
//                    @Override
//                    public void onLost(Network network) {
//                        super.onLost(network);
//                        Log.d(TAG, "onLost()");
//                    }
//                });
//            }
//        }
//    }
//
//    private SpeechRecognizer getSpeechRecognizer() {
//        if (sr == null) {
//            sr = SpeechRecognizer.createSpeechRecognizer(this);
//            sr.setRecognitionListener(this);
//        }
//        return sr;
//    }
//
//    public void startListening(){
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        // intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.example.myapplication");
//        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
//        sr.startListening(intent);
//    }
//
//    @Override
//    public void onReadyForSpeech(Bundle params) { }
//
//    @Override
//    public void onBeginningOfSpeech(){Log.d(TAG, "onBeginningOfSpeech");}
//
//    @Override
//    public void onRmsChanged(float rmsdB)
//    {
//        Log.d(TAG, "onRmsChanged");
//    }
//
//    @Override
//    public void onBufferReceived(byte[] buffer)
//    {
//        Log.d(TAG, "onBufferReceived");
//    }
//
//    @Override
//    public void onEndOfSpeech()
//    {
//        Log.d(TAG, "onEndofSpeech");
//    }
//
//    @Override
//    public void onError(int error) {
//
//        startListening();
////        tts = new TextToSpeech(VocalUIActivity.this, VocalUIActivity.this );
////        startListening();
//        Log.d(TAG, "error " + error);
//        String message;
//        switch (error) {
//            case SpeechRecognizer.ERROR_AUDIO:
//                message = "Audio recording error";
//                break;
//            case SpeechRecognizer.ERROR_CLIENT:
//                message = "Client side error";
//                break;
//            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
//                message = "Insufficient permissions";
//                break;
//            case SpeechRecognizer.ERROR_NETWORK:
//                message = "Network error";
//                break;
//            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
//                message = "Network timeout";
//                break;
//            case SpeechRecognizer.ERROR_NO_MATCH:
//                message = "No match";
//                break;
//            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
//                message = "RecognitionService busy";
//                break;
//            case SpeechRecognizer.ERROR_SERVER:
//                message = "error from server";
//                break;
//            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
//                message = "No speech input";
//                break;
//            default:
//                message = "Didn't understand, please try again.";
//                break;
//        }
//    }
//
//    @Override
//    public void onResults(Bundle results)
//    {
//        Log.d(TAG, "onResults " + results);
//        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        Log.d(TAG, "onResults 1 " + data.get(0));
//
//        if(data.get(0).equals("stop")){
//            Log.d(TAG, "onResults setEnabled false");
//            mButtonStopPlaying.setEnabled(false);
//            mPlayer.stop();
//        }
//        if(data.get(0).equals("play")){
//            Log.d(TAG, "play");
//            mButtonStopPlaying.setEnabled(true);
//            mButtonPlay.setEnabled(false);
//            play();
//        }else{
//            startListening();
//
//        }
//
//        //startListening();
//    }
//    @Override
//    public void onPartialResults(Bundle partialResults)
//    {
//        Log.d(TAG, "onPartialResults");
//    }
//
//    @Override
//    public void onEvent(int eventType, Bundle params)
//    {
//        Log.d(TAG, "onEvent " + eventType);
//    }
//
//    public static String getNow() {
//        String formattedDate = "";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
//                Locale.US);
//        formattedDate = sdf.format(new Date());
//        return formattedDate;
//    }
//
//    private boolean isNetworkConnected() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
//    }
//}