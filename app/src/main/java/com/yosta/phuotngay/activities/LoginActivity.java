package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.FirebaseManager;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.firebase.model.UserManager;
import com.yosta.phuotngay.interfaces.ActivityBehavior;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends ActivityBehavior {

    private final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @BindView(R.id.button_facebook_login)
    LoginButton loginButton;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
/*
        Glide.with(this)
                .load(R.drawable.ic_launcher_anim)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);*/

        onInitializeFirebase();
        onInitializeFacebookLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void onInitializeFirebase() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }

    private void onInitializeFacebookLogin() {
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                user = UserManager.inject().getFacebookUserInfo(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", UserManager.PARAMETERS);
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        //user.setFbToken(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                    user.setFirebaseId(task.getResult().getUser().getUid());
                    FirebaseManager.inject(LoginActivity.this).onUpdateUserInfo(user);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick(R.id.btn_facebook)
    public void onFacebookLogin() {
        loginButton.performClick();
    }
}

/*

public class LoginActivity extends ActivityBehavior */
/*implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks*//*

        */
/*implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks *//*
 {

    private static final int RC_SIGN_IN = 9001;


    private User mFirebaseUser;

    // Google
    // private GoogleApiClient mGoogleClient;

    @Override
    protected void onStart() {
        super.onStart();
        onCallConfiguration();

        this.mFirebaseAuth = FirebaseAuth.inject();
        this.mFirebaseUser = this.mFirebaseAuth.getCurrentUser();

        // Not signed in, launch the Sign In activity
        if (mFirebaseUser == null) {
            */
/*startActivity(new Intent(this, SignInActivity.class));
            finish();*//*

        } else {
            // TODO
            String mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                // TODO
                String mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
    }

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
    }

    @OnClick(R.id.btn_google)
    public void onCallGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(this.mGoogleClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // this.mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseGoogleAuth(account);
            } else {
                Log.e(TAG, result.getStatus().toString());
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    */
/*@OnClick(R.id.btn_google_logout)*//*

    public void onCallGoogleLogout() {
        this.mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(this.mGoogleClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
        */
/*mUsername = ANONYMOUS;
        startActivity(new Intent(this, SignInActivity.class));*//*

    }
    */
/*private void onCallConfiguration() {

        // [START config_sign_in]
        // Configure Google Sign In
        GoogleSignInOptions inOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.google_client_id))
                .requestEmail()
                .build();
        // [END config_sign_in]

        this.mGoogleClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, inOptions)
                .build();
    }*//*

*/
/*
    private void firebaseGoogleAuth(GoogleSignInAccount account) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        // mProgress.show();
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
                            Toast.makeText(LoginActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                            // TODO
                            *//*
*/
/*User user = task.getResult().getUser();
                            mAppConfig.setEmail(user.getEmail());
                            mAppConfig.setUserId(user.getUid());
                            mAppConfig.setUserName(user.getDisplayName());
                            Toast.makeText(LoginActivity.this, mAppConfig.getUser().toString(), Toast.LENGTH_SHORT).show();*//*
*/
/*
                        }
                        // TODO
                        // mProgress.hide();
                    }
                });
    }*//*


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    */
/*
    // Google
    private boolean mGoogleLoginClicked;
    private boolean mGoogleIntentInProgress;

    private ConnectionResult mGoogleConnectionResult;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    // Facebook
    private CallbackManager mCallbackManager;

    private AppConfig mAppConfig = null;
    private DialogProgress mProgress = null;*//*


   */
/* @Override
    protected void onStart() {
        super.onStart();
        *//*
*/
/*this.mFirebaseAuth.addAuthStateListener(this.mAuthStateListener);
        this.mAppConfig = (AppConfig) getApplication();
        this.mProgress = new DialogProgress(this, false, false);*//*
*/
/*
    }*//*

*/
/*
    @Override
    protected void onStop() {
        super.onStop();
        *//*
*/
/*if (this.mAuthStateListener != null) {
            this.mFirebaseAuth.removeAuthStateListener(this.mAuthStateListener);
        }*//*
*/
/*
    }

    @Override
    protected void onPause() {
        super.onPause();
        // this.mAppConfig.saveUser();
    }

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
*//*
*/
/*
        onCallAnimation();
        onCallConfiguration();*//*
*/
/*
        *//*
*/
/*onCallSetupFacebook();*//*
*/
/*
    }*//*




    */
/*@OnClick(R.id.btn_facebook)
    public void onCallFacebookLogin() {
        loginButton.callOnClick();
    }*//*



*/
/*
    @OnClick(R.id.btn_google_logout)
    public void onCallFacebookLogout() {
        this.mFirebaseAuth.signOut();
        LoginManager.inject().logOut();
    }*//*

*/
/*
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }*//*


    */
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
    }*//*

*/
/*

    @Override
    public void onApplyEvents() {
        super.onApplyEvents();

        this.mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                User user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String str = String.format("%s - %s", user.getEmail(), user.getDisplayName());
                    Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


*/
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
*//*

*/
/*
    private void onCallAnimation() {

        *//*
*/
/*Glide.with(LoginActivity.this).load(R.drawable.ic_launcher_anim).asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter().error(R.drawable.ic_launcher)
                .dontAnimate().into(imageView);*//*
*/
/*
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
    }*//*

}
*/
