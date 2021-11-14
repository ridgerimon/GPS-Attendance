package com.example.smartgpsattendance.HrApp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.smartgpsattendance.HrApp.HRFragment.HomeFragment;
import com.example.smartgpsattendance.Model.HrModel;
import com.example.smartgpsattendance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class HrActivity extends AppCompatActivity {
    String token;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        databaseReference.child("Hr").child("Details").child(getUID()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HrModel hrModelOld = snapshot.getValue(HrModel.class);
                                HrModel hrModel = new HrModel(hrModelOld.getFull_name_txt(),hrModelOld.getEmail_txt(),hrModelOld.getMobile_txt(),hrModelOld.getHrID_txt(),hrModelOld.getProfilePhoto(),hrModelOld.getuId(),token);

                                databaseReference.child("Hr").child("Details").child(getUID()).setValue(hrModel);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //Toast.makeText(HrActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        startFragment(new HomeFragment());

    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_hr_container, fragment)
                .disallowAddToBackStack()
                .commit();
    }

    private String getUID() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }
}