//package com.yannick.radioo;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FavouriteActivity extends AppCompatActivity {
//    private List<Station> mListStations;
//    private FavouriteAdapter mAdapter;
//    private ListView listView;
//    private Station selectedStation;
//    private Button mButtonFavouriteDelete;
//
//    @SuppressLint("WrongViewCast")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_favourite);
//
//        //mListStations.clear();
//
//        mButtonFavouriteDelete =  findViewById(R.id.favourite_delete);
//
//        StationDAO datasource = new StationDAO(this);
//        datasource.open();
//
//        mListStations = new ArrayList<>();
//        mListStations.addAll(datasource.getAllStations());
//
//        listView = (ListView) findViewById(R.id.favourite_list_view);
//
//        mAdapter = new FavouriteAdapter(FavouriteActivity.this, mListStations);
//        listView.setAdapter(mAdapter);
//
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
//                selectedStation = mListStations.get(position);
//                Intent intent = new Intent(FavouriteActivity.this, PlayingActivity.class);
//                intent.putExtra("STATION", selectedStation);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//
//}
