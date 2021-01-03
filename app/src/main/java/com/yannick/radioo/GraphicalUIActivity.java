//package com.yannick.radioo;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class GraphicalUIActivity extends AppCompatActivity {
//
//
//    private List<Station> mListStations;
//    private StationAdapter mAdapter;
//    private ListView listView;
//    private Station selectedStation;
//    private boolean isReady;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_graphical_ui);
//
//        getAllStation();
//
//        listView = (ListView) findViewById(R.id.track_list_view);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
//
//                selectedStation = mListStations.get(position);
//
//                Intent intent = new Intent(GraphicalUIActivity.this, PlayingActivity.class);
//                intent.putExtra("STATION", selectedStation);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void getAllStation() {
//
//        final GraphicalUIActivity m = this;
//        RadioService radioService = RetrofitManager.getRetrofit().create(RadioService.class);
//        Call<List<Station>> call = radioService.getStations();
//        radioService.getStations().enqueue(new Callback<List<Station>>() {
//            @Override
//            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
//                if (response.isSuccessful()) {
//
//                    mListStations = new ArrayList<>();
//                    mListStations.addAll(response.body());
//
//                    mAdapter = new StationAdapter(m, mListStations);
//                    listView.setAdapter(mAdapter);
//                }
//                else {
//
//                    switch (response.code()) {
//                        case 404:
//                            Log.v("error","not found");
//                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
//                            break;
//                        case 500:
//                            Log.v("error","server side error");
//                            // Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
//                            break;
//                        default:
//                            Log.v("error","unknown error");
//                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                }
//
//            }
//            @Override
//            public void onFailure(Call<List<Station>> call, Throwable t) {
//                Log.v("response",t.getMessage());
//            }
//        });
//    }
//}
