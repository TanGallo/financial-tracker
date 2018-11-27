package ca.gotchasomething.mynance;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    Menu menu;
    Intent i, i2, i3, i4, i5, i6, i7, i8;
    DbHelper setUpHelper;
    SQLiteDatabase setUpDbDb;
    Cursor setUpCursor;
    int setUpDoneCheck;
    boolean before = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //menuConfig();

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /*public void menuConfig() {
        beforeSetUpOrAfter();

        menu = navigationView.getMenu();
        if(before) {
            menu.findItem(R.id.menu_daily_money).setEnabled(false);
            menu.findItem(R.id.menu_budget).setEnabled(false);
            menu.findItem(R.id.menu_debt).setEnabled(false);
            menu.findItem(R.id.menu_savings).setEnabled(false);
            menu.findItem(R.id.menu_edit_entries).setVisible(false);
            menu.findItem(R.id.menu_income_entries).setVisible(false);
            menu.findItem(R.id.menu_spending_entries).setVisible(false);
        } else {
            menu.findItem(R.id.menu_set_up).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    public int doneSetUpCheck() {
        setUpHelper = new DbHelper(this);
        setUpDbDb = setUpHelper.getReadableDatabase();
        setUpCursor = setUpDbDb.rawQuery("SELECT max(tourDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        try {
            setUpCursor.moveToFirst();
            setUpDoneCheck = setUpCursor.getInt(0);
        } catch (CursorIndexOutOfBoundsException e) {
            setUpDoneCheck = 0;
        }
        setUpCursor.close();

        return setUpDoneCheck;
    }

    public boolean beforeSetUpOrAfter() {
        doneSetUpCheck();

        if (setUpDoneCheck <= 0) {
            before = true;
        } else {
            before = false;
        }

        return before;
    }*/


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_daily_money:
                i = new Intent(MainNavigation.this, LayoutDailyMoney.class);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i);
                break;
            case R.id.menu_budget:
                i2 = new Intent(MainNavigation.this, LayoutBudget.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i2);
                break;
            case R.id.menu_debt:
                i4 = new Intent(MainNavigation.this, LayoutDebt.class);
                i4.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i4);
                break;
            case R.id.menu_savings:
                i5 = new Intent(MainNavigation.this, LayoutSavings.class);
                i5.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i5);
                break;
            case R.id.menu_help:
                i6 = new Intent(MainNavigation.this, LayoutHelp.class);
                i6.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i6);
                break;
            case R.id.menu_spending_entries:
                i3 = new Intent(MainNavigation.this, LayoutBudget.class);
                i3.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i3);
                break;
            case R.id.menu_income_entries:
                i7 = new Intent(MainNavigation.this, LayoutEntriesIncome.class);
                i7.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i7);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
