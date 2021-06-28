package com.yannick.radioo.radios;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yannick.radioo.Favourite;
import com.yannick.radioo.FavouriteDAO;
import com.yannick.radioo.Podcast;
import com.yannick.radioo.PodcastDAO;
import com.yannick.radioo.R;
import com.yannick.radioo.RadioService;
import com.yannick.radioo.RetrofitManager;
import com.yannick.radioo.ScheduledStation;
import com.yannick.radioo.ScheduledStationDAO;
import com.yannick.radioo.SchedulerStatus;
import com.yannick.radioo.Station;
import com.yannick.radioo.StationAdapter;

import org.joda.time.DateTime;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.yannick.radioo.radios.PlayerFragment.getNow;


public class SchedulerFragment extends Fragment implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    //, TimePickerDialog.OnTimeSetListener

    String item[]={
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};

    String[] countries ;

    PendingIntent myPendingIntent;
    PendingIntent myStopingIntent;
    Intent intent;
    AlarmManager alarmManager;
    AlarmManager alarmManagerStop;
    BroadcastReceiver myBroadcastReceiver;
    BroadcastReceiver myStopinBroadcastReceiver;
    Calendar firingCal;
    Calendar stopingCal;

    long lastInsertedId;

    //TextView statusView ;

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

    ScheduledStation scheduledStation;

    private ScheduledStationDAO datasource;


    AutoCompleteTextView autoCompleteTextView;

    RadioService radioService;

    private TextView dateTextView;

    private TextView timeTextView;

    RadioRecyclerViewAdapter adapter;


    private TextView beginningDateTextView;
    private TextView beginningTimeTextView;

    private TextView endDateTextView;
    private TextView endTimeTextView;

    private Button sendScheduledStation;

    private Context context;


    private DatePickerDialog dpd;
    private TimePickerDialog tpd;


    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RadioFragment.OnListFragmentInteractionListener mListener;

    static ArrayList<Station> mListStations = new ArrayList<Station>();
//    private CheckBox modeDarkDate;
//    private CheckBox modeCustomAccentDate;
//    private CheckBox vibrateDate;
//    private CheckBox dismissDate;
//    private CheckBox titleDate;
//    private CheckBox showYearFirst;
//    private CheckBox showVersion2;
//    private CheckBox switchOrientation;
//    private CheckBox limitSelectableDays;
//    private CheckBox highlightDays;
//    private CheckBox defaultSelection;




//    private CheckBox mode24Hours;
//    private CheckBox modeDarkTime;
//    private CheckBox modeCustomAccentTime;
//    private CheckBox vibrateTime;
//    private CheckBox dismissTime;
//    private CheckBox titleTime;
//    private CheckBox enableSeconds;
//    private CheckBox limitSelectableTimes;
//    private CheckBox disableSpecificTimes;




    public SchedulerFragment() {
        // Required empty public constructor
    }

    public static SchedulerFragment newInstance() {
        SchedulerFragment fragment = new SchedulerFragment();
        Bundle args = new Bundle();
      //  args.putParcelable("STATION", station);
        fragment.setArguments(args);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);
        //context = this.getActivity();

        context=this.getContext();
        datasource = new ScheduledStationDAO(context);
        datasource.open();

        //intent = new Intent(this.context, BroadcastReceiver.class);
        //Intent broadcast_intent = new Intent(this, AlarmBroadcastReceiver.class);

        podcast= new Podcast();
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState){
//        //statusView = view.findViewById(R.id.item_status);
//    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        countries =  new String[] {"usa","china","russia","bangladesh"};
        //view = inflater.inflate(R.layout.fragment_scheduler, container, false);
        final View view= inflater.inflate(R.layout.fragment_scheduler, container, false);
        RadioService radioService = RetrofitManager.getRetrofit().create(RadioService.class);
        radioService.getStations().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (response.isSuccessful()) {
                    mListStations.addAll(response.body());
                    if (view instanceof RecyclerView) {
                        Context context = view.getContext();
                        RecyclerView recyclerView = (RecyclerView) view;
                        if (mColumnCount <= 1) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        } else {
                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                        }

                        RadioRecyclerViewAdapter adapter = new RadioRecyclerViewAdapter(mListStations, mListener);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);

                        recyclerView.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Log.v("response",t.getMessage());
            }
        });
       // return view;

