package com.yosta.phuotngay.activities.dialogs;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogLogin /*extends Dialog implements DialogBehavior */ {
/*

    @BindView(R.id.textView)
    TextInputEditText txtEmail;

    @BindView(R.id.textView_title)
    TextInputEditText txtPwd;

    @BindView(R.id.button_ok)
    Button buttonLogin;

    private boolean isEmailEmpty = true;
    private boolean isPassEmpty = true;

    private Activity ownerActivity = null;

    public DialogLogin(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        onAttachedWindow(context);
        setCancelable(true);
    }

    public DialogLogin(Context context, boolean isCancelable) {
        super(context, R.style.AppTheme_CustomDialog);
        setCancelable(isCancelable);
        onAttachedWindow(context);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ownerActivity = getOwnerActivity();
    }

    @Override
    public void onAttachedWindow(Context context) {
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideRightLeft;
        }
        ownerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (ownerActivity != null)
            setOwnerActivity(ownerActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.view_dialog_login);
        ButterKnife.bind(this);
        */
/*callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile,email");
        *//*

        onApplyComponents();

        onApplyData();

        onApplyEvents();
    }

    private void checkInputIsValidWhenTyping() {
        buttonLogin.setEnabled((!isEmailEmpty && !isPassEmpty));
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
                    onLogin(v);
                }
                return false;
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputIsValid()) {
                    if (AppUtils.isNetworkConnected(getContext())) {
                        onLogin(v);
                    } else {
                        // TODO
                        AppUtils.showSnackBarNotify(v, "Lost connection! Please check again!");
                    }
                }
            }
        });
    }

    private void onLogin(final View v) {
        String userName = txtEmail.getText().toString();
        String passWord = txtPwd.getText().toString();
        final DialogProgress dialogProgress = new DialogProgress(ownerActivity, false);
        dialogProgress.show();
        PhuotNgayApiService.inject(ownerActivity)
                .ApiLogin(userName, passWord, new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, LoginResponse<String> response) {
                        int code = response.code();
                        if (code == 200) {

                            String token = response.body();
                            if (token != null && !TextUtils.isEmpty(token)) {

                                AppConfig appConfig = (AppConfig) ownerActivity.getApplication();
                                if (appConfig.setCurrentUserToken(token)) {
                                    if (isShowing()) dismiss();
                                    ownerActivity.finish();
                                    ownerActivity.startActivity(new Intent(ownerActivity, MainActivity.class));
                                }
                            } else {
                                // TODO
                                AppUtils.showSnackBarNotify(v, "The account that you've entered is incorrect. FORGOT PASSWORD!");
                            }

                        }
                        dialogProgress.dismiss();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        AppUtils.showSnackBarNotify(v, t.getMessage());
                        dialogProgress.dismiss();
                        if (isShowing()) dismiss();
                    }
                });
    }

    @Override
    public void onApplyComponents() {

    }

    private boolean checkInputIsValid() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return ValidateHelper.IsEmailAccepted(txtEmail, true) && ValidateHelper.IsPasswordAccepted(txtPwd, true);
    }
*/

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
                                                    ListenerHelpers.saveImage(getApplicationContext(), StorageHelper.KEY_USER_AVATAR_URL, profilePicUrl, "profile");
                                                }
                                                if (data.has("cover")) {
                                                    String coverUrl = data.getJSONObject("cover").getString("source");
                                                    appConfig.setCurrentUserCoverUrl(coverUrl);
                                                    ListenerHelpers.saveImage(getApplicationContext(), StorageHelper.KEY_USER_COVER_URL, coverUrl, "cover");
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