package com.example.criminalintent.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.criminalintent.R;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.CrimeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.controller.crimeId";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private List<Button> buttons = new ArrayList<>();

    public static Intent newIntent(Context context, UUID uuid){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,uuid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        buttons.add((Button) findViewById(R.id.button_first));
        buttons.add((Button) findViewById(R.id.button_previous));
        buttons.add((Button) findViewById(R.id.button_next));
        buttons.add((Button) findViewById(R.id.button_last));

        mCrimes = CrimeRepository.getInstance().getCrimes();
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        int position = CrimeRepository.getInstance().getPosition(id);

        mViewPager = findViewById(R.id.viewpager_crime);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return CrimeDetailFragment.newInstance(mCrimes.get(0).getId());
                    case 101:
                        return CrimeDetailFragment.newInstance(mCrimes.get(mCrimes.size()-1).getId());
                    default:
                        return CrimeDetailFragment.newInstance(mCrimes.get(position-1).getId());
                }

            }

            @Override
            public int getCount() {
                return mCrimes.size()+2;
            }
        });
        mViewPager.setCurrentItem(position+1);

        buttons.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });
        buttons.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() == 1){
                    mViewPager.setCurrentItem(mCrimes.size());
                }else mViewPager.setCurrentItem(getPosition(-1));
            }
        });
        buttons.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() == mCrimes.size()){
                    mViewPager.setCurrentItem(1);
                }else mViewPager.setCurrentItem(getPosition(1));
            }
        });
        buttons.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mCrimes.size());
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mViewPager.getCurrentItem() == 0){
                    mViewPager.setCurrentItem(mCrimes.size());
                }
                if (mViewPager.getCurrentItem() == mCrimes.size()+1){
                    mViewPager.setCurrentItem(1);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private int getPosition(int deviate){
        return mViewPager.getCurrentItem() + deviate;
    }
}
