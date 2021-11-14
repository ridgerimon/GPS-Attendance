package com.example.smartgpsattendance.ActivitiesAndFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgpsattendance.AdminApp.AdminMainActivity;
import com.example.smartgpsattendance.EmployeeApp.EmployeeMainActivity;
import com.example.smartgpsattendance.HrApp.HrActivity;
import com.example.smartgpsattendance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.rotate.RotateLoading;

public class SignInActivity extends AppCompatActivity {


    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;

    EditText emailField, passwordField;
    TextView forgotpassword;
    Button signInBtn;
    RotateLoading rotateLoading;
    CheckBox checkBox;

    Boolean saveLogin;

    String email_txt, password_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loginPreferences = getApplicationContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initViews();
        setupButton();
    }

    void initViews() {
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        signInBtn = findViewById(R.id.sign_in_btn);
        forgotpassword = findViewById(R.id.forgot_password_txt);
        rotateLoading = findViewById(R.id.signinrotateloading);
        checkBox = findViewById(R.id.remember_me_checkbox);
    }

    void setupButton() {
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String emailAddress = emailField.getText().toString();

                if (TextUtils.isEmpty(emailAddress)) {
                    Toast.makeText(getApplicationContext(), "please enter your email firstly", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (emailAddress.equals("admin@admin.com")) {
                    Toast.makeText(getApplicationContext(), "you can't reset admin account password", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "password reset email has been sent to : " + emailAddress, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin) {
            emailField.setText(loginPreferences.getString("username", ""));
            passwordField.setText(loginPreferences.getString("password", ""));
            checkBox.setChecked(true);
        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_txt = emailField.getText().toString();
                password_txt = passwordField.getText().toString();

                if (TextUtils.isEmpty(email_txt)) {
                    Toast.makeText(getApplicationContext(), "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password_txt)) {
                    Toast.makeText(getApplicationContext(), "please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email_txt.equals("admin@admin.com") && password_txt.equals("adminadmin")) {
                    rotateLoading.start();

                    AdminLogin(email_txt, password_txt);

                    loginPrefsEditor.putBoolean("savepassword", true);
                    loginPrefsEditor.putString("pass", password_txt);
                    loginPrefsEditor.apply();

                    if (checkBox.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", email_txt);
                        loginPrefsEditor.putString("password", password_txt);
                        loginPrefsEditor.apply();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.apply();
                    }

                } else {
                    rotateLoading.start();

                    UserLogin(email_txt, password_txt);

                    loginPrefsEditor.putBoolean("savepassword", true);
                    loginPrefsEditor.putString("pass", password_txt);
                    loginPrefsEditor.apply();

                    if (checkBox.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", email_txt);
                        loginPrefsEditor.putString("password", password_txt);
                        loginPrefsEditor.apply();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.apply();
                    }
                }
            }
        });
    }

    private void UserLogin(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            category();
                        } else {
                            rotateLoading.stop();
                            String taskmessage = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), taskmessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void AdminLogin(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            rotateLoading.stop();

                            Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                            startActivity(intent);

                        } else {
                            rotateLoading.stop();
                            String taskmessage = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), taskmessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void category() {
        final String id = getUID();
        databaseReference.child("Employees").child("Details").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(id)) {
                            rotateLoading.stop();
                            Toast.makeText(SignInActivity.this, "Employee : " + id, Toast.LENGTH_SHORT).show();
                            updateEmployeeUI();
                        } else {
                            databaseReference.child("Hr").child("Details").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(id)) {
                                                rotateLoading.stop();
                                                Toast.makeText(SignInActivity.this, "HR : " + id, Toast.LENGTH_SHORT).show();
                                                updateHrUI();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            //rotateLoading.stop();
                                            Toast.makeText(SignInActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //rotateLoading.stop();
                        Toast.makeText(SignInActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateEmployeeUI() {
        Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);
        startActivity(intent);
    }

    public void updateHrUI() {
        Intent intent = new Intent(getApplicationContext(), HrActivity.class);
        startActivity(intent);
    }

    private String getUID() {
        user = auth.getCurrentUser();
        String UserID = user.getUid();

        return UserID;
    }
}



