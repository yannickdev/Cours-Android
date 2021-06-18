package com.yannick.radioo.radios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.yannick.radioo.Podcast;
import com.yannick.radioo.PodcastDAO;
import com.yannick.radioo.R;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.File;
import java.util.List;

public class PodcastRecyclerViewAdapter extends RecyclerView.Adapter<PodcastRecyclerViewAdapter.ViewHolder> {

    private final List<Podcast> mValues;
    private final PodcastFragment.OnListFragmentInteractionListener mListener;

    private Context context;

    public PodcastRecyclerViewAdapter(List<Podcast> items, PodcastFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public PodcastRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_podcast, parent, false);

        return new PodcastRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PodcastRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.titleTextView.setText(holder.mItem.getTitle());

        Duration duration = new Duration(holder.mItem.getDuration());
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix("d")
                .appendHours()
                .appendSuffix("h")
                .appendMinutes()
                .appendSuffix("m")
                .appendSeconds()
                .appendSuffix("s")
                .toFormatter();
        String formatted = formatter.print(duration.toPeriod());

        holder.durationTextView.setText(formatted);

        holder.deleteTextView.setOnClickListener(new View.OnClickListener() {
            PodcastDAO datasource = new PodcastDAO(context);

            @Override
            public void onClick(View v) {
                Log.v("delete", "ok delete podcast");
                datasource.open();
                datasource.deletePodcast(holder.mItem);
                mValues.remove(holder.mItem);
                notifyDataSetChanged();
                datasource.close();
            }
        });

        File imgFile = new File(holder.mItem.getFaviconUrl());

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.circularImageView.setImageBitmap(myBitmap);
        } else {
            holder.circularImageView.setImageResource(R.drawable.radio_default);
        }

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

    public void update(List<Podcast> list){
       // mValues.clear();
        mValues.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircularImageView circularImageView;
        public final TextView titleTextView;
        public final TextView durationTextView;
        public final TextView deleteTextView;
        public Podcast mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            circularImageView = view.findViewById(R.id.radio_image);
            titleTextView = view.findViewById(R.id.podcast_title);
            durationTextView = view.findViewById(R.id.podcast_duration);
            deleteTextView = view.findViewById(R.id.podcast_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleTextView.getText() + "'";
        }

    }

}
