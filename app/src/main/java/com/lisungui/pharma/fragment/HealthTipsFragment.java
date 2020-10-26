package com.lisungui.pharma.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.MainActivity;
import com.lisungui.pharma.adapter.HealthTipsNotificationAdapter;
import com.lisungui.pharma.adapter.NotificationAdapter;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.HealthTipData;
import com.lisungui.pharma.models.HealthTipDataDetails;
import com.lisungui.pharma.models.Notification;
import com.lisungui.pharma.rest.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthTipsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private String user_id;
    private SharedPreferences pref;

    private ProgressDialog pDialog;

    private TextView txt_empty;
    private ListView list_pharma;
    private TextView txt_title;
    private NotificationAdapter adapter;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private SQLiteDataBaseHelper dataBaseHelper;
    private Spanned titles;

    private List<HealthTipDataDetails> healthTipDataDetailsList;

    public HealthTipsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HealthTipsFragment newInstance(String param1, String param2) {
        HealthTipsFragment fragment = new HealthTipsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to make visible/invisible the icons/logos in menu on actionbar
        setHasOptionsMenu(true);
        //Toast.makeText(context,"on Create HealthNotification", Toast.LENGTH_LONG ).show();

        dataBaseHelper = new SQLiteDataBaseHelper(getActivity());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Toast.makeText(context,"on CreateView HealthNotification", Toast.LENGTH_LONG ).show();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health_tips, container, false);

        init(view);

        pref = ((MainActivity)getActivity()).getSharedPreferences("Pharmacy", 0);
        user_id = pref.getString("USER_ID", "");

        healthTipDataDetailsList = new ArrayList<>();

        if(Locale.getDefault().getLanguage().equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + "Health tips " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + "Health tips " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + getString(R.string.health_tips)+" " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_not_list2) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + getString(R.string.health_tips) +" "+ "</font>" + "<font color=\"#729619\">" + getString(R.string.str_not_list2) + "</font>"));
            }
        }

        txt_title.setText(titles);

        //getFromDB();

//        if(notifications.size() == 0) {
//            txt_empty.setText(getString(R.string.str_no_not));
//            txt_empty.setVisibility(View.VISIBLE);
//            list_pharma.setVisibility(View.GONE);
//        } else {
//            txt_empty.setVisibility(View.GONE);
//            list_pharma.setVisibility(View.VISIBLE);
//        }
        //setListAdapter();

        //displayHealthTipNotifications(user_id);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);

        menu.findItem(R.id.call_help_item).setVisible(false);
        menu.findItem(R.id.action_cart).setVisible(false);

//        inflater.inflate(R.menu.menu, menu);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        //Toast.makeText(context,"on Attach HealthNotification", Toast.LENGTH_LONG ).show();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(getString(R.string.str_loading));
        pDialog.setCancelable(false);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Toast.makeText(context,"on Detach HealthNotification", Toast.LENGTH_LONG ).show();
    }

    private void getFromDB() {
        notifications = dataBaseHelper.getAllNotification();
        dataBaseHelper.closeDB();
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressDialog();
        displayHealthTipNotifications(user_id);
        //Toast.makeText(context,"on Resume HealthNotification", Toast.LENGTH_LONG ).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Toast.makeText(context,"on Start HealthNotification", Toast.LENGTH_LONG ).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Toast.makeText(context,"on Stop HealthNotification", Toast.LENGTH_LONG ).show();
    }

    private void setListAdapter() {
        adapter = new NotificationAdapter(getActivity(), notifications);
        list_pharma.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void init(View view) {
        txt_empty = view.findViewById(R.id.txt_empty);
        list_pharma = view.findViewById(R.id.list_pharma);
        txt_title = view.findViewById(R.id.txt_title);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void displayHealthTipNotifications(String user_id)
    {
        RestClient.RestApiInterface service = RestClient.getClient();
        Call<HealthTipData> call = service.getHealthTipsNotifications(user_id);
        call.enqueue(new Callback<HealthTipData>() {
            @Override
            public void onResponse(Call<HealthTipData> call, Response<HealthTipData> response) {
                hideProgressDialog();
                if(response.isSuccessful())
                {
                    if(response.body().getStatus() == 1) {
                        healthTipDataDetailsList = response.body().getHealthtip();
                        //ListView healthTipsNotificationsListView = (ListView)getView().findViewById(R.id.list_pharma);
                        HealthTipsNotificationAdapter healthTipsNotificationAdapter = new HealthTipsNotificationAdapter(context, healthTipDataDetailsList);
                        list_pharma.setAdapter(healthTipsNotificationAdapter);
                        list_pharma.smoothScrollToPosition(healthTipDataDetailsList.size());
                        healthTipsNotificationAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        String str_check_int = getResources().getString(R.string.data_not_available);
                        Toast.makeText(context, str_check_int, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<HealthTipData> call, Throwable t) {
                hideProgressDialog();
                //Log.d(TAG, "onFailure");
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(context, str_check_int, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
