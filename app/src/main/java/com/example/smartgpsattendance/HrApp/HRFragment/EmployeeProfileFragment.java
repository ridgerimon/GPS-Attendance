package com.example.smartgpsattendance.HrApp.HRFragment;

import static com.example.smartgpsattendance.ActivitiesAndFragments.frameWork.replaceFragmentBloodBank;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.smartgpsattendance.Model.EmployeeModel;
import com.example.smartgpsattendance.Model.HrModel;
import com.example.smartgpsattendance.R;
import com.example.smartgpsattendance.databinding.EmployeeHrProfileFragmentBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class EmployeeProfileFragment extends Fragment
{
    EmployeeHrProfileFragmentBinding binding;

    String EmployeeID;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    String name, mobile, email, employeeID, profile_image_url, token, selected_placeImageURL, jopTitle_txt, jopType_txt;
    Uri photoPath;

    public EmployeeProfileFragment(String employeeID) {
        EmployeeID = employeeID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EmployeeHrProfileFragmentBinding.inflate(getLayoutInflater());
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
        returndata();
        binding.saveCancelBtn.setVisibility(View.GONE);
        binding.emailEmployee.setEnabled(false);
        binding.mobileEmployee.setEnabled(false);
        binding.nameEmployee.setEnabled(false);
        binding.idEmployee.setEnabled(false);
        binding.profilePic.setEnabled(false);
        binding.JobTypeSpinner.setEnabled(false);
        binding.JobTitleSpinner.setEnabled(false);

        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.saveCancelBtn.setVisibility(View.VISIBLE);
                binding.mobileEmployee.setEnabled(true);
                binding.nameEmployee.setEnabled(true);
                binding.profilePic.setEnabled(true);
                binding.JobTypeSpinner.setEnabled(false);
                binding.JobTitleSpinner.setEnabled(false);
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.saveCancelBtn.setVisibility(View.GONE);
                binding.emailEmployee.setEnabled(false);
                binding.mobileEmployee.setEnabled(false);
                binding.nameEmployee.setEnabled(false);
                binding.idEmployee.setEnabled(false);
                binding.profilePic.setEnabled(false);
                binding.JobTypeSpinner.setEnabled(false);
                binding.JobTitleSpinner.setEnabled(false);
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.nameEmployee.getText().toString();
                mobile = binding.mobileEmployee.getText().toString();
                email = binding.emailEmployee.getText().toString();
                jopTitle_txt = binding.JobTitleSpinner.getText().toString();
                jopType_txt = binding.JobTypeSpinner.getText().toString();

                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(getContext(), "Must write our name", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(mobile))
                {
                    Toast.makeText(getContext(), "Must write our mobile", Toast.LENGTH_LONG).show();
                    return;
                }

                binding.saveCancelBtn.setVisibility(View.GONE);
                binding.emailEmployee.setEnabled(false);
                binding.mobileEmployee.setEnabled(false);
                binding.nameEmployee.setEnabled(false);
                binding.idEmployee.setEnabled(false);
                binding.profilePic.setEnabled(false);
                binding.JobTypeSpinner.setEnabled(false);
                binding.JobTitleSpinner.setEnabled(false);

                if (photoPath == null) {
                    UpdateHrProfile(name, email, mobile, jopTitle_txt, jopType_txt , employeeID,profile_image_url);
                } else {
                    uploadImage(name, email, mobile, jopTitle_txt, jopType_txt , employeeID);
                }

            }

            private void uploadImage(String name, String email, String mobile, String jopTitle, String jopType, String employeeID) {
                binding.rotateloading.start();
                UploadTask uploadTask;

                final StorageReference ref = storageReference.child("images/" + photoPath.getLastPathSegment());

                uploadTask = ref.putFile(photoPath);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri downloadUri = task.getResult();

                        binding.rotateloading.stop();

                        selected_placeImageURL = downloadUri.toString();

                        UpdateHrProfile(name, email, mobile, jopTitle_txt, jopType_txt , employeeID, selected_placeImageURL);

                        Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();

                        returndata();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void UpdateHrProfile(String name, String email, String mobile, String jopTitle, String jopType, String employeeID,  String profile_image_url) {
                EmployeeModel employeeModel = new EmployeeModel(name,email,mobile,jopTitle,jopType,employeeID,profile_image_url,EmployeeID);
                databaseReference.child("Employees").child("Details").child(EmployeeID).setValue(employeeModel);
            }
        });

        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1, 1)
                        .start(getContext(), EmployeeProfileFragment.this);
            }
        });

        binding.scheduleEmployeeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentBloodBank(EmployeeProfileFragment.this, new EmployeeAttendanceFragment(EmployeeID), true);
            }
        });
    }

    private void returndata() {
        databaseReference.child("Employees").child("Details").child(EmployeeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployeeModel employeeModel = snapshot.getValue(EmployeeModel.class);

                assert employeeModel != null;
                binding.nameEmployee.setText(employeeModel.getFull_name_txt());
                binding.emailEmployee.setText(employeeModel.getEmail_txt());
                binding.idEmployee.setText(employeeModel.getEmployeeID_txt());
                binding.mobileEmployee.setText(employeeModel.getMobile_txt());
                binding.JobTypeSpinner.setText(employeeModel.getJop_type_txt());
                binding.JobTitleSpinner.setText(employeeModel.getJop_title_txt());
                profile_image_url = employeeModel.getProfilePhoto();
                employeeID = employeeModel.getEmployeeID_txt();

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                if (result != null) {
                    photoPath = result.getUri();

                    Picasso.get()
                            .load(photoPath)
                            .placeholder(R.drawable.addphoto)
                            .error(R.drawable.addphoto)
                            .into(binding.profilePic);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
