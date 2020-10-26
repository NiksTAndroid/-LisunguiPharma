package com.lisungui.pharma.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.CallHelpActivity;
import com.lisungui.pharma.activities.MainActivity;
import com.lisungui.pharma.adapter.CountryMedicineListAdapter;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.CountryMedicineList;
import com.lisungui.pharma.models.CountryMedicinesPojo;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.CommonUtility;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountriesPriceFragment extends Fragment {

    private static final String TAG = CountriesPriceFragment.class.getSimpleName();
    private CountryMedicineListAdapter mAdapter;
    private ArrayList<CountryMedicineList> mMedList = new ArrayList<>();
//    private TextView mtTitle;
    private EditText mESearch;
    private ImageView mISearch;
    private ListView mLMed;
    private Context mContext;
    private int totalData = -1;
    private int lastID = 0;
    private Spanned titles;

    private TextView txt_title;

    private ProgressDialog pDialog;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private SQLiteDataBaseHelper dataBaseHelper;
    String countryid;
    String cityid;
    private boolean isClickedMore = true;
    private View footerView;
    private Button footertext;
    private Parcelable parcelableNedListState;

    public CountriesPriceFragment() {

    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to make visible/invisible the icons/logos in menu on actionbar
        setHasOptionsMenu(true);
        dataBaseHelper = new SQLiteDataBaseHelper(getActivity());
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.Medicines));
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.str_loading));
        pDialog.setCancelable(false);
        pref = mContext.getSharedPreferences("Pharmacy", 0);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        lastID = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_price, container, false);
        countryid=  getArguments().getString("CountryId");
        cityid=  getArguments().getString("CityId");
        Log.d(TAG, "onCreateView: "+countryid);
        Log.d(TAG, "onCreateView: "+cityid);
        init(view);

        if(Locale.getDefault().getLanguage().equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + "Search " + "</font>" + "<font color=\"#729619\">" + "Medicines" + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + "Search " + "</font>" + "<font color=\"#729619\">" + "Medicines" + "</font>"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_search_medicine1) +" "+ "</font>" + "<font color=\"#729619\">" + getString(R.string.str_search_medicine2) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_search_medicine1)+" " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_search_medicine2) + "</font>"));
            }
        }

        txt_title.setText(titles);
        getMedicineList();
        //mLMed.setOnScrollListener(onScrollListener());

        setListAdapter();

        mISearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isClickedMore = false;

                if (!CommonUtility.isInternetON(mContext)) {
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(mContext, str_check_int, Toast.LENGTH_SHORT).show();
                } else if (mESearch.getText().toString().trim().length() < 3) {
                    Toast.makeText(mContext, "Please enter at least 3 characters", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

        mESearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString().toLowerCase());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                lastID = 0;
            }
        });

        /*mLMed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MedicinePojo medicine = mMedList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("MEDICINE", medicine);

                Intent intent = new Intent(getActivity(), MedDetailsActivity.class);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });*/

        return view;
    }

//    private AbsListView.OnScrollListener onScrollListener() {
//        return new AbsListView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                int threshold = 20;
//                int count = mLMed.getCount();
//
//                if (scrollState == SCROLL_STATE_IDLE) {
//                    if (mLMed.getLastVisiblePosition() >= count - threshold && count < totalData) {
////                        Log.i(TAG, "loading more data");
//                        getMedicineList();
//                    }
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
//                                 int totalItemCount) {
//            }
//
//        };
//    }

    private void setListAdapter() {
        mAdapter = new CountryMedicineListAdapter(mContext, mMedList, dataBaseHelper,countryid,cityid);
        mLMed.setAdapter(mAdapter);

//        if(!isClickedMore && mLMed.isShown())
//        {
//            mLMed.addFooterView(footerView);
//        }
//        else
//        {
//            isClickedMore = false;
//        }

        mAdapter.notifyDataSetChanged();
    }

    private void init(View view) {

        txt_title = view.findViewById(R.id.txt_title);
//        mtTitle = (TextView) view.findViewById(R.id.txt_title);
        mESearch = view.findViewById(R.id.edt_search);
        mISearch = view.findViewById(R.id.img_search);
        mLMed = view.findViewById(R.id.list_medicine);

        footerView = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.med_list_footer_layout, null, false);
        //mLMed.addFooterView(footerView);
        footertext = footerView.findViewById(R.id.medlist_footer_button);
        footertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isClickedMore)
                    isClickedMore = true;
                mLMed.removeFooterView(footerView);
                Log.d("OnClick_Footer", "\t"+ isClickedMore);
                //Toast.makeText(mContext, "OnClick_Footer", Toast.LENGTH_LONG).show();
                getMedicineList();
//                footerView.setVisibility(View.GONE);

            }
        });
    }

    private void getMedicineList() {

//        Log.d(TAG, "162");
        showProgressDialog();

        RestClient.RestApiInterface service = RestClient.getClient();
        Call<CountryMedicinesPojo> call = service.getMedicines(Integer.parseInt(countryid),Integer.parseInt(cityid));
      //  Call<ListMedicine> call = service.getMedicines(lastID, mESearch.getText().toString().trim());
        call.enqueue(new Callback<CountryMedicinesPojo>() {
            @Override
            public void onResponse(Call<CountryMedicinesPojo> call, Response<CountryMedicinesPojo> response) {

                if (response.isSuccessful()) {

                    hideProgressDialog();

                    CountryMedicinesPojo listMedicine = response.body();
//                    Log.d(TAG, "response : " + new Gson().toJson(response));

                    if(listMedicine.getStatus() == 1) {
                        lastID = lastID + listMedicine.getMedicineCountry().size();
                       // totalData = listMedicine.getMedCount();
                        totalData = 1;

//                        mMedList.clear();
//                        mMedList.addAll(listMedicine.getMedData());

                        //to show only three item medicine list, if not clicked on more results footer
                        if(isClickedMore == false)
                        {
                            mMedList.clear();
                            //mMedList.addAll(listMedicine.getMedData());
                            ArrayList<CountryMedicineList> medicinePojos = new ArrayList<>();
                            medicinePojos.addAll(listMedicine.getMedicineCountry());
                            if(medicinePojos.size() >= 3)
                            {
                                for(int i = 0; i < 3; i++)
                                    mMedList.add(medicinePojos.get(i));
                                mLMed.addFooterView(footerView);
                            }
                            else
                            {
                                mMedList.clear();
                                mMedList.addAll(medicinePojos);
                                mLMed.removeFooterView(footerView);
                            }

                            //isClickedMore = true;
                        }
                        else if(isClickedMore == true)
                        {
                            mMedList.clear();
                            mMedList.addAll(listMedicine.getMedicineCountry());

                            isClickedMore = false;
                            mLMed.removeFooterView(footerView);
                        }
//                        Log.d(TAG, "List Size : " + mMedList.size());
                    } else {
                        mMedList.clear();
                    }

                    setListAdapter();

                } else {
                    mMedList.clear();
//                    Log.d(TAG, "No Response");
                }
            }

            @Override
            public void onFailure(Call<CountryMedicinesPojo> call, Throwable t) {
//                Log.d(TAG, "Fail");
                mMedList.clear();
                setListAdapter();
//                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);

        menu.findItem(R.id.call_help_item).setVisible(true);

//        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.call_help_item) {

            Intent intent1 = new Intent(mContext, CallHelpActivity.class);
            startActivity(intent1);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cart) {
            Intent intent = new Intent(getActivity(), PlaceOrderActivity.class);
            getActivity().startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}