//        RadioService radioService = RetrofitManager.getRetrofit().create(RadioService.class);
//        radioService.getStations().enqueue(new Callback<List<Station>>() {
//            @Override
//            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
//                if (response.isSuccessful()) {
//                    mListStations.addAll(response.body());
//
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Station>> call, Throwable t) {
//                Log.v("response",t.getMessage());
//            }
//        });







        sendScheduledStation = view.findViewById(R.id.scheduler_button);
        Context c = getActivity().getApplicationContext();
        autoCompleteTextView=view.findViewById(R.id.autoComplete);
       // ArrayAdapter<String> adapter = new ArrayAdapter<String> (c, R.layout.autocomplete_item, list);






      //  ArrayList objects = new ArrayList<Map<String, String>>();

//        Map<String, String> NamePhoneType = new HashMap<String, String>();
//        NamePhoneType.put("Name", "John");
//        NamePhoneType.put("Phone", "1234567890");
//        objects.add(NamePhoneType);
//        NamePhoneType.put("Name", "Steve");
//        NamePhoneType.put("Phone", "4567890123");
//        objects.add(NamePhoneType);

     //   ArrayAdapter<Map<String, String>> adapter = new ArrayAdapter<Map<String, String>>(c, android.R.layout.simple_list_item_1, objects);

//        ArrayAdapter adapter = new ArrayAdapter(c,android.R.layout.simple_list_item_1,countries);
//
//        autoCompleteTextView.setAdapter(adapter);
//
//        // Set the minimum number of characters, to show suggestions
//        autoCompleteTextView.setThreshold(1);
//
//
//        //autoCompleteTextView.setThreshold(3);
//        adapter.setNotifyOnChange(true);
        // adapter.addAll(list);
       // autoCompleteTextView.setAdapter(adapter);

//        final RadioService radioService = RetrofitManager.getRetrofit().create(RadioService.class);
//
//        radioService.getStations().enqueue(new Callback<List<Station>>() {
//
//
//            @Override
//            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
//
//
//
//                if(response.isSuccessful()){
//                    mListStations.addAll(response.body());
//                    Log.v("size", ""+mListStations.size());
//                    ArrayAdapter<Station> adapter = new ArrayAdapter<Station> (c, android.R.layout.simple_dropdown_item_1line, mListStations);
//
//                    autoCompleteTextView.setAdapter(adapter);
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<List<Station>> call, Throwable t) {
//                Log.v("response",t.getMessage());
//            }
//        });

        // Find our View instances


//        modeDarkDate = view.findViewById(R.id.mode_dark_date);
//        modeCustomAccentDate = view.findViewById(R.id.mode_custom_accent_date);
//        vibrateDate = view.findViewById(R.id.vibrate_date);
//        dismissDate = view.findViewById(R.id.dismiss_date);
//        titleDate = view.findViewById(R.id.title_date);
//        showYearFirst = view.findViewById(R.id.show_year_first);
//        showVersion2 = view.findViewById(R.id.show_version_2);
//        switchOrientation = view.findViewById(R.id.switch_orientation);
//        limitSelectableDays = view.findViewById(R.id.limit_dates);
//        highlightDays = view.findViewById(R.id.highlight_dates);
//        defaultSelection = view.findViewById(R.id.default_selection);


        beginningDateTextView = view.findViewById(R.id.beginning_date_textview);
        Button beginningDateButton = view.findViewById(R.id.beginning_date_button);
        beginningTimeTextView = view.findViewById(R.id.beginning_time_textview);
        Button beginningTimeButton = view.findViewById(R.id.beginning_time_button);


        endDateTextView = view.findViewById(R.id.end_date_textview);
        Button endDateButton = view.findViewById(R.id.end_date_button);
        endTimeTextView = view.findViewById(R.id.end_time_textview);
        Button endTimeButton = view.findViewById(R.id.end_time_button);




//        mode24Hours = view.findViewById(R.id.mode_24_hours);
//        modeDarkTime = view.findViewById(R.id.mode_dark_time);
//        modeCustomAccentTime = view.findViewById(R.id.mode_custom_accent_time);
//        vibrateTime = view.findViewById(R.id.vibrate_time);
//        dismissTime = view.findViewById(R.id.dismiss_time);
//        titleTime = view.findViewById(R.id.title_time);
//        enableSeconds = view.findViewById(R.id.enable_seconds);
//        limitSelectableTimes = view.findViewById(R.id.limit_times);
//        disableSpecificTimes = view.findViewById(R.id.disable_times);
//        showVersion2 = view.findViewById(R.id.show_version_2);

//        view.findViewById(R.id.original_button).setOnClickListener(v -> {
//            Calendar now = Calendar.getInstance();
//            new android.app.DatePickerDialog(
//                    requireActivity(),
//                    (view1, year, month, dayOfMonth) -> Log.d("Orignal", "Got clicked"),
//                    now.get(Calendar.YEAR),
//                    now.get(Calendar.MONTH),
//                    now.get(Calendar.DAY_OF_MONTH)
//            ).show();
//        });

        // Show a datepicker when the dateButton is clicked
        beginningDateButton.setOnClickListener(v -> {
            dateTextView = beginningDateTextView;
            manageDatePicker();
        });

        endDateButton.setOnClickListener(v -> {
            dateTextView = endDateTextView;
            manageDatePicker();
        });

        // Show a timepicker when the timeButton is clicked
        beginningTimeButton.setOnClickListener(v -> {
            timeTextView = beginningTimeTextView;
            manageTimePicker();
        });

        endTimeButton.setOnClickListener(v -> {
            timeTextView = endTimeTextView;
            manageTimePicker();
        });

        sendScheduledStation.setOnClickListener(v -> {
            firingCal= Calendar.getInstance();
            firingCal.set(Calendar.HOUR, 2); // At the hour you want to fire the alarm
            firingCal.set(Calendar.MINUTE, 5); // alarm minute
            firingCal.set(Calendar.SECOND, 0); // and alarm second
            long intendedTime = firingCal.getTimeInMillis();

            stopingCal= Calendar.getInstance();
            stopingCal.set(Calendar.HOUR, 2); // At the hour you want to fire the alarm
            stopingCal.set(Calendar.MINUTE,6); // alarm minute
            stopingCal.set(Calendar.SECOND, 0); // and alarm second
            long intendedStopTime = stopingCal.getTimeInMillis();

            scheduledStation = new ScheduledStation();
            scheduledStation.setStationuuid("aeffe1234e78ui3");
            scheduledStation.setUrl("http://icecast.radiofrance.fr/franceculture-midfi.mp3");
            scheduledStation.setName("france culture scheduled");
            scheduledStation.setCodec("MP3");
            scheduledStation.setFavicon("https://www.franceculture.fr//favicons/android-icon-192x192.png");
            scheduledStation.setCountrycode("fr");
            scheduledStation.setCountry("france");
            scheduledStation.setEndDate("10-06_2021");
            scheduledStation.setStartDate("9-06_2021");
            scheduledStation.setStatus(SchedulerStatus.PENDING);

            datasource.create(scheduledStation);

            registerMyAlarmBroadcast();
            registerMyStopingAlarmBroadcast();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, intendedTime, myPendingIntent);
                alarmManagerStop.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, intendedStopTime, myStopingIntent);        }
            else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, intendedTime, myPendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, intendedStopTime, myStopingIntent);
            }

