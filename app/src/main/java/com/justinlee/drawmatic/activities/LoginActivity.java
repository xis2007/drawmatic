package com.justinlee.drawmatic.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.justinlee.drawmatic.Drawmatic;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.bases.BaseActivity;
import com.justinlee.drawmatic.constants.Constants;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    @Override
    protected void onDestroy() {
        setResult(Constants.Login.LOGIN_EXIT);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(Constants.Login.LOGIN_EXIT);
        super.onBackPressed();
    }

    private void initViews() {
        final EditText nameInputEdittext = findViewById(R.id.edittext_user_name_input);
        final Button getStartedButton = findViewById(R.id.button_start_login);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameInputString = nameInputEdittext.getText().toString();
                if (!Drawmatic.isNetworkConnected()) {
                    Snackbar.make(findViewById(R.id.constraintLayout_login), R.string.snackbar_hint_no_network, Snackbar.LENGTH_LONG).show();
                } else if ("".equals(userNameInputString) || userNameInputString.isEmpty()) {
                    Snackbar.make(findViewById(R.id.constraintLayout_login), R.string.snackbar_hint_name_input, Snackbar.LENGTH_LONG).show();
                } else {
                    getStartedButton.setBackground(getResources().getDrawable(R.drawable.box_button_greyed_login));

                    FirebaseAuth.getInstance()
                            .signInAnonymously()
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        setResult(Constants.Login.LOGIN_SUCCESS);
                                        saveUserInfo(task);
                                        finish();
                                    } else {
                                        getStartedButton.setBackground(getResources().getDrawable(R.drawable.box_button_login));
                                        Snackbar.make(findViewById(R.id.constraintLayout_login), R.string.snackbar_hint_something_went_wrong, Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @SuppressLint("ApplySharedPref")
    private void saveUserInfo(Task<AuthResult> task) {
        EditText nameInputEdittext = findViewById(R.id.edittext_user_name_input);
        Drawmatic.getAppContext().getSharedPreferences(Constants.UserData.SHAREPREF_USER_DATA_KEY, Context.MODE_PRIVATE).edit()
                .putString(Constants.UserData.SHAREPREF_USER_NAME_KEY, nameInputEdittext.getText().toString())
                .putString(Constants.UserData.SHAREPREF_USER_ID_KEY, task.getResult().getUser().getUid())
                .commit();
    }
}
