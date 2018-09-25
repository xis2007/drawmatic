package com.justinlee.drawmatic.activities;

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
    private static final String TAG = "justinx";

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
        Button getStartedButton = findViewById(R.id.button_start_login);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameInputString = nameInputEdittext.getText().toString();
                if ("".equals(userNameInputString) || userNameInputString.isEmpty()) {
                    Snackbar.make(findViewById(R.id.constraintLayout_login), "Name Required", Snackbar.LENGTH_LONG).show();
                } else {
                    FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                setResult(Constants.Login.LOGIN_SUCCESS);
                                saveUserInfo(task);
                                finish();
                            } else {
                                Snackbar.make(findViewById(R.id.constraintLayout_login), "Something went Wrong. Please try again", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void saveUserInfo(Task<AuthResult> task) {
        EditText nameInputEdittext = findViewById(R.id.edittext_user_name_input);
        Drawmatic.getAppContext().getSharedPreferences(Constants.UserData.SHAREPREF_USER_DATA_KEY, Context.MODE_PRIVATE).edit()
                .putString(Constants.UserData.SHAREPREF_USER_NAME_KEY, nameInputEdittext.getText().toString())
                .putString(Constants.UserData.SHAREPREF_USER_ID_KEY, task.getResult().getUser().getUid())
                .commit();
    }


}
