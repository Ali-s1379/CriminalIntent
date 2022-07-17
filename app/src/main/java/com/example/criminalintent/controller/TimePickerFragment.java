package com.example.criminalintent.controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment {

    private static final String ARG_CRIME_TIME = "Crime_Time";
    private static final String EXTRA_CRIME_TIME = "com.example.criminalintent.crimeTime";
    private Date mDate;
    private TimePicker timePicker;


    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static String getExtraCrimeTime() {
        return EXTRA_CRIME_TIME;
    }

    public static TimePickerFragment newInstance(Date date) {
        
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_TIME,date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(ARG_CRIME_TIME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_time_picker,null,false);
        timePicker = view.findViewById(R.id.timepicker);
        initTimePicker();
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title)
                .setView(view)
                .setPositiveButton(R.string.button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Calendar cal = Calendar.getInstance();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cal.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cal.set(Calendar.MINUTE,timePicker.getMinute());
                        }

//                        Date date = cal.getTime();
                        sendResult(cal);
                    }
                })
                .create();
    }

    private void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setMinute(minute);
        }
    }

    private void sendResult(Calendar calendar){
        Fragment fragment = getTargetFragment();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CRIME_TIME,calendar);
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false);
    }

}
