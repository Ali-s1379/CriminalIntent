package com.example.criminalintent.controller;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.criminalintent.R;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.CrimeRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeDetailFragment extends Fragment {

    public static final String TAG = "CrimeDetailFragment";
    private static final String ARG_CRIME_ID = "crimeId";
    public static final String DATE_PICKER = "Date_picker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final String TIME_PICKER = "Time_picker";
    public static final int REQUEST_CODE_TIME_PICKER = 1;


    private Crime mCrime;

    private EditText mEditTextTitle;
    private Button mButtonDate;
    private Button mButtonTime;
    private CheckBox mCheckBoxSolved;
    private TextView dateAndTime;
    private Calendar mCalendar = Calendar.getInstance();

    public CrimeDetailFragment() {
        // Required empty public constructor
    }

    public static CrimeDetailFragment newInstance(UUID id) {
        Bundle args = new Bundle();

        CrimeDetailFragment fragment = new CrimeDetailFragment();

        //put id in fragment arguments
        args.putSerializable(ARG_CRIME_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(CrimeDetailActivity.TAG, "CrimeDetailFragment:onCreate");

//        UUID crimeId = (UUID) getActivity().getIntent().
//                getSerializableExtra(CrimeDetailActivity.EXTRA_CRIME_ID);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeRepository.getInstance().getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(CrimeDetailActivity.TAG, "CrimeDetailFragment:onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_detail, container, false);

        mEditTextTitle = view.findViewById(R.id.edittext_title);
        mButtonDate = view.findViewById(R.id.button_date);
        mCheckBoxSolved = view.findViewById(R.id.checkbox_solved);
        mButtonTime = view.findViewById(R.id.button_time);
        dateAndTime = view.findViewById(R.id.textView_date_time);

        mEditTextTitle.setText(mCrime.getTitle());
        dateAndTime.setText(mCrime.getDate().toString());
        mCheckBoxSolved.setChecked(mCrime.isSolved());


        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment fragment = DatePickerFragment.newInstance(mCrime.getDate());
                fragment.setTargetFragment(CrimeDetailFragment.this,REQUEST_CODE_DATE_PICKER);

                fragment.show(getFragmentManager(), DATE_PICKER);
            }
        });

        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment fragment = TimePickerFragment.newInstance(mCrime.getDate());
                fragment.setTargetFragment(CrimeDetailFragment.this,REQUEST_CODE_TIME_PICKER);

                fragment.show(getFragmentManager(), TIME_PICKER);
            }
        });

        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            CrimeRepository.getInstance().updateCrime(mCrime);
        } catch (Exception e) {
            Log.e(TAG, "cannot update crime", e);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Calendar cal = (Calendar) data.getSerializableExtra(DatePickerFragment.getExtraCrimeDate());
            mCalendar.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
            Date date = mCalendar.getTime();
            mCrime.setDate(date);

            dateAndTime.setText(date.toString());
        }
        if (requestCode == REQUEST_CODE_TIME_PICKER) {
            Calendar cal = (Calendar) data.getSerializableExtra(TimePickerFragment.getExtraCrimeTime());
            mCalendar.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY));
            mCalendar.set(Calendar.MINUTE,cal.get(Calendar.MINUTE));
            Date date = mCalendar.getTime();
            mCrime.setDate(date);

            dateAndTime.setText(date.toString());
        }
    }
}
