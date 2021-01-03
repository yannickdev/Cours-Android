//package com.yannick.radioo;
//
//import androidx.appcompat.app.AppCompatActivity;
//
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
//public class PodcastActivity extends AppCompatActivity {
//
//    private List<Podcast> mListPodcasts;
//    private PodcastAdapter mAdapter;
//    private ListView listView;
//    private Podcast selectedPodcast;
//    private Button mButtonPocastDelete;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_podcast);
//
//        PodcastDAO datasource = new PodcastDAO(this);
//        datasource.open();
//
//        mListPodcasts = new ArrayList<>();
//        mListPodcasts.addAll(datasource.getAllPodcasts());
//
//        for(Podcast p:datasource.getAllPodcasts()){
//            Log.v("podcast: ",""+p);
//        }
//
//        listView = (ListView) findViewById(R.id.podcasts_list_view);
//
//        mAdapter = new PodcastAdapter(PodcastActivity.this, mListPodcasts);
//        listView.setAdapter(mAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
//
//                selectedPodcast = mListPodcasts.get(position);
//
//                Log.v("selectedPodcast",""+selectedPodcast.getFileUrl()) ;
//
//                Intent intent = new Intent(PodcastActivity.this, PlayingActivity.class);
//                intent.putExtra("PODCAST", selectedPodcast);
//                startActivity(intent);
//            }
//        });
//    }
//}
