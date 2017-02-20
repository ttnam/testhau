package com.yosta.phuotngay.activities.user;

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
import com.yosta.phuotngay.activities.dialogs.DialogProgress;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.firebase.model.UserManager;
import com.yosta.phuotngay.helpers.StorageHelper;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.interfaces.CallBackStringParam;
import com.yosta.phuotngay.services.PhuotNgayService;

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

    private User user;
    private DialogProgress progress = null;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.progress = new DialogProgress(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onInitializeFireBase();
        onInitializeFacebookLogin();
    }

    private void onInitializeFireBase() {
        // Initialize Fire base Auth
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
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "facebook:onError", error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                } else {
                    progress.show();
                    user.setFireBaseId(task.getResult().getUser().getUid());
                    onCallToServer(user);
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

    private void onCallToServer(final User user) {
        if (user != null) {
            PhuotNgayService.connect().onLogin(user.getEmail(), user.getFbId(), user.getFireBaseId(), new CallBackStringParam() {
                @Override
                public void run(String authen) {
                    user.setAuthen(authen);
                    dismissDialog();
                    StorageHelper.inject(LoginActivity.this).save(User.AUTHORIZATION, authen);
                    StorageHelper.inject(LoginActivity.this).save(user);
                    startActivity(new Intent(LoginActivity.this, FirstSetupActivity.class));
                    finish();
                }
            }, new CallBackStringParam() {
                @Override
                public void run(String error) {
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    dismissDialog();
                }
            });
        }
    }

    private void dismissDialog() {
        if (progress.isShowing())
            progress.dismiss();
    }
}