package com.example.smartgpsattendance.HrApp.HRFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartgpsattendance.EmployeeApp.EmployeeFragment.AttendanceSheetFragment;
import com.example.smartgpsattendance.Model.AttendanceModel;
import com.example.smartgpsattendance.R;
import com.example.smartgpsattendance.databinding.EmployeeHrAttendanceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAttendanceFragment extends Fragment
{
    EmployeeHrAttendanceBinding binding;
    String EmployeeID;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    StorageReference storageReference;

    ArrayList<AttendanceModel> attendanceModelsList = new ArrayList<AttendanceModel>();

    public EmployeeAttendanceFragment(String employeeID) {
        this.EmployeeID = employeeID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EmployeeHrAttendanceBinding.inflate(getLayoutInflater());
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

    private void initViews()
    {
        binding.attendanceHrRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference.child("Employees").child("Attendance").child(EmployeeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendanceModelsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    AttendanceModel attendanceModels = postSnapshot.getValue(AttendanceModel.class);
                    attendanceModelsList.add(attendanceModels);
                }
                binding.attendanceHrRecyclerview.setAdapter(new AttendanceAdapter(attendanceModelsList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
