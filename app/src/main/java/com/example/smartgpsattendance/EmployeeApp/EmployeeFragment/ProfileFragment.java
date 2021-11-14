package com.example.smartgpsattendance.EmployeeApp.EmployeeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartgpsattendance.ActivitiesAndFragments.SplashScreen;
import com.example.smartgpsattendance.Model.EmployeeModel;
import com.example.smartgpsattendance.R;
import com.example.smartgpsattendance.databinding.EmployeeProfileFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment
{
    EmployeeProfileFragmentBinding binding;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EmployeeProfileFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");

        initViews();
    }

    private void initViews() {
        databaseReference.child("Employees").child("Details").child(getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployeeModel employeeModel = snapshot.getValue(EmployeeModel.class);

                assert employeeModel != null;
                binding.idEmployee.setText("ID: " + employeeModel.getEmployeeID_txt());
                binding.nameEmployee.setText("Name: " + employeeModel.getFull_name_txt());
                binding.emailEmployee.setText("Email: " + employeeModel.getEmail_txt());
                binding.mobileEmployee.setText("Mobile: " + employeeModel.getMobile_txt());
                binding.jopTitleEmployee.setText("Jop title: " + employeeModel.getJop_title_txt());
                binding.jopTypeEmployee.setText("Jop type: " + employeeModel.getJop_type_txt());

                Picasso.get()
                        .load(employeeModel.getProfilePhoto())
                        .placeholder(R.drawable.logologo)
                        .error(R.drawable.logologo)
                        .into(binding.profilePic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), SplashScreen.class);
                startActivity(intent);
            }
        });
    }

    private String getUID() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }

}
