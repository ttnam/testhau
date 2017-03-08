package io.yostajsc.izigo.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.MainActivity;
import io.yostajsc.backend.config.APIManager;
import io.yostajsc.izigo.base.ActivityBehavior;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.firebase.FirebaseManager;
import io.yostajsc.izigo.models.user.User;
import io.yostajsc.izigo.firebase.model.UserManager;
import io.yostajsc.izigo.interfaces.CallBackParam;
import io.yostajsc.izigo.managers.EventManager;
import io.yostajsc.utils.NetworkUtils;
import io.yostajsc.utils.StorageUtils;

public class LoginActivity extends ActivityBehavior {

    private final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @BindView(R.id.button_facebook_login)
    LoginButton loginButton;

    @BindView(R.id.btn_facebook)
    LinearLayout layoutFacebook;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private User user;

    // Fire base instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        onApplyViews();
        onFacebookConfig();
        onFireBaseConfig();
    }

    @Override
    public void onApplyData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AccessToken token = AccessToken.getCurrentAccessToken();
                if (token != null) {
                    onCallToServer();
                } else {
                    onReset();
                }
            }
        }, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (NetworkUtils.isNetworkConnected(this))
            onApplyData();
    }

    @Override
    public void onApplyViews() {
        Glide.with(this)
                .load(R.drawable.ic_loading)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    private void onFireBaseConfig() {
        // Initialize Fire base Auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.e(TAG, "onAuthStateChanged");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }

    private void onFacebookConfig() {
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile", "user_friends");

        loginButton.registerCallback(mCallbackManager, EventManager.connect().registerFacebookCallback(new CallBackParam<AccessToken>() {
            @Override
            public void run(AccessToken token) {
                handleFacebookAccessToken(token);
                GraphRequest request = GraphRequest.newMeRequest(token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                user = UserManager.inject().getFacebookUserInfo(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", AppDefine.PARAMETERS);
                request.setParameters(parameters);
                request.executeAsync();
            }
        }));
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.e(TAG, "handleFacebookAccessToken:" + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    // TODO
                    Log.e(TAG, "Authentication failed.");
                } else {
                    user.setFireBaseId(task.getResult().getUser().getUid());
                    StorageUtils.inject(LoginActivity.this).save(user);
                    onCallToServer();
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


    private void onCallToServer() {
        User user = StorageUtils.inject(this).getUser();
        if (user != null) {
            String email = user.getEmail();
            String fbId = user.getFbId();
            String fireBaseId = user.getFireBaseId();
            String fcm = StorageUtils.inject(this).getString(FirebaseManager.FIRE_BASE_TOKEN);

            APIManager.connect().onLogin(email, fbId, fireBaseId, fcm, new CallBackParam<String>() {
                @Override
                public void run(String authorization) {
                    Log.d(TAG, authorization);

                    StorageUtils.inject(LoginActivity.this).save(AppDefine.AUTHORIZATION, authorization);

                    int isFirstTime = StorageUtils.inject(LoginActivity.this)
                            .getInt(AppDefine.FIRST_TIME);
                    Intent intent;
                    if (isFirstTime == 1) {
                        intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        intent.putExtra(AppDefine.FIRST_TIME, true);
                    } else {
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                    }
                    StorageUtils.inject(LoginActivity.this).save(AppDefine.FIRST_TIME, 0);
                    startActivity(intent);
                    finish();
                }
            }, new CallBackParam<String>() {
                @Override
                public void run(String error) {
                    onReset();
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            onReset();
            Toast.makeText(LoginActivity.this, getString(R.string.error_st_wrongs), Toast.LENGTH_LONG).show();
        }
    }

    private void onReset() {
        LoginManager.getInstance().logOut();
        progressBar.setVisibility(View.GONE);
        layoutFacebook.setVisibility(View.VISIBLE);
        StorageUtils.inject(LoginActivity.this).save(AppDefine.FIRST_TIME, 1);
    }

    @Override
    protected void onInternetConnected() {
        super.onInternetConnected();
        onApplyData();
    }
}