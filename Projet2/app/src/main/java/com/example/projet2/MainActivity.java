package com.example.projet2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_EVENT_REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventIntent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivityForResult(addEventIntent, ADD_EVENT_REQUEST_CODE);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MonthlyFragment(), "Monthly");
        adapter.addFragment(new WeeklyFragment(), "Weekly");
        adapter.addFragment(new DailyFragment(), "Daily");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Event event = (Event) data.getSerializableExtra("event");
            String eventToast = String.format("Event: %s, Date: %s, Time: %s, Coefficient: %d, Type: %s",
                    event.getTitle(), event.getDate(), event.getTime(), event.getCoefficient(), event.getType());
            Toast.makeText(this, eventToast, Toast.LENGTH_LONG).show();
        }
    }
}