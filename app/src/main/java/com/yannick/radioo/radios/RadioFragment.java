package com.yannick.radioo.radios;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yannick.radioo.R;
import com.yannick.radioo.RadioService;
import com.yannick.radioo.RetrofitManager;
import com.yannick.radioo.Station;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RadioFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    static ArrayList<Station> mListStations = new ArrayList<Station>();

    public RadioFragment() {

    }

    public static RadioFragment newInstance(int columnCount) {
        RadioFragment fragment = new RadioFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onSaveInstanceState(Bundle bundle)
//    {
//        super.onSaveInstanceState(bundle);
//        bundle.putParcelableArrayList("RADIOLIST" , mListStations);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // getAllStation();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {


        final View view= inflater.inflate(R.layout.fragment_radio_list, container, false);
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
        return view;
    }


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
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Station item);
    }

//    public static void getAllStation() {
//
//        RadioService radioService = RetrofitManager.getRetrofit().create(RadioService.class);
//        Call<List<Station>> call = radioService.getStations();
//        radioService.getStations().enqueue(new Callback<List<Station>>() {
//            @Override
//            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
//                if (response.isSuccessful()) {
//                    mListStations.addAll(response.body());
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

}
