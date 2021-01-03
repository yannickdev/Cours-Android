package com.yannick.radioo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;


public class StationAdapter extends BaseAdapter {
    private Context mContext;
    private List<Station> stations;

    public StationAdapter(Context context, List<Station> stations) {
        mContext = context;
        this.stations = stations;
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Station getItem(int position) {
        return stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Station station = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.track_list_row, parent, false);
            holder = new ViewHolder();
            holder.circularImageView =  convertView.findViewById(R.id.track_image);
            holder.titleTextView     =  convertView.findViewById(R.id.track_title);
            holder.ratingTextView    =  convertView.findViewById(R.id.track_rating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!station.getFavicon().isEmpty()){
            Picasso.get().load(station.getFavicon()).into(holder.circularImageView);
        }
        else{
            holder.circularImageView.setImageResource(R.drawable.radio_default);
        }

        holder.titleTextView.setText(station.getName());
        holder.ratingTextView.setText("" + station.getVotes());

        return convertView;
    }

    static class ViewHolder {
        CircularImageView circularImageView;
        TextView titleTextView;
        TextView ratingTextView;
    }

    private class RetrieveImage extends AsyncTask<String, Void, Drawable> {

        private Exception exception;
        ViewHolder holder;
        View convertView;

        public RetrieveImage(ViewHolder holder,View convertView){
            this.holder=holder;
            this.convertView=convertView;
        }


        @Override
        protected Drawable doInBackground(String... urls) {
            InputStream is = null;
            Drawable d = null;
            try {
                if (urls[0].length() != 0) {
                    URI uri = new URI(urls[0]);//"http", urls[0].substring(7, urls[0].length()), null
                    Log.v("favicon",urls[0].substring(7, urls[0].length()));
                    URL url = uri.toURL();
                    is = (InputStream) url.getContent();//openStream();//getContent()
                    d = Drawable.createFromStream(is, "name");
                } else {

                    d  = mContext.getResources().getDrawable(R.drawable.radio_default);
                    Log.v("",d.toString());

                }
                return d;
            } catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
                return null;
            }
        }

            @Override
            protected void onPostExecute (Drawable d){
                holder.circularImageView.setImageDrawable(d);
            }
        }
    }



