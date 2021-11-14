package com.example.smartgpsattendance.AdminApp.AdminFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.smartgpsattendance.Model.EmployeeModel;
import com.example.smartgpsattendance.Model.HrModel;
import com.example.smartgpsattendance.R;
import com.example.smartgpsattendance.databinding.AddEmployeeFragmentBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddEmployee extends Fragment
{
    AddEmployeeFragmentBinding binding;

    int randomNo;

    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    CircleImageView profile_image;
    static EditText first_name, last_name,employeeID, email_address, personal_id, password, phone_number, closest_number, address, date_edittext;
    String first_name_txt, last_name_txt, full_name_txt, employeeID_txt, email_txt, password_txt, mobile_txt, address_txt, closest_txt;

    LinearLayout jopTitleLinear, jopTypeLinear;
    Spinner jop_type, jop_title;
    Button sign_up_btn, cancel_btn;
    String jop_type_txt, jop_title_txt;
    String selectedimageurl, selectedimageurl2;
    Uri photoPath, photoPathHr;
    ProgressDialog progressDialog;

    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = AddEmployeeFragmentBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");
        
        binding.employeeSignUpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRandomID();
                showEmployeeDialog();
            }
        });
        binding.hrSignUpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRandomID();
                showHrDialog();
            }
        });
    }

    private void showEmployeeDialog() {
        dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.employee_register_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        profile_image = dialog.findViewById(R.id.profile_image);
        first_name = dialog.findViewById(R.id.first_name_field);
        last_name = dialog.findViewById(R.id.last_name_field);
        employeeID = dialog.findViewById(R.id.id_num_field);
        email_address = dialog.findViewById(R.id.email_field);
        password = dialog.findViewById(R.id.password_field);
        phone_number = dialog.findViewById(R.id.mobile_field);
        jop_title = dialog.findViewById(R.id.Job_Title_spinner);
        jop_type = dialog.findViewById(R.id.Job_Type_spinner);

        sign_up_btn = dialog.findViewById(R.id.sign_up_btn);
        cancel_btn = dialog.findViewById(R.id.cancel_btn);

        employeeID.setText(String.valueOf(randomNo) );

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                R.array.Job_Title, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.Job_Type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        jop_title.setAdapter(adapter1);
        jop_type.setAdapter(adapter2);

        jop_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jop_title_txt = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jop_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jop_type_txt = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_name_txt = first_name.getText().toString();
                last_name_txt = last_name.getText().toString();
                full_name_txt = first_name_txt + " " + last_name_txt;
                employeeID_txt = employeeID.getText().toString();
                email_txt = email_address.getText().toString();
                password_txt = password.getText().toString();
                mobile_txt = phone_number.getText().toString();

                if (TextUtils.isEmpty(first_name_txt)) {
                    Toast.makeText(getContext(), "please enter your first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(last_name_txt)) {
                    Toast.makeText(getContext(), "please enter your last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email_txt)) {
                    Toast.makeText(getContext(), "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password_txt)) {
                    Toast.makeText(getContext(), "please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(employeeID_txt)) {
                    Toast.makeText(getContext(), "please enter your employee ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mobile_txt)) {
                    Toast.makeText(getContext(), "please enter your mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (photoPath == null) {
                    Toast.makeText(getContext(), "please add your picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (jop_title_txt.equals("Select your jop Job Title")) {
                    Toast.makeText(getContext(), "please select your jop title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (jop_type_txt.equals("Select your Job Type")) {
                    Toast.makeText(getContext(), "please select your Job Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Employee Sign Up");
                progressDialog.setMessage("Please Wait Until Creating Account ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);


                CreateEmployeeAccount(email_txt, password_txt, full_name_txt, mobile_txt, jop_title_txt,jop_type_txt, employeeID_txt);

                //CustomerRegister(full_name_txt,email_txt,password_txt,mobile_txt,"Customer");
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1, 1)
                        .start(getContext(), AddEmployee.this);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showHrDialog() {
        dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.employee_register_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        profile_image = dialog.findViewById(R.id.profile_image);
        first_name = dialog.findViewById(R.id.first_name_field);
        last_name = dialog.findViewById(R.id.last_name_field);
        employeeID = dialog.findViewById(R.id.id_num_field);
        email_address = dialog.findViewById(R.id.email_field);
        password = dialog.findViewById(R.id.password_field);
        phone_number = dialog.findViewById(R.id.mobile_field);
        jop_title = dialog.findViewById(R.id.Job_Title_spinner);
        jop_type = dialog.findViewById(R.id.Job_Type_spinner);
        jopTitleLinear = dialog.findViewById(R.id.jop_title_linear);
        jopTypeLinear = dialog.findViewById(R.id.jop_type_linear);

        jopTitleLinear.setVisibility(View.GONE);
        jopTypeLinear.setVisibility(View.GONE);

        sign_up_btn = dialog.findViewById(R.id.sign_up_btn);
        cancel_btn = dialog.findViewById(R.id.cancel_btn);

        employeeID.setText(String.valueOf(randomNo) );

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_name_txt = first_name.getText().toString();
                last_name_txt = last_name.getText().toString();
                full_name_txt = first_name_txt + " " + last_name_txt;
                employeeID_txt = employeeID.getText().toString();
                email_txt = email_address.getText().toString();
                password_txt = password.getText().toString();
                mobile_txt = phone_number.getText().toString();

                if (TextUtils.isEmpty(first_name_txt)) {
                    Toast.makeText(getContext(), "please enter your first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(last_name_txt)) {
                    Toast.makeText(getContext(), "please enter your last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email_txt)) {
                    Toast.makeText(getContext(), "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password_txt)) {
                    Toast.makeText(getContext(), "please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(employeeID_txt)) {
                    Toast.makeText(getContext(), "please enter your employee ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mobile_txt)) {
                    Toast.makeText(getContext(), "please enter your mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (photoPathHr == null) {
                    Toast.makeText(getContext(), "please add your picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("HR Sign Up");
                progressDialog.setMessage("Please Wait Until Creating Account ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);


                CreateHrAccount(email_txt, password_txt, full_name_txt, mobile_txt, employeeID_txt);

                //CustomerRegister(full_name_txt,email_txt,password_txt,mobile_txt,"Customer");
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1, 1)
                        .start(getContext(), AddEmployee.this);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void CreateHrAccount(String email_txt, String password_txt, String full_name_txt, String mobile_txt, String HrID_txt) {
        auth.createUserWithEmailAndPassword(email_txt, password_txt)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uploadImageHr(full_name_txt, email_txt, mobile_txt, HrID_txt);
                        } else {
                            String error_message = task.getException().getMessage();
                            Toast.makeText(getContext(), error_message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void uploadImageHr(String full_name_txt, String email_txt, String mobile_txt, String employeeID_txt) {
        UploadTask uploadTask;

        final StorageReference ref = storageReference.child("images/" + photoPathHr.getLastPathSegment());

        uploadTask = ref.putFile(photoPathHr);

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

                selectedimageurl = downloadUri.toString();

                AddHrToDB(full_name_txt, email_txt, mobile_txt, employeeID_txt,selectedimageurl);
                progressDialog.dismiss();
                sendEmail(email_txt);
                dialog.dismiss();
                Toast.makeText(getContext(), "successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void AddHrToDB(String full_name_txt, String email_txt, String mobile_txt, String employeeID_txt, String selectedimageurl) {
        HrModel hrModel = new HrModel(full_name_txt,email_txt,mobile_txt, employeeID_txt,selectedimageurl,getUID(),"");

        databaseReference.child("Hr").child("Details").child(getUID()).setValue(hrModel);
    }

    private void CreateEmployeeAccount(String email_txt, String password_txt, String full_name_txt, String mobile_txt, String jop_title_txt, String jop_type_txt, String employeeID_txt) {
        auth.createUserWithEmailAndPassword(email_txt, password_txt)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uploadImage(full_name_txt, email_txt, mobile_txt, jop_title_txt, jop_type_txt, employeeID_txt);
                        } else {
                            String error_message = task.getException().getMessage();
                            Toast.makeText(getContext(), error_message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void uploadImage(String full_name_txt, String email_txt, String mobile_txt, String jop_title_txt, String jop_type_txt, String employeeID_txt) {
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

                selectedimageurl = downloadUri.toString();

                AddEmployeeToDB(full_name_txt, email_txt, mobile_txt, jop_title_txt, jop_type_txt, employeeID_txt,selectedimageurl);
                progressDialog.dismiss();
                sendEmail(email_txt);
                dialog.dismiss();
                Toast.makeText(getContext(), "successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void AddEmployeeToDB(String full_name_txt, String email_txt, String mobile_txt, String jop_title_txt, String jop_type_txt, String employeeID_txt, String selectedimageurl) {
        EmployeeModel employeeModel = new EmployeeModel(full_name_txt,email_txt,mobile_txt,jop_title_txt,jop_type_txt,employeeID_txt,selectedimageurl,getUID());

        databaseReference.child("Employees").child("Details").child(getUID()).setValue(employeeModel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                if (result != null) {
                    photoPath = result.getUri();
                    photoPathHr = result.getUri();

                    Picasso.get()
                            .load(photoPath)
                            .placeholder(R.drawable.addphoto)
                            .error(R.drawable.addphoto)
                            .into(profile_image);

                    Picasso.get()
                            .load(photoPathHr)
                            .placeholder(R.drawable.addphoto)
                            .error(R.drawable.addphoto)
                            .into(profile_image);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    void makeRandomID(){
        Random r = new Random();
        randomNo = r.nextInt(1000+1);
    }

    void sendEmail(String email){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "Smart GPS Attendance");
        i.putExtra(Intent.EXTRA_TEXT   , "Your account have bean created and the email is " + email + " And the password is 000000 you can change it from our application" );
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUID() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }
}
