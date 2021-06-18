package com.yannick.radioo.radios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yannick.radioo.R;
import com.yannick.radioo.ScheduledStation;
import com.yannick.radioo.Station;
import com.yannick.radioo.radios.ScheduledStationsFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class ScheduledStationsRecyclerViewAdapter extends RecyclerView.Adapter<ScheduledStationsRecyclerViewAdapter.ViewHolder> {

    private final List<ScheduledStation> mValues;
    private final OnListFragmentInteractionListener mListener;

    private List<ScheduledStation> suggestions, tempItems;

    public ScheduledStationsRecyclerViewAdapter(List<ScheduledStation> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_radio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        if (!holder.mItem.getFavicon().isEmpty()) {
//            Picasso.get().load(holder.mItem.getFavicon()).into(holder.circularImageView);
//        } else {
//            holder.circularImageView.setImageResource(R.drawable.radio_default);
//        }

        holder.titleTextView.setText(mValues.get(position).getName());
        holder.ratingTextView.setText("" + mValues.get(position).getVotes());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircularImageView circularImageView;
        public final TextView titleTextView;
        public final TextView ratingTextView;
        public Station mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            circularImageView = view.findViewById(R.id.track_image);
            titleTextView = view.findViewById(R.id.track_title);
            ratingTextView = view.findViewById(R.id.track_rating);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleTextView.getText() + "'";
        }
    }
}
