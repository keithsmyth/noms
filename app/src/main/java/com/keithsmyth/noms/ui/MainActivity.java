package com.keithsmyth.noms.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.keithsmyth.noms.R;


public class MainActivity extends AppCompatActivity implements Navigatable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EntryListFragment())
                    .commit();
        }
    }

    @Override public void next(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    @Override public void previous() {
        getSupportFragmentManager().popBackStack();
    }
}
