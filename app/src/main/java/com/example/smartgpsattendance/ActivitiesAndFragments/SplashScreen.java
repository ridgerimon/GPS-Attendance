package com.example.smartgpsattendance.ActivitiesAndFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgpsattendance.AdminApp.AdminMainActivity;
import com.example.smartgpsattendance.EmployeeApp.EmployeeMainActivity;
import com.example.smartgpsattendance.HrApp.HrActivity;
import com.example.smartgpsattendance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    ImageView imageView;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    SharedPreferences loginPreferences;

    Boolean saveLogin;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        imageView = findViewById(R.id.splash);

        imageView.setScaleX(0);
        imageView.setScaleY(0);

        imageView.animate().rotation(1).scaleX(1).scaleYBy(1).setDuration(3000);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        loginPreferences = getApplicationContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(saveLogin)
                {
                    if (user != null) {
                        category(user.getUid());
                    } else {
                        Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else {
                    Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        };
        new Timer().schedule(task, 3000);
    }

    @Override
    public void onBackPressed() {
    }

    public void category(String getUID) {
        final String id = getUID;
        databaseReference.child("Employees").child("Details").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(id)) {
                            Toast.makeText(SplashScreen.this, "Employee : " + id, Toast.LENGTH_SHORT).show();
                            updateEmployeeUI();
                        } else {
                            databaseReference.child("Hr").child("Details").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(id)) {
                                                Toast.makeText(SplashScreen.this, "HR : " + id, Toast.LENGTH_SHORT).show();
                                                updateHrUI();
                                            }else {
                                                //updateAdminUI();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            //rotateLoading.stop();
                                            Toast.makeText(SplashScreen.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //rotateLoading.stop();
                        Toast.makeText(SplashScreen.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void updateAdminUI() {
        Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
        startActivity(intent);
    }
}