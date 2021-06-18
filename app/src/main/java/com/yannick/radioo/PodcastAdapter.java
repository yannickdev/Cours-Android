//package com.yannick.radioo;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.mikhaellopez.circularimageview.CircularImageView;
//
//import org.joda.time.Duration;
//import org.joda.time.format.PeriodFormatter;
//import org.joda.time.format.PeriodFormatterBuilder;
//
//import java.io.File;
//import java.util.List;
//
//public class PodcastAdapter extends BaseAdapter {
//
//    private Context mContext;
//    private List<Podcast> podcasts;
//
//    public PodcastAdapter(Context context, List<Podcast> podcasts) {
//        mContext = context;
//        this.podcasts = podcasts;
//    }
//
//    @Override
//    public int getCount() {
//        return podcasts.size();
//    }
//
//    @Override
//    public Podcast getItem(int position) {
//        return podcasts.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        final Podcast podcast = getItem(position);
//
//        PodcastAdapter.ViewHolder holder;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.podcast_list_row, parent, false);
//            holder = new PodcastAdapter.ViewHolder();
//            holder.circularImageView =  convertView.findViewById(R.id.radio_image);
//            holder.titleTextView     =  convertView.findViewById(R.id.podcast_title);
//            holder.durationTextView  =  convertView.findViewById(R.id.podcast_duration);
//            holder.deleteTextView    =  convertView.findViewById(R.id.podcast_delete);
//            convertView.setTag(holder);
//        } else {
//            holder = (PodcastAdapter.ViewHolder) convertView.getTag();
//        }
//
//        holder.titleTextView.setText(podcast.getTitle());
//
//        Duration duration = new Duration(podcast.getDuration());
//        PeriodFormatter formatter = new PeriodFormatterBuilder()
//                .appendDays()
//                .appendSuffix("d")
//                .appendHours()
//                .appendSuffix("h")
//                .appendMinutes()
//                .appendSuffix("m")
//                .appendSeconds()
//                .appendSuffix("s")
//                .toFormatter();
//        String formatted = formatter.print(duration.toPeriod());
//
//        holder.durationTextView.setText(formatted);
//
//        holder.deleteTextView.setOnClickListener(new View.OnClickListener() {
//            PodcastDAO datasource = new PodcastDAO(mContext);
//
//            @Override
//            public void onClick(View v) {
//                Log.v("delete","ok delete podcast");
//                datasource.open();
//                datasource.deletePodcast(podcast);
//                podcasts.remove(podcast);
//                notifyDataSetChanged();
//                datasource.close();
//            }
//        });
//
//        File imgFile = new  File(podcast.getFaviconUrl());
//
//        if(imgFile.exists()){
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            holder.circularImageView.setImageBitmap(myBitmap);
//        }
//        else{
//            holder.circularImageView.setImageResource(R.drawable.radio_default);
//        }
//        return convertView;
//    }
//
//    static class ViewHolder {
//        CircularImageView circularImageView;
//        TextView titleTextView;
//        TextView durationTextView;
//        TextView deleteTextView;
//    }
//
//}
