package com.example.smartgpsattendance.EmployeeApp.EmployeeFragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartgpsattendance.ActivitiesAndFragments.GPStracker;
import com.example.smartgpsattendance.FCM.FcmNotificationsSender;
import com.example.smartgpsattendance.Model.AttendanceModel;
import com.example.smartgpsattendance.Model.HrModel;
import com.example.smartgpsattendance.R;
import com.example.smartgpsattendance.databinding.AttendanceSheetFragmentBinding;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceSheetFragment extends Fragment {
    AttendanceSheetFragmentBinding binding;

    double radiusInMeters = 1000.0;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    StorageReference storageReference;
    String startTime;
    String endTime;
    String newTime;
    String currentDate;

    ArrayList<AttendanceModel> attendanceModelsList = new ArrayList<AttendanceModel>();

    private CircleOptions mCircle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AttendanceSheetFragmentBinding.inflate(getLayoutInflater());
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
        String currentDay = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        binding.attendanceRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fabOut.setVisibility(View.GONE);



        databaseReference.child("week work").child(currentDay.toUpperCase(Locale.ROOT)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                startTime = snapshot.child("start").getValue().toString();
                endTime = snapshot.child("end").getValue().toString();
                //Toast.makeText(getContext(), endTime, Toast.LENGTH_LONG).show();

                SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
                Date d = null;
                try {
                    d = df.parse(startTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, 15);
                newTime = df.format(cal.getTime());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Employees").child("Attendance").child(getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendanceModelsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    AttendanceModel attendanceModels = postSnapshot.getValue(AttendanceModel.class);
                    attendanceModelsList.add(attendanceModels);
                    assert attendanceModels != null;
                    if(attendanceModels.getDate().equals(currentDate))
                    {
                        binding.fab.setVisibility(View.GONE);

                        if(!attendanceModels.getTimeOut().equals(""))
                        {
                            binding.fabOut.setVisibility(View.GONE);
                        }else {
                            binding.fabOut.setVisibility(View.VISIBLE);
                        }

                    }else {
                        binding.fab.setVisibility(View.VISIBLE);
                    }

                }
                binding.attendanceRecyclerview.setAdapter(new AttendanceAdapter(attendanceModelsList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LatLng latLng = new LatLng(30.0991833, 31.3289655);
        CircleOptions circleOptions = new CircleOptions().center(latLng).radius(radiusInMeters).strokeWidth(8);
        mCircle = circleOptions;

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String currentDay = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());

                GPStracker gps;
                // Create class object
                gps = new GPStracker(getContext());
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    float[] distance = new float[2];

                    Location.distanceBetween(latitude, longitude,
                            mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);

                    if (distance[0] > mCircle.getRadius()) {
                        Toast.makeText(getContext(), "Outside, distance from center: " + distance[0] /*+ " radius: " + mCircle.getRadius()*/, Toast.LENGTH_LONG).show();
                    } else {
                        LocalTime time = LocalTime.parse(currentTime);

                        Boolean isAfter = (time.isAfter(LocalTime.parse(startTime)));
                        Boolean isBefore = (time.isBefore(LocalTime.parse(newTime)));
                        Boolean isDayOff = (LocalTime.parse(startTime).toString().equals("00:00"));

                        if(isDayOff)
                        {
                            Toast.makeText(getContext(), "This day is off", Toast.LENGTH_LONG).show();
                        }else {
                            if (isAfter && isBefore) {
                                addAttendance(currentTime, currentDate, currentDay.toUpperCase(Locale.ROOT), "1");
                                Toast.makeText(getContext(), "Thanks and have a good day", Toast.LENGTH_LONG).show();
                                //Toast.makeText(getContext(), "Inside, distance from center: " + distance[0] + " radius: " + mCircle.getRadius() , Toast.LENGTH_LONG).show();
                                binding.fab.setVisibility(View.GONE);
                                binding.fabOut.setVisibility(View.VISIBLE);

                                databaseReference.child("Hr").child("Details").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            HrModel hrModel =  postSnapshot.getValue(HrModel.class);
                                            assert hrModel != null;
                                            FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(hrModel.getToken(), "Gps Attendance","Check-in",getContext(), getActivity());
                                            fcmNotificationsSender.SendNotifications();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else {
                                Toast.makeText(getContext(), "The time has been ended", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });

        binding.fabOut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String currentDay = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
                String currentEndTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());

                GPStracker gps;
                // Create class object
                gps = new GPStracker(getContext());
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    float[] distance = new float[2];

                    Location.distanceBetween(latitude, longitude,
                            mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);

                    if (distance[0] > mCircle.getRadius()) {
                        Toast.makeText(getContext(), "Outside, distance from center: " + distance[0] /*+ " radius: " + mCircle.getRadius()*/, Toast.LENGTH_LONG).show();
                    } else {
                        LocalTime time = LocalTime.parse(currentEndTime);

                        Boolean isAfter = (time.isAfter(LocalTime.parse(endTime)));
                        //Boolean isBefore = (time.isBefore(LocalTime.parse(endNewTime)));

                        if (isAfter) {
                            databaseReference.child("Employees").child("Attendance").child(getUID()).child(currentDate).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    AttendanceModel attendanceModel = snapshot.getValue(AttendanceModel.class);
                                    addAttendanceOut(attendanceModel.getTimeIn(),currentDate,currentDay.toUpperCase(Locale.ROOT),"0",currentEndTime);
                                    Toast.makeText(getContext(), "Thank you", Toast.LENGTH_LONG).show();

                                    binding.fabOut.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else {
                            Toast.makeText(getContext(), "The end time not get yet", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void addAttendance(String timeIn, String date, String currentDay, String checkIn) {
        AttendanceModel attendanceModel = new AttendanceModel(getUID(), date, timeIn, "", currentDay, checkIn);

        databaseReference.child("Employees").child("Attendance").child(getUID()).child(date).setValue(attendanceModel);
    }

    private void addAttendanceOut (String timeIn, String date, String currentDay, String checkIn, String timeOut) {
        AttendanceModel attendanceModel = new AttendanceModel(getUID(), date, timeIn, timeOut, currentDay, checkIn);

        databaseReference.child("Employees").child("Attendance").child(getUID()).child(date).setValue(attendanceModel);
    }

    private String getUID() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }

    public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
        List<AttendanceModel> attendanceModels;

        public AttendanceAdapter(List<AttendanceModel> attendanceModels) {
            this.attendanceModels = attendanceModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.attendance_item, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.attendance_day.setText(attendanceModels.get(position).getCurrentDay());
            holder.attendance_time.setText("Time in: " + attendanceModels.get(position).getTimeIn());

            if(attendanceModels.get(position).getTimeOut().equals(""))
            {
                holder.attendance_end_time.setVisibility(View.GONE);
            }else {
                holder.attendance_end_time.setVisibility(View.VISIBLE);
                holder.attendance_end_time.setText("Time out: " + attendanceModels.get(position).getTimeOut());
            }

            databaseReference.child("week work").child(attendanceModels.get(position).getCurrentDay()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    holder.attendance_start.setText("Work start: " + snapshot.child("start").getValue().toString());
                    holder.attendance_end.setText("Work end: " + snapshot.child("end").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return attendanceModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView attendance_day,attendance_time,attendance_start,attendance_end, attendance_end_time;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                attendance_day = itemView.findViewById(R.id.attendance_day);
                attendance_time = itemView.findViewById(R.id.attendance_time);
                attendance_start = itemView.findViewById(R.id.attendance_start);
                attendance_end = itemView.findViewById(R.id.attendance_end);
                attendance_end_time = itemView.findViewById(R.id.attendance_end_time);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }
}
