package ca.gotchasomething.mynance;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.SavingsDbManager;

public class AddSavings extends LayoutSavings {

    EditText savingsNameEntry, savingsAmountEntry, savingsPercentEntry, savingsPaymentsEntry, savingsGoalAmountEntry;
    TextView savingsDateResult;
    RadioGroup savingsFrequencyRadioGroup;
    RadioButton savingsWeeklyRadioButton, savingsBiWeeklyRadioButton, savingsMonthlyRadioButton, savingsAnnuallyRadioButton;
    Button saveSavingsButton, updateSavingsButton, cancelSavingsButton;
    String savingsName = null, savingsDate = null, savingsFrequencyS = null, expenseName = null, expensePriority = null, expenseWeekly = null, debtEndString;
    Double savingsAmount = 0.0, savingsRate = 0.0, savingsPayments = 0.0, savingsFrequency = 0.0, savingsGoal = 0.0, expenseAmount = 0.0, expenseFrequency = 0.0,
            expenseAnnualAmount = 0.0, expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, frequency = 0.0, amount = 0.0, rate = 0.0, payments = 0.0,
            numberOfYearsToSavingsGoal = 0.0;
    long expRefKeyS;
    SavingsDbManager savingsDbManager;
    SavingsDb saving;
    ExpenseBudgetDb expenseBudgetDb;
    ExpenseBudgetDbManager expenseDbManager;
    SQLiteDatabase db;
    Integer numberOfDaysToSavingsGoal = 0;
    SimpleDateFormat savingsDateS;
    Calendar savingsCal;
    Date savingsDateD;
    Intent backToSavingsLayout, backToSavingsLayout2;
    boolean success;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_savings);

        savingsDbManager = new SavingsDbManager(this);
        expenseDbManager = new ExpenseBudgetDbManager(this);

        savingsNameEntry = findViewById(R.id.savingsNameEntry);
        savingsAmountEntry = findViewById(R.id.savingsAmountEntry);
        savingsPercentEntry = findViewById(R.id.savingsPercentEntry);
        savingsPaymentsEntry = findViewById(R.id.savingsPaymentsEntry);
        savingsFrequencyRadioGroup = findViewById(R.id.savingsFrequencyRadioGroup);
        savingsWeeklyRadioButton = findViewById(R.id.savingsWeeklyRadioButton);
        savingsBiWeeklyRadioButton = findViewById(R.id.savingsBiWeeklyRadioButton);
        savingsMonthlyRadioButton = findViewById(R.id.savingsMonthlyRadioButton);
        savingsAnnuallyRadioButton = findViewById(R.id.savingsAnnuallyRadioButton);
        savingsGoalAmountEntry = findViewById(R.id.savingsGoalAmountEntry);
        savingsDateResult = findViewById(R.id.savingsDateResult);
        saveSavingsButton = findViewById(R.id.saveSavingsButton);
        updateSavingsButton = findViewById(R.id.updateSavingsButton);
        updateSavingsButton.setVisibility(View.GONE);
        cancelSavingsButton = findViewById(R.id.cancelSavingsButton);
        db = openOrCreateDatabase("Mynance.db", MODE_PRIVATE, null);

        savingsFrequencyRadioGroup.setOnCheckedChangeListener(onCheckSavingsFrequency);
        cancelSavingsButton.setOnClickListener(onClickCancelSavingsButton);
        saveSavingsButton.setOnClickListener(onClickSaveSavingsButton);
    }

    RadioGroup.OnCheckedChangeListener onCheckSavingsFrequency = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.savingsWeeklyRadioButton:
                    savingsFrequencyS = "52";
                    savingsDateResult.setText(calcSavingsDate());
                    break;
                case R.id.savingsBiWeeklyRadioButton:
                    savingsFrequencyS = "26";
                    savingsDateResult.setText(calcSavingsDate());
                    break;
                case R.id.savingsMonthlyRadioButton:
                    savingsFrequencyS = "12";
                    savingsDateResult.setText(calcSavingsDate());
                    break;
                case R.id.savingsAnnuallyRadioButton:
                    savingsFrequencyS = "1";
                    savingsDateResult.setText(calcSavingsDate());
                    break;
            }
        }
    };

    public long findExpenseId() {
        List<Long> ids = new ArrayList<>();
        expenseDbHelper = new DbHelper(this);
        expenseDb = expenseDbHelper.getReadableDatabase();
        expenseCursor = expenseDb.rawQuery("SELECT " + DbHelper.ID + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        expenseDbHelper.getWritableDatabase();
        expenseCursor.moveToFirst();
        if (expenseCursor.moveToFirst()) {
            do {
                ids.add(expenseCursor.getLong(0));
            } while (expenseCursor.moveToNext());
        }

        idResult = Collections.max(ids);

        return idResult;
    }

    public String calcSavingsDate() {

        amount = Double.valueOf(savingsGoalAmountEntry.getText().toString()) - Double.valueOf(savingsAmountEntry.getText().toString());
        rate = Double.valueOf(savingsPercentEntry.getText().toString());
        payments = Double.valueOf(savingsPaymentsEntry.getText().toString());
        frequency = Double.valueOf(savingsFrequencyS);

        savingsCal = Calendar.getInstance();
        numberOfYearsToSavingsGoal = -(Math.log(1 - (amount * (rate / 100) / (payments * frequency))) / (frequency * Math.log(1 + ((rate / 100) / frequency))));
        numberOfDaysToSavingsGoal = (int) Math.round(numberOfYearsToSavingsGoal * 365);

        if (amount <= 0) {
            savingsDate = getString(R.string.goal_achieved);

        } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE || numberOfDaysToSavingsGoal <= 0) {

            Toast.makeText(getApplicationContext(), R.string.too_far, Toast.LENGTH_LONG).show();
            savingsDate = getString(R.string.too_far);

        } else {

            savingsCal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
            savingsDateD = savingsCal.getTime();
            savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
            savingsDate = getString(R.string.goal_will) + " " + savingsDateS.format(savingsDateD);
        }

        return savingsDate;
    }

    View.OnClickListener onClickCancelSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            backToSavingsLayout2 = new Intent(AddSavings.this, LayoutSavings.class);
            backToSavingsLayout2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSavingsLayout2);
        }
    };

    final View.OnClickListener onClickSaveSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            expenseName = savingsNameEntry.getText().toString();
            expenseAmount = Double.valueOf(savingsPaymentsEntry.getText().toString());
            expenseFrequency = Double.valueOf(savingsFrequencyS);
            expensePriority = "A";
            expenseWeekly = "N";
            expenseAnnualAmount = expenseAmount * expenseFrequency;
            expenseAAnnualAmount = expenseAnnualAmount;
            expenseBAnnualAmount = 0.0;

            expenseBudgetDb = new ExpenseBudgetDb(
                    expenseName,
                    expenseAmount,
                    expenseFrequency,
                    expensePriority,
                    expenseWeekly,
                    expenseAnnualAmount,
                    expenseAAnnualAmount,
                    expenseBAnnualAmount,
                    0);

            expenseDbManager.addExpense(expenseBudgetDb);

            savingsName = savingsNameEntry.getText().toString();
            savingsAmount = Double.valueOf(savingsAmountEntry.getText().toString());
            savingsRate = Double.valueOf(savingsPercentEntry.getText().toString());
            savingsPayments = Double.valueOf(savingsPaymentsEntry.getText().toString());
            savingsFrequency = Double.valueOf(savingsFrequencyS);
            savingsGoal = Double.valueOf(savingsGoalAmountEntry.getText().toString());
            savingsDate = calcSavingsDate();
            expRefKeyS = findExpenseId();

            saving = new SavingsDb(
                    savingsName,
                    savingsAmount,
                    savingsRate,
                    savingsPayments,
                    savingsFrequency,
                    savingsGoal,
                    savingsDate,
                    expRefKeyS,
                    0);

            savingsDbManager.addSavings(saving);

            Toast toast = Toast.makeText(getBaseContext(), "This savings info has been saved to your BUDGET",
                    Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            savingsHeaderText();

            backToSavingsLayout = new Intent(AddSavings.this, LayoutSavings.class);
            backToSavingsLayout.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSavingsLayout);
        }
    };

}
