package com.yannick.radioo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.util.List;


public class FavouriteAdapter extends BaseAdapter {

    private Context mContext;
    private List<Favourite> favourites;

    public FavouriteAdapter(Context context, List<Favourite> favourites) {
        mContext = context;
        this.favourites = favourites;
    }

    @Override
    public int getCount() {
        return favourites.size();
    }

    @Override
    public Favourite getItem(int position) {
        return favourites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Favourite station = getItem(position);

        FavouriteAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.favourite_list_row, parent, false);
            holder = new FavouriteAdapter.ViewHolder();
            holder.circularImageView =  convertView.findViewById(R.id.favourite_radio_image);
            holder.titleTextView     =  convertView.findViewById(R.id.favourite_title);
            holder.deleteTextView    =  convertView.findViewById(R.id.favourite_delete);
            convertView.setTag(holder);
        } else {
            holder = (FavouriteAdapter.ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(station.getName());
        holder.deleteTextView.setText("X" );

        holder.deleteTextView.setOnClickListener(new View.OnClickListener() {
            FavouriteDAO datasource = new FavouriteDAO(mContext);

            @Override
            public void onClick(View v) {
                Log.v("delete","ok delete favourite");
                datasource.open();
                datasource.deleteStation(station);
                favourites.remove(station);
                notifyDataSetChanged();
                datasource.close();
            }
        });

        File imgFile = new  File(station.getFavicon());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.circularImageView.setImageBitmap(myBitmap);
        }
        else{
            holder.circularImageView.setImageResource(R.drawable.radio_default);
        }
        return convertView;
    }

    static class ViewHolder {
        CircularImageView circularImageView;
        TextView titleTextView;
        TextView deleteTextView;
    }

}