//         // Log.v("saveFaviconPath: ",""+saveFaviconPath);

//          manageDatePicker();
        });



//        radioService = RetrofitManager.getRetrofit().create(RadioService.class);
//        radioService.getStations().enqueue(new Callback<List<Station>>() {
//            @Override
//            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
//                if (response.isSuccessful()) {
//                    mListStations.addAll(response.body());
//                    if (view instanceof RecyclerView) {
//                        Context context = view.getContext();
//                        RecyclerView recyclerView = (RecyclerView) view;
//                        if (mColumnCount <= 1) {
//                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                        } else {
//                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//                        }
//
//                        autoCompleteTextView.setThreshold(1);
//                        adapter = new RadioRecyclerViewAdapter(mListStations, mListener);
//                        adapter.notifyDataSetChanged();
//
//                        //recyclerView.setAdapter(adapter);
//
//                        SearchView searchView =view.findViewById(R.id.simpleSearchView);
//                        recyclerView.setAdapter(adapter);
//                        search(searchView);
//
//                        // Set the minimum number of characters, to show suggestions
//
//                        //ArrayAdapter adapter = new ArrayAdapter(c,R.layout.autocomplete_item,countries);//marche avec countries
//                        //StationAdapter adapter = new StationAdapter(c,mListStations);
//
//                        //autoCompleteTextView.setAdapter(adapter); //voir pour implements filter dans baseapater
//                       // autoCompleteTextView.setAdapter(adapter);
//
//
//
//
//
//
//
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Station>> call, Throwable t) {
//                Log.v("response",t.getMessage());
//            }
//        });





        //autoCompleteTextView.setThreshold(3);
        //adapter.setNotifyOnChange(true);

        return view;
    }



    private void registerMyAlarmBroadcast()
    {
        Log.i(TAG, "Going to register Intent.RegisterAlramBroadcast");

        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        myBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.i(TAG,"BroadcastReceiver::OnReceive() start");
                Toast.makeText(context, "Your Alarm is there for start", Toast.LENGTH_LONG).show();

                SchedulerFragment.RecordAudioStreamTask startudiotask = new SchedulerFragment.RecordAudioStreamTask();
                startudiotask.execute(new String[] { scheduledStation.getUrl() });

            }
        };

        intent =new Intent("com.yannick.radioo");
        this.getActivity().registerReceiver(myBroadcastReceiver, new IntentFilter("com.yannick.radioo") );
        myPendingIntent = PendingIntent.getBroadcast( this.getActivity(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT );

        alarmManager = (AlarmManager)(context.getSystemService( Context.ALARM_SERVICE ));
    }

    private void registerMyStopingAlarmBroadcast()
    {
        Log.i(TAG, "Going to register Intent.RegisterAlramBroadcast");

        myStopinBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.i(TAG,"BroadcastReceiver::OnReceive() stops");
                Toast.makeText(context, "Your Alarm is there for stop", Toast.LENGTH_LONG).show();

                SchedulerFragment.StopRecordAudioStreamTask stopaudiotask = new SchedulerFragment.StopRecordAudioStreamTask();
                stopaudiotask.execute(new String[] { scheduledStation.getUrl() });



            }
        };

        this.getActivity().registerReceiver(myStopinBroadcastReceiver, new IntentFilter("com.yannick.radioo") );
        //myStopingIntent = PendingIntent.getBroadcast( this.getActivity(), 1, intent,0 );
        myStopingIntent = PendingIntent.getBroadcast( this.getActivity(), 1, new Intent("com.yannick.radioo"),PendingIntent.FLAG_UPDATE_CURRENT );

        alarmManagerStop = (AlarmManager)(this.getActivity().getSystemService( Context.ALARM_SERVICE ));
    }

    private void unregisterAlarmBroadcast()
    {
        alarmManager.cancel(myPendingIntent);
        alarmManager.cancel(myStopingIntent);
        this.getActivity().getBaseContext().unregisterReceiver(myBroadcastReceiver);
    }



    private class RecordAudioStreamTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            startTime = System.currentTimeMillis();
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Log.i(TAG,"BroadcastReceiver::ok records!");
                    //statusView.setText("downloading");

                    //if(station ==null) station = favourite;
                    try {
                        File directory = new File(context.getFilesDir() + File.separator + "MyPodcasts");

                        if (!directory.exists()) {
                            directory.mkdir();
                        }

                        try {

                            String path = context.getFilesDir() + File.separator + "MyPodcasts";//+File.separator+station.getName()+(new Date()).toString()+"."+station.getCodec().toLowerCase()

                            String streamURL = null;
                            if (scheduledStation != null)
                                streamURL = scheduledStation.getUrl();
                            else
                                streamURL = favourite.getUrl();

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

                                if (scheduledStation != null)
                                    saveFilePath = path + File.separator + fileName + getNow() + "." + scheduledStation.getCodec();


                                if (input != null) input.close();
                                input = httpConn.getInputStream();

                                outputStream = new FileOutputStream(saveFilePath);

                                int bytesRead = -1;
                                byte[] buffer = new byte[BUFFER_SIZE];
                                try {
                                    while ((input != null) && (bytesRead = input.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, bytesRead);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.i(TAG,"BroadcastReceiver::ok files downloaded!");
                                System.out.println("File downloaded");
                            } else {
                                System.out.println("No stream. Server replied HTTP code: " + responseCode);
                            }
                            httpConn.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
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
            SchedulerFragment.DownloadImageStreamTask downloadTask = new SchedulerFragment.DownloadImageStreamTask();
            downloadTask.execute(new String[] { scheduledStation.getFavicon() });
            podcast.setFileUrl(saveFilePath);
            System.out.println("saveFaviconPath: "+saveFaviconPath);
            podcast.setFaviconUrl(saveFaviconPath);
            podcast.setFaviconUrl("http://www.0nradio.com/images/favicon/mstile-144x144.png");
            intent.putExtra("podcast", podcast);
            intent.putExtra("startTime",startTime);
            // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(saveFilePath)));
            //isRecording = true;
        }
    }

    private class StopRecordAudioStreamTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //Log.i(TAG,"BroadcastReceiver::ok stops recording!");
            //Log.v("BroadcastReceiver ", "stops :fileUrl"+podcast.getFileUrl());

            long duration =  System.currentTimeMillis() - intent.getLongExtra("startTime",0);

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    //statusView.setText("stopped");
                    try  {

                        Log.v("duration",""+duration);
                        PodcastDAO datasource = new PodcastDAO(context);
                        datasource.open();

                        podcast.setId(0);

                        if(scheduledStation !=null){
                            podcast.setTitle(scheduledStation.getName()+(new Date()).toString());
                            //+"."+station.getCodec().toLowerCase());
                        }

                        Podcast p= intent.getParcelableExtra("podcast");
                        System.out.println("podcast pathfile: "+podcast.getFileUrl());
                        podcast.setDate(new Date().toString());
                        if(saveFaviconPath==null)saveFaviconPath ="";
                        podcast.setFaviconUrl(saveFaviconPath);
                        podcast.setDuration(duration);
                        //Log.i(TAG,"BroadcastReceiver::saveFilePath 3"+podcast);
                        //System.out.println("podcast dan stop avant create: "+podcast);
                        if(podcast.getFileUrl()!=null)
                            datasource.create(podcast);
                        //String pathfile = intent.getStringExtra("podcast");

                        //   Uri file = intent.getParcelableExtra("Intent.EXTRA_STREAM");


                        try {
                            if(outputStream!=null){
                                outputStream.close();
                            }

                            //  input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG,"BroadcastReceiver::ok stops  : File downloaded!");
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


            //isRecording = false;
            //mTextViewRecordingState.setText(result);
        }
    }

//    public void setAdapter(List<Podcast> results, RecyclerView recyclerView){
//        lp=datasource.getAllPodcasts();
//
//        adapter = new PodcastRecyclerViewAdapter(lp, mListener);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

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
                            Log.v("scheduledStation",""+scheduledStation.getFavicon());
                            String[] parts =scheduledStation.getFavicon().split("/");
                            String fileName = parts[parts.length-1];

                            BufferedInputStream in;
                            // if(station.getFavicon()==null)
                            // in = new BufferedInputStream(new URL("http://www."+station.getFavicon()).openStream());
                            //else
                            in = new BufferedInputStream(new URL(scheduledStation.getFavicon()).openStream());


                            saveFaviconPath = path + File.separator + fileName;
                            Log.v("saveFaviconPath dim",saveFaviconPath);
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

            return saveFaviconPath;

        }
        @Override
        protected void onPostExecute(String result) {
            Log.v("result image",""+result);
            podcast= intent.getParcelableExtra("podcast");
            podcast.setFaviconUrl(saveFaviconPath);
            //mTextViewRecordingState.setText(result);
        }
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("OK NEW TEXT: "+newText);
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

