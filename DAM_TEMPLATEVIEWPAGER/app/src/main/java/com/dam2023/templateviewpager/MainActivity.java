package com.dam2023.templateviewpager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.tabs.TabLayout;

import java.util.Stack;

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

        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_sports_kabaddi_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.baseline_rocket_launch_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.baseline_sick_24);
    }

    // 4 *** gestion des clics sur les tabs
    private ViewPager2.OnPageChangeCallback callback;
    private Stack<Integer> pageHistory;
    boolean saveToHistory;

    private void gestionClickTab() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                Log.i("TAG", "TabSelected: " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        saveToHistory = true;
        viewPager2.registerOnPageChangeCallback(callback);
    }

    private void gestionCallBack() {
        viewPager2.setOffscreenPageLimit(2);
        pageHistory = new Stack<>();
        callback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (saveToHistory) {
                    pageHistory.push(position);
                    Log.i("TAG", "Page push: " + position);
                    tabLayout.selectTab(tabLayout.getTabAt(position));
                }

            }
        };
    }


    // CTRL + O pour avoir les méthodes pour OVERRIDE

    @Override
    public void onBackPressed() {
        if (pageHistory.isEmpty())
            super.onBackPressed();
        else {

            viewPager2.setCurrentItem(pageHistory.pop()); // pour évacuer la page courante

            saveToHistory = false; // pour éviter de push
            int popPage = pageHistory.pop();
            Log.i("TAG", "Pop page: " + popPage);
            viewPager2.setCurrentItem(popPage);
            saveToHistory = true;
        }
    }


    Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initViewPager();
        initTabLayout();
        gestionCallBack();
        gestionClickTab();

        //////////////////////drawable =
    }

}