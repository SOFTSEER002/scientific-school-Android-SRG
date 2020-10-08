package com.jeannypr.scientificstudy.Login.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.HelpActivity;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Dashboard.model.FamilyMembersModel;
import com.jeannypr.scientificstudy.Login.api.LoginService;
import com.jeannypr.scientificstudy.Login.model.LogSignInOutInputModel;
import com.jeannypr.scientificstudy.Login.model.LoginBean;
import com.jeannypr.scientificstudy.Login.model.LoginInputModel;
import com.jeannypr.scientificstudy.Login.model.LoginModel;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Base.activity.MainActivity;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "LoginLogs";
    Context context;
    LoginService service;
    UserPreference userPref;
    Spinner userRole;
    TextView unableToLogin, changeSclKey, regLink;
    MaterialButton loginBtn;
    TextInputEditText txtUserName, txtPassword;
    ProgressBar pb;
    SchoolDetailModel schoolData;
    CoordinatorLayout parent;
    Boolean isPaidApp = false;
    TextView txtHelp;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        userPref = UserPreference.getInstance(this);
        service = new DataRepo<>(LoginService.class, this).getService();
        isPaidApp = userPref.getSchoolData().getPaidVersionOfApp();

        if (userPref.isLoggedIn()) {
            Intent dashboardIntent = new Intent(context, MainActivity.class);
            dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(dashboardIntent);
            LoginActivity.this.finish();
        }

        setContentView(R.layout.activity_login);

        ImageView imgView = findViewById(R.id.logo);
        pb = findViewById(R.id.progressBar);
        TextView title = findViewById(R.id.title);
        parent = findViewById(R.id.parent);
        txtHelp = findViewById(R.id.helpLink);
        txtHelp.setOnClickListener(this);

        //TODO: save the logo image in the external device and load it everytime from local then

        schoolData = userPref.getSchoolData();
        if (schoolData != null && schoolData.SchoolLogo != null && !schoolData.SchoolLogo.equals("")) {
            Glide.with(this).load(schoolData.SchoolLogo).into(imgView);
        } else {
            Glide.with(this).load(R.drawable.default_school2).into(imgView);
        }

        if (schoolData != null && schoolData.SchoolName != null && !schoolData.SchoolName.equals("")) {
            title.setText(schoolData.SchoolName);
        }

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        unableToLogin = findViewById(R.id.unableToLogin);
        unableToLogin.setOnClickListener(this);
        changeSclKey = findViewById(R.id.getSchoolKey);
        changeSclKey.setOnClickListener(this);

        userRole = findViewById(R.id.userRole);
        txtUserName = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);
        regLink = findViewById(R.id.regLink);
        regLink.setOnClickListener(this);

        SetEditorActions();
        if (isPaidApp) {
            changeSclKey.setVisibility(View.GONE);
        }
    }

    private void SetEditorActions() {
        txtUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    txtPassword.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utility.CloseKeyboard(context);
                    InputValidation();
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void showLoader() {
        pb.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);
    }

    private void hideLoader() {
        pb.setVisibility(View.GONE);
        loginBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.loginBtn:

                InputValidation();
                break;

            case R.id.unableToLogin:
                Log.d(TAG, "unable to login case");
                Intent unableToLoginIntent = new Intent(this, UnableToLoginActivity.class);
                startActivity(unableToLoginIntent);

                break;
            case R.id.getSchoolKey:
                Log.d(TAG, "get key case");
                Intent enterSchoolKeyIntent = new Intent(this, EnterSchoolKeyActivity.class);
                startActivity(enterSchoolKeyIntent);
                break;

            case R.id.regLink:
                Intent signupIntent = new Intent(context, HelpActivity.class);
                signupIntent.putExtra("webUrl", Constants.SIGN_UP_URL);
                signupIntent.putExtra("title", schoolData.getSchoolName());
                signupIntent.putExtra("subtitle", "Registration");
                startActivity(signupIntent);
                break;

            case R.id.helpLink:
                Intent i = new Intent(this, HelpActivity.class);
                i.putExtra("webUrl", Constants.UNABLE_TO_LOGIN_HELP_URL);
                i.putExtra("title", getResources().getString(R.string.app_name));
                i.putExtra("subtitle", "Help");
                startActivity(i);
                break;

            default:
                Log.d(TAG, "default case");
                break;
        }
    }

    private void InputValidation() {
        final String userName = txtUserName.getText().toString();
        if (userName.equals("")) {
            Toast.makeText(context, "Please enter your registered username!", Toast.LENGTH_LONG).show();
            return;
        }
        String password = txtPassword.getText().toString();
        if (password.equals("")) {
            Toast.makeText(context, "Please enter your password", Toast.LENGTH_LONG).show();
            return;
        }

        LoginInputModel input = new LoginInputModel();
        input.UserName = userName;
        input.Password = password;

        showLoader();
        Login(input);
    }

    public void Login(final LoginInputModel input) {
        LoginService service = new DataRepo<>(LoginService.class, this).getService();
        service.login(input).enqueue(new Callback<LoginBean>() {

            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                LoginBean loginBean = response.body();

                if (loginBean != null) {
                    if (loginBean.rcode == Constants.Rcode.OK) {
                        LoginModel detail = loginBean.data;

                        if (detail != null) {
                            if (detail.User.getRoleTitle().equals(Constants.Role.PARENT)) {
                                if (detail.Student.size() < 1) {
                                    HavingNoChild();
                                    return;
                                }
                            }

                            if (detail.User != null) {
                                //GetDeviceToken();
                                SaveLogSignInOut(detail.User, userPref.getSchoolData().getSchoolKey(), Constants.LogType.LOGIN, userPref.getDeviceToken());
                                // SaveLogSignInOut(detail.User, userPref.getSchoolData().getSchoolKey(), Constants.LogType.LOGIN);
                            }

                            setDataInPref(detail);

                            Intent dashboardIntent = new Intent(context, MainActivity.class);
                            startActivity(dashboardIntent);
                            LoginActivity.this.finish();
                            finish();
                        }
                    }

                    hideLoader();
                    Toast.makeText(context, loginBean.msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                Log.d(TAG, "server call error");
                hideLoader();
                Toast.makeText(context, "Authentication failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDataInPref(LoginModel detail) {
        detail.User.setUserName(txtUserName.getText().toString());
        userPref.setUserData(detail.User);
        userPref.setChildren(detail.Student);

        if (detail.FatherOrHubby != null && detail.MotherOrWife != null) {
            ArrayList members = new ArrayList<FamilyMembersModel>();
            detail.MotherOrWife.setGender(Constants.Gender.FEMALE);
            detail.MotherOrWife.isStudent = false;

            detail.FatherOrHubby.setGender(Constants.Gender.MALE);
            detail.FatherOrHubby.isStudent = false;

            members.add(detail.MotherOrWife);
            members.add(detail.FatherOrHubby);
            userPref.setFamilyMembersData(members);
        }
    }

    private void HavingNoChild() {
        hideLoader();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.choose_child_alert, parent, false);
        builder.setTitle("No Child Found...");
        builder.setMessage("Please contact school admin.");
        //  parent.addView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               /* userPref.logOut();
                Intent loginIntent = new Intent(context, LoginActivity.class);
                startActivity(loginIntent);
                finish();*/
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void SaveLogSignInOut(UserModel user, String schoolCode, int logType, String token) {

        LogSignInOutInputModel deviceModel = new LogSignInOutInputModel();
        deviceModel.SchoolCode = schoolCode;
        deviceModel.UserId = user.getUserId();
        deviceModel.DeviceToken = token;
        deviceModel.Platform = Constants.Platform.ANDROID;
        deviceModel.RoleTitle = user.getRoleTitle();
        deviceModel.Name = user.getFirstName() + " " + user.getLastName();

        ChatService chatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();

        chatService.SaveLogSignInOut(deviceModel, logType).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    JsonObject obj = response.body();
                    boolean status = obj.get("status").getAsBoolean();
                    Log.i(TAG, "Status : " + status);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Failed to save device sign in/out log : " + t);

            }
        });
    }

    public void GetDeviceToken() {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
