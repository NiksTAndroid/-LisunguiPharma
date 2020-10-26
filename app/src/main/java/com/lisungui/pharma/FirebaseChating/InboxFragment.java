package com.lisungui.pharma.FirebaseChating;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lisungui.pharma.R;
import com.lisungui.pharma.utility.CommonUtility;
import com.lisungui.pharma.utility.PrefManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import static com.lisungui.pharma.utility.CommonUtility.updateUser;


public class InboxFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayoutManager linearLayoutManager;
    Context mContext;

    private RecyclerView RV_inbox_conversation_list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private HashMap<String, String> getProfileDetails;

    Query mDatabase;

    int totalUsers = 0;
    ProgressDialog pd;
    private String msg_receiver_userid, msg_sender_uid, Userstate;
    private String msg_receiver_name = "", msg_receiverpic, msg_sender_name, msg_senderpic;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();

    String recettext = null;
    String date = null;
    ArrayList<RecentMassageModel> inboxlistalldata;


    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        RV_inbox_conversation_list = view.findViewById(R.id.RV_inbox_conversation_list);
        getAllChatWithUsers();
        msg_sender_uid = PrefManager.getUserID(getContext());
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        Bundle b = getArguments();
        if (b != null) {

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUser(getContext(), true, ""+CommonUtility.getDateTime(), true);

        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.inbox);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        updateUser(getContext(), false, "0", true);

        super.onPause();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.findItem(R.id.action_cart).setVisible(false);

//        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onStop() {
        updateUser(getContext(), false, "0", true);

        super.onStop();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //### get all list users you or other
    private void getAllChatWithUsers() {


        inboxlistalldata = new ArrayList<>();
        inboxlistalldata.clear();

        msg_sender_uid = PrefManager.getUserID(getActivity());
        msg_sender_name = PrefManager.getUserName(getActivity());
        msg_senderpic = ""/*BASE_URL_FOR_PROFILE_IMAGES + getProfileDetails.get(SessionManager.KEY_PROFILE_PIC)*/;

        mDatabase = FirebaseDatabase.getInstance().getReference("chat_room");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    inboxlistalldata.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String key = snapshot.getKey();
                        String id;
                        /*if (msg_sender_uid.equals("4")) {
                            id = msg_sender_uid;
                        } else {*/
                            id = msg_sender_uid;
                       // }


                        if (key.contains(id)) {
                            RecentMassageModel rs = snapshot.child("recent")
                                    .getValue(RecentMassageModel.class);
                            rs.setKey(key);
                            inboxlistalldata.add(rs);

                        }
                    }

                    if (inboxlistalldata.size() > 1) {
                        Collections.sort(inboxlistalldata, new Comparator<RecentMassageModel>() {
                            @Override
                            public int compare(RecentMassageModel o1, RecentMassageModel o2) {
                                if (null == o1.getDate() || null == o2.getDate()) {
                                    return 0;
                                } else {
                                    return o1.getDate().compareTo(o2.getDate());
                                }
                            }
                        });

                    }


                    RV_inbox_conversation_list.scrollToPosition(inboxlistalldata.size() - 1);
                    RV_inbox_conversation_list.setLayoutManager(linearLayoutManager);

                    Collections.reverse(inboxlistalldata);

                    NewInboxChattingListAdapter inboxChattingListAdapter = new NewInboxChattingListAdapter(mContext, inboxlistalldata);
                    RV_inbox_conversation_list.setAdapter(inboxChattingListAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("DatabaseError", databaseError.toString());
            }
        });


    }


}