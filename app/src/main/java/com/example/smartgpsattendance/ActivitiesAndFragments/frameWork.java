package com.example.smartgpsattendance.ActivitiesAndFragments;

import androidx.fragment.app.Fragment;

import com.example.smartgpsattendance.R;

public class frameWork
{
    public static void replaceFragmentBloodBank(Fragment from, Fragment to, boolean save) {
        if (save) {
            from
                    .requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_hr_container, to)
                    .addToBackStack(null)
                    .commit();
        } else {
            from
                    .requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();

            from
                    .requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_hr_container, to)
                    .disallowAddToBackStack()
                    .commit();
        }
    }
}
