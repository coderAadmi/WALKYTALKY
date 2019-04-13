package com.example.prady.walkytalky;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private String UserTAG = "FIRST_TIME_OPENED";
    private String FragmentBackStack = "MainActivity";
    private FragmentManager fragmentManager;
    private String[] USER_INFO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.MainFragmentContainer);
        if (fragment == null)
        {
            fragment = new MainFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.MainFragmentContainer, fragment, UserTAG)
                    .commit();
        }

    }
}
