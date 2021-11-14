package com.example.smartgpsattendance.HrApp.HRFragment;

import static com.example.smartgpsattendance.ActivitiesAndFragments.frameWork.replaceFragmentBloodBank;

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

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.smartgpsattendance.EmployeeApp.EmployeeFragment.AttendanceSheetFragment;
import com.example.smartgpsattendance.Model.AttendanceModel;
import com.example.smartgpsattendance.Model.EmployeeModel;
import com.example.smartgpsattendance.R;
import com.example.smartgpsattendance.databinding.AllEmployeeFragmentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllEmployeesFragment extends Fragment
{
    AllEmployeeFragmentBinding binding;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    ArrayList<EmployeeModel> employeeModelsList = new ArrayList<EmployeeModel>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AllEmployeeFragmentBinding.inflate(getLayoutInflater());
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
        binding.employeeRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference.child("Employees").child("Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeeModelsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    EmployeeModel employeeModel = postSnapshot.getValue(EmployeeModel.class);
                    employeeModelsList.add(employeeModel);
                }
                binding.employeeRecyclerview.setAdapter(new EmployeeAdapter(employeeModelsList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
        List<EmployeeModel> employeeModels;

        public EmployeeAdapter(List<EmployeeModel> employeeModels) {
            this.employeeModels = employeeModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.employee_item, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.employee_name.setText("Name: " + employeeModels.get(position).getFull_name_txt());
            holder.employee_jop_title.setText("Jop title: " + employeeModels.get(position).getJop_title_txt());
            holder.employee_jop_type.setText("Jop type: " + employeeModels.get(position).getJop_type_txt());

            Picasso.get()
                    .load(employeeModels.get(position).getProfilePhoto())
                    .placeholder(R.drawable.logologo)
                    .error(R.drawable.logologo)
                    .into(holder.profileImage);

            holder.materialRippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceFragmentBloodBank(AllEmployeesFragment.this, new EmployeeProfileFragment(employeeModels.get(position).getuId().toString()), true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return employeeModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView employee_name,employee_jop_title,employee_jop_type;
            CircleImageView profileImage;
            MaterialRippleLayout materialRippleLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                employee_name = itemView.findViewById(R.id.employee_name);
                employee_jop_title = itemView.findViewById(R.id.employee_jop_title);
                employee_jop_type = itemView.findViewById(R.id.employee_jop_type);
                profileImage = itemView.findViewById(R.id.employee_profile_picture);
                materialRippleLayout = itemView.findViewById(R.id.details_btn);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }
}
