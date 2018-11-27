package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.DebtDbManager;
import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;
import ca.gotchasomething.mynance.data.SetUpDb;
import ca.gotchasomething.mynance.data.SetUpDbManager;

public class LayoutSetUp extends MainNavigation {

    Button setUpDebtsButton, setUpSavingsButton, setUpBudgetButton;
    TextView setUpDebtsLabel, setUpSavingsLabel, setUpBudgetLabel, setUpAccountAmountLabel, setUpAccountAmountLabel2, setUpAccountAmountLabel3,
            setUpAccountAmountResult, almostDone, setUpTourLabel, setUpTourLabel2, setUpTourLabel3, setUpTourLabel4, setUpGotItLabel;
    CheckBox setUpDebtsCheckbox, setUpSavingsCheckbox, setUpBudgetCheckbox, setUpAccountCheckbox, setUpTourCheckbox;
    Intent setUpDebts, setUpSavings, setUpBudget, toMainActivity, backToSetUp;
    DbHelper setUpHelper, setUpHelper2, setUpHelper3, setUpHelper4, setUpHelper5, setUpHelper6;
    SQLiteDatabase setUpDbDb, setUpDbDb2, setUpDbDb3, setUpDbDb4, setUpDbDb5, setUpDbDb6;
    Cursor setUpCursor, setUpCursor2, setUpCursor3, setUpCursor4, setUpCursor5, setUpCursor6;
    SetUpDbManager setUpDbManager;
    SetUpDb setUpDb;
    int debtsDoneCheck, savingsDoneCheck, budgetDoneCheck, balanceDoneCheck = 0, tourDoneCheck = 0, debtsDone, savingsDone, budgetDone, balanceDone, tourDone;;
    Double startingBalance = 0.0, startingBalanceResult = 0.0, balanceAmount = 0.0;
    EditText setUpAccountAmount;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    String startingBalanceS, startingBalance2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_up);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setUpDbManager = new SetUpDbManager(this);

        setUpDebtsButton = findViewById(R.id.setUpDebtsButton);
        setUpDebtsLabel = findViewById(R.id.setUpDebtsLabel);
        setUpDebtsLabel.setVisibility(View.GONE);
        setUpDebtsCheckbox = findViewById(R.id.setUpDebtsCheckbox);

        setUpSavingsButton = findViewById(R.id.setUpSavingsButton);
        setUpSavingsButton.setVisibility(View.GONE);
        setUpSavingsLabel = findViewById(R.id.setUpSavingsLabel);
        setUpSavingsCheckbox = findViewById(R.id.setUpSavingsCheckbox);

        setUpBudgetButton = findViewById(R.id.setUpBudgetButton);
        setUpBudgetButton.setVisibility(View.GONE);
        setUpBudgetLabel = findViewById(R.id.setUpBudgetLabel);
        setUpBudgetCheckbox = findViewById(R.id.setUpBudgetCheckbox);

        setUpAccountAmount = findViewById(R.id.setUpAccountAmount);
        setUpAccountAmount.setVisibility(View.GONE);
        setUpAccountAmountLabel = findViewById(R.id.setUpAccountAmountLabel);
        setUpAccountAmountLabel.setVisibility(View.GONE);
        setUpAccountAmountLabel2 = findViewById(R.id.setUpAccountAmountLabel2);
        setUpAccountAmountLabel2.setVisibility(View.GONE);
        setUpAccountAmountLabel3 = findViewById(R.id.setUpAccountAmountLabel3);
        setUpAccountCheckbox = findViewById(R.id.setUpAccountCheckbox);
        setUpAccountAmountResult = findViewById(R.id.setUpAccountAmountResult);
        setUpAccountAmountResult.setVisibility(View.GONE);

        almostDone = findViewById(R.id.almostDone);
        almostDone.setVisibility(View.GONE);
        setUpTourLabel = findViewById(R.id.setUpTourLabel);
        setUpTourLabel.setVisibility(View.GONE);
        setUpTourLabel2 = findViewById(R.id.setUpTourLabel2);
        setUpTourLabel2.setVisibility(View.GONE);
        setUpTourLabel3 = findViewById(R.id.setUpTourLabel3);
        setUpTourLabel3.setVisibility(View.GONE);
        setUpTourLabel4 = findViewById(R.id.setUpTourLabel4);
        setUpTourLabel4.setVisibility(View.GONE);
        setUpGotItLabel = findViewById(R.id.setUpGotItLabel);
        setUpGotItLabel.setVisibility(View.GONE);
        setUpTourCheckbox = findViewById(R.id.setUpTourCheckbox);
        setUpTourCheckbox.setVisibility(View.GONE);

        setUpDebtsButton.setOnClickListener(onClickSetUpDebtsButton);
        setUpSavingsButton.setOnClickListener(onClickSetUpSavingsButton);
        setUpBudgetButton.setOnClickListener(onClickSetUpBudgetButton);
        setUpAccountCheckbox.setOnCheckedChangeListener(onCheckBalanceDone);
        setUpTourCheckbox.setOnCheckedChangeListener(onCheckTourCheckbox);

        debtSetUpCheck();
        if(debtsDoneCheck > 0) {
            setUpDebtsLabel.setVisibility(View.VISIBLE);
            setUpDebtsButton.setVisibility(View.GONE);
            setUpDebtsCheckbox.setChecked(true);
            setUpSavingsButton.setVisibility(View.VISIBLE);
            setUpSavingsLabel.setVisibility(View.GONE);
        }

        savingsSetUpCheck();
        if(savingsDoneCheck > 0) {
            setUpSavingsLabel.setVisibility(View.VISIBLE);
            setUpSavingsButton.setVisibility(View.GONE);
            setUpSavingsCheckbox.setChecked(true);
            setUpBudgetButton.setVisibility(View.VISIBLE);
            setUpBudgetLabel.setVisibility(View.GONE);
        }

        budgetSetUpCheck();
        if(budgetDoneCheck > 0) {
            setUpBudgetLabel.setVisibility(View.VISIBLE);
            setUpBudgetButton.setVisibility(View.GONE);
            setUpBudgetCheckbox.setChecked(true);
            setUpAccountAmountLabel.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel2.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel3.setVisibility(View.GONE);
            setUpAccountAmount.setVisibility(View.VISIBLE);
        }

        balanceSetUpCheck();
        if(balanceDoneCheck > 0) {
            setUpAccountAmountLabel.setVisibility(View.GONE);
            setUpAccountAmountLabel2.setVisibility(View.GONE);
            setUpAccountAmountLabel3.setVisibility(View.VISIBLE);
            setUpAccountAmount.setVisibility(View.GONE);
            setUpAccountAmountResult.setVisibility(View.VISIBLE);
            retrieveStartingBalance();
            startingBalance2 = currencyFormat.format(startingBalanceResult);
            setUpAccountAmountResult.setText(startingBalance2);
            setUpAccountCheckbox.setChecked(true);
            almostDone.setVisibility(View.VISIBLE);
            setUpTourLabel.setVisibility(View.VISIBLE);
            setUpTourLabel2.setVisibility(View.VISIBLE);
            setUpTourLabel3.setVisibility(View.VISIBLE);
            setUpTourLabel4.setVisibility(View.VISIBLE);
            setUpGotItLabel.setVisibility(View.VISIBLE);
            setUpTourCheckbox.setVisibility(View.VISIBLE);
        }

        tourSetUpCheck();
        if(tourDoneCheck > 0) {
            toMainActivity = new Intent(LayoutSetUp.this, MainActivity.class);
            toMainActivity.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(toMainActivity);
        }

    }

    public int tourSetUpCheck() {
        setUpHelper6 = new DbHelper(this);
        setUpDbDb6 = setUpHelper6.getReadableDatabase();
        setUpCursor6 = setUpDbDb6.rawQuery("SELECT max(tourDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor6.moveToFirst();
        tourDoneCheck = setUpCursor6.getInt(0);
        setUpCursor6.close();

        return tourDoneCheck;
    }

    CheckBox.OnCheckedChangeListener onCheckTourCheckbox = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            debtsDone = 0;
            savingsDone = 0;
            budgetDone = 0;
            balanceDone = 0;
            balanceAmount = 0.0;
            tourDone = 1;

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            setUpDbManager.addSetUp(setUpDb);

            toMainActivity = new Intent(LayoutSetUp.this, MainActivity.class);
            toMainActivity.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(toMainActivity);
        }
    };

    public Double retrieveStartingBalance() {
        setUpHelper5 = new DbHelper(this);
        setUpDbDb5 = setUpHelper5.getReadableDatabase();
        setUpCursor5 = setUpDbDb5.rawQuery("SELECT max(balanceAmount)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor5.moveToFirst();
        startingBalanceResult = setUpCursor5.getDouble(0);
        setUpCursor5.close();

        return startingBalanceResult;
    }

    public int balanceSetUpCheck() {
        setUpHelper4 = new DbHelper(this);
        setUpDbDb4 = setUpHelper4.getReadableDatabase();
        setUpCursor4 = setUpDbDb4.rawQuery("SELECT max(balanceDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor4.moveToFirst();
        balanceDoneCheck = setUpCursor4.getInt(0);
        setUpCursor4.close();

        return balanceDoneCheck;
    }

    CheckBox.OnCheckedChangeListener onCheckBalanceDone = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            balanceDone = 1;

            try {
                startingBalanceS = setUpAccountAmount.getText().toString();
                if (startingBalanceS != null && !startingBalanceS.equals("")) {
                    balanceAmount = Double.valueOf(startingBalanceS);
                } else {
                    balanceAmount = 0.0;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            setUpDbManager.addSetUp(setUpDb);

            setUpAccountAmountLabel.setVisibility(View.GONE);
            setUpAccountAmountLabel2.setVisibility(View.GONE);
            setUpAccountAmountLabel3.setVisibility(View.VISIBLE);
            setUpAccountAmount.setVisibility(View.GONE);
            setUpAccountAmountResult.setVisibility(View.VISIBLE);
            retrieveStartingBalance();
            startingBalance2 = currencyFormat.format(startingBalanceResult);
            setUpAccountAmountResult.setText(startingBalance2);
            setUpAccountCheckbox.setChecked(true);
            almostDone.setVisibility(View.VISIBLE);
            setUpTourLabel.setVisibility(View.VISIBLE);
            setUpTourLabel2.setVisibility(View.VISIBLE);
            setUpTourLabel3.setVisibility(View.VISIBLE);
            setUpTourLabel4.setVisibility(View.VISIBLE);
            setUpGotItLabel.setVisibility(View.VISIBLE);
            setUpTourCheckbox.setVisibility(View.VISIBLE);
        }
    };

    public int budgetSetUpCheck() {
        setUpHelper3 = new DbHelper(this);
        setUpDbDb3 = setUpHelper3.getReadableDatabase();
        setUpCursor3 = setUpDbDb3.rawQuery("SELECT max(budgetDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor3.moveToFirst();
        budgetDoneCheck = setUpCursor3.getInt(0);
        setUpCursor3.close();

        return budgetDoneCheck;
    }

    View.OnClickListener onClickSetUpBudgetButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setUpBudget = new Intent(LayoutSetUp.this, LayoutBudget.class);
            setUpBudget.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(setUpBudget);
        }
    };

    public int savingsSetUpCheck() {
        setUpHelper2 = new DbHelper(this);
        setUpDbDb2 = setUpHelper2.getReadableDatabase();
        setUpCursor2 = setUpDbDb2.rawQuery("SELECT max(savingsDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor2.moveToFirst();
        savingsDoneCheck = setUpCursor2.getInt(0);
        setUpCursor2.close();

        return savingsDoneCheck;
    }

    View.OnClickListener onClickSetUpSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setUpSavings = new Intent(LayoutSetUp.this, LayoutSavings.class);
            setUpSavings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(setUpSavings);
        }
    };

    public int debtSetUpCheck() {
        setUpHelper = new DbHelper(this);
        setUpDbDb = setUpHelper.getReadableDatabase();
        setUpCursor = setUpDbDb.rawQuery("SELECT max(debtsDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor.moveToFirst();
        debtsDoneCheck = setUpCursor.getInt(0);
        setUpCursor.close();

        return debtsDoneCheck;
    }

    View.OnClickListener onClickSetUpDebtsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setUpDebts = new Intent(LayoutSetUp.this, LayoutDebt.class);
            setUpDebts.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(setUpDebts);
        }
    };

}
