package com.lisungui.pharma.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.CallHelpActivity;
import com.lisungui.pharma.activities.MainActivity;
import com.lisungui.pharma.activities.OrderTackingActivity;
import com.lisungui.pharma.activities.CountryPlaceOrderActivity;
import com.lisungui.pharma.activities.PH_AllOrdersActivity;
import com.lisungui.pharma.activities.PlaceOrderActivity;
import com.lisungui.pharma.utility.PrefManager;

import ss.com.bannerslider.views.BannerSlider;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //NewHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewHomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private Context context;
    private View view;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private BannerSlider bannerSlider;
    private String title;
    private ImageButton pharmaciesondutyImageButton;
    private ImageButton pricesordersImageButton;
    private ImageButton treatmentImageButton;
    private ImageButton healthtipsImageButton;
    private ImageButton deliverytrackingImageButton;
    private ImageButton notificationsImageButton;
    private ImageView bannerImageView;
    private RelativeLayout bannerRelativeLayout;
    private int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 52;


    private void initValues(View view) {
        fragmentManager = ((MainActivity) getActivity()).getSupportFragmentManager();

        bannerSlider = (BannerSlider) ((MainActivity) getActivity()).findViewById(R.id.banner_slider1);

        bannerImageView = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.banner_imageview);
        bannerRelativeLayout = (RelativeLayout) ((MainActivity) getActivity()).findViewById(R.id.banner_rl);

        TextView textView = view.findViewById(R.id.tx5);

        pharmaciesondutyImageButton = view.findViewById(R.id.button1);
        pricesordersImageButton = view.findViewById(R.id.button2);
        treatmentImageButton = view.findViewById(R.id.button3);

        healthtipsImageButton = view.findViewById(R.id.button4);
        deliverytrackingImageButton = view.findViewById(R.id.button5);
        notificationsImageButton = view.findViewById(R.id.button6);

        if (PrefManager.getUserType(getActivity()).equals("4")) {
            textView.setText(getString(R.string.str_orders));
            //treatmentImageButton.setBackground(getActivity().getDrawable(R.drawable.stick_man));
            view.findViewById(R.id.rl2).setVisibility(View.GONE);
            view.findViewById(R.id.rl5).setVisibility(View.GONE);
            view.findViewById(R.id.rl6).setVisibility(View.GONE);
        }
    }

    public NewHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewHomeFragment newInstance(String param1, String param2) {
        NewHomeFragment fragment = new NewHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button1:
                fragment = new HomeFragment();
                title = getString(R.string.pharmaacies_on_duty);
                bannerSlider.setVisibility(View.GONE);
                bannerRelativeLayout.setVisibility(View.GONE);
                break;

            case R.id.button2:
                fragment = new PriceFragment();
                title = getString(R.string.str_check_med);
                bannerSlider.setVisibility(View.GONE);
                bannerRelativeLayout.setVisibility(View.GONE);
                /*fragment = new PriceFragment();
                title = "Countries";
                bannerSlider.setVisibility(View.GONE);
                bannerRelativeLayout.setVisibility(View.GONE);*/
                break;

            case R.id.button3:


                if (PrefManager.getUserType(getActivity()).equals("4")) {
                    Intent intent = new Intent(context, PH_AllOrdersActivity.class);
                    startActivity(intent);
                } else {
                    fragment = new CountriesFragment();
                    title = getString(R.string.Countries);
                    bannerSlider.setVisibility(View.GONE);
                    bannerRelativeLayout.setVisibility(View.GONE);
                }
                break;

            case R.id.button4:
                fragment = new HealthTipsFragment();
                title = getString(R.string.health_tips);
                bannerSlider.setVisibility(View.GONE);

                bannerRelativeLayout.setVisibility(View.GONE);
                break;

            case R.id.button5:
                Intent intent1 = new Intent(context, OrderTackingActivity.class);
                startActivity(intent1);
                break;

            case R.id.button6:

                /*
                fragment = new NotificationFragment();
                //fragment = new NewNotificationFragment();
                title = getString(R.string.str_notification);
                bannerSlider.setVisibility(View.GONE);
                bannerRelativeLayout.setVisibility(View.GONE);*/

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!checkPermissionForWriteExtertalStorage()) {
                        try {
                            requestPermissionForWriteExtertalStorage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Intent intent4 = new Intent(context, PlaceOrderActivity.class);

                        intent4.putExtra("camera_intent", "CAMERA");

                        startActivity(intent4);
                    }
                }
                else {
                    Intent intent5 = new Intent(context, PlaceOrderActivity.class);

                    intent5.putExtra("camera_intent", "CAMERA");

                    startActivity(intent5);
                }
                break;
        }
        if (view.getId() == (R.id.button1) || view.getId() == (R.id.button2) || view.getId() == (R.id.button3) || view.getId() == (R.id.button4) /*|| view.getId() == (R.id.button6)*/) {
            if(fragment!=null) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
            }
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);

        menu.findItem(R.id.call_help_item).setVisible(true);

//        inflater.inflate(R.menu.menu, menu);
    }

    public boolean checkPermissionForWriteExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    //if (it) above method:checkPermissionForReadExtertalStorage(), return false then call below method, it will show a dialog for permission
    public void requestPermissionForWriteExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.call_help_item) {

            Intent intent1 = new Intent(context, CallHelpActivity.class);
            startActivity(intent1);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to make visible/invisible the icons/logos in menu on actionbar
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_home, container, false);

        initValues(view);

        pharmaciesondutyImageButton.setOnClickListener(this);
        pricesordersImageButton.setOnClickListener(this);
        treatmentImageButton.setOnClickListener(this);
        healthtipsImageButton.setOnClickListener(this);
        deliverytrackingImageButton.setOnClickListener(this);
        notificationsImageButton.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
/*
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
/*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
/*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
*/
}
