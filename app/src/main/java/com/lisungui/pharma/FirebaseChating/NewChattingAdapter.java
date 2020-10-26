package com.lisungui.pharma.FirebaseChating;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.FullScreenImage;
import com.lisungui.pharma.utility.PrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class NewChattingAdapter extends RecyclerView.Adapter<NewChattingAdapter.ViewHolder> {
    Context context;
    List<ChattingMassageModel> chattingList;
    String userid;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public NewChattingAdapter(List<ChattingMassageModel> chattingList, Context mContext) {
        this.chattingList = chattingList;
        this.context = mContext;
        userid = PrefManager.getUserID(context);
    }

    @NonNull
    @Override
    public NewChattingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitemview, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final NewChattingAdapter.ViewHolder holder, int position) {

        final ChattingMassageModel data = chattingList.get(position);
        if (data == null) {
            return;
        }

        String resulttime = null, resulttime2 = null;

        holder.senderimage.setVisibility(View.GONE);
        holder.senderlocation.setVisibility(View.GONE);
        holder.videoRightLayout.setVisibility(View.GONE);
        holder.sendpdf.setVisibility(View.GONE);
        holder.text.setVisibility(View.GONE);

        holder.receiverimage.setVisibility(View.GONE);
        holder.receiverlocation.setVisibility(View.GONE);
        holder.videoLeftLayout.setVisibility(View.GONE);
        holder.receciverpdf.setVisibility(View.GONE);
        holder.text2.setVisibility(View.GONE);

        if (userid.equalsIgnoreCase(data.getSenderID())) {//sender

            holder.rl1.setVisibility(View.VISIBLE);
            holder.rl2.setVisibility(View.GONE);


            switch (data.getType()) {
                case Constant.IMAGE:

                    holder.text.setVisibility(View.GONE);
                    holder.senderimage.setVisibility(View.VISIBLE);

                    final String url = data.getMassage_file();

                    Glide.with(context).load(url).into(holder.senderimage);

                    holder.senderimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(context, FullScreenImage.class);
                            holder.senderimage.buildDrawingCache();

                            Bundle extras = new Bundle();
                            extras.putString("imagebitmap", url);
                            intent.putExtras(extras);
                            context.startActivity(intent);
                        }
                    });

                    break;

                case Constant.LOCATION:


                    holder.text.setVisibility(View.GONE);
                    holder.senderlocation.setVisibility(View.VISIBLE);

                    holder.senderlocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Uri uri = Uri.parse("geo:" + data.getLocation_lat() + "," + data.getLocation_long() + "?q=" + data.getLocation_lat() + "," + data.getLocation_long());
                            Intent in = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(in);

                        }
                    });

                    break;

                case Constant.VIDEO:
                   /* String videourl = data.getMassage_file();

                    holder.text.setVisibility(View.GONE);
                    holder.videoRightLayout.setVisibility(View.VISIBLE);

                    Glide.with(context)
                            .asBitmap()
                            .load(videourl)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(holder.videorightImage);

                    holder.videoRightLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(context, ActivityVideoPlayer.class)
                                    .putExtra("video_url", data.getMassage_file()));
                        }
                    });*/
                    break;

                case Constant.DOCUMENT:

                    holder.text.setVisibility(View.GONE);
                    holder.sendpdf.setVisibility(View.VISIBLE);
                    holder.sendpdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String pdf2 = data.getMassage_file();

                            if (pdf2 != null) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf2));
                                context.startActivity(browserIntent);
                            } else {

                                Log.e("pdf", "onClick: null url2");
                            }

                        }
                    });

                    break;

                default:
                    holder.text.setVisibility(View.VISIBLE);
                    holder.text.setText(Html.fromHtml(data.getMassage_file()));
                    break;
            }

            holder.sendername.setText(data.getSenderName());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            try {
                Date m1Date = sdf.parse(data.getDate());
                long timeInMilliseconds = m1Date.getTime();
                resulttime = getTimeAgo(timeInMilliseconds);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.date.setText(resulttime);

            if (data.isRead()) {

                holder.right_status_read_unread.setBackground(context.getResources().getDrawable(R.drawable.doublechcek_green));

            } else {
                holder.right_status_read_unread.setBackground(context.getResources().getDrawable(R.drawable.doublecheck_gray));
            }

        }


        if (!userid.equalsIgnoreCase(data.getSenderID())) {//receiver
            holder.rl1.setVisibility(View.GONE);
            holder.rl2.setVisibility(View.VISIBLE);

            switch (data.getType()) {
                case Constant.IMAGE:
                    holder.receiverimage.setVisibility(View.VISIBLE);
                    final String url = data.getMassage_file();

                    Glide.with(context).load(url).into(holder.receiverimage);

                    holder.receiverimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            holder.senderimage.buildDrawingCache();
                            Bitmap image = holder.senderimage.getDrawingCache();
                            Bundle extras = new Bundle();
                            extras.putString("imagebitmap", url);
                            intent.putExtras(extras);
                            context.startActivity(intent);
                        }
                    });


                    break;
                case Constant.LOCATION:

                    holder.text2.setVisibility(View.GONE);
                    holder.receiverlocation.setVisibility(View.VISIBLE);

                    holder.receiverlocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Uri gmmIntentUri = Uri.parse("geo:" + data.getLocation_lat() + "," + data.getLocation_long() + "?q=" + data.getLocation_lat() + "," + data.getLocation_long());
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    context.startActivity(mapIntent);
                                }
                            }, 1000);
                        }
                    });


                    break;
                case Constant.VIDEO:

                 /*   holder.text2.setVisibility(View.GONE);
                    holder.videoLeftLayout.setVisibility(View.VISIBLE);

                    Glide.with(context)
                            .asBitmap()
                            .load(data.getMassage_file())
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(holder.videoleftImage);

                    holder.videoLeftLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(context, ActivityVideoPlayer.class)
                                    .putExtra("video_url", data.getMassage_file()));
                        }
                    });*/
                    break;
                case Constant.DOCUMENT:
                    final String url1 = data.getMassage_file();


                    holder.receciverpdf.setVisibility(View.VISIBLE);
                    holder.receciverpdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (url1 != null) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                                context.startActivity(browserIntent);
                            }
                        }
                    });
                    break;
                default:
                    holder.text2.setVisibility(View.VISIBLE);
                    holder.text2.setText(data.getMassage_file());
                    break;
            }

            holder.receivername.setText(data.getSenderName());

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            try {
                Date m1Date = sdf.parse(data.getDate());

                long timeInMilliseconds = m1Date.getTime();

                resulttime2 = getTimeAgo(timeInMilliseconds);


            } catch (Exception e) {
                e.printStackTrace();
                Log.v("EROR",e.toString());
            }

            holder.date2.setText(resulttime2);


        }

    }

    @Override
    public int getItemCount() {
        return chattingList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl1, rl2;
        private TextView text, date, sendername;
        private TextView receivername, text2, date2;
        private ImageView senderimage, senderlocation, receiverimage, receiverlocation;
        RelativeLayout videoRightLayout, videoLeftLayout;
        private ImageView sendpdf, receciverpdf, videorightImage, videoleftImage;
        private ImageView right_status_read_unread;

        public ViewHolder(View itemView) {
            super(itemView);
            rl1 = itemView.findViewById(R.id.Relativelayout1);
            rl2 = itemView.findViewById(R.id.Relativelayout2);
            //sender
            sendername = itemView.findViewById(R.id.text_username);
            senderimage = itemView.findViewById(R.id.senderimage);
            text = itemView.findViewById(R.id.text_msg);
            date = itemView.findViewById(R.id.text_time);
            senderlocation = itemView.findViewById(R.id.senderlocationview);

            //receiver
            receivername = itemView.findViewById(R.id.tv_recivername);
            receiverimage = itemView.findViewById(R.id.reciverimage);
            text2 = itemView.findViewById(R.id.tv_recivermsg);
            date2 = itemView.findViewById(R.id.tv_reciversendtime);
            receiverlocation = itemView.findViewById(R.id.recieverlocationview);

            sendpdf = itemView.findViewById(R.id.RdocumentImageView);
            videorightImage = itemView.findViewById(R.id.videorightImage);
            videoleftImage = itemView.findViewById(R.id.videoleftImage);

            videoRightLayout = itemView.findViewById(R.id.videoRightLayout);
            videoLeftLayout = itemView.findViewById(R.id.videoLeftLayout);

            receciverpdf = itemView.findViewById(R.id.LdocumentImageView);

            right_status_read_unread = itemView.findViewById(R.id.right_status_read_unread);
        }
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = 0;
        //long now = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy hh:mm:ss");
        String test = sdf.format(cal.getTime());
        long millis = cal.getTimeInMillis();
        Log.e("TEST", "" + millis);

        try {
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(test);
            System.out.println("Given Time in milliseconds : " + date.getTime());

            Calendar calendar = Calendar.getInstance();
            //Setting the Calendar date and time to the given date and time
            calendar.setTime(date);
            System.out.println("Given Time in milliseconds : " + calendar.getTimeInMillis());
            now = date.getTime();


        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < SECOND_MILLIS) {
            return "now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1m ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1hr ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

}
