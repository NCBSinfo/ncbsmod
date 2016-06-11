package com.secretbiology.ncbsmod.activity;

//Disclaimer : few of code from this file has been adapted from Google's original examples of sign in APIs

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.rohitsuratekar.retro.google.fusiontable.Commands;
import com.rohitsuratekar.retro.google.fusiontable.Service;
import com.rohitsuratekar.retro.google.fusiontable.reponse.SpecificRowValue;
import com.secretbiology.ncbsmod.Home;
import com.secretbiology.ncbsmod.LoadData;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.constants.Network;
import com.secretbiology.ncbsmod.helpers.GeneralFunctions;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //Public Constants
    public static final int RC_SIGN_IN = 9001;
    public static final int AUTH_CODE_REQUEST_CODE = 2000;
    public static final String SCOPES = "https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/fusiontables";
    public static final String EMAIL = "currentEmail";
    public static final String NAME = "currentName";
    public static final String KEY = "currentKey";
    public static final String LOGTABLE = "logTable";
    public static final String EXTRA_CODE = "currentExtraCodes";
    public static final String ACCESS_LEVEL = "currentAccessLevel";

    //Private constants
    private static String TAG = Login.class.getSimpleName();
    private SharedPreferences pref;

    //Variables
    GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //Sign In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        if(new GeneralFunctions().isNetworkAvailable(getBaseContext())){
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }}
        else
        {
            RelativeLayout layout = (RelativeLayout)findViewById(R.id.login_screen);
            if (layout != null) {
                Snackbar.make(layout, "Network is not available", Snackbar.LENGTH_LONG).show();
            }
        }


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getBaseContext(), "Network connection failed", Toast.LENGTH_LONG).show();
        Log.e(TAG, "CONNECTION FAILED");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == AUTH_CODE_REQUEST_CODE) {
            getToken(currentEmail);

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                currentEmail = acct.getEmail();
                pref.edit().putString(EMAIL,currentEmail).apply();
                showProgressDialog();
                getToken(currentEmail);
            }

        } else {
            Toast.makeText(getBaseContext(), "Unauthorized Access, Please log in", Toast.LENGTH_LONG).show();
            if(result.getStatus().getStatusCode()==12501){
                Log.e(TAG, "Bad configuration");
            }
        }
    }

    public void signIn(View v) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Log.i(TAG, "Login requested");
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void getToken(final String email) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    token = GoogleAuthUtil.getToken(getApplicationContext(), email, "oauth2:" + SCOPES);
                } catch (UserRecoverableAuthException e) {
                    // Requesting an authorization code will always throw
                    // UserRecoverableAuthException on the first call to GoogleAuthUtil.getToken
                    // because the user must consent to offline access to their data.  After
                    // consent is granted control is returned to your activity in onActivityResult
                    // and the second call to GoogleAuthUtil.getToken will succeed.
                    hideProgressDialog();
                    startActivityForResult(e.getIntent(), AUTH_CODE_REQUEST_CODE);

                } catch (IOException | GoogleAuthException e) {
                    e.printStackTrace();
                    hideProgressDialog();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                Log.i(TAG, "Access token retrieved:" + token);
                getRowsByValue(Network.MODTABLE, token, "email", email, getBaseContext());
            }

        };
        task.execute();
    }

    public void getRowsByValue(String TableID, String AccessToken, String Column, String Rowvalue, final Context context) {
        String sql_query = "SELECT * FROM " + TableID + " WHERE " + Column + "='" + Rowvalue + "'";
        Commands fusionService = Service.createService(Commands.class, AccessToken);
        Call<SpecificRowValue> call2 = fusionService.getSpecificRow(sql_query, AccessToken);

        call2.enqueue(new Callback<SpecificRowValue>() {
            @Override
            public void onResponse(Call<SpecificRowValue> call, Response<SpecificRowValue> response) {
                if (response.isSuccess()) {

                    if (response.body().getRows().size() == 0) {
                        hideProgressDialog();

                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                Login.this).create();

                        alertDialog.setTitle("Something is wrong");
                        alertDialog.setMessage("Your access to moderator's zone has been revoked! Please contact developers.");
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                hideProgressDialog();
                                deleteInfo();
                                startActivity(new Intent(Login.this, Home.class));

                            }
                        });

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Showing Alert Message
                        alertDialog.show();

                    } else {
                        pref.edit().putString(EMAIL,response.body().getRows().get(0).get(0)).apply();
                        pref.edit().putString(NAME,response.body().getRows().get(0).get(1)).apply();
                        pref.edit().putString(KEY,response.body().getRows().get(0).get(2)).apply();
                        pref.edit().putString(ACCESS_LEVEL,response.body().getRows().get(0).get(3)).apply();
                        pref.edit().putString(EXTRA_CODE,response.body().getRows().get(0).get(4)).apply();
                        pref.edit().putString(LOGTABLE,response.body().getRows().get(0).get(5)).apply();
                        pref.edit().putBoolean(ModeratorZone.IS_MODERATOR,true).apply();
                        Toast.makeText(context, "Successfully Loaded preferences", Toast.LENGTH_LONG);
                        hideProgressDialog();
                        startActivity(new Intent(Login.this, LoadData.class));
                    }


                } else {
                    Toast.makeText(context, "Failed to get database.", Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                    if (response.code() == 403) {
                        hideProgressDialog();
                        Log.i(TAG, "Not permitted");
                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                Login.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Oops");
                        alertDialog.setMessage("You don't have access to moderator's zone!");
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                           //TODO: Write your code here to execute after dialog closed
                            startActivity(new Intent(Login.this, Home.class));

                            }
                        });
                        alertDialog.setCanceledOnTouchOutside(false);
                        // Showing Alert Message
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SpecificRowValue> call, Throwable t) {
                Toast.makeText(context, "Error:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteInfo(){
        pref.edit().remove(KEY).apply();
        pref.edit().remove(LOGTABLE).apply();
        pref.edit().remove(ACCESS_LEVEL).apply();
        pref.edit().remove(EXTRA_CODE).apply();
        pref.edit().remove(ModeratorZone.IS_MODERATOR).apply();
    }
}
