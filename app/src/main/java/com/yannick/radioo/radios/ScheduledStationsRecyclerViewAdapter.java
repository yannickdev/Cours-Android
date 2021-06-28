package com.yannick.radioo.radios;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yannick.radioo.FavouriteDAO;
import com.yannick.radioo.R;
import com.yannick.radioo.ScheduledStation;
import com.yannick.radioo.ScheduledStationDAO;
import com.yannick.radioo.Station;
import com.yannick.radioo.radios.ScheduledStationsFragment.OnListFragmentInteractionListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ScheduledStationsRecyclerViewAdapter extends RecyclerView.Adapter<ScheduledStationsRecyclerViewAdapter.ViewHolder> {

    private final List<ScheduledStation> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    private List<ScheduledStation> suggestions, tempItems;

    public ScheduledStationsRecyclerViewAdapter(List<ScheduledStation> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheduled_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Return the Intent for PendingIntent.
     * Return null in case of some (impossible) errors: see Android source.
     * @throws IllegalStateException in case of something goes wrong.
     * See {@link Throwable#getCause()} for more details.
     */
    public Intent getIntent(PendingIntent pendingIntent) throws IllegalStateException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getIntent = PendingIntent.class.getDeclaredMethod("getIntent");
        return (Intent) getIntent.invoke(pendingIntent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        if (!holder.mItem.getFavicon().isEmpty()) {
//            Picasso.get().load(holder.mItem.getFavicon()).into(holder.circularImageView);
//        } else {
//            holder.circularImageView.setImageResource(R.drawable.radio_default);
//        }

       // Intent intent = new Intent();
       // String status = intent.getStringExtra("status");
        //System.out.println("on a pour status: "+status);
        holder.titleTextView.setText(mValues.get(position).getName());

        //holder.statusTextView.setText(status);
        holder.statusTextView.setText("waiting");

        holder.deleteTextView.setText("X" );

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

        holder.deleteTextView.setOnClickListener(new View.OnClickListener() {
            ScheduledStationDAO datasource = new ScheduledStationDAO(context);
            @Override
            public void onClick(View v) {

                Intent intent =new Intent("com.yannick.radioo");
                Log.v("Valeur scheduled id",""+(int)holder.mItem.getId());
                PendingIntent pendingIntent = PendingIntent.getService(context, (int)holder.mItem.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                System.out.println("pending intent: "+pendingIntent);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();

                Log.v("delete","ok delete scheduled station");
                datasource.open();
                datasource.deleteStation(holder.mItem);
                mValues.remove(holder.mItem);
                notifyDataSetChanged();
                datasource.close();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final CircularImageView circularImageView;
        public final TextView titleTextView;
        public final TextView statusTextView;
        public final TextView deleteTextView;
        public ScheduledStation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           // circularImageView = view.findViewById(R.id.track_image);
            titleTextView = view.findViewById(R.id.item_title);
            statusTextView = view.findViewById(R.id.item_status);
            deleteTextView = view.findViewById(R.id.item_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleTextView.getText() + "'";
        }
    }
}
