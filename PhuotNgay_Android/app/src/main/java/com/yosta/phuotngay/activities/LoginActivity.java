package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.dialogs.DialogProgress;
import com.yosta.phuotngay.configs.AppConfig;
import com.yosta.phuotngay.interfaces.ActivityBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends ActivityBehavior
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final int RC_SIGN_IN = 9001;
    private final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @BindView(R.id.button_facebook_login)
    LoginButton loginButton;

    // Google
    private boolean mGoogleLoginClicked;
    private boolean mGoogleIntentInProgress;
    private GoogleApiClient mGoogleClient;
    private ConnectionResult mGoogleConnectionResult;

    // Firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    // Facebook
    private CallbackManager mCallbackManager;

    private AppConfig mAppConfig = null;
    private DialogProgress mProgress = null;

    @Override
    protected void onStart() {
        super.onStart();
        this.mFirebaseAuth.addAuthStateListener(this.mAuthStateListener);
        this.mAppConfig = (AppConfig) getApplication();
        this.mProgress = new DialogProgress(this, false, false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.mAuthStateListener != null) {
            this.mFirebaseAuth.removeAuthStateListener(this.mAuthStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mAppConfig.saveUser();
    }

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        onCallAnimation();
        onCallConfiguration();
        /*onCallSetupFacebook();*/
    }

    // [START on Activity Result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*this.mCallbackManager.onActivityResult(requestCode, resultCode, data);*/
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                onCallFirebaseGoogleAuth(account);
            } else {
                Log.e(TAG, result.getStatus().toString());
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // [END on Activity Result]

    /*@OnClick(R.id.btn_facebook)
    public void onCallFacebookLogin() {
        loginButton.callOnClick();
    }*/

    @OnClick(R.id.btn_google)
    public void onCallGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(this.mGoogleClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.btn_google_logout)
    public void onCallGoogleLogout() {
        this.mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(this.mGoogleClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }
/*
    @OnClick(R.id.btn_google_logout)
    public void onCallFacebookLogout() {
        this.mFirebaseAuth.signOut();
        LoginManager.getInstance().logOut();
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    /*private void onCallSetupFacebook() {
        this.mCallbackManager = CallbackManager.Factory.create();
        this.loginButton.setReadPermissions("email", "public_profile");
        this.loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "facebook:onError", error);
            }
        });
    }*/

    private void onCallConfiguration() {

        // [START config_sign_in]
        // Configure Google Sign In
        GoogleSignInOptions inOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_sign_in]

        this.mGoogleClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, inOptions)
                .build();

        // [START initialize_auth]
        this.mFirebaseAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    @Override
    public void onApplyEvents() {
        super.onApplyEvents();

        this.mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String str = String.format("%s - %s", user.getEmail(), user.getDisplayName());
                    Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    // [START auth_with_google]
    private void onCallFirebaseGoogleAuth(GoogleSignInAccount account) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        // [START_EXCLUDE silent]
        mProgress.show();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(
                account.getIdToken(), null);

        this.mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = task.getResult().getUser();
                            mAppConfig.setEmail(user.getEmail());
                            mAppConfig.setUserId(user.getUid());
                            mAppConfig.setUserName(user.getDisplayName());
                            Toast.makeText(LoginActivity.this, mAppConfig.getUser().toString(), Toast.LENGTH_SHORT).show();
                        }
                        mProgress.hide();
                    }
                });
    }
/*

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        this.mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
*/

    private void onCallAnimation() {

        /*Glide.with(LoginActivity.this).load(R.drawable.ic_launcher_anim).asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter().error(R.drawable.ic_launcher)
                .dontAnimate().into(imageView);*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}
