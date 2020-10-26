package com.lisungui.pharma.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lisungui.pharma.R;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.Address;
import com.lisungui.pharma.models.LoginPojo;
import com.lisungui.pharma.models.LoginWithGoogleResposeModel;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.CommonUtility;
import com.lisungui.pharma.utility.PrefManager;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private SQLiteDataBaseHelper dataBaseHelper;
    private ProgressDialog pDialog;
    private EditText mEUname, mEPass;
    private GoogleApiClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private TextView pharmacy_login;
    private com.google.android.gms.common.SignInButton googlesinginbtn;
    private boolean isuserLogin = true;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        init();

        dataBaseHelper = new SQLiteDataBaseHelper(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.str_login));
        pDialog.setCancelable(false);

        findViewById(R.id.btn_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonUtility.isInternetON(LoginActivity.this)) {
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(LoginActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                } else
                    attemptLogin();
            }
        });

        findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.btn_forgot_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
            }
        });
        googlesinginbtn = findViewById(R.id.btn_google_sign_in);
        googlesinginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                mGoogleSignInClient.connect();
            }
        });

    }

    private void attemptLogin() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mEUname.setError(null);
        mEPass.setError(null);

        boolean cancel = false;
        View focusView = null;

        String user_name = mEUname.getText().toString().trim();
        String password = mEPass.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            mEPass.setError(getString(R.string.error_field_required));
            focusView = mEPass;
            cancel = true;
        }

        if (TextUtils.isEmpty(user_name)) {
            mEUname.setError(getString(R.string.error_field_required));
            focusView = mEUname;
            cancel = true;
        }
        /*else if (!CommonUtility.isValidEmail(email_id)) {
            mEUname.setError(getString(R.string.error_invalid_email));
            focusView = mEUname;
            cancel = true;
        }*/


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgressDialog();
            String type;
            if (isuserLogin) {
                type = "user";
            } else {
                type = "pharmacy";
            }

            RestClient.RestApiInterface service = RestClient.getClient();
            Call<LoginPojo> call = service.getLoginDetails(type, user_name, password);
            call.enqueue(new Callback<LoginPojo>() {
                @Override
                public void onResponse(Call<LoginPojo> call, retrofit2.Response<LoginPojo> response) {
                    hideProgressDialog();

                    Log.d(TAG, "onResponse : " + response.body().getStatus());

                    if (response.isSuccessful()) {

                        LoginPojo loginPOJO = response.body();

                        if (loginPOJO.getStatus() == 1) {
                            String str_login_success = getResources().getString(R.string.str_login_success);
                            Toast.makeText(LoginActivity.this, str_login_success, Toast.LENGTH_SHORT).show();

                            SharedPreferences pref = getSharedPreferences("Pharmacy", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("USER", mEUname.getText().toString().trim());
                            String usertype = loginPOJO.getUser().get(0).getUserType();
                            if (usertype.equals("4")) {
                                editor.putString("USER_ID", "" + loginPOJO.getUser().get(0).getPharmId());
                                PrefManager.setUserProfilePic(LoginActivity.this, loginPOJO.getUser().get(0).getPharmImg());

                            } else {
                                editor.putString("USER_ID", "" + loginPOJO.getUser().get(0).getUserId());
                                PrefManager.setUserProfilePic(LoginActivity.this, loginPOJO.getUser().get(0).getUserImg());

                            }
                            editor.putBoolean("LoggedIn", true);

                            editor.putString("USERTYPEID", usertype);

                            editor.apply();


                            Log.d(TAG, "usertype_onResponse : " + loginPOJO.getUser().get(0).getUserType());

                            String selection1 = String.format(SQLiteDataBaseHelper.COLUMN_USER_SERVER_ID + "='%s'", loginPOJO.getUser().get(0).getUserId());
                            boolean isLoggedIn = dataBaseHelper.isSameUser(selection1);

                            if (!isLoggedIn) {
                                deleteAllTables();
                            }

                            String[] col = new String[]{
                                    SQLiteDataBaseHelper.COLUMN_USER_SERVER_ID,
                                    SQLiteDataBaseHelper.COLUMN_USER_NAME,
                                    SQLiteDataBaseHelper.COLUMN_USER_GENDER,
                                    SQLiteDataBaseHelper.COLUMN_USER_MB_NO,
                                    SQLiteDataBaseHelper.COLUMN_USER_EMAIL_ID,
                                    SQLiteDataBaseHelper.COLUMN_USER_ADDRESS
                            };


                            String[] val = new String[]{
                                    "" + loginPOJO.getUser().get(0).getUserId(),
                                    mEUname.getText().toString().trim(),
                                    "" + loginPOJO.getUser().get(0).getUserGender(),
                                    "" + loginPOJO.getUser().get(0).getUserMbNo(),
                                    "" + loginPOJO.getUser().get(0).getUserEmailId(),
                                    "" + loginPOJO.getUser().get(0).getUserAddress()
                            };

                            String selection = String.format(SQLiteDataBaseHelper.COLUMN_USER_SERVER_ID + "='%s'", loginPOJO.getUser().get(0).getUserId());
                            long user_id = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.USER_TABLE, selection, col, val);
                            Log.d(TAG, "user_id : " + user_id);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

//                            Run the Service
//                            Intent serviceIntent = new Intent(LoginActivity.this, DownloadService.class);
//                            startService(serviceIntent);

                            finish();
                        } else if (loginPOJO.getStatus() == 3) {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.account_verification_pending), Toast.LENGTH_SHORT).show();
                        } else {
                            String str_invalid_uname = getResources().getString(R.string.str_invalid_uname);
                            Toast.makeText(LoginActivity.this, str_invalid_uname, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        String str_check_int = getResources().getString(R.string.str_check_int);
                        Toast.makeText(LoginActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginPojo> call, Throwable t) {
                    hideProgressDialog();
                    Log.d(TAG, "onFailure");
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(LoginActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                }

            });

        }
    }

    private void deleteAllTables() {
        String[] table_names = new String[]{
                SQLiteDataBaseHelper.ALARM_TABLE,
                SQLiteDataBaseHelper.TREAT_TABLE,
                SQLiteDataBaseHelper.ORDER_TABLE,
                SQLiteDataBaseHelper.TEMP_ORDER_TABLE,
                SQLiteDataBaseHelper.USER_TABLE,
                SQLiteDataBaseHelper.ORDER_DETAILS_TABLE,
                SQLiteDataBaseHelper.NOTIFICATION_TABLE
        };

        for (int i = 0; i < table_names.length; i++) {
            dataBaseHelper.deleteAll(table_names[i]);
        }
    }

    private void init() {
        mEUname = findViewById(R.id.edt_uname);
        mEPass = findViewById(R.id.edt_passwd);
        pharmacy_login = findViewById(R.id.pharmacy_login);
        pharmacy_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isuserLogin) {
                    pharmacy_login.setText("I'm not a Pharmacy");
                    googlesinginbtn.setVisibility(View.GONE);
                    isuserLogin = false;
                } else {
                    isuserLogin = true;
                    pharmacy_login.setText(getResources().getString(R.string.pharmacy_log_in));
                    googlesinginbtn.setVisibility(View.VISIBLE);
                }

            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = "";
            String email = "";
            String personId = "";


            if (null != acct.getId()) {
                personId = acct.getId();
            }

            if (null != acct.getEmail()) {
                email = acct.getEmail();
                googleSingin(personId, email);

            }


        }
    }

    private void googleSingin(String personId, final String email) {
        if (personId == null || personId.isEmpty()) {
            Toast.makeText(this, "Error in sing in", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressDialog();
        RestClient.getClient().loginWithGoogle(personId, email)
                .enqueue(new Callback<LoginWithGoogleResposeModel>() {
                    @Override
                    public void onResponse(Call<LoginWithGoogleResposeModel> call, Response<LoginWithGoogleResposeModel> response) {

                        LoginWithGoogleResposeModel.User reponse = response.body().getUser().get(0);
                        String userType = reponse.getUserType();
                        String userMbNo = reponse.getUserMbNo().trim();
                        PrefManager.setUserType(LoginActivity.this, userType);
                        PrefManager.setUserID(LoginActivity.this, reponse.getUserId());


                        hideProgressDialog();
                        if (userType.isEmpty() || userMbNo.isEmpty()) {
                            askForDetailsDialog(reponse.getUserId(), email);
                        } else if (response.body().getStatus() == 0) {
                            Toast.makeText(LoginActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();

                        } else if (response.body().getStatus() == 3) {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.account_verification_pending), Toast.LENGTH_SHORT).show();

                        } else {
                            updateUserToDatabase(reponse.getUserId(),email,userMbNo,email,reponse.getUserInsCompName(),reponse.getUserAddress());
                            PrefManager.setUserProfilePic(LoginActivity.this, reponse.getUserImg());
                            PrefManager.getUserLogedIn(LoginActivity.this, true);

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginWithGoogleResposeModel> call, Throwable t) {

                    }
                });

    }

    String userType;
    String countryCode = "";
    Dialog dialog;

    private void askForDetailsDialog(final String userId, final String email) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ask_details);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        final EditText mobile = dialog.findViewById(R.id.edt_mb_no);
        final CountryPicker countryPicker = CountryPicker.newInstance("Select country");
        final Button countryCodeBtn = dialog.findViewById(R.id.countrycode_imageview);

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
                mobile.requestFocus();
                countryPicker.dismiss();
            }
        });

        final Spinner spinner = dialog.findViewById(R.id.usertype_registration_spinner);
        String[] types;
        if(getResources().getString(R.string.lang).equalsIgnoreCase("English")){
        types =    getResources().getStringArray(R.array.user_types);
        }else {
            types = getResources().getStringArray(R.array.user_typesfr);
        }
        final ArrayAdapter<String> userTypelistAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1,types);


        userTypelistAdapter.setDropDownViewResource(R.layout.simple_list_item_1);
        spinner.setAdapter(userTypelistAdapter);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mob = mobile.getText().toString();

                if (mob.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.str_enter_mo), Toast.LENGTH_LONG).show();
                    return;
                }
                if (countryCode.trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.select_country_code), Toast.LENGTH_LONG).show();
                    return;
                }

                if (spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.user_type), Toast.LENGTH_LONG).show();
                    return;
                }

                switch (spinner.getSelectedItemPosition()) {
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
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.user_type), Toast.LENGTH_LONG).show();
                        break;
                }
                dialog.dismiss();
                showProgressDialog();
                PrefManager.setUserType(LoginActivity.this, userType);
                updateUserToDatabase(userId,email,mob,email,null,null);
                RestClient.getClient().updateWithGoogleSingin(userId, userType, email,countryCode, mob)
                        .enqueue(new Callback<Address>() {
                            @Override
                            public void onResponse(Call<Address> call, Response<Address> response) {
                                hideProgressDialog();
                                if (userType.equals("user") && response.body().getStatus() == 1) {

                                    PrefManager.setUserID(LoginActivity.this, userId);
                                    PrefManager.getUserLogedIn(LoginActivity.this, true);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();

                                } else if (response.body().getStatus() == 3) {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.account_verification_pending), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Address> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        dialog.show();

    }

    private void updateUserToDatabase(String userId, String name, String mb_no, String email, String insurance_name, String address) {
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
                null,
                mb_no,
                email,
                insurance_name,
                address
        };

        String selection = String.format(SQLiteDataBaseHelper.COLUMN_USER_SERVER_ID + "='%s'", userId);
        SQLiteDataBaseHelper sqLiteDataBaseHelper = new SQLiteDataBaseHelper(context);
        long user_id = sqLiteDataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.USER_TABLE, selection, col, val);

        if (user_id != 0) {
            Toast.makeText(context, getString(R.string.str_saved), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
