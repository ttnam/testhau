package com.yosta.phuotngay.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.MainActivity;
import com.yosta.phuotngay.activities.interfaces.DialogBehavior;
import com.yosta.phuotngay.helpers.AppUtils;
import com.yosta.phuotngay.helpers.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogLogin extends Dialog implements DialogBehavior {

    @BindView(R.id.textView)
    TextInputEditText txtEmail;

    @BindView(R.id.textView_title)
    TextInputEditText txtPwd;

    @BindView(R.id.button_ok)
    Button buttonLogin;

    private int iCheckNet = 0;
    private boolean isEmailEmpty = true;
    private boolean isPassEmpty = true;

    private Activity owner = null;

    public DialogLogin(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        onAttachedWindow(context);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        owner = getOwnerActivity();
    }

    @Override
    public void onAttachedWindow(Context context) {
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideDownUp;
        }
        owner = (context instanceof Activity) ? (Activity) context : null;
        if (owner != null)
            setOwnerActivity(owner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.view_dialog_login);
        ButterKnife.bind(this);
        /*callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile,email");
        */

        onApplyComponents();

        onApplyData();

        onApplyEvents();
    }

    private void checkInputIsValidWhenTyping() {
        buttonLogin.setEnabled((!isEmailEmpty && !isPassEmpty));
    }

    @Override
    public void onClose() {
        dismiss();
    }

    @Override
    public void onApplyData() {

    }

    @Override
    public void onApplyEvents() {
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                isEmailEmpty = TextUtils.isEmpty(charSequence);
                checkInputIsValidWhenTyping();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                isPassEmpty = TextUtils.isEmpty(charSequence);
                checkInputIsValidWhenTyping();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    onLogin();
                }
                return false;
            }
        });

    }

    @Override
    public void onApplyComponents() {
        /*directionDetector = new SwipeDirectionDetector(owner) {
            @Override
            public void onDirectionDetected(Direction direction) {
                mDirection = direction;
            }
        };
        swipeDismissListener = new SwipeToDismissListener(dialogView, this, this);*/
    }

    @OnClick(R.id.button_ok)
    public void onLogin() {
        if (checkInputIsValid()) {
            if (AppUtils.isNetworkConnected(getContext())) {
                callNextAction();
            } else {
                // Check network connection
                if (iCheckNet % 10 == 0) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Lost network connection")
                            .setMessage("No worries - you can still use SAM. Points will sync one you get back online.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
            iCheckNet++;
        }
    }

    private void callNextAction() {
        String userName = txtEmail.getText().toString();
        String passWord = txtPwd.getText().toString();
        owner.startActivity(new Intent(getContext(), MainActivity.class));
        dismiss();
        owner.onBackPressed();
    }

    private boolean checkInputIsValid() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return UIUtils.IsEmailAccepted(txtEmail, true) && UIUtils.IsPasswordAccepted(txtPwd, true);
    }

/*@OnClick(R.id.button_no)
    public void onFacebookLogin() {
        if (AppUtils.isNetworkConnected(this)) {
            loginButton.performClick();
        } else {
        }
    }

    private void onLoginUsingFacebook() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        String userId = loginResult.getAccessToken().getUserId();
                        appConfig.setCurrentUserId(userId);
                        appConfig.setCurrentUserToken(AccessToken.getCurrentAccessToken().getToken());
                        Bundle params = new Bundle();
                        params.putString("fields", User.permission);

                        new GraphRequest(AccessToken.getCurrentAccessToken(),
                                userId, params, HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        if (response != null) {
                                            try {
                                                JSONObject data = response.getJSONObject();
                                                if (data.has("picture")) {
                                                    String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                                    appConfig.setCurrentUserAvatarUrl(profilePicUrl);
                                                    ListenerHelpers.saveImage(getApplicationContext(), SharedPresUtils.KEY_USER_AVATAR_URL, profilePicUrl, "profile");
                                                }
                                                if (data.has("cover")) {
                                                    String coverUrl = data.getJSONObject("cover").getString("source");
                                                    appConfig.setCurrentUserCoverUrl(coverUrl);
                                                    ListenerHelpers.saveImage(getApplicationContext(), SharedPresUtils.KEY_USER_COVER_URL, coverUrl, "cover");
                                                }
                                                if (data.has("name")) {
                                                    String name = data.getString("name");
                                                    appConfig.setCurrentUserName(name);
                                                }
                                                if (data.has("gender")) {
                                                    String gender = data.getString("gender");
                                                    appConfig.setCurrentUserGender(gender);
                                                }
                                                if (data.has("link")) {
                                                    String link = data.getString("link");
                                                    appConfig.setCurrentUserLink(link);
                                                }
                                                if (data.has("email")) {
                                                    String email = data.getString("email");
                                                    appConfig.setCurrentUserEmail(email);
                                                }
                                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                                onClose();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                        ).executeAsync();

                        //
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
*/

}