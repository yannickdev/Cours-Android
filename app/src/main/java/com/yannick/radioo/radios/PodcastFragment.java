package com.yannick.radioo.radios;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yannick.radioo.Podcast;
import com.yannick.radioo.PodcastDAO;
import com.yannick.radioo.R;

import java.util.List;


public class PodcastFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Context context;

    private PodcastDAO datasource;

    List<Podcast> lp;

    PodcastRecyclerViewAdapter adapter;


    public PodcastFragment() {
    }

    public static PodcastFragment newInstance(int columnCount) {
        PodcastFragment fragment = new PodcastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=this.getContext();
        datasource = new PodcastDAO(context);
        datasource.open();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_list, container, false);

        lp=datasource.getAllPodcasts();
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
                adapter = new PodcastRecyclerViewAdapter(lp, mListener);
                adapter.notifyItemInserted(lp.size());
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

        }
        return view;
    }

//    public void setAdapter(List<Podcast> results, RecyclerView recyclerView){
//        lp=datasource.getAllPodcasts();
//
//        adapter = new PodcastRecyclerViewAdapter(lp, mListener);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Podcast podcast);
    }
}
