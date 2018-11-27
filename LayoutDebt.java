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

import android.text.Editable;
import android.text.TextWatcher;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.DebtDbManager;
import ca.gotchasomething.mynance.data.SetUpDb;
import ca.gotchasomething.mynance.data.SetUpDbManager;

public class LayoutDebt extends MainNavigation {

    ListView debtListView;
    DebtDbManager debtDbManager;
    DebtDbAdapter debtAdapter;
    DebtDb debtDb;
    DbHelper setUpHelper, expenseDbHelper, debtDbHelper;
    SQLiteDatabase setUpDbDb, expenseDb, debtDbDb;
    Cursor setUpCursor, expenseCursor, debtCursor;
    long id;
    ExpenseBudgetDbManager expenseBudgetDbManager;
    SetUpDbManager setUpDbManager;
    SetUpDb setUpDb;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    Double amount = 0.0, rate = 0.0, payments = 0.0, frequency = 0.0, expenseAnnualAmount = 0.0;
    String totalDebt2 = null, latestDate = null, totalDebtS = null, debtAmountS = null, debtAmount2 = null, debtFrequencyS = null, debtEnd = null;
    FloatingActionButton addDebtButton;
    TextView totalDebtOwing, totalDebtPaidByDate, debtListName, debtListAmount, debtListFreeDate, debtDateResult;
    EditText debtNameEntry, debtAmountEntry, debtPercentEntry, debtPaymentsEntry;
    RadioGroup debtFrequencyRadioGroup;
    RadioButton debtWeeklyRadioButton, debtBiWeeklyRadioButton, debtMonthlyRadioButton;
    Button saveDebtButton, updateDebtButton, cancelDebtButton, doneDebtsSetUpButton;
    Double totalDebt = 0.0, totalDebtD = 0.0, debtAmountD = 0.0, a = 0.0, numberOfYearsToPayDebt = 0.0, balanceAmount;
    Integer numberOfDaysToPayDebt = 0;
    Calendar debtCal;
    Date debtEndD, dateObj, latestDateD;
    Intent addNewDebt, backToDebtScreen, backToDebtScreen2, backToSetUp;
    SimpleDateFormat latestDateS, debtEndS;
    boolean success;
    int debtsDoneCheck, debtsDone, savingsDone, budgetDone, balanceDone, tourDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_debt);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        totalDebtOwing = findViewById(R.id.totalDebtOwing);
        totalDebtPaidByDate = findViewById(R.id.totalDebtPaidByDate);

        debtListView = findViewById(R.id.debtListView);
        addDebtButton = findViewById(R.id.addDebtButton);
        addDebtButton.setOnClickListener(onClickAddDebtButton);

        doneDebtsSetUpButton = findViewById(R.id.doneDebtsSetUpButton);
        doneDebtsSetUpButton.setOnClickListener(onClickDoneDebtsSetUpButton);

        debtSetUpCheck();
        if(debtsDoneCheck >0) {
            doneDebtsSetUpButton.setVisibility(View.GONE);
        }

        debtDbManager = new DebtDbManager(this);
        debtAdapter = new DebtDbAdapter(this, debtDbManager.getDebts());
        debtListView.setAdapter(debtAdapter);

        setUpDbManager = new SetUpDbManager(this);

        debtHeaderText();
    }

    public int debtSetUpCheck() {
        setUpHelper = new DbHelper(this);
        setUpDbDb = setUpHelper.getReadableDatabase();
        setUpCursor = setUpDbDb.rawQuery("SELECT max(debtsDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor.moveToFirst();
        debtsDoneCheck = setUpCursor.getInt(0);
        setUpCursor.close();

        return debtsDoneCheck;
    }

    View.OnClickListener onClickDoneDebtsSetUpButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debtsDone = 1;
            savingsDone = 0;
            budgetDone = 0;
            balanceDone = 0;
            balanceAmount = 0.0;
            tourDone = 0;

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            setUpDbManager.addSetUp(setUpDb);

            Toast toast = Toast.makeText(getApplicationContext(), "You can edit this list by clicking DEBTS on the menu", Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            backToSetUp = new Intent(LayoutDebt.this, LayoutSetUp.class);
            backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSetUp);

        }
    };

    public void debtHeaderText() {

        try {
            totalDebtS = String.valueOf(sumTotalDebt());
            if (totalDebtS != null && !totalDebtS.equals("")) {
                totalDebtD = Double.valueOf(totalDebtS);
            } else {
                totalDebtD = 0.0;
            }

            totalDebt2 = currencyFormat.format(totalDebtD);
            totalDebtOwing.setText(totalDebt2);

        } catch (NumberFormatException e) {
            totalDebtOwing.setText(totalDebt2);
        }

        totalDebtPaidByDate.setText(latestDate());
    }

    public Double sumTotalDebt() {
        debtDbHelper = new DbHelper(this);
        debtDbDb = debtDbHelper.getReadableDatabase();
        debtCursor = debtDbDb.rawQuery("SELECT sum(debtAmount)" + " FROM " + DbHelper.DEBTS_TABLE_NAME, null);
        debtCursor.moveToFirst();
        totalDebt = debtCursor.getDouble(0);
        debtCursor.close();

        return totalDebt;
    }

    public String latestDate() {
        List<String> dates = new ArrayList<>();
        debtDbHelper = new DbHelper(this);
        debtDbDb = debtDbHelper.getReadableDatabase();
        debtCursor = debtDbDb.rawQuery("SELECT (debtEnd)" + " FROM " + DbHelper.DEBTS_TABLE_NAME, null);
        debtDbHelper.getWritableDatabase();
        debtCursor.moveToFirst();
        if (debtCursor.moveToFirst()) {
            do {
                dates.add(debtCursor.getString(0));
            } while (debtCursor.moveToNext());
        }

        List<Date> dates2 = new ArrayList<>(dates.size());

        for (String s : dates) {
            try {
                dateObj = new SimpleDateFormat("dd-MMM-yyyy").parse(s);
                dates2.add(dateObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            latestDateD = Collections.max(dates2);
        } catch (NoSuchElementException e) {
            totalDebtPaidByDate.setVisibility(View.GONE);
        }

        try {
            latestDateS = new SimpleDateFormat("dd-MMM-yyyy");
            latestDate = latestDateS.format(latestDateD);
        } catch (Exception e2) {
            totalDebtPaidByDate.setVisibility(View.GONE);
        }

        return latestDate;
    }

    public long findExpenseId() {
        expenseDbHelper = new DbHelper(this);
        expenseDb = expenseDbHelper.getReadableDatabase();
        expenseCursor = expenseDb.rawQuery("SELECT " + DbHelper.ID + " FROM " + DbHelper.EXPENSES_TABLE_NAME + " WHERE " + DbHelper.ID +
                " = '" + String.valueOf(debtDb.getExpRefKeyD()) + "'", null);
        expenseCursor.moveToFirst();
        id = expenseCursor.getLong(0);
        expenseCursor.close();

        return id;
    }

    public String calcDebtDate() {

        if(debtAmountEntry.getText().toString().equals("")) {
            amount = 0.0;
        } else {
            amount = Double.valueOf(debtAmountEntry.getText().toString());
        }

        if(debtPercentEntry.getText().toString().equals("")) {
            rate = 0.0;
        } else {
            rate = Double.valueOf(debtPercentEntry.getText().toString());
        }

        if(debtPaymentsEntry.getText().toString().equals("")) {
            payments = 0.0;
        } else {
            payments = Double.valueOf(debtPaymentsEntry.getText().toString());
        }

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

    TextWatcher onChangeDebtAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtDateResult.setText(calcDebtDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebtPercent = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtDateResult.setText(calcDebtDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebtPayments = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtDateResult.setText(calcDebtDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public class DebtDbAdapter extends ArrayAdapter<DebtDb> {

        private Context context;
        private List<DebtDb> debts;

        private DebtDbAdapter(
                Context context,
                List<DebtDb> debts) {

            super(context, -1, debts);

            this.context = context;
            this.debts = debts;
        }

        public void updateDebts(List<DebtDb> debts) {
            this.debts = debts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return debts.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final DebtViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_debt,
                        parent, false);

                holder = new DebtViewHolder();
                holder.debtListName = convertView.findViewById(R.id.debtListName);
                holder.debtListAmount = convertView.findViewById(R.id.debtListAmount);
                holder.debtListFreeDate = convertView.findViewById(R.id.debtListFreeDate);
                holder.debtDeleted = convertView.findViewById(R.id.deleteDebtButton);
                holder.debtEdit = convertView.findViewById(R.id.editDebtButton);
                convertView.setTag(holder);

            } else {
                holder = (DebtViewHolder) convertView.getTag();
            }

            //retrieve debtName
            holder.debtListName.setText(debts.get(position).getDebtName());

            //retrieve debtAmount and format as currency
            try {
                debtAmountS = (String.valueOf(debts.get(position).getDebtAmount()));
                if (debtAmountS != null && !debtAmountS.equals("")) {
                    debtAmountD = Double.valueOf(debtAmountS);
                } else {
                    debtAmountD = 0.0;
                }
                debtAmount2 = currencyFormat.format(debtAmountD);
                holder.debtListAmount.setText(debtAmount2);
            } catch (NumberFormatException e) {
                holder.debtListAmount.setText(debtAmount2);
            }

            //retrieve debtEnd
            holder.debtListFreeDate.setText(debts.get(position).getDebtEnd());

            holder.debtDeleted.setTag(debts.get(position));
            holder.debtEdit.setTag(debts.get(position));

            //click on pencil icon
            holder.debtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_debt);
                    LayoutDebt.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    expenseBudgetDbManager = new ExpenseBudgetDbManager(getApplicationContext());

                    debtNameEntry = findViewById(R.id.debtNameEntry);
                    debtAmountEntry = findViewById(R.id.debtAmountEntry);
                    debtPercentEntry = findViewById(R.id.debtPercentEntry);
                    debtPaymentsEntry = findViewById(R.id.debtPaymentsEntry);
                    debtDateResult = findViewById(R.id.debtDateResult);

                    debtFrequencyRadioGroup = findViewById(R.id.debtFrequencyRadioGroup);

                    debtWeeklyRadioButton = findViewById(R.id.debtWeeklyRadioButton);
                    debtBiWeeklyRadioButton = findViewById(R.id.debtBiWeeklyRadioButton);
                    debtMonthlyRadioButton = findViewById(R.id.debtMonthlyRadioButton);

                    saveDebtButton = findViewById(R.id.saveDebtButton);
                    saveDebtButton.setVisibility(View.GONE);
                    updateDebtButton = findViewById(R.id.updateDebtButton);
                    cancelDebtButton = findViewById(R.id.cancelDebtButton);

                    debtDb = (DebtDb) holder.debtEdit.getTag();

                    debtNameEntry.setText(debtDb.getDebtName());

                    debtAmountEntry.setText(String.valueOf(debtDb.getDebtAmount()));
                    debtAmountEntry.addTextChangedListener(onChangeDebtAmount);

                    debtPercentEntry.setText(String.valueOf(debtDb.getDebtRate()));
                    debtPercentEntry.addTextChangedListener(onChangeDebtPercent);

                    debtPaymentsEntry.setText(String.valueOf(debtDb.getDebtPayments()));
                    debtPaymentsEntry.addTextChangedListener(onChangeDebtPayments);

                    //set radio button selections from data
                    if (debtDb.getDebtFrequency() == 52) {
                        debtWeeklyRadioButton.setChecked(true);
                        debtFrequencyS = "52";
                    } else if (debtDb.getDebtFrequency() == 26) {
                        debtBiWeeklyRadioButton.setChecked(true);
                        debtFrequencyS = "26";
                    } else if (debtDb.getDebtFrequency() == 12) {
                        debtMonthlyRadioButton.setChecked(true);
                        debtFrequencyS = "12";
                    }

                    debtDateResult.setText(calcDebtDate());

                    //update db if radio buttons changed
                    debtFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                    });

                    updateDebtButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String[] args = new String[]{String.valueOf(findExpenseId())};
                            ContentValues values = new ContentValues();

                            values.put(DbHelper.EXPENSENAME, debtNameEntry.getText().toString());
                            values.put(DbHelper.EXPENSEAMOUNT, Double.valueOf(debtPaymentsEntry.getText().toString()));
                            values.put(DbHelper.EXPENSEFREQUENCY, Double.valueOf(debtFrequencyS));

                            expenseAnnualAmount = Double.valueOf(debtPaymentsEntry.getText().toString()) * Double.valueOf(debtFrequencyS);

                            values.put(DbHelper.EXPENSEANNUALAMOUNT, expenseAnnualAmount);
                            values.put(DbHelper.EXPENSEAANNUALAMOUNT, expenseAnnualAmount);

                            expenseDb.update(DbHelper.EXPENSES_TABLE_NAME, values, DbHelper.ID + "=?", args);

                            debtDb.setDebtName(debtNameEntry.getText().toString());
                            debtDb.setDebtAmount(Double.valueOf(debtAmountEntry.getText().toString()));
                            debtDb.setDebtRate(Double.valueOf(debtPercentEntry.getText().toString()));
                            debtDb.setDebtPayments(Double.valueOf(debtPaymentsEntry.getText().toString()));
                            debtDb.setDebtFrequency(Double.valueOf(debtFrequencyS));

                            debtDbManager.updateDebt(debtDb);
                            debtAdapter.updateDebts(debtDbManager.getDebts());
                            notifyDataSetChanged();

                            Toast.makeText(getBaseContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            backToDebtScreen2 = new Intent(LayoutDebt.this, LayoutDebt.class);
                            backToDebtScreen2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDebtScreen2);

                            debtHeaderText();
                        }
                    });

                    cancelDebtButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToDebtScreen = new Intent(LayoutDebt.this, LayoutDebt.class);
                            backToDebtScreen.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDebtScreen);
                        }
                    });
                }
            });

            //click on trash can icon
            holder.debtDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    debtDb = (DebtDb) holder.debtDeleted.getTag();
                    debtDbManager.deleteDebt(debtDb);

                    try {
                        String[] args = new String[]{String.valueOf(findExpenseId())};
                        expenseDb.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
                    } catch (CursorIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    debtAdapter.updateDebts(debtDbManager.getDebts());
                    notifyDataSetChanged();

                    debtHeaderText();
                }
            });

            return convertView;
        }
    }

    private static class DebtViewHolder {
        public TextView debtListName;
        public TextView debtListAmount;
        public TextView debtListFreeDate;
        ImageButton debtDeleted;
        ImageButton debtEdit;
    }

    View.OnClickListener onClickAddDebtButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addNewDebt = new Intent(LayoutDebt.this, AddDebt.class);
            addNewDebt.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addNewDebt);
        }
    };
}