//    private AdapterView.OnItemClickListener onItemClickListener =
//            new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//
//


                    //autoCompleteTextView
//                    AutoCompleteTextView textView = (AutoCompleteTextView)
//                            findViewById(R.id.countries_list);
//            @Override
//            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
////                if (response.isSuccessful()) {
//                    mListStations.addAll(response.body());
//
//
//
//
////                    if (view instanceof RecyclerView) {
////                        Context context = view.getContext();
////                        RecyclerView recyclerView = (RecyclerView) view;
////                        if (mColumnCount <= 1) {
////                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
////                        } else {
////                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
////                        }
////
////                        RadioRecyclerViewAdapter adapter = new RadioRecyclerViewAdapter(mListStations, mListener);
////                        adapter.notifyDataSetChanged();
////                        recyclerView.setAdapter(adapter);
////
////                        recyclerView.setAdapter(adapter);
//
//                        StationAdapter adapter = new StationAdapter(getContext(),mListStations);
//
//                    autoCompleteTextView.setThreshold(1);
//                    autoCompleteTextView.setAdapter(adapter);
//
//                                //getContext(),mListStations);
//
//                        autoCompleteTextView.setAdapter(adapter);
//                        autoCompleteTextView.setThreshold(1);
//                        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                                Log.d("beforeTextChanged", String.valueOf(s));
//                            }
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                Log.d("onTextChanged", String.valueOf(s));
//                            }
//                            @Override
//                            public void afterTextChanged(Editable s) {
//                                Log.d("afterTextChanged", String.valueOf(s));
//                            }
//                        });
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Station>> call, Throwable t) {
//                Log.v("response",t.getMessage());
//            }
//        });
//        view.findViewById(R.id.original_button).setOnClickListener(view1 -> {
//            Calendar now = Calendar.getInstance();
//            new android.app.TimePickerDialog(
//                    getActivity(),
//                    (view11, hour, minute) -> Log.d("Original", "Got clicked"),
//                    now.get(Calendar.HOUR_OF_DAY),
//                    now.get(Calendar.MINUTE),
//                    mode24Hours.isChecked()
//            ).show();
//        });




//
//                    System.out.println("autocomplete position: "+ adapterView.getItemAtPosition(i));
//                }
//
//
//            };

    public void manageDatePicker(){
        Calendar now = Calendar.getInstance();
//        if (defaultSelection.isChecked()) {
//            now.add(Calendar.DATE, 7);
//        }
            /*
            It is recommended to always create a new instance whenever you need to show a Dialog.
            The sample app is reusing them because it is useful when looking for regressions
            during testing
             */
        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    SchedulerFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    SchedulerFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        }

        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
//        dpd.setThemeDark(modeDarkDate.isChecked());
//        dpd.vibrate(vibrateDate.isChecked());
//        dpd.dismissOnPause(dismissDate.isChecked());
//        dpd.showYearPickerFirst(showYearFirst.isChecked());
//        dpd.setVersion(showVersion2.isChecked() ? DatePickerDialog.Version.VERSION_2 : DatePickerDialog.Version.VERSION_1);
//        if (modeCustomAccentDate.isChecked()) {
//            dpd.setAccentColor(Color.parseColor("#9C27B0"));
//        }
//        if (titleDate.isChecked()) {
//            dpd.setTitle("DatePicker Title");
//        }
//        if (highlightDays.isChecked()) {
//            Calendar date1 = Calendar.getInstance();
//            Calendar date2 = Calendar.getInstance();
//            date2.add(Calendar.WEEK_OF_MONTH, -1);
//            Calendar date3 = Calendar.getInstance();
//            date3.add(Calendar.WEEK_OF_MONTH, 1);
//            Calendar[] days = {date1, date2, date3};
//            dpd.setHighlightedDays(days);
//        }
//        if (limitSelectableDays.isChecked()) {
//            Calendar[] days = new Calendar[13];
//            for (int i = -6; i < 7; i++) {
//                Calendar day = Calendar.getInstance();
//                day.add(Calendar.DAY_OF_MONTH, i * 2);
//                days[i + 6] = day;
//            }
//            dpd.setSelectableDays(days);
//        }
//        if (switchOrientation.isChecked()) {
//            if (dpd.getVersion() == DatePickerDialog.Version.VERSION_1) {
//                dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.HORIZONTAL);
//            } else {
//                dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.VERTICAL);
//            }
//        }
        dpd.setOnCancelListener(dialog -> {
            Log.d("DatePickerDialog", "Dialog was cancelled");
            dpd = null;
        });
        dpd.show(requireFragmentManager(), "Datepickerdialog");
    }

    public void manageTimePicker(){
        Calendar now = Calendar.getInstance();
            /*
            It is recommended to always create a new instance whenever you need to show a Dialog.
            The sample app is reusing them because it is useful when looking for regressions
            during testing
             */
        if (tpd == null) {
            tpd = TimePickerDialog.newInstance(
                    SchedulerFragment.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            );
        } else {
            tpd.initialize(
                    SchedulerFragment.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    now.get(Calendar.SECOND),
                    true
            );
        }

        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
//        tpd.setThemeDark(modeDarkTime.isChecked());
//        tpd.vibrate(vibrateTime.isChecked());
//        tpd.dismissOnPause(dismissTime.isChecked());
//        tpd.enableSeconds(enableSeconds.isChecked());
//        tpd.setVersion(showVersion2.isChecked() ? TimePickerDialog.Version.VERSION_2 : TimePickerDialog.Version.VERSION_1);
//        if (modeCustomAccentTime.isChecked()) {
//            tpd.setAccentColor(Color.parseColor("#9C27B0"));
//        }
//        if (titleTime.isChecked()) {
//            tpd.setTitle("TimePicker Title");
//        }
//        if (limitSelectableTimes.isChecked()) {
//            if (enableSeconds.isChecked()) {
//                tpd.setTimeInterval(3, 5, 10);
//            } else {
//                tpd.setTimeInterval(3, 5, 60);
//            }
//        }
//        if (disableSpecificTimes.isChecked()) {
//            Timepoint[] disabledTimes = {
//                    new Timepoint(10),
//                    new Timepoint(10, 30),
//                    new Timepoint(11),
//                    new Timepoint(12, 30)
//            };
//            tpd.setDisabledTimes(disabledTimes);
//        }
        tpd.setOnCancelListener(dialogInterface -> {
            Log.d("TimePicker", "Dialog was cancelled");
            tpd = null;
        });
        tpd.show(requireFragmentManager(), "Timepickerdialog");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        tpd = null;
        dpd = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) requireFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);

        TimePickerDialog tpd = (TimePickerDialog) requireFragmentManager().findFragmentByTag("Timepickerdialog");
        if(tpd != null) tpd.setOnTimeSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
//        dateTextView.setText(date);
        dpd = null;

        Date dt = new Date(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        timeTextView.setText(time);
        tpd = null;
    }
}
