package com.example.projet2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private static final int ADD_EVENT_REQUEST_CODE = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    String eventsJson;
    Type typeOfEvents;
    List<Event> events;
    MonthlyFragment monthlyFragment;
    WeeklyFragment weeklyFragment;
    DailyFragment dailyFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        monthlyFragment = new MonthlyFragment();
        weeklyFragment = new WeeklyFragment();
        dailyFragment = new DailyFragment();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        sharedPreferences = getSharedPreferences("events", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
        eventsJson = sharedPreferences.getString("eventsList", null);
        typeOfEvents = new TypeToken<List<Event>>(){}.getType();
        events = gson.fromJson(eventsJson, typeOfEvents);
        if (events == null) {
            events = new ArrayList<>();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventIntent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivityForResult(addEventIntent, ADD_EVENT_REQUEST_CODE);
            }
        });

        //update the events list when the page is selected so the events are displayed on creation
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //UPDATE LES FRAGMENTS AU LANCEMENT
                dailyFragment.updateEvents(events);

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(monthlyFragment, "Monthly");
        adapter.addFragment(weeklyFragment, "Weekly");
        adapter.addFragment(dailyFragment, "Daily");
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
            //Toast.makeText(this, eventToast, Toast.LENGTH_LONG).show();

            events.add(event);

            String json = this.gson.toJson(event);
            String newEventsJson = gson.toJson(events);
            editor.putString("eventsList", newEventsJson);
            editor.apply();
            //UPDATE LES EVENTS DANS LES FRAGMENTS
            dailyFragment.updateEvents(events);
            Log.v("events",events.toString());
        }
    }

    void saveEventsToSharedPreferences(List<Event> e) {
        this.events=e;
        String json = gson.toJson(events);
        editor.putString("eventsList", json);
        editor.apply();

        Log.v("events",events.toString());
    }
}