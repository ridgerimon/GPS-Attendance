package com.example.smartgpsattendance.EmployeeApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.smartgpsattendance.ActivitiesAndFragments.GPStracker;
import com.example.smartgpsattendance.EmployeeApp.EmployeeFragment.HomeFragment;
import com.example.smartgpsattendance.R;

public class EmployeeMainActivity extends AppCompatActivity {

    GPStracker gpStracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emplyee_main);

        startFragment(new HomeFragment());
        checkRunTimePermission();
    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_employee_container, fragment)
                .disallowAddToBackStack()
                .commit();
    }

    public void checkRunTimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(EmployeeMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(EmployeeMainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                    ActivityCompat.checkSelfPermission(EmployeeMainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gpStracker = new GPStracker(EmployeeMainActivity.this);

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        10);
            }
        } else {
            gpStracker = new GPStracker(EmployeeMainActivity.this); //GPSTracker is class that is used for retrieve user current location
        }
    }
}