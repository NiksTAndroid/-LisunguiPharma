package com.lisungui.pharma.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.ForgotPojo;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.CommonUtility;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPassActivity extends AppCompatActivity {

    private static final String TAG = ForgotPassActivity.class.getSimpleName();

    private EditText mEmail;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.str_sending));
        pDialog.setCancelable(false);

        init();

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonUtility.isInternetON(ForgotPassActivity.this))
                    Toast.makeText(ForgotPassActivity.this, getString(R.string.str_check_int), Toast.LENGTH_SHORT).show();
                else
                    forgotPassword();
            }
        });

    }

    private void init() {
        mEmail = (EditText) findViewById(R.id.edt_email_id);
    }


    /*private void forgotPassword() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mEmail.setError(null);

        boolean cancel = false;
        View focusView = null;

        String user_name = mEmail.getText().toString().trim();

        if (TextUtils.isEmpty(user_name)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } *//*else if (!CommonUtility.isValidEmail(email_id)) {
            mEUname.setError(getString(R.string.error_invalid_email));
            focusView = mEUname;
            cancel = true;
        }*//*

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgressDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    StringConstant.URL_3, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);

                    hideProgressDialog();

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        int i = jsonObject.getInt("status");

                        if (i == 1) {
                            String str_sent_email = getResources().getString(R.string.str_sent_email);
                            Toast.makeText(ForgotPassActivity.this, str_sent_email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ForgotPassActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        String str_check_int = getResources().getString(R.string.str_check_int);
                        Toast.makeText(ForgotPassActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hideProgressDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("user_email_id", mEmail.getText().toString().trim());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };

            *//*strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*//*

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            strReq.setRetryPolicy(policy);

            AppController.getInstance().addToRequestQueue(strReq, "json_req");
        }
    }*/

    private void forgotPassword() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mEmail.setError(null);

        boolean cancel = false;
        View focusView = null;

        String user_name = mEmail.getText().toString().trim();

        if (TextUtils.isEmpty(user_name)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } /*else if (!CommonUtility.isValidEmail(email_id)) {
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

            RestClient.RestApiInterface service = RestClient.getClient();
            Call<ForgotPojo> call = service.sendPassword(user_name);
            call.enqueue(new Callback<ForgotPojo>() {
                @Override
                public void onResponse(Call<ForgotPojo> call, retrofit2.Response<ForgotPojo> response) {
                    hideProgressDialog();

                    Log.d(TAG, "onResponse : " + response);

                    if (response.isSuccessful()) {

                        ForgotPojo forgotPojo = response.body();

                        if (forgotPojo.getStatus() == 1) {
                            String str_sent_email = getResources().getString(R.string.str_sent_email);
                            Toast.makeText(ForgotPassActivity.this, str_sent_email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            String str_check_int = getResources().getString(R.string.str_check_int);
                            Toast.makeText(ForgotPassActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        String str_check_int = getResources().getString(R.string.str_check_int);
                        Toast.makeText(ForgotPassActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ForgotPojo> call, Throwable t) {
                    hideProgressDialog();
                    Log.d(TAG, "onFailure");
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(ForgotPassActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
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

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
