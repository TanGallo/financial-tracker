package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDbManager;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.DebtDbManager;
import ca.gotchasomething.mynance.data.SetUpDb;
import ca.gotchasomething.mynance.data.SetUpDbManager;

public class LayoutBudget extends MainNavigation {

    TextView budgetIncomeTotalText, budgetExpensesTotalText, budgetOopsText, budgetOopsAmountText, headerLabel2, incomeAvailable, weeklyGuidanceLabel;
    FloatingActionButton budgetIncomePlusButton, budgetExpensePlusButton;
    ListView budgetIncomeDetails, budgetExpensesDetails;
    DbHelper setUpHelper, incomeDbHelper, expenseDbHelper, expenseADbHelper, expenseBDbHelper, debtDbHelper, debtDbHelper2, debtDbHelper3, savingsDbHelper,
            savingsDbHelper2, savingsDbHelper3;
    SQLiteDatabase setUpDbDb, incomeDb, expenseDb, expenseADb, expenseBDb, debtDb, debtDb2, debtDb3, savingsDb, savingsDb2, savingsDb3, savingsDb4,
            savingsDb5, savingsDb6, savingsDb7;
    Cursor setUpCursor, incomeCursor, expenseCursor, expenseACursor, expenseBCursor, debtCursor, debtCursor2, debtCursor3, debtCursor4, debtCursor5,
            debtCursor6, savingsCursor, savingsCursor2, savingsCursor3, savingsCursor4, savingsCursor5, savingsCursor6, savingsCursor7;
    IncomeDbAdapter incomeAdapter;
    IncomeBudgetDb incomeBudgetDb;
    IncomeBudgetDbManager incomeDbManager;
    ExpenseDbAdapter expenseAdapter;
    ExpenseBudgetDb expenseBudgetDb;
    ExpenseBudgetDbManager expenseDbManager;
    DebtDbManager debtDbManager;
    DebtDb debt;
    SetUpDbManager setUpDbManager;
    SetUpDb setUpDb;
    boolean success, success2, foundNoDebtId, foundNoSavingsId;
    int numberOfDaysToPayDebt = 0, numberOfDaysToSavingsGoal = 0, budgetDoneCheck = 0, debtsDone, savingsDone, budgetDone, balanceDone, tourDone;
    Calendar debtCal, savingsCal;
    Date debtEndD, savingsDateD;
    SimpleDateFormat debtEndS, savingsDateS;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    EditText budgetIncomeCategoryText, budgetIncomeAmountText, budgetIncomeCategory, budgetIncomeAmount, budgetExpenseCategoryText,
            budgetExpenseAmountText, budgetExpenseCategory, budgetExpenseAmount;
    ImageButton editIncomeButton, deleteIncomeButton, editExpenseButton, deleteExpenseButton;
    RadioGroup budgetIncomeFrequencyRadioGroup, budgetExpenseFrequencyRadioGroup, budgetExpenseABRadioGroup, budgetExpenseReminderRadioGroup;
    RadioButton budgetIncomeWeeklyRadioButton, budgetIncomeBiWeeklyRadioButton, budgetIncomeBiMonthlyRadioButton,
            budgetIncomeMonthlyRadioButton, budgetIncomeBiAnnuallyRadioButton, budgetIncomeAnnuallyRadioButton, budgetExpenseWeeklyRadioButton,
            budgetExpenseBiWeeklyRadioButton, budgetExpenseBiMonthlyRadioButton, budgetExpenseMonthlyRadioButton,
            budgetExpenseBiAnnuallyRadioButton, budgetExpenseAnnuallyRadioButton, budgetExpenseARadioButton, budgetExpenseBRadioButton,
            budgetExpenseYesRadioButton, budgetExpenseNoRadioButton;
    Button doneBudgetSetUpButton, budgetCancelIncomeButton, budgetAddIncomeButton, budgetUpdateIncomeButton, budgetCancelExpenseButton,
            budgetAddExpenseButton, budgetUpdateExpenseButton;
    String incomeFrequencyS = null, incomeAnnualAmountS = null, incomeAnnualAmount2 = null, expenseFrequencyS = null, expenseWeeklyS = null,
            expensePriorityS = null, expenseAnnualAmount2 = null, totalIncomeS = null, totalIncome2 = null, expenseAnnualAmountS = null,
            totalExpensesS = null, totalExpenses2 = null, incomeAvailable2 = null, incomeAvailableN2 = null, debtEnd = null, savingsDate = null;
    Double totalIncome = 0.0, totalIncomeD = 0.0, incomeAnnualAmountD = 0.0, totalExpenses = 0.0, totalAExpenses = 0.0, totalBExpenses = 0.0,
            totalExpensesD = 0.0, expenseAnnualAmountD = 0.0, incomeAvailableD = 0.0, incomeAvailableN, numberOfYearsToPayDebt = 0.0,
            numberOfYearsToSavingsGoal = 0.0, balanceAmount = 0.0;
    long id;
    Intent backToSetUp, incomePlusButton, expensePlusButton, backToBudget, backToBudget2, backToBudget3, backToBudget4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_budget);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        budgetIncomeTotalText = findViewById(R.id.budgetIncomeTotalText);
        budgetExpensesTotalText = findViewById(R.id.budgetExpensesTotalText);
        headerLabel2 = findViewById(R.id.headerLabel2);
        incomeAvailable = findViewById(R.id.incomeAvailable);
        budgetOopsText = findViewById(R.id.budgetOopsText);
        budgetOopsText.setVisibility(View.GONE);
        budgetOopsAmountText = findViewById(R.id.budgetOopsAmountText);
        budgetOopsAmountText.setVisibility(View.GONE);
        budgetIncomePlusButton = findViewById(R.id.budgetIncomePlusButton);
        budgetExpensePlusButton = findViewById(R.id.budgetExpensePlusButton);
        budgetIncomeDetails = findViewById(R.id.budgetIncomeDetails);
        budgetExpensesDetails = findViewById(R.id.budgetExpensesDetails);

        budgetIncomePlusButton.setOnClickListener(onClickIncomePlusButton);
        budgetExpensePlusButton.setOnClickListener(onClickExpensePlusButton);

        doneBudgetSetUpButton = findViewById(R.id.doneBudgetSetUpButton);
        doneBudgetSetUpButton.setOnClickListener(onClickDoneBudgetSetUpButton);

        budgetSetUpCheck();
        if(budgetDoneCheck > 0) {
            doneBudgetSetUpButton.setVisibility(View.GONE);
        }

        setUpDbManager = new SetUpDbManager(this);
        incomeDbManager = new IncomeBudgetDbManager(this);
        incomeAdapter = new IncomeDbAdapter(this, incomeDbManager.getIncomes());
        budgetIncomeDetails.setAdapter(incomeAdapter);

        expenseDbManager = new ExpenseBudgetDbManager(this);
        expenseAdapter = new ExpenseDbAdapter(this, expenseDbManager.getExpense());
        budgetExpensesDetails.setAdapter(expenseAdapter);

        debtDbManager = new DebtDbManager(this);

        budgetHeaderText();

    }

    public int budgetSetUpCheck() {
        setUpHelper = new DbHelper(this);
        setUpDbDb = setUpHelper.getReadableDatabase();
        setUpCursor = setUpDbDb.rawQuery("SELECT max(budgetDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor.moveToFirst();
        budgetDoneCheck = setUpCursor.getInt(0);
        setUpCursor.close();

        return budgetDoneCheck;
    }

    View.OnClickListener onClickDoneBudgetSetUpButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           budgetDone = 1;

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            setUpDbManager.addSetUp(setUpDb);

            Toast toast = Toast.makeText(getApplicationContext(), "You can edit this list by clicking BUDGET on the menu", Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            backToSetUp = new Intent(LayoutBudget.this, LayoutSetUp.class);
            backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSetUp);

        }
    };

    public void budgetHeaderText() {

        sumTotalIncome();
        sumTotalExpenses();
        //sumTotalAExpenses();
        //sumTotalBExpenses();

        try {
            totalIncomeS = String.valueOf(sumTotalIncome());
            if (totalIncomeS != null && !totalIncomeS.equals("")) {
                totalIncomeD = Double.valueOf(totalIncomeS);
            } else {
                totalIncomeD = 0.0;
            }

            totalIncome2 = currencyFormat.format(totalIncomeD);
            budgetIncomeTotalText.setText(totalIncome2);

        } catch (NumberFormatException e) {
            budgetIncomeTotalText.setText(totalIncome2);
        }

        try {
            totalExpensesS = String.valueOf(sumTotalExpenses());
            if (totalExpensesS != null && !totalExpensesS.equals("")) {
                totalExpensesD = Double.valueOf(totalExpensesS);
            } else {
                totalExpensesD = 0.0;
            }

            totalExpenses2 = currencyFormat.format(totalExpensesD);
            budgetExpensesTotalText.setText(totalExpenses2);

        } catch (NumberFormatException e) {
            budgetExpensesTotalText.setText(totalExpenses2);
        }

        if (totalExpensesD > totalIncomeD) {

            incomeAvailableN = (sumTotalExpenses() - sumTotalIncome());
            incomeAvailableN2 = currencyFormat.format(incomeAvailableN);

            budgetOopsText.setVisibility(View.VISIBLE);
            budgetOopsAmountText.setVisibility(View.VISIBLE);
            budgetOopsAmountText.setText("-" + incomeAvailableN2);
            headerLabel2.setVisibility(View.GONE);
            incomeAvailable.setVisibility(View.GONE);
        }

        if (totalIncomeD >= totalExpensesD) {
            budgetOopsText.setVisibility(View.GONE);
            budgetOopsAmountText.setVisibility(View.GONE);
            headerLabel2.setVisibility(View.VISIBLE);
            incomeAvailable.setVisibility(View.VISIBLE);
        }

        incomeAvailableD = (sumTotalIncome() - sumTotalExpenses());
        incomeAvailable2 = currencyFormat.format(incomeAvailableD);
        incomeAvailable.setText(incomeAvailable2);

    }

    View.OnClickListener onClickIncomePlusButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            incomePlusButton = new Intent(LayoutBudget.this, AddBudgetIncome.class);
            incomePlusButton.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(incomePlusButton);

        }
    };

    View.OnClickListener onClickExpensePlusButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            expensePlusButton = new Intent(LayoutBudget.this, AddBudgetExpense.class);
            expensePlusButton.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(expensePlusButton);

        }
    };

    public Double sumTotalExpenses() {
        expenseDbHelper = new DbHelper(this);
        expenseDb = expenseDbHelper.getReadableDatabase();
        expenseCursor = expenseDb.rawQuery("SELECT sum(expenseAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        expenseCursor.moveToFirst();
        totalExpenses = expenseCursor.getDouble(0);
        expenseCursor.close();

        return totalExpenses;
    }

    public Double sumTotalIncome() {
        incomeDbHelper = new DbHelper(this);
        incomeDb = incomeDbHelper.getReadableDatabase();
        incomeCursor = incomeDb.rawQuery("SELECT sum(incomeAnnualAmount)" + " FROM " + DbHelper.INCOME_TABLE_NAME, null);
        incomeCursor.moveToFirst();
        totalIncome = incomeCursor.getDouble(0);
        incomeCursor.close();

        return totalIncome;
    }

    /*public Double sumTotalAExpenses() {
        expenseADbHelper = new DbHelper(this);
        expenseADb = expenseADbHelper.getReadableDatabase();
        expenseACursor = expenseADb.rawQuery("SELECT sum(expenseAAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        expenseACursor.moveToFirst();
        totalAExpenses = expenseACursor.getDouble(0);
        expenseACursor.close();

        return totalAExpenses;
    }

    public Double sumTotalBExpenses() {
        expenseBDbHelper = new DbHelper(this);
        expenseBDb = expenseBDbHelper.getReadableDatabase();
        expenseBCursor = expenseBDb.rawQuery("SELECT sum(expenseBAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        expenseBCursor.moveToFirst();
        totalBExpenses = expenseBCursor.getDouble(0);
        expenseBCursor.close();

        return totalBExpenses;
    }*/

    public long findDebtId() {
        debtDbHelper = new DbHelper(this);
        debtDb = debtDbHelper.getReadableDatabase();
        debtCursor = debtDb.rawQuery("SELECT " + DbHelper.ID + " FROM " + DbHelper.DEBTS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYD +
                " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        debtCursor.moveToFirst();
        id = debtCursor.getLong(0);
        debtCursor.close();

        return id;
    }

    public boolean foundNoDebtId(Cursor debtCursor6) {
        debtDbHelper3 = new DbHelper(this);
        debtDb3 = debtDbHelper3.getReadableDatabase();
        debtCursor6 = debtDb3.rawQuery("SELECT " + DbHelper.ID + " FROM " + DbHelper.DEBTS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYD +
                " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        foundNoDebtId = (!debtCursor6.moveToFirst() || debtCursor6.getCount() == 0);

        return foundNoDebtId;
    }

    public String updateDebtEndDate() {

        debtDbHelper2 = new DbHelper(this);
        debtDb2 = debtDbHelper2.getReadableDatabase();

        debtCursor2 = debtDb2.rawQuery("SELECT " + DbHelper.DEBTAMOUNT + " FROM " + DbHelper.DEBTS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYD
                + " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        debtCursor2.moveToFirst();
        Double debtAmount2 = debtCursor2.getDouble(0);
        debtCursor2.close();

        debtCursor3 = debtDb2.rawQuery("SELECT " + DbHelper.DEBTRATE + " FROM " + DbHelper.DEBTS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYD
                + " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        debtCursor3.moveToFirst();
        Double debtRate2 = debtCursor3.getDouble(0);
        debtCursor3.close();

        debtCursor4 = debtDb2.rawQuery("SELECT " + DbHelper.DEBTPAYMENTS + " FROM " + DbHelper.DEBTS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYD
                + " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        debtCursor4.moveToFirst();
        Double debtPayments2 = debtCursor4.getDouble(0);
        debtCursor4.close();

        debtCursor5 = debtDb2.rawQuery("SELECT " + DbHelper.DEBTFREQUENCY + " FROM " + DbHelper.DEBTS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYD
                + " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        debtCursor5.moveToFirst();
        Double debtFrequency2 = debtCursor5.getDouble(0);
        debtCursor5.close();

        debtCal = Calendar.getInstance();
        numberOfYearsToPayDebt = -(Math.log(1 - (debtAmount2 * (debtRate2 / 100) / (debtPayments2 * debtFrequency2))) / (debtFrequency2 *
                Math.log(1 + ((debtRate2 / 100) / debtFrequency2))));
        numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);

        if(debtAmount2 == 0) {
            debtEnd = getString(R.string.debt_paid);

        } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfDaysToPayDebt <= 0) {
            debtEnd = getString(R.string.too_far);

        } else {
            debtCal.add(Calendar.DATE, numberOfDaysToPayDebt);
            debtEndD = debtCal.getTime();
            debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
            debtEnd = getString(R.string.debt_will) + " " + debtEndS.format(debtEndD);
        }

        return debtEnd;
    }

    public long findSavingsId() {
        savingsDbHelper = new DbHelper(this);
        savingsDb = savingsDbHelper.getReadableDatabase();
        savingsCursor = savingsDb.rawQuery("SELECT " + DbHelper.ID + " FROM " + DbHelper.SAVINGS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYS +
                " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        savingsCursor.moveToFirst();
        id = savingsCursor.getLong(0);
        savingsCursor.close();

        return id;
    }

    public boolean foundNoSavingsId(Cursor savingsCursor2) {
        savingsDbHelper2 = new DbHelper(this);
        savingsDb2 = savingsDbHelper2.getReadableDatabase();
        savingsCursor2 = savingsDb2.rawQuery("SELECT " + DbHelper.ID + " FROM " + DbHelper.SAVINGS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYS +
                " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        foundNoSavingsId = (!savingsCursor2.moveToFirst() || savingsCursor2.getCount() == 0);

        return foundNoSavingsId;
    }

    public String updateSavingsDate() {

        savingsDbHelper3 = new DbHelper(this);
        savingsDb3 = savingsDbHelper3.getReadableDatabase();

        savingsCursor3 = savingsDb3.rawQuery("SELECT " + DbHelper.SAVINGSAMOUNT + " FROM " + DbHelper.SAVINGS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYS
                + " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        savingsCursor3.moveToFirst();
        Double savingsAmount2 = savingsCursor3.getDouble(0);
        savingsCursor3.close();

        savingsCursor7 = savingsDb3.rawQuery("SELECT " + DbHelper.SAVINGSGOAL + " FROM " + DbHelper.SAVINGS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYS
                + " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        savingsCursor7.moveToFirst();
        Double savingsGoal2 = savingsCursor7.getDouble(0);
        savingsCursor7.close();

        savingsCursor4 = savingsDb3.rawQuery("SELECT " + DbHelper.SAVINGSRATE + " FROM " + DbHelper.SAVINGS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYS
                + " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        savingsCursor4.moveToFirst();
        Double savingsRate2 = savingsCursor4.getDouble(0);
        savingsCursor4.close();

        savingsCursor5 = savingsDb3.rawQuery("SELECT " + DbHelper.SAVINGSPAYMENTS + " FROM " + DbHelper.SAVINGS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYS
                + " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        savingsCursor5.moveToFirst();
        Double savingsPayments2 = savingsCursor5.getDouble(0);
        savingsCursor5.close();

        savingsCursor6 = savingsDb3.rawQuery("SELECT " + DbHelper.SAVINGSFREQUENCY + " FROM " + DbHelper.SAVINGS_TABLE_NAME + " WHERE " + DbHelper.EXPREFKEYS
                + " = '" + String.valueOf(expenseBudgetDb.getId()) + "'", null);
        savingsCursor6.moveToFirst();
        Double savingsFrequency2 = savingsCursor6.getDouble(0);
        savingsCursor6.close();

        savingsCal = Calendar.getInstance();
        numberOfYearsToSavingsGoal = -(Math.log(1 - ((savingsGoal2 - savingsAmount2) * (savingsRate2 / 100) / (savingsPayments2 * savingsFrequency2))) / (savingsFrequency2 *
                Math.log(1 + ((savingsRate2 / 100) / savingsFrequency2))));
        numberOfDaysToSavingsGoal = (int) Math.round(numberOfYearsToSavingsGoal * 365);

        if (savingsGoal2 - savingsAmount2 <= 0) {
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

    public class IncomeDbAdapter extends ArrayAdapter<IncomeBudgetDb> {

        public Context context;
        public List<IncomeBudgetDb> incomes;

        public IncomeDbAdapter(
                Context context,
                List<IncomeBudgetDb> incomes) {

            super(context, -1, incomes);

            this.context = context;
            this.incomes = incomes;
        }

        public void updateIncomes(List<IncomeBudgetDb> incomes) {
            this.incomes = incomes;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return incomes.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderIncome incomeHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.fragment_list_budget_income,
                        parent, false);

                incomeHolder = new ViewHolderIncome();
                incomeHolder.incomeName = convertView.findViewById(R.id.budgetIncomeCategoryText);
                incomeHolder.incomeAmount = convertView.findViewById(R.id.budgetIncomeAmountText);
                incomeHolder.incomeDeleted = convertView.findViewById(R.id.deleteIncomeButton);
                incomeHolder.incomeEdit = convertView.findViewById(R.id.editIncomeButton);
                convertView.setTag(incomeHolder);

            } else {
                incomeHolder = (ViewHolderIncome) convertView.getTag();
            }

            incomeHolder.incomeName.setText(incomes.get(position).getIncomeName());

            //retrieve incomeAnnualAmount and format as currency
            try {
                incomeAnnualAmountS = String.valueOf((incomes.get(position).getIncomeAmount()) * (incomes.get(position).getIncomeFrequency()));
                if (!incomeAnnualAmountS.equals("")) {
                    incomeAnnualAmountD = Double.valueOf(incomeAnnualAmountS);
                } else {
                    incomeAnnualAmountD = 0.0;
                }
                incomeAnnualAmount2 = currencyFormat.format(incomeAnnualAmountD);
                incomeHolder.incomeAmount.setText(incomeAnnualAmount2);
            } catch (NumberFormatException e) {
                incomeHolder.incomeAmount.setText(incomeAnnualAmount2);
            }

            incomeHolder.incomeDeleted.setTag(incomes.get(position));
            incomeHolder.incomeEdit.setTag(incomes.get(position));

            //click on pencil icon to edit a data record
            incomeHolder.incomeEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_budget_income);
                    LayoutBudget.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    budgetIncomeCategory = findViewById(R.id.budgetIncomeCategory);
                    budgetIncomeAmount = findViewById(R.id.budgetIncomeAmount);
                    budgetIncomeFrequencyRadioGroup = findViewById(R.id.budgetIncomeFrequencyRadioGroup);
                    budgetIncomeWeeklyRadioButton = findViewById(R.id.budgetIncomeWeeklyRadioButton);
                    budgetIncomeBiWeeklyRadioButton = findViewById(R.id.budgetIncomeBiWeeklyRadioButton);
                    budgetIncomeBiMonthlyRadioButton = findViewById(R.id.budgetIncomeBiMonthlyRadioButton);
                    budgetIncomeMonthlyRadioButton = findViewById(R.id.budgetIncomeMonthlyRadioButton);
                    budgetIncomeBiAnnuallyRadioButton = findViewById(R.id.budgetIncomeBiAnnuallyRadioButton);
                    budgetIncomeAnnuallyRadioButton = findViewById(R.id.budgetIncomeAnnuallyRadioButton);
                    budgetAddIncomeButton = findViewById(R.id.budgetAddIncomeButton);
                    budgetUpdateIncomeButton = findViewById(R.id.budgetUpdateIncomeButton);
                    budgetAddIncomeButton.setVisibility(View.GONE);
                    budgetCancelIncomeButton = findViewById(R.id.budgetCancelIncomeButton);

                    incomeBudgetDb = (IncomeBudgetDb) incomeHolder.incomeEdit.getTag();

                    budgetIncomeCategory.setText(incomeBudgetDb.getIncomeName());
                    budgetIncomeAmount.setText(String.valueOf(incomeBudgetDb.getIncomeAmount()));

                    if (incomeBudgetDb.getIncomeFrequency() == 52) {
                        budgetIncomeWeeklyRadioButton.setChecked(true);
                        incomeFrequencyS = "52";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 26) {
                        budgetIncomeBiWeeklyRadioButton.setChecked(true);
                        incomeFrequencyS = "26";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 24) {
                        budgetIncomeBiMonthlyRadioButton.setChecked(true);
                        incomeFrequencyS = "24";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 12) {
                        budgetIncomeMonthlyRadioButton.setChecked(true);
                        incomeFrequencyS = "12";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 2) {
                        budgetIncomeBiAnnuallyRadioButton.setChecked(true);
                        incomeFrequencyS = "2";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 1) {
                        budgetIncomeAnnuallyRadioButton.setChecked(true);
                        incomeFrequencyS = "1";
                    }

                    //update db if changed
                    budgetIncomeFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetIncomeWeeklyRadioButton:
                                    incomeFrequencyS = "52";
                                    break;
                                case R.id.budgetIncomeBiWeeklyRadioButton:
                                    incomeFrequencyS = "26";
                                    break;
                                case R.id.budgetIncomeBiMonthlyRadioButton:
                                    incomeFrequencyS = "24";
                                    break;
                                case R.id.budgetIncomeMonthlyRadioButton:
                                    incomeFrequencyS = "12";
                                    break;
                                case R.id.budgetIncomeBiAnnuallyRadioButton:
                                    incomeFrequencyS = "2";
                                    break;
                                case R.id.budgetIncomeAnnuallyRadioButton:
                                    incomeFrequencyS = "1";
                                    break;
                            }
                        }
                    });

                    budgetCancelIncomeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget2 = new Intent(LayoutBudget.this, LayoutBudget.class);
                            backToBudget2.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToBudget2);
                        }
                    });

                    budgetUpdateIncomeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            incomeBudgetDb.setIncomeName(budgetIncomeCategory.getText().toString());
                            incomeBudgetDb.setIncomeAmount(Double.valueOf(budgetIncomeAmount.getText().toString()));
                            incomeBudgetDb.setIncomeFrequency(Double.valueOf(incomeFrequencyS));
                            incomeBudgetDb.setIncomeAnnualAmount(incomeBudgetDb.getIncomeAmount() * incomeBudgetDb.getIncomeFrequency());

                            incomeDbManager.updateIncome(incomeBudgetDb);
                            incomeAdapter.updateIncomes(incomeDbManager.getIncomes());
                            incomeAdapter.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            backToBudget = new Intent(LayoutBudget.this, LayoutBudget.class);
                            backToBudget.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToBudget);

                            budgetHeaderText();
                        }
                    });
                }
            });

            //click on trash can to delete data record
            incomeHolder.incomeDeleted.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    incomeBudgetDb = (IncomeBudgetDb) incomeHolder.incomeDeleted.getTag();
                    incomeDbManager.deleteIncome(incomeBudgetDb);
                    incomeAdapter.updateIncomes(incomeDbManager.getIncomes());
                    incomeAdapter.notifyDataSetChanged();

                    budgetHeaderText();
                }
            });

            return convertView;
        }
    }

    public class ExpenseDbAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        public Context context;
        public List<ExpenseBudgetDb> expenses;

        public ExpenseDbAdapter(
                Context context,
                List<ExpenseBudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateExpenses(List<ExpenseBudgetDb> expenses) {
            this.expenses = expenses;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return expenses.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderExpense expenseHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.fragment_list_budget_expense,
                        parent, false);

                expenseHolder = new ViewHolderExpense();
                expenseHolder.expenseName = convertView.findViewById(R.id.budgetExpenseCategoryText);
                expenseHolder.expenseAmount = convertView.findViewById(R.id.budgetExpenseAmountText);
                expenseHolder.expenseDeleted = convertView.findViewById(R.id.deleteExpenseButton);
                expenseHolder.expenseEdit = convertView.findViewById(R.id.editExpenseButton);
                convertView.setTag(expenseHolder);

            } else {
                expenseHolder = (ViewHolderExpense) convertView.getTag();
            }

            expenseHolder.expenseName.setText(expenses.get(position).getExpenseName());

            //retrieve incomeAmount and format as currency
            try {
                expenseAnnualAmountS = String.valueOf((expenses.get(position).getExpenseAmount()) * (expenses.get(position).getExpenseFrequency()));
                if (!expenseAnnualAmountS.equals("")) {
                    expenseAnnualAmountD = Double.valueOf(expenseAnnualAmountS);
                } else {
                    expenseAnnualAmountD = 0.0;
                }
                expenseAnnualAmount2 = currencyFormat.format(expenseAnnualAmountD);
                expenseHolder.expenseAmount.setText(expenseAnnualAmount2);
            } catch (NumberFormatException e) {
                expenseHolder.expenseAmount.setText(expenseAnnualAmount2);
            }

            expenseHolder.expenseDeleted.setTag(expenses.get(position));
            expenseHolder.expenseEdit.setTag(expenses.get(position));

            //click on pencil icon to edit a data record
            expenseHolder.expenseEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_budget_expense);
                    LayoutBudget.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    budgetExpenseCategory = findViewById(R.id.budgetExpenseCategory);
                    budgetExpenseAmount = findViewById(R.id.budgetExpenseAmount);
                    budgetExpenseFrequencyRadioGroup = findViewById(R.id.budgetExpenseFrequencyRadioGroup);
                    weeklyGuidanceLabel = findViewById(R.id.weeklyGuidanceLabel);
                    budgetExpenseABRadioGroup = findViewById(R.id.budgetExpenseABRadioGroup);
                    budgetExpenseReminderRadioGroup = findViewById(R.id.budgetExpenseReminderRadioGroup);
                    budgetCancelExpenseButton = findViewById(R.id.budgetCancelExpenseButton);
                    budgetAddExpenseButton = findViewById(R.id.budgetAddExpenseButton);
                    budgetUpdateExpenseButton = findViewById(R.id.budgetUpdateExpenseButton);
                    budgetAddExpenseButton.setVisibility(View.GONE);
                    weeklyGuidanceLabel.setVisibility(View.GONE);
                    budgetExpenseReminderRadioGroup.setVisibility(View.GONE);

                    budgetExpenseWeeklyRadioButton = findViewById(R.id.budgetExpenseWeeklyRadioButton);
                    budgetExpenseBiWeeklyRadioButton = findViewById(R.id.budgetExpenseBiWeeklyRadioButton);
                    budgetExpenseBiMonthlyRadioButton = findViewById(R.id.budgetExpenseBiMonthlyRadioButton);
                    budgetExpenseMonthlyRadioButton = findViewById(R.id.budgetExpenseMonthlyRadioButton);
                    budgetExpenseBiAnnuallyRadioButton = findViewById(R.id.budgetExpenseBiAnnuallyRadioButton);
                    budgetExpenseAnnuallyRadioButton = findViewById(R.id.budgetExpenseAnnuallyRadioButton);

                    budgetExpenseARadioButton = findViewById(R.id.budgetExpenseARadioButton);
                    budgetExpenseBRadioButton = findViewById(R.id.budgetExpenseBRadioButton);

                    budgetExpenseYesRadioButton = findViewById(R.id.budgetExpenseYesRadioButton);
                    budgetExpenseNoRadioButton = findViewById(R.id.budgetExpenseNoRadioButton);

                    expenseBudgetDb = (ExpenseBudgetDb) expenseHolder.expenseEdit.getTag();

                    budgetExpenseCategory.setText(expenseBudgetDb.getExpenseName());

                    foundNoDebtId(debtCursor6);
                    foundNoSavingsId(savingsCursor2);

                    if (!foundNoDebtId) {
                        budgetExpenseWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiMonthlyRadioButton.setVisibility(View.GONE);
                        budgetExpenseMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiAnnuallyRadioButton.setVisibility(View.GONE);
                        budgetExpenseAnnuallyRadioButton.setVisibility(View.GONE);
                    } else if (!foundNoSavingsId) {
                        budgetExpenseWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiMonthlyRadioButton.setVisibility(View.GONE);
                        budgetExpenseMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiAnnuallyRadioButton.setVisibility(View.GONE);
                        budgetExpenseAnnuallyRadioButton.setVisibility(View.VISIBLE);
                    } else {
                        budgetExpenseWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiAnnuallyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseAnnuallyRadioButton.setVisibility(View.VISIBLE);
                    }

                    budgetExpenseAmount.setText(String.valueOf(expenseBudgetDb.getExpenseAmount()));

                    //set radio buttons from data
                    if (expenseBudgetDb.getExpenseFrequency() == 52) {
                        budgetExpenseWeeklyRadioButton.setChecked(true);
                        expenseFrequencyS = "52";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 26) {
                        budgetExpenseBiWeeklyRadioButton.setChecked(true);
                        expenseFrequencyS = "26";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 24) {
                        budgetExpenseBiMonthlyRadioButton.setChecked(true);
                        expenseFrequencyS = "24";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 12) {
                        budgetExpenseMonthlyRadioButton.setChecked(true);
                        expenseFrequencyS = "12";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 2) {
                        budgetExpenseBiAnnuallyRadioButton.setChecked(true);
                        expenseFrequencyS = "2";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 1) {
                        budgetExpenseAnnuallyRadioButton.setChecked(true);
                        expenseFrequencyS = "1";
                    }

                    switch (String.valueOf(expenseBudgetDb.getExpensePriority())) {
                        case "A":
                            budgetExpenseARadioButton.setChecked(true);
                            expensePriorityS = "A";
                            budgetExpenseReminderRadioGroup.setVisibility(View.GONE);
                            weeklyGuidanceLabel.setVisibility(View.GONE);
                            break;
                        case "B":
                            budgetExpenseBRadioButton.setChecked(true);
                            expensePriorityS = "B";
                            budgetExpenseReminderRadioGroup.setVisibility(View.VISIBLE);
                            weeklyGuidanceLabel.setVisibility(View.VISIBLE);
                            break;
                    }

                    switch (String.valueOf(expenseBudgetDb.getExpenseWeekly())) {
                        case "Y":
                            budgetExpenseYesRadioButton.setChecked(true);
                            expenseWeeklyS = "Y";
                            break;
                        case "N":
                            budgetExpenseNoRadioButton.setChecked(true);
                            expenseWeeklyS = "N";
                            break;
                    }


                    //update db if changed
                    budgetExpenseFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetExpenseWeeklyRadioButton:
                                    expenseFrequencyS = "52";
                                    break;
                                case R.id.budgetExpenseBiWeeklyRadioButton:
                                    expenseFrequencyS = "26";
                                    break;
                                case R.id.budgetExpenseBiMonthlyRadioButton:
                                    expenseFrequencyS = "24";
                                    break;
                                case R.id.budgetExpenseMonthlyRadioButton:
                                    expenseFrequencyS = "12";
                                    break;
                                case R.id.budgetExpenseBiAnnuallyRadioButton:
                                    expenseFrequencyS = "2";
                                    break;
                                case R.id.budgetExpenseAnnuallyRadioButton:
                                    expenseFrequencyS = "1";
                                    break;
                            }
                        }
                    });

                    budgetExpenseABRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetExpenseARadioButton:
                                    expensePriorityS = "A";
                                    expenseWeeklyS = "N";
                                    budgetExpenseReminderRadioGroup.setVisibility(View.GONE);
                                    weeklyGuidanceLabel.setVisibility(View.GONE);
                                    break;
                                case R.id.budgetExpenseBRadioButton:
                                    expensePriorityS = "B";
                                    budgetExpenseReminderRadioGroup.setVisibility(View.VISIBLE);
                                    weeklyGuidanceLabel.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                    });

                    budgetExpenseReminderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetExpenseYesRadioButton:
                                    expenseWeeklyS = "Y";
                                    break;
                                case R.id.budgetExpenseNoRadioButton:
                                    expensePriorityS = "N";
                                    break;
                            }
                        }

                    });

                    budgetCancelExpenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget3 = new Intent(LayoutBudget.this, LayoutBudget.class);
                            backToBudget3.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToBudget3);
                        }
                    });

                    budgetUpdateExpenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            expenseBudgetDb.setExpenseName(budgetExpenseCategory.getText().toString());
                            expenseBudgetDb.setExpenseAmount(Double.valueOf(budgetExpenseAmount.getText().toString()));
                            expenseBudgetDb.setExpenseFrequency(Double.valueOf(expenseFrequencyS));
                            expenseBudgetDb.setExpensePriority(String.valueOf(expensePriorityS));
                            expenseBudgetDb.setExpenseWeekly(String.valueOf(expenseWeeklyS));
                            expenseBudgetDb.setExpenseAnnualAmount(expenseBudgetDb.getExpenseAmount() * expenseBudgetDb.getExpenseFrequency());

                            if (expenseBudgetDb.getExpensePriority() == "A") {
                                expenseBudgetDb.setExpenseAAnnualAmount(expenseBudgetDb.getExpenseAnnualAmount());
                                expenseBudgetDb.setExpenseBAnnualAmount(0.0);
                            } else if (expenseBudgetDb.getExpensePriority() == "B") {
                                expenseBudgetDb.setExpenseBAnnualAmount(expenseBudgetDb.getExpenseAnnualAmount());
                                expenseBudgetDb.setExpenseAAnnualAmount(0.0);
                            }

                                expenseDbManager.updateExpense(expenseBudgetDb);

                                try {
                                    String[] args = new String[]{String.valueOf(findDebtId())};
                                    ContentValues debtValues = new ContentValues();

                                    debtValues.put(DbHelper.DEBTNAME, budgetExpenseCategory.getText().toString());
                                    debtValues.put(DbHelper.DEBTPAYMENTS, Double.valueOf(budgetExpenseAmount.getText().toString()));
                                    debtValues.put(DbHelper.DEBTFREQUENCY, Double.valueOf(expenseFrequencyS));

                                    debtDb.update(DbHelper.DEBTS_TABLE_NAME, debtValues, DbHelper.ID + "=?", args);
                                    ContentValues debtValues2 = new ContentValues();
                                    debtValues2.put(DbHelper.DEBTEND, updateDebtEndDate());
                                    debtDb.update(DbHelper.DEBTS_TABLE_NAME, debtValues2, DbHelper.ID + "=?", args);

                                } catch (CursorIndexOutOfBoundsException e2) {
                                    e2.printStackTrace();
                                }

                                try {
                                    String[] args2 = new String[]{String.valueOf(findSavingsId())};
                                    ContentValues savingsValues = new ContentValues();

                                    savingsValues.put(DbHelper.SAVINGSNAME, budgetExpenseCategory.getText().toString());
                                    savingsValues.put(DbHelper.SAVINGSPAYMENTS, Double.valueOf(budgetExpenseAmount.getText().toString()));
                                    savingsValues.put(DbHelper.SAVINGSFREQUENCY, Double.valueOf(expenseFrequencyS));
                                    savingsValues.put(DbHelper.SAVINGSNAME, budgetExpenseCategory.getText().toString());
                                    savingsValues.put(DbHelper.SAVINGSNAME, budgetExpenseCategory.getText().toString());
                                    savingsValues.put(DbHelper.SAVINGSNAME, budgetExpenseCategory.getText().toString());

                                    savingsDb.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues, DbHelper.ID + "=?", args2);
                                    ContentValues savingsValues2 = new ContentValues();
                                    savingsValues2.put(DbHelper.SAVINGSDATE, updateSavingsDate());
                                    savingsDb.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues2, DbHelper.ID + "=?", args2);

                                } catch (CursorIndexOutOfBoundsException e3) {
                                    e3.printStackTrace();
                                }

                            expenseAdapter.updateExpenses(expenseDbManager.getExpense());
                            expenseAdapter.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            backToBudget4 = new Intent(LayoutBudget.this, LayoutBudget.class);
                            backToBudget4.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToBudget4);

                            budgetHeaderText();
                        }
                    });
                }
            });

            //click on trash can to delete data record
            expenseHolder.expenseDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    expenseBudgetDb = (ExpenseBudgetDb) expenseHolder.expenseDeleted.getTag();
                    expenseDbManager.deleteExpense(expenseBudgetDb);

                    try {
                        String[] args = new String[]{String.valueOf(findDebtId())};
                        debtDb.delete(DbHelper.DEBTS_TABLE_NAME, DbHelper.ID + "=?", args);
                    } catch (Exception e) {
                        e.getStackTrace();
                    }

                    try {
                        String[] args2 = new String[]{String.valueOf(findSavingsId())};
                        savingsDb.delete(DbHelper.SAVINGS_TABLE_NAME, DbHelper.ID + "=?", args2);
                    } catch (Exception e) {
                        e.getStackTrace();
                    }

                    expenseAdapter.updateExpenses(expenseDbManager.getExpense());
                    budgetHeaderText();
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private static class ViewHolderIncome {
        private TextView incomeName;
        private TextView incomeAmount;
        private ImageButton incomeDeleted;
        private ImageButton incomeEdit;
    }

    private static class ViewHolderExpense {
        private TextView expenseName;
        private TextView expenseAmount;
        private ImageButton expenseDeleted;
        private ImageButton expenseEdit;
    }
}
