package ca.gotchasomething.mynance;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;

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

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.DebtDbManager;

public class AddDebt extends LayoutDebt {

    EditText debtNameEntry, debtAmountEntry, debtPercentEntry, debtPaymentsEntry;
    TextView debtDateResult;
    RadioGroup debtFrequencyRadioGroup;
    RadioButton debtWeeklyRadioButton, debtBiWeeklyRadioButton, debtMonthlyRadioButton;
    Button saveDebtButton, updateDebtButton, cancelDebtButton;
    String debtName = null, debtEnd = null, debtFrequencyS = null, expenseName = null, expensePriority = null, expenseWeekly = null, debtEndString;
    Double debtAmount = 0.0, debtRate = 0.0, debtPayments = 0.0, debtFrequency = 0.0, expenseAmount = 0.0, expenseFrequency = 0.0, expenseAnnualAmount = 0.0,
            expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, amount = 0.0, rate = 0.0, frequency = 0.0, payments = 0.0, numberOfYearsToPayDebt = 0.0;
    long expRefKeyD, idResult;
    Integer numberOfDaysToPayDebt = 0;
    DebtDbManager debtDbManager;
    DebtDb debt;
    ExpenseBudgetDb expenseBudgetDb;
    ExpenseBudgetDbManager expenseDbManager;
    SQLiteDatabase db;
    Calendar debtCal;
    SimpleDateFormat debtEndS;
    Date debtEndD;
    Intent backToDebtLayout, backToDebtLayout2;
    boolean success;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_debt);

        debtDbManager = new DebtDbManager(this);
        expenseDbManager = new ExpenseBudgetDbManager(this);

        debtNameEntry = findViewById(R.id.debtNameEntry);
        debtAmountEntry = findViewById(R.id.debtAmountEntry);
        debtPercentEntry = findViewById(R.id.debtPercentEntry);
        debtPaymentsEntry = findViewById(R.id.debtPaymentsEntry);
        debtFrequencyRadioGroup = findViewById(R.id.debtFrequencyRadioGroup);
        debtWeeklyRadioButton = findViewById(R.id.debtWeeklyRadioButton);
        debtBiWeeklyRadioButton = findViewById(R.id.debtBiWeeklyRadioButton);
        debtMonthlyRadioButton = findViewById(R.id.debtMonthlyRadioButton);
        debtDateResult = findViewById(R.id.debtDateResult);
        saveDebtButton = findViewById(R.id.saveDebtButton);
        updateDebtButton = findViewById(R.id.updateDebtButton);
        updateDebtButton.setVisibility(View.GONE);
        cancelDebtButton = findViewById(R.id.cancelDebtButton);
        db = openOrCreateDatabase("Mynance.db", MODE_PRIVATE, null);

        debtFrequencyRadioGroup.setOnCheckedChangeListener(onCheckDebtFrequency);
        cancelDebtButton.setOnClickListener(onClickCancelDebtButton);
        saveDebtButton.setOnClickListener(onClickSaveDebtButton);
    }

    RadioGroup.OnCheckedChangeListener onCheckDebtFrequency = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.debtWeeklyRadioButton:
                    debtFrequencyS = "52";
                    debtDateResult.setText(calcDebtDate());
                    break;
                case R.id.debtBiWeeklyRadioButton:
                    debtFrequencyS = "26";
                    debtDateResult.setText(calcDebtDate());
                    break;
                case R.id.debtMonthlyRadioButton:
                    debtFrequencyS = "12";
                    debtDateResult.setText(calcDebtDate());
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

    public String calcDebtDate() {

        amount = Double.valueOf(debtAmountEntry.getText().toString());
        rate = Double.valueOf(debtPercentEntry.getText().toString());
        payments = Double.valueOf(debtPaymentsEntry.getText().toString());
        frequency = Double.valueOf(debtFrequencyS);

        debtCal = Calendar.getInstance();
        numberOfYearsToPayDebt = -(Math.log(1 - (amount * (rate / 100) / (payments * frequency))) / (frequency * Math.log(1 + ((rate / 100) / frequency))));
        numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);

        if (amount <= 0) {
            debtEnd = getString(R.string.debt_paid);

        } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfDaysToPayDebt <= 0) {
            debtEnd = getString(R.string.too_far);

        } else {
            debtCal = Calendar.getInstance();
            debtCal.add(Calendar.DATE, numberOfDaysToPayDebt);
            debtEndD = debtCal.getTime();
            debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
            debtEnd = getString(R.string.debt_will) + " " + debtEndS.format(debtEndD);
        }

        return debtEnd;
    }

    View.OnClickListener onClickCancelDebtButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            backToDebtLayout2 = new Intent(AddDebt.this, LayoutDebt.class);
            backToDebtLayout2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDebtLayout2);
        }
    };

    final View.OnClickListener onClickSaveDebtButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            expenseName = debtNameEntry.getText().toString();
            expenseAmount = Double.valueOf(debtPaymentsEntry.getText().toString());
            expenseFrequency = Double.valueOf(debtFrequencyS);
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

            debtName = debtNameEntry.getText().toString();
            debtAmount = Double.valueOf(debtAmountEntry.getText().toString());
            debtRate = Double.valueOf(debtPercentEntry.getText().toString());
            debtPayments = Double.valueOf(debtPaymentsEntry.getText().toString());
            debtFrequency = Double.valueOf(debtFrequencyS);
            debtEnd = calcDebtDate();
            expRefKeyD = findExpenseId();

            debt = new DebtDb(
                    debtName,
                    debtAmount,
                    debtRate,
                    debtPayments,
                    debtFrequency,
                    debtEnd,
                    expRefKeyD,
                    0);

            debtDbManager.addDebt(debt);

            Toast toast = Toast.makeText(getBaseContext(), "This debt has been saved to your BUDGET",
                    Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            debtHeaderText();

            backToDebtLayout = new Intent(AddDebt.this, LayoutDebt.class);
            backToDebtLayout.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDebtLayout);
        }
    };

}
