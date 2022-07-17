package com.example.criminalintent.controller;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

public class CrimeListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, CrimeListActivity.class);
    }

    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }
}
