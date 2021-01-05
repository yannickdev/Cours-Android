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
import com.yannick.radioo.Favourite;
import com.yannick.radioo.R;
import com.yannick.radioo.FavouriteDAO;

import java.io.File;
import java.util.List;

public class FavouriteRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder> {

    private final List<Favourite> mValues;
    private final FavouriteFragment.OnListFragmentInteractionListener mListener;
    private Context context;

    public FavouriteRecyclerViewAdapter(List<Favourite> items, FavouriteFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public FavouriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favourite, parent, false);
        return new FavouriteRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavouriteRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.titleTextView.setText(holder.mItem.getName());
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
            FavouriteDAO datasource = new FavouriteDAO(context);
            @Override
            public void onClick(View v) {
                Log.v("delete","ok delete favourite");
                datasource.open();
                datasource.deleteStation(holder.mItem);
                mValues.remove(holder.mItem);
                notifyDataSetChanged();
                datasource.close();
            }
        });

        File imgFile = new  File(holder.mItem.getFavicon());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.circularImageView.setImageBitmap(myBitmap);
        }
        else{
            holder.circularImageView.setImageResource(R.drawable.radio_default);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircularImageView circularImageView;
        public final TextView titleTextView;
        public final TextView deleteTextView;
        public Favourite mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            circularImageView = view.findViewById(R.id.favourite_radio_image);
            titleTextView = view.findViewById(R.id.favourite_title);
            deleteTextView = view.findViewById(R.id.favourite_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleTextView.getText() + "'";
        }
    }


}
