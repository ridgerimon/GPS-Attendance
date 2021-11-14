package com.example.smartgpsattendance.HrApp.HRFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.smartgpsattendance.R;
import com.example.smartgpsattendance.databinding.HomeHrFragmentBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment
{
    HomeHrFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeHrFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupBottom();
    }

    private void setupBottom()
    {
        BottomNavigationView bottomNavigationView = binding.navigation;
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_hr_nav);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
