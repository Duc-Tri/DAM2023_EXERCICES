package com.dam2023.templateviewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    // 1 *** init des composants graphiques
    private void initUI() {
        tabLayout = findViewById(R.id.tlTabLayout);
        viewPager2 = findViewById(R.id.vpViewPager);
    }

    // 2 *** paramétrage du ViewPAger
    private FragmentAdapter adapter;
    private ViewPager2 viewPager2;

    private void initViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);
    }

    // 3 *** parametrage du tablayout
    private TabLayout tabLayout;

    private void initTabLayout() {
        // les tabs

        tabLayout.addTab(tabLayout.newTab().setText("ONE"));
        tabLayout.addTab(tabLayout.newTab().setText("TWO"));
        tabLayout.addTab(tabLayout.newTab().setText("TRI"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initViewPager();
        initTabLayout();
    }

}