package com.swayam.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

public class UserActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewpager);

        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout){
                    ParseUser.logOut();
                    startActivity(new Intent(UserActivity.this, LoginActivity.class));
                    finish();
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
