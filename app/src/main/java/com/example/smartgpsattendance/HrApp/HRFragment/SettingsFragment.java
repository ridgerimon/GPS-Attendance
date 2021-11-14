package com.example.smartgpsattendance.HrApp.HRFragment;

import static com.example.smartgpsattendance.ActivitiesAndFragments.frameWork.replaceFragmentBloodBank;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartgpsattendance.ActivitiesAndFragments.SplashScreen;
import com.example.smartgpsattendance.Model.HrModel;
import com.example.smartgpsattendance.R;
import com.example.smartgpsattendance.databinding.SchedulDayWorkDialogBinding;
import com.example.smartgpsattendance.databinding.SettingsHrFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class SettingsFragment extends Fragment {
    SettingsHrFragmentBinding binding;
    SchedulDayWorkDialogBinding bindingDialog;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SettingsHrFragmentBinding.inflate(getLayoutInflater());
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
        databaseReference.child("Hr").child("Details").child(getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HrModel hrModel = snapshot.getValue(HrModel.class);

                assert hrModel != null;
                binding.nameHrProfile.setText("Name: " + hrModel.getFull_name_txt());
                binding.emailHrProfile.setText("Name: " + hrModel.getEmail_txt());

                Picasso.get()
                        .load(hrModel.getProfilePhoto())
                        .placeholder(R.drawable.logologo)
                        .error(R.drawable.logologo)
                        .into(binding.profileHrImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.viewProfileHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentBloodBank(SettingsFragment.this, new ProfileFragment(), true);
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

        binding.editScheduleHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHrDialog();
            }
        });
    }

    private void showHrDialog() {
        dialog = new Dialog(getActivity());

        bindingDialog = SchedulDayWorkDialogBinding.inflate(getLayoutInflater());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(bindingDialog.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        bindingDialog.startMONDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.startMONDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.endMONDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.endMONDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.startTUESDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.startTUESDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.endTUESDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.endTUESDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.startWEDNESDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.startWEDNESDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.endWEDNESDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.endWEDNESDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.startTHURSDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.startTHURSDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.endTHURSDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.endTHURSDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.startFRIDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.startFRIDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.endFRIDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.endFRIDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.startSATURDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.startSATURDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.endSATURDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.endSATURDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.startSUNDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.startSUNDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        bindingDialog.endSUNDAYField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String second = "00";
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHour_txt;
                        String selectedMinute_txt;
                        if (selectedHour < 10) {
                            selectedHour_txt = "0" + selectedHour;
                        } else {
                            selectedHour_txt = String.valueOf(selectedHour);
                        }
                        if (selectedMinute == 0) {
                            selectedMinute_txt = "0" + selectedMinute;
                        } else {
                            selectedMinute_txt = String.valueOf(selectedMinute);
                        }
                        bindingDialog.endSUNDAYField.setText(selectedHour_txt + ":" + selectedMinute_txt + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        databaseReference.child("week work").child("MONDAY").child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.startMONDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("MONDAY").child("end").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.endMONDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("TUESDAY").child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.startTUESDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("TUESDAY").child("end").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.endTUESDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("WEDNESDAY").child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.startWEDNESDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("WEDNESDAY").child("end").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.endWEDNESDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("THURSDAY").child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.startTHURSDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("THURSDAY").child("end").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.endTHURSDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("FRIDAY").child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.startFRIDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("FRIDAY").child("end").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.endFRIDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("SATURDAY").child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.startSATURDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("SATURDAY").child("end").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.endSATURDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("SUNDAY").child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.startSUNDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("week work").child("SUNDAY").child("end").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bindingDialog.endSUNDAYField.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bindingDialog.scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateDays(bindingDialog.startMONDAYField.getText().toString(), bindingDialog.endMONDAYField.getText().toString(),
                        bindingDialog.startTUESDAYField.getText().toString(), bindingDialog.endTUESDAYField.getText().toString(),
                        bindingDialog.startWEDNESDAYField.getText().toString(), bindingDialog.endWEDNESDAYField.getText().toString(),
                        bindingDialog.startTHURSDAYField.getText().toString(), bindingDialog.endTHURSDAYField.getText().toString(),
                        bindingDialog.startFRIDAYField.getText().toString(), bindingDialog.endFRIDAYField.getText().toString(),
                        bindingDialog.startSATURDAYField.getText().toString(), bindingDialog.endSATURDAYField.getText().toString(),
                        bindingDialog.startSUNDAYField.getText().toString(), bindingDialog.endSUNDAYField.getText().toString());
            }


        });
        bindingDialog.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void updateDays(String toString, String toString1, String toString2, String toString3, String toString4, String toString5, String toString6, String toString7, String toString8, String toString9, String toString10, String toString11, String toString12, String toString13) {
        databaseReference.child("week work").child("MONDAY").child("start").setValue(toString);
        databaseReference.child("week work").child("MONDAY").child("end").setValue(toString1);
        databaseReference.child("week work").child("TUESDAY").child("start").setValue(toString2);
        databaseReference.child("week work").child("TUESDAY").child("end").setValue(toString3);
        databaseReference.child("week work").child("WEDNESDAY").child("start").setValue(toString4);
        databaseReference.child("week work").child("WEDNESDAY").child("end").setValue(toString5);
        databaseReference.child("week work").child("THURSDAY").child("start").setValue(toString6);
        databaseReference.child("week work").child("THURSDAY").child("end").setValue(toString7);
        databaseReference.child("week work").child("FRIDAY").child("start").setValue(toString8);
        databaseReference.child("week work").child("FRIDAY").child("end").setValue(toString9);
        databaseReference.child("week work").child("SATURDAY").child("start").setValue(toString10);
        databaseReference.child("week work").child("SATURDAY").child("end").setValue(toString11);
        databaseReference.child("week work").child("SUNDAY").child("start").setValue(toString12);
        databaseReference.child("week work").child("SUNDAY").child("end").setValue(toString13);
    }

    private String getUID() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }
}
