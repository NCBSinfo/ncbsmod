package com.secretbiology.ncbsmod.activity;

//Disclaimer : few of code from this file has been adapted from Google's original examples of sign in APIs

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.helpers.GeneralFunctions;

import java.io.IOException;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //Public Constants
    public static final int RC_SIGN_IN = 9001;
    public static final int AUTH_CODE_REQUEST_CODE = 2000;
    public static final String SCOPES = "https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/fusiontables";

    //Private constants
    private static String TAG = Login.class.getSimpleName();

    //Variables
    Bundle bundle;
    GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        bundle = savedInstanceState;

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
                showProgressDialog();
                getToken(currentEmail);
            }

        } else {
            Toast.makeText(getBaseContext(), "Unauthorized Access, Please log in", Toast.LENGTH_LONG).show();
            Log.e(TAG, "UNAUTHORIZED");
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
                    token = GoogleAuthUtil.getToken(getApplicationContext(), email, "oauth2:" + SCOPES, bundle);
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
                //getRowsByValue(ConstantsNetwork.MOD_TABLEID, token, "Email", email, getBaseContext());
            }

        };
        task.execute();
    }

}
