package com.lisungui.pharma.FirebaseChating;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lisungui.pharma.R;
import com.lisungui.pharma.models.UserFirebaseModel;
import com.lisungui.pharma.utility.CommonUtility;
import com.lisungui.pharma.utility.PrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.lisungui.pharma.utility.CommonUtility.getTimeAgo;


public class NewInboxChattingListAdapter extends RecyclerView.Adapter<NewInboxChattingListAdapter.MyViewHolder> {


    private Context context;

    private FragmentManager fragmentManager;
    long timeInMilliseconds;


    private String Userstate;
    private int pos;
    ArrayList<RecentMassageModel> list;

    public NewInboxChattingListAdapter(Context context, ArrayList<RecentMassageModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_for_conversation_list, parent, false);
        return new MyViewHolder(v);
    }


    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        RecentMassageModel data = list.get(position);
        holder.fabCounter.setVisibility(View.INVISIBLE);

        pos = position;


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date m1Date = sdf.parse(data.getDate());
            timeInMilliseconds = m1Date.getTime();
            holder.txt_recentdatetime.setText(getTimeAgo(timeInMilliseconds));
            holder.txt_recent_message.setText(data.getMassage_file());
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ChattingFragment chatFragment_ = new ChattingFragment();
                fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("PHARMACYID", holder.user2Id);
                bundle.putString("TYPE", holder.Type);
                bundle.putString("PHARMACYNAME", holder.user2Name);
                chatFragment_.setArguments(bundle);
                holder.unread = 0;
                notifyDataSetChanged();
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, chatFragment_, chatFragment_.getTag())
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .commit();
                }
            }
        });


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("chat_room")
                .child(data.getKey())
                .child("Massages");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                    boolean isread = (boolean) uniqueKeySnapshot.child("read").getValue();

                    String id = (String) uniqueKeySnapshot.child("senderID").getValue();

                    if (!isread && !id.equalsIgnoreCase(PrefManager.getUserID(context))) {
                        holder.unread++;
                        holder.fabCounter.setVisibility(View.VISIBLE);
                        holder.card_view_in_progress.setCardBackgroundColor(context.getResources().getColor(R.color.gray_bagro));
                        holder.fabCounter.setText(String.valueOf(holder.unread));
                    }
                    if (id.equalsIgnoreCase(PrefManager.getUserID(context))) {
                        if (holder.Type == null) {
                            holder.Type = (String) uniqueKeySnapshot.child("receiverType").getValue();
                        }
                    } else {
                        if (holder.Type == null) {
                            holder.Type = (String) uniqueKeySnapshot.child("senderType").getValue();
                        }
                    }

                    if (holder.mUser_state == null) {
                        String idd = holder.Type + "_" + holder.user2Id;
                        holder.mUser_state = CommonUtility.getUserDatabase(idd);

                        holder.mUser_state.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {

                                    UserFirebaseModel user = dataSnapshot.getValue(UserFirebaseModel.class);

                                    try {

                                        if (!user.isUserStatus()) {

                                            holder.off.setVisibility(View.VISIBLE);
                                            holder.on.setVisibility(View.GONE);

                                        } else {

                                            holder.off.setVisibility(View.GONE);
                                            holder.on.setVisibility(View.VISIBLE);

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        holder.off.setVisibility(View.VISIBLE);
                                        holder.on.setVisibility(View.GONE);
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }


        });


        String id = String.valueOf(PrefManager.getUserID(context));

        if (data.getUser2ID().equals(id)) {
            holder.user2Id = data.getUser1ID();
            holder.user2Name = data.getUser1Name();
        } else {
            holder.user2Id = data.getUser2ID();
            holder.user2Name = data.getUser2Name();
        }


        holder.txt_reciver_user_name.setText(holder.user2Name);

    }

/*

    private String getOnlineStatus(RecyclerView.ViewHolder viewHolder) {
        mUser_state = FirebaseDatabase.getInstance().getReference("users")
                .child(arrayList.get(pos).getOther_id()).child("state");
        mUser_state.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    try {
                        Userstate = dataSnapshot.getValue().toString();

                        if (Userstate.equalsIgnoreCase("offline")) {
                            viewholder.off.setVisibility(View.VISIBLE);
                            viewholder.on.setVisibility(View.GONE);
                        } else {
                            viewholder.off.setVisibility(View.GONE);
                            viewholder.on.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return Userstate;
    }
*/

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000L);
        String date = android.text.format.DateFormat.format("dd-MMM-yyyy", cal).toString();
        return date;
    }


    @Override
    public int getItemCount() {

        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView card_view_in_progress;
        private TextView txt_comment;
        private TextView txt_recentdatetime;
        private CircleImageView on, off;
        TextView fabCounter;
        private DatabaseReference mUser_state;
        String user2Id = null;
        String user2Name = null;
        private TextView txt_reciver_user_name;
        private CircleImageView ReceiverPic;
        private TextView txt_recent_message;
        private RatingBar RB_home_products;
        int unread = 0;
        String Type;

        //        private RelativeLayout Rlrootlayout;
        public MyViewHolder(View v) {
            super(v);

            ReceiverPic = v.findViewById(R.id.img_profile_pic);
            txt_reciver_user_name = v.findViewById(R.id.txt_user_name);
            txt_recent_message = v.findViewById(R.id.txt_message);
            txt_recentdatetime = v.findViewById(R.id.txt_dt);
            on = v.findViewById(R.id.on);
            off = v.findViewById(R.id.off);
            fabCounter = v.findViewById(R.id.fabCounter);
            // btn_confirm=v.findViewById(R.id.btn_confirm);
            card_view_in_progress = v.findViewById(R.id.rootlayout1);

        }

    }


}

