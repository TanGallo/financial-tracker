package ca.gotchasomething.mynance;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import androidx.fragment.app.FragmentTransaction;
import ca.gotchasomething.mynance.data.SetUpDb;
import ca.gotchasomething.mynance.data.SetUpDbManager;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyInOut;
import ca.gotchasomething.mynance.tabFragments.DailyCreditCard;
import ca.gotchasomething.mynance.tabFragments.DailyWeeklyLimits;

public class LayoutDailyMoney extends MainNavigation {

    TabLayout tl;
    FrameLayout container;
    TextView totalAccountText, availableAccountText;
    DbHelper setUpHelper, moneyInHelper, moneyOutHelper, moneyOutHelper2, expenseHelper, incomeHelper;
    SQLiteDatabase setUpDbDb, moneyInDbDb, moneyOutDbDb, moneyOutDbDb2, expenseDbDb, incomeDbDb;
    Cursor setUpCursor, moneyInCursor, moneyOutCursor, moneyOutCursor2, expenseCursor, incomeCursor;
    Double startingBalance, startingBalanceB, startingBalanceResult, newAccountBalance, income, incomeB, spent, spentB, spentOnB, incomeTotal,
            totalAExpenses, totalIncome, percentB, spentFromAccountTotal, newAvailableBalance;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    String availableBalance2, accountBalance2;
    //Context context;
    Button moneyInButton, moneyOutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_daily_money);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        totalAccountText = findViewById(R.id.totalAccountText);
        availableAccountText = findViewById(R.id.availableAccountText);

        dailyHeaderText();

        tl = findViewById(R.id.daily_tab_layout);
        container = findViewById(R.id.daily_fragment_container);

        tl.addTab(tl.newTab().setText("Daily Journal"));
        tl.addTab(tl.newTab().setText("Credit Card"));
        tl.addTab(tl.newTab().setText("Weekly Limits"));

        replaceFragment(new DailyMoneyInOut());

        tl.addOnTabSelectedListener(onTabSelectedListener);

    }

    public Double retrieveStartingBalance() {
        setUpHelper = new DbHelper(this);
        setUpDbDb = setUpHelper.getReadableDatabase();
        setUpCursor = setUpDbDb.rawQuery("SELECT max(balanceAmount)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor.moveToFirst();
        startingBalanceResult = setUpCursor.getDouble(0);
        setUpCursor.close();

        return startingBalanceResult;
    }

    public Double retrieveIncomeTotal() {
        moneyInHelper = new DbHelper(this);
        moneyInDbDb = moneyInHelper.getReadableDatabase();
        moneyInCursor = moneyInDbDb.rawQuery("SELECT sum(moneyInAmount)" + " FROM " + DbHelper.MONEY_IN_TABLE_NAME, null);
        moneyInCursor.moveToFirst();
        incomeTotal = moneyInCursor.getDouble(0);
        moneyInCursor.close();

        return incomeTotal;
    }

    public Double retrieveSpentFromAccountTotal() {
        moneyOutHelper = new DbHelper(this);
        moneyOutDbDb = moneyOutHelper.getReadableDatabase();
        moneyOutCursor = moneyOutDbDb.rawQuery("SELECT sum(moneyOutAmount)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " + DbHelper.MONEYOUTCC
                 + " = 'N'", null);
        moneyOutCursor.moveToFirst();
        spentFromAccountTotal = moneyOutCursor.getDouble(0);
        moneyOutCursor.close();

        return spentFromAccountTotal;
    }

    public Double retrieveBPercentage() {
        expenseHelper = new DbHelper(this);
        expenseDbDb = expenseHelper.getReadableDatabase();
        expenseCursor = expenseDbDb.rawQuery("SELECT sum(expenseAAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        expenseCursor.moveToFirst();
        totalAExpenses = expenseCursor.getDouble(0);
        expenseCursor.close();

        incomeHelper = new DbHelper(this);
        incomeDbDb = incomeHelper.getReadableDatabase();
        incomeCursor = incomeDbDb.rawQuery("SELECT sum(incomeAnnualAmount)" + " FROM " + DbHelper.INCOME_TABLE_NAME, null);
        incomeCursor.moveToFirst();
        totalIncome = incomeCursor.getDouble(0);
        incomeCursor.close();

        percentB = 1 - (totalAExpenses / totalIncome);

        return percentB;

    }

    public Double retrieveBSpent() {
        moneyOutHelper2 = new DbHelper(this);
        moneyOutDbDb2 = moneyOutHelper2.getReadableDatabase();
        moneyOutCursor2 = moneyOutDbDb2.rawQuery("SELECT sum(moneyOutAmount)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " +
                DbHelper.MONEYOUTCC + " = 'N' AND " + DbHelper.MONEYOUTPRIORITY + " = 'B'", null);
        moneyOutCursor2.moveToFirst();
        spentOnB = moneyOutCursor2.getDouble(0);
        moneyOutCursor2.close();

        return spentOnB;
    }

    public void dailyHeaderText() {

        startingBalance = retrieveStartingBalance(); //initial balance in account at set up
        income = retrieveIncomeTotal(); //total ever into bank account
        spent = retrieveSpentFromAccountTotal(); //total spent from bank account, not on credit card
        newAccountBalance = startingBalance + income - spent;

        startingBalanceB = retrieveBPercentage() * startingBalance; //percentage of initial balance in account allocated to B
        incomeB = retrieveBPercentage() * income; //percentage of total income ever into bank account allocated to B
        spentB = retrieveBSpent(); //total spent from bank account, not on credit card, in categories marked as B
        newAvailableBalance = startingBalanceB + incomeB - spentB;
        if(newAvailableBalance.isNaN() || newAvailableBalance < 0) {
            newAvailableBalance = 0.0;
        }

        accountBalance2 = currencyFormat.format(newAccountBalance);
        availableBalance2 = currencyFormat.format(newAvailableBalance);

        totalAccountText.setText(accountBalance2);
        availableAccountText.setText(availableBalance2);

    }

    TabLayout.OnTabSelectedListener onTabSelectedListener = (new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            if (tab.getPosition() == 0) {
                replaceFragment(new DailyMoneyInOut());
            } else if (tab.getPosition() == 1) {
                replaceFragment(new DailyCreditCard());
            } else if (tab.getPosition() == 2) {
                replaceFragment(new DailyWeeklyLimits());
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    });

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.daily_fragment_container, fragment);

        transaction.commit();
    }
}
