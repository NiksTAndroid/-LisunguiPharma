package com.lisungui.pharma.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.CallHelpActivity;
import com.lisungui.pharma.activities.MessagingActivity;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.SignUpPojo;
import com.lisungui.pharma.models.UserPojo;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.CommonUtility;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class MyAccountFragment extends Fragment {

    private static final String TAG = MyAccountFragment.class.getSimpleName();
    private SQLiteDataBaseHelper sqLiteDataBaseHelper;
    private UserPojo userPojo;
    private View convertView;
    private TextView txt_title;
    private EditText edt_email, edt_mb_no, edt_name, edt_address, edt_insurance;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2;
    private RadioButton radioGenderButton;
    private Spanned titles;

    //    private SharedPreferences pref;
    private ProgressDialog pDialog;

    private Context context;

    public MyAccountFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(getActivity());
//        pref = getActivity().getSharedPreferences("Pharmacy", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        init(view);

        convertView = view;

        if (Locale.getDefault().getLanguage().equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + "General " + "</font>" + "<font color=\"#729619\">" + "Information" + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + "General " + "</font>" + "<font color=\"#729619\">" + "Information" + "</font>"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_gen_info1) + " " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_gen_info2) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_gen_info1) + " " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_gen_info2) + "</font>"));
            }
        }

        txt_title.setText(titles);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.str_updating));
        pDialog.setCancelable(false);

        userPojo = sqLiteDataBaseHelper.getUser();
        edt_name.setEnabled(false);

//        Log.d(TAG, "User ID : "+userPojo.getUser_server_id());

        try {

            edt_email.setText(userPojo.getUser_email_id());
            edt_mb_no.setText(userPojo.getUser_mb_no());
            edt_name.setText(userPojo.getUser_name());
            edt_address.setText(userPojo.getUser_address());
            edt_insurance.setText(userPojo.getUser_insurnace());
            if (userPojo.getUser_gender() != null) {
                if (!userPojo.getUser_gender().isEmpty()) {

                    if (userPojo.getUser_gender().equalsIgnoreCase("Male")) {
                        radioButton1.setChecked(true);
                        radioButton2.setChecked(false);
                    } else {
                        radioButton1.setChecked(false);
                        radioButton2.setChecked(true);
                    }

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void init(View view) {
        txt_title = view.findViewById(R.id.txt_title);
        edt_email = view.findViewById(R.id.edt_email);
        edt_mb_no = view.findViewById(R.id.edt_mb_no);
        edt_name = view.findViewById(R.id.edt_name);
        edt_address = view.findViewById(R.id.edt_address);
        edt_insurance = view.findViewById(R.id.edt_insurance);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.radioButton1);
        radioButton2 = view.findViewById(R.id.radioButton2);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void updateInfo() {

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        edt_email.setError(null);
        edt_mb_no.setError(null);
        edt_name.setError(null);
        edt_address.setError(null);

        boolean cancel = false;
        View focusView = null;

        final String email = edt_email.getText().toString().trim();
        final String mb_no = edt_mb_no.getText().toString().trim();
        final String name = edt_name.getText().toString().trim();
        final String address = edt_address.getText().toString().trim();
        final String insurance_name = edt_insurance.getText().toString().trim();

        if (address.isEmpty()) {
            edt_address.setError(getString(R.string.error_field_required));
            focusView = edt_address;
            cancel = true;
        }

        if (mb_no.isEmpty()) {
            edt_mb_no.setError(getString(R.string.error_field_required));
            focusView = edt_mb_no;
            cancel = true;
        }

        /*else if (mb_no.length() < 10) {
            edt_mb_no.setError("Enter Valid Mobile No");
            focusView = edt_mb_no;
            cancel = true;
        }*/

        if (email.isEmpty()) {
            edt_email.setError(getString(R.string.error_field_required));
            focusView = edt_email;
            cancel = true;
        }

        if (name.isEmpty()) {
            edt_name.setError(getString(R.string.error_field_required));
            focusView = edt_name;
            cancel = true;
        }

        if (insurance_name.isEmpty()) {
            edt_insurance.setError(getString(R.string.error_field_required));
            focusView = edt_insurance;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            radioGenderButton = convertView.findViewById(selectedId);

            showProgressDialog();

            Log.d(TAG, "insurance_name : " + insurance_name);

            RestClient.RestApiInterface service = RestClient.getClient();
            Call<SignUpPojo> call = service.updateProfile(String.valueOf(userPojo.getUser_server_id()), email, mb_no, radioGenderButton.getText().toString(), address, insurance_name);
            call.enqueue(new Callback<SignUpPojo>() {
                @Override
                public void onResponse(Call<SignUpPojo> call, retrofit2.Response<SignUpPojo> response) {

                    hideProgressDialog();

                    if (response.isSuccessful()) {

                        SignUpPojo signUpPojo = response.body();

                        if (signUpPojo.getStatus() == 1) {

                            userPojo.setUser_address(address);
                            userPojo.setUser_email_id(email);
                            userPojo.setUser_gender(radioGenderButton.getText().toString());
                            userPojo.setUser_mb_no(mb_no);
                            userPojo.setUser_insurnace(insurance_name);

                            updateUserToDatabase(name, mb_no, email, insurance_name, address);

                        } else if (signUpPojo.getStatus() == 2) {
                            Toast.makeText(getActivity(), signUpPojo.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            String str_check_int = getResources().getString(R.string.str_check_int);
                            Toast.makeText(getActivity(), str_check_int, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        String str_check_int = getResources().getString(R.string.str_check_int);
                        Toast.makeText(getActivity(), str_check_int, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SignUpPojo> call, Throwable t) {
                    hideProgressDialog();
//                    Log.d(TAG, "onFailure");
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(getActivity(), str_check_int, Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    private void updateUserToDatabase(String name, String mb_no, String email, String insurance_name, String address) {
        String[] col = new String[]{
                SQLiteDataBaseHelper.COLUMN_USER_NAME,
                SQLiteDataBaseHelper.COLUMN_USER_GENDER,
                SQLiteDataBaseHelper.COLUMN_USER_MB_NO,
                SQLiteDataBaseHelper.COLUMN_USER_EMAIL_ID,
                SQLiteDataBaseHelper.COLUMN_USER_INSURANCE,
                SQLiteDataBaseHelper.COLUMN_USER_ADDRESS
        };

        String[] val = new String[]{
                name,
                radioGenderButton.getText().toString(),
                mb_no,
                email,
                insurance_name,
                address
        };

        String selection = String.format(SQLiteDataBaseHelper.COLUMN_USER_SERVER_ID + "='%s'", userPojo.getUser_id());
        long user_id = sqLiteDataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.USER_TABLE, selection, col, val);

        if (user_id != 0) {
            Toast.makeText(getActivity(), getString(R.string.str_saved), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.menu_item_save).setVisible(true);
        menu.findItem(R.id.menu_item_delete).setVisible(false);
//        inflater.inflate(R.menu.menu, menu);

        menu.findItem(R.id.text_message_item).setVisible(false);
        menu.findItem(R.id.call_help_item).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_save) {

            if (!CommonUtility.isInternetON(getActivity())) {
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(getActivity(), str_check_int, Toast.LENGTH_LONG).show();
            } else {
                updateInfo();
            }

            return true;
        }

        if (id == R.id.text_message_item) {

            Intent intent1 = new Intent(context, MessagingActivity.class);
            startActivity(intent1);

            return true;
        }

        if (id == R.id.call_help_item) {

            Intent intent1 = new Intent(context, CallHelpActivity.class);
            startActivity(intent1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}