package com.lisungui.pharma.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.SignUpPojo;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.CommonUtility;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private String str_check_int;
    private SQLiteDataBaseHelper dataBaseHelper;
    private EditText mEName, mEMail, mEMbNo, mEPass;
    private ProgressDialog pDialog;
    private ImageView ivPhoto;

    private String userType = "";
    private Spinner userTypeSpinner;
    private Context context;
    private String countryCode = "";
    private String mobileNum = "";
    private Button countryCodeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dataBaseHelper = new SQLiteDataBaseHelper(this);

        String str_reg = getResources().getString(R.string.str_reg);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(str_reg);
        pDialog.setCancelable(false);

        init();

        str_check_int = getResources().getString(R.string.str_check_int);
        findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonUtility.isInternetON(SignUpActivity.this))
                    Toast.makeText(SignUpActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                else {
                    attemptSignUp();
                }
            }
        });

    }


    private void attemptSignUp() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mEName.setError(null);
        mEMbNo.setError(null);
        mEPass.setError(null);

        boolean cancel = false;
        View focusView = null;

        String user_name = mEName.getText().toString().trim();
        String password = mEPass.getText().toString().trim();
        final String email_id = mEMail.getText().toString().trim();
        final String mb_no = mEMbNo.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            mEPass.setError(getString(R.string.error_field_required));
            focusView = mEPass;
            cancel = true;
        }



        if (TextUtils.isEmpty(user_name)) {
            mEName.setError(getString(R.string.error_field_required));
            focusView = mEName;
            cancel = true;
        }

        Log.d(TAG, "onSignUp_1 :" + user_name + "\t" + email_id + "\t" + userType + "\t" + countryCode + "\t" + mb_no + "\t" + password);

        switch (userTypeSpinner.getSelectedItemPosition()) {
            case 1:
                userType = "user";
                break;
            case 2:
                userType = "doctor";
                break;
            case 3:
                userType = "hospitals";
                break;

            case 4:
                userType = "laboratories";
                break;
            case 0:
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.user_type), Toast.LENGTH_LONG).show();
                focusView = userTypeSpinner;
                cancel = true;
                break;
        }

        if (!countryCode.isEmpty() && mobileNum.isEmpty()) {
            mEMbNo.setError(getString(R.string.error_field_required));
            focusView = mEMbNo;
            cancel = true;
        }

        if (countryCode.isEmpty() && !mobileNum.isEmpty()) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.select_country_code), Toast.LENGTH_LONG).show();
            return;
        }

        if (countryCode.isEmpty() && mobileNum.isEmpty()) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.select_country_code), Toast.LENGTH_LONG).show();
            mEMbNo.setError(getString(R.string.error_field_required));
            focusView = mEMbNo;
            cancel = true;
        }

        if (countryCode.isEmpty()) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.select_country_code), Toast.LENGTH_LONG).show();
            return;
        }

        if (mobileNum.isEmpty()) {
            mEMbNo.setError(getString(R.string.error_field_required));
            focusView = mEMbNo;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgressDialog();

            Log.d(TAG, "onSignUp_2:" + user_name + "\t" + email_id + "\t" + userType + "\t" + countryCode + "\t" + mb_no + "\t" + password);

            RestClient.RestApiInterface service = RestClient.getClient();
            //Call<SignUpPojo> call = service.getSignupDetails(user_name, email_id, mb_no, password);
            Call<SignUpPojo> call = service.getSignupDetails(user_name, email_id, userType, countryCode, mb_no, password);
            call.enqueue(new Callback<SignUpPojo>() {
                @Override
                public void onResponse(Call<SignUpPojo> call, retrofit2.Response<SignUpPojo> response) {
                    hideProgressDialog();

                    Log.d(TAG, "onResponse : " + response);

                    if (response.isSuccessful()) {

                        SignUpPojo signUpPojo = response.body();

                        if (signUpPojo.getStatus() == 1) {
                            String str_reg_suc = getResources().getString(R.string.str_reg_suc);
                            Toast.makeText(SignUpActivity.this, str_reg_suc, Toast.LENGTH_SHORT).show();
                            SharedPreferences pref = getSharedPreferences("Pharmacy", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("USER", mEName.getText().toString().trim());
                            editor.putString("USER_ID", signUpPojo.getUser_id());


                            String[] col = new String[]{
                                    SQLiteDataBaseHelper.COLUMN_USER_SERVER_ID,
                                    SQLiteDataBaseHelper.COLUMN_USER_NAME,
                                    SQLiteDataBaseHelper.COLUMN_USER_MB_NO,
                                    SQLiteDataBaseHelper.COLUMN_USER_EMAIL_ID
                            };

                            String[] val = new String[]{
                                    signUpPojo.getUser_id(),
                                    mEName.getText().toString().trim(),
                                    mb_no,
                                    email_id,
                            };


                            if (!userType.equals("user")) {
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else if (userType.equals("user")) {
                                editor.putBoolean("LoggedIn", true);
                                editor.putString("USERTYPEID", "1");
                                editor.apply();

                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

//
                        } else if (signUpPojo.getStatus() == 2) {
                            Toast.makeText(SignUpActivity.this, signUpPojo.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            String str_check_int = getResources().getString(R.string.str_check_int);
                            Toast.makeText(SignUpActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        String str_check_int = getResources().getString(R.string.str_check_int);
                        Toast.makeText(SignUpActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SignUpPojo> call, Throwable t) {
                    hideProgressDialog();
                    Log.d(TAG, "onFailure");
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(SignUpActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

            default:
                break;
        }

        return true;

    }

    private void init() {
        mEName = (EditText) findViewById(R.id.edt_name);
        mEMail = (EditText) findViewById(R.id.edt_email);
        mEMbNo = (EditText) findViewById(R.id.edt_mb_no);
        mEPass = (EditText) findViewById(R.id.edt_passwd);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        countryCodeBtn = (Button) findViewById(R.id.countrycode_imageview);

        final CountryPicker countryPicker = CountryPicker.newInstance("Select country");

        countryCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryPicker.show(getSupportFragmentManager(), "country_code_picker");
            }
        });

        countryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String s, String s1, String s2, int i) {
                countryCode = s2;
                countryCodeBtn.setText(s2);
                mEMbNo.requestFocus();
                countryPicker.dismiss();
            }
        });

        mEMbNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged_1 : " + mobileNum + "\t" + countryCode);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged_1 : " + mobileNum + "\t" + countryCode);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "onResponse_afterTextChanged : " + s);
                mobileNum = s.toString();
                Log.d(TAG, "afterTextChanged_1 : " + mobileNum + "\t" + countryCode);
            }
        });


        String[] alltypes = getResources().getStringArray(R.array.user_types);
        if(!getResources().getString(R.string.lang).equalsIgnoreCase("English")){
            alltypes = getResources().getStringArray(R.array.user_typesfr);
        }

        final ArrayAdapter<String> userTypelistAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, alltypes);
        userTypeSpinner =  findViewById(R.id.usertype_registration_spinner);
        userTypelistAdapter.setDropDownViewResource(R.layout.simple_list_item_1);
        userTypeSpinner.setAdapter(userTypelistAdapter);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
