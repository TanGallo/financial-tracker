package ca.gotchasomething.mynance;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.widget.FrameLayout;

import androidx.fragment.app.FragmentTransaction;
import ca.gotchasomething.mynance.tabFragments.HelpPage1;

public class LayoutHelp extends MainNavigation {

    TabLayout tl;
    FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_help);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        tl = findViewById(R.id.tab_layout);
        container = findViewById(R.id.fragment_container);

        tl.addTab(tl.newTab().setText("Set Up"));
        //tl.addTab(tl.newTab().setIcon(R.drawable.ic_developer_mode_teal_24dp));


        replaceFragment(new HelpPage1());
        tl.addOnTabSelectedListener(onTabSelectedListener);
    }

    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            if (tab.getPosition() == 0) {
                replaceFragment(new HelpPage1());
            } /*else if (tab.getPosition() == 1) {
                    replaceFragment(new HelpPage2());
                } */
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }
}
