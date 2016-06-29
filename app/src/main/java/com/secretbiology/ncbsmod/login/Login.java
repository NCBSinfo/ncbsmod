package com.secretbiology.ncbsmod.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.secretbiology.ncbsmod.BaseActivity;
import com.secretbiology.ncbsmod.dashboard.Dashboard;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.helpers.Utilities;
import com.secretbiology.ncbsmod.interfaces.Network;
import com.secretbiology.ncbsmod.interfaces.User;

import java.util.Map;

public class Login extends BaseActivity implements User, Network {

    //Activity specific settings
    static {
        menuItem = R.id.nav_send;
    }

    private final String TAG = getClass().getSimpleName();
    SharedPreferences pref;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Button signInButton, forgotpassButton;
    TextInputEditText username, password;
    TextInputLayout user_layout, password_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub viewStub = (ViewStub) findViewById(R.id.baseView);
        viewStub.setLayoutResource(R.layout.login);
        viewStub.inflate();

        mProgressDialog = new ProgressDialog(Login.this);
        mProgressDialog.setCanceledOnTouchOutside(false);

        //Firebase code
        mAuth = FirebaseAuth.getInstance();
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "Login state changed ");
                if (firebaseAuth.getCurrentUser() != null) {
                    //Do something

                }
            }
        };

        signInButton = (Button) findViewById(R.id.button_sign_in);
        forgotpassButton = (Button) findViewById(R.id.button_signin_forgot_pass);
        username = (TextInputEditText) findViewById(R.id.edittext_signin_username);
        password = (TextInputEditText) findViewById(R.id.edittext_signin_password);
        user_layout = (TextInputLayout) findViewById(R.id.input_layout_signin_user);
        password_layout = (TextInputLayout) findViewById(R.id.input_layout_signin_pass);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Utilities().isNetworkAvailable(getBaseContext())) {
                    if (validateEmail() && validatePass()) {
                        showProgressDialog();
                        mProgressDialog.setMessage("Signing in...");
                        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    hideProgressDialog();
                                    Log.w(TAG, "Sign in failed : ", task.getException());
                                    FirebaseErrors firebaseErrors = new FirebaseErrors(getBaseContext(), task.getException(), Login.this);
                                    String warning = firebaseErrors.getWarningMessage();
                                    String type = firebaseErrors.getType();
                                    switch (type) {
                                        case FirebaseErrors.INVALID_USER:
                                            user_layout.setError(warning);
                                            requestFocus(username);
                                            break;
                                        case FirebaseErrors.WRONG_PASSWORD:
                                            password_layout.setError(warning);
                                            requestFocus(password);
                                            break;
                                        case FirebaseErrors.TOO_MANY_ATTEMPTS:
                                            firebaseErrors.dialogWarning();
                                            break;
                                        default:
                                            firebaseErrors.dialogWarning();
                                    }

                                } else {
                                    pref.edit().clear().apply();
                                    //TODO: delete database
                                    pref.edit().putString(profile.EMAIL, username.getText().toString()).apply();
                                    mDatabase.child(nodes.users).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.i(TAG, "Retrieving data... ");
                                            Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                                            if(data!=null) {
                                                if (data.get(nodes.topics) != null) {
                                                    pref.edit().putString(profile.TOPICS,data.get(nodes.topics).toString()).apply();

                                                    if (data.get(nodes.api_key) != null) {
                                                        pref.edit().putString(profile.KEY,data.get(nodes.api_key).toString()).apply();
                                                    }
                                                    if (data.get(nodes.name) != null) {
                                                        pref.edit().putString(profile.NAME,data.get(nodes.name).toString()).apply();
                                                    }
                                                    mDatabase.child(nodes.users + "/" + mAuth.getCurrentUser().getUid() + "/" + nodes.token).setValue(FirebaseInstanceId.getInstance().getToken());
                                                    pref.edit().putString(profile.TOKEN, FirebaseInstanceId.getInstance().getToken()).apply();
                                                    pref.edit().putBoolean(AUTHORIZED,true).apply();
                                                    hideProgressDialog();
                                                    Intent intent = new Intent(Login.this, Dashboard.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);

                                                }
                                                else{

                                                    new AlertDialog.Builder(Login.this)
                                                            .setTitle("Oops!")
                                                            .setMessage("There are no topics attached to your account. Please contact developers.")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    hideProgressDialog();
                                                                }
                                                            })
                                                            .show();
                                                    hideProgressDialog();
                                                }
                                            }
                                            else {

                                                new AlertDialog.Builder(Login.this)
                                                        .setTitle("Oops!")
                                                        .setMessage("Your moderator account is not activated yet. Please contact developers.")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                hideProgressDialog();
                                                            }
                                                        })
                                                        .show();
                                                hideProgressDialog();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e(TAG, databaseError.getMessage());
                                        }
                                    });



                                }//Successful Login2
                            }
                        });
                    }//Valid information
                }//IsOnline
                else {
                    Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_LONG).show();
                }
            }//On click
        });


        forgotpassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail()) {
                    new AlertDialog.Builder(Login.this)
                            .setTitle("Password reset")
                            .setMessage(getString(R.string.warning_password_reset, username.getText().toString()))
                            .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.sendPasswordResetEmail(username.getText().toString());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            super.hideProgressDialog();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePass() {
        if (password.getText().toString().trim().isEmpty()) {
            password_layout.setError(getString(R.string.warning_registration_empty_password));
            requestFocus(password);
            return false;
        } else if (password.length() < 6) {
            password_layout.setError(getString(R.string.warning_registration_bad_password));
            requestFocus(password);
            return false;
        } else {
            password_layout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {

        if ((username.getText().toString().trim().isEmpty()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches())) {
            user_layout.setError(getString(R.string.warning_registration_invalid_email));
            requestFocus(username);
            return false;
        } else {
            user_layout.setErrorEnabled(false);
        }
        return true;
    }

}
