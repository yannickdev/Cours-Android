package com.yannick.radioo.radios;

import android.content.Context;
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
import com.yannick.radioo.R;
import com.yannick.radioo.RadioService;
import com.yannick.radioo.RetrofitManager;
import com.yannick.radioo.ScheduledStation;
import com.yannick.radioo.ScheduledStationDAO;
import com.yannick.radioo.Station;
import com.yannick.radioo.StationAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SchedulerFragment extends Fragment implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    //, TimePickerDialog.OnTimeSetListener

    String item[]={
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};

    String[] countries ;

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
        context = this.getActivity();
    }


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

//        ArrayList<String> list = new ArrayList<>();
//        list.add("aaaaaa");
//        list.add("bbbbbbbbbbb");
//        list.add("ccccccccc");
//        list.add("dddddddddd");

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
            Station station = mListStations.get(0);

            ScheduledStation s= new ScheduledStation();
            s.setStationuuid("0");
            s.setName(station.getName()+(new Date()).toString()+"."+station.getCodec().toLowerCase());
           // s.setFavicon(saveFilePath);
            s.setCountry(station.getCountry());
            s.setUrl(station.getUrl());
            s.setCountrycode(station.getCountrycode());
            s.setName(station.getName());
            s.setState(station.getState());
            s.setCodec(station.getCodec());
            s.setVotes(station.getVotes());

            ScheduledStation f = new ScheduledStation(s);

            ScheduledStationDAO datasource = new ScheduledStationDAO(context);
            datasource.open();
           // Log.v("saveFaviconPath: ",""+saveFaviconPath);

            datasource.create(f);
            manageDatePicker();
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
