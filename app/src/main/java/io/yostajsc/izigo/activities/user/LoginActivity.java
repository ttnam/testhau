package io.yostajsc.izigo.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
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
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.PrefsUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.MainActivity;
import io.yostajsc.izigo.activities.OwnCoreActivity;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.AppConfig;
import io.yostajsc.sdk.model.user.IgUser;
import io.yostajsc.izigo.managers.UserManager;
import io.yostajsc.izigo.managers.EventManager;

public class LoginActivity extends OwnCoreActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.button_facebook_login)
    LoginButton loginButton;

    @BindView(R.id.btn_facebook)
    LinearLayout layoutFacebook;

    @BindView(R.id.layout)
    FrameLayout layout;

    private IgUser igUser;

    // Fire base instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        onFacebookConfig();
        onFireBaseConfig();
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

        loginButton.registerCallback(mCallbackManager, EventManager.connect().registerFacebookCallback(new CallBackWith<AccessToken>() {
            @Override
            public void run(final AccessToken token) {
                handleFacebookAccessToken(token);
                GraphRequest request = GraphRequest.newMeRequest(token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                igUser = UserManager.inject().getFacebookUserInfo(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", AppConfig.PARAMETERS);
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
                if (task.isSuccessful()) {
                    igUser.setFireBaseId(task.getResult().getUser().getUid());
                    onCallToServer(igUser);
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

    private void onCallToServer(IgUser igUser) {
        if (igUser != null) {

            String email = igUser.getEmail();
            final String fbId = igUser.getFbId();
            String fireBaseId = igUser.getFireBaseId();
            showProgress();

            IzigoSdk.UserExecutor.login(
                    AppConfig.getInstance().getFbToken(),   // Facebook token
                    email,                                  // Email
                    fbId,                                   // Facebook id
                    fireBaseId,                             // Fire base id
                    AppConfig.getInstance().getFcmKey(),    // Fcm key
                    igUser.getAvatar(),
                    igUser.getName(),
                    new CallBack() {
                        @Override
                        public void run() {
                            PrefsUtils.inject(LoginActivity.this).save(AppConfig.FB_ID, fbId);
                            hideProgress();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }, new CallBackWith<String>() {
                        @Override
                        public void run(String error) {
                            onReset();
                            AppConfig.showToast(LoginActivity.this, error);
                        }
                    });
        }
    }

    private void onReset() {
        LoginManager.getInstance().logOut();
        IzigoSdk.UserExecutor.logOut();
        hideProgress();
        layoutFacebook.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        layout.setVisibility(View.GONE);
    }

    private void showProgress() {
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInternetDisConnected() {
        onReset();
        super.onInternetDisConnected();
    }
}