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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.SavingsDbManager;
import ca.gotchasomething.mynance.data.SetUpDb;
import ca.gotchasomething.mynance.data.SetUpDbManager;

public class LayoutSavings extends MainNavigation {

    ListView savingsListView;
    SavingsDbManager savingsDbManager;
    SavingsDbAdapter savingsAdapter;
    SavingsDb savingsDb;
    DbHelper setUpHelper, expenseDbHelper, savingsDbHelper;
    SQLiteDatabase expenseDb, savingsDbDb, setUpDbDb;
    Cursor expenseCursor, savingsCursor, setUpCursor;
    long id, idResult;
    ExpenseBudgetDbManager expenseBudgetDbManager;
    SetUpDbManager setUpDbManager;
    SetUpDb setUpDb;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    Double amount = 0.0, savingsGoal = 0.0, amount3 = 0.0, amount5 = 0.0, amount7 = 0.0, numberOfYearsToPayDebt = 0.0, expenseAmount = 0.0, expenseFrequency = 0.0,
            expenseAnnualAmount = 0.0, expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, totalSavings = 0.0, totalSavingsD = 0.0, savingsGoalD = 0.0,
            savingsCurrentD = 0.0, payment = 0.0, capital = 0.0, rate = 0.0, frequencyD = 0.0, a = 0.0, b = 0.0, payments = 0.0, frequency = 0.0,
            numberOfYearsToSavingsGoal = 0.0, balanceAmount = 0.0;
    String totalSavings2 = null, latestDate = null, amount2 = null, amount4 = null, amount6 = null, amount8 = null, totalSavingsS = null,
            savingsGoalS = null, savingsGoal2 = null, savingsCurrentS = null, savingsCurrent2 = null, amount3S = null, amount5S = null, amount7S = null, savingsName = null, savingsAmount = null,
            savingsRate = null, savingsPayments = null, savingsFrequency = null, savingsDate = null, savingsFrequencyS = null, expenseName = null,
            expensePriority = null, expenseWeekly = null;
    FloatingActionButton addSavingsButton;
    TextView totalSavedText, savingsListName, savingsListGoalAmount, savingsListDate, savingsListCurrentAmount, savingsDateResult;
    EditText savingsNameEntry, savingsAmountEntry, savingsPercentEntry, savingsPaymentsEntry, savingsGoalAmountEntry;
    RadioGroup savingsFrequencyRadioGroup;
    RadioButton savingsWeeklyRadioButton, savingsBiWeeklyRadioButton, savingsMonthlyRadioButton, savingsAnnuallyRadioButton;
    Button saveSavingsButton, updateSavingsButton, cancelSavingsButton, doneSavingsSetUpButton;
    Integer numberOfDaysToSavingsGoal = 0;
    Calendar savingsCal;
    Date savingsDateD, dateObj, latestDateD, latestDateD2;
    Intent addNewSavings, backToSavingsScreen, backToSavingsScreen2, backToSetUp;
    SimpleDateFormat savingsDateS, dateS, latestDateS;
    boolean success;
    int savingsDoneCheck = 0, debtsDone, savingsDone, budgetDone, balanceDone, tourDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_savings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        totalSavedText = findViewById(R.id.totalSavedText);

        savingsListView = findViewById(R.id.savingsListView);
        addSavingsButton = findViewById(R.id.addSavingsButton);
        addSavingsButton.setOnClickListener(onClickAddSavingsButton);

        doneSavingsSetUpButton = findViewById(R.id.doneSavingsSetUpButton);
        doneSavingsSetUpButton.setOnClickListener(onClickDoneSavingsSetUpButton);

        savingsSetUpCheck();
        if(savingsDoneCheck > 0) {
            doneSavingsSetUpButton.setVisibility(View.GONE);
        }

        setUpDbManager = new SetUpDbManager(this);
        savingsDbManager = new SavingsDbManager(this);
        savingsAdapter = new SavingsDbAdapter(this, savingsDbManager.getSavings());
        savingsListView.setAdapter(savingsAdapter);

        savingsHeaderText();
    }

    public int savingsSetUpCheck() {
        setUpHelper = new DbHelper(this);
        setUpDbDb = setUpHelper.getReadableDatabase();
        setUpCursor = setUpDbDb.rawQuery("SELECT max(savingsDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor.moveToFirst();
        savingsDoneCheck = setUpCursor.getInt(0);
        setUpCursor.close();

        return savingsDoneCheck;
    }

    View.OnClickListener onClickDoneSavingsSetUpButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savingsDone = 1;

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            setUpDbManager.addSetUp(setUpDb);

            Toast toast = Toast.makeText(getApplicationContext(), "You can edit this list by clicking SAVINGS on the menu", Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            backToSetUp = new Intent(LayoutSavings.this, LayoutSetUp.class);
            backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSetUp);

        }
    };

    public void savingsHeaderText() {

        try {
            totalSavingsS = String.valueOf(sumTotalSavings());
            if (totalSavingsS != null && !totalSavingsS.equals("")) {
                totalSavingsD = Double.valueOf(totalSavingsS);
            } else {
                totalSavingsD = 0.0;
            }

            totalSavings2 = currencyFormat.format(totalSavingsD);
            totalSavedText.setText(totalSavings2);

        } catch (NumberFormatException e) {
            totalSavedText.setText(totalSavings2);
        }
    }

    public Double sumTotalSavings() {
        savingsDbHelper = new DbHelper(this);
        savingsDbDb = savingsDbHelper.getReadableDatabase();
        savingsCursor = savingsDbDb.rawQuery("SELECT sum(savingsAmount)" + " FROM " + DbHelper.SAVINGS_TABLE_NAME, null);
        savingsCursor.moveToFirst();
        totalSavings = savingsCursor.getDouble(0);
        savingsCursor.close();

        return totalSavings;
    }

    public long findExpenseId() {
        expenseDbHelper = new DbHelper(this);
        expenseDb = expenseDbHelper.getReadableDatabase();
        expenseCursor = expenseDb.rawQuery("SELECT " + DbHelper.ID + " FROM " + DbHelper.EXPENSES_TABLE_NAME + " WHERE " + DbHelper.ID +
                " = '" + String.valueOf(savingsDb.getExpRefKeyS()) + "'", null);
        expenseCursor.moveToFirst();
        id = expenseCursor.getLong(0);
        expenseCursor.close();

        return id;
    }

    public String calcSavingsDate() {

        if(savingsGoalAmountEntry.getText().toString().equals("") || savingsAmountEntry.getText().toString().equals("")) {
            amount = 0.0;
        } else {
            amount = Double.valueOf(savingsGoalAmountEntry.getText().toString()) - Double.valueOf(savingsAmountEntry.getText().toString());
        }

        if(savingsPercentEntry.getText().toString().equals("")) {
            rate = 0.0;
        } else {
            rate = Double.valueOf(savingsPercentEntry.getText().toString());
        }

        if(savingsPaymentsEntry.getText().toString().equals("")) {
            payments = 0.0;
        } else {
            payments = Double.valueOf(savingsPaymentsEntry.getText().toString());
        }

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

    TextWatcher onChangeSavingsAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savingsDateResult.setText(calcSavingsDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSavingsPercent = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savingsDateResult.setText(calcSavingsDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSavingsPayments = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savingsDateResult.setText(calcSavingsDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSavingsGoal = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savingsDateResult.setText(calcSavingsDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public class SavingsDbAdapter extends ArrayAdapter<SavingsDb> {

        private Context context;
        private List<SavingsDb> savings;

        private SavingsDbAdapter(
                Context context,
                List<SavingsDb> savings) {

            super(context, -1, savings);

            this.context = context;
            this.savings = savings;
        }

        public void updateSavings(List<SavingsDb> savings) {
            this.savings = savings;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return savings.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final SavingsViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_savings,
                        parent, false);

                holder = new SavingsViewHolder();
                holder.savingsListName = convertView.findViewById(R.id.savingsListName);
                holder.savingsListGoalAmount = convertView.findViewById(R.id.savingsListGoalAmount);
                holder.savingsListDate = convertView.findViewById(R.id.savingsListDate);
                holder.savingsListCurrentLabel = convertView.findViewById(R.id.savingsListCurrentLabel);
                holder.savingsListCurrentAmount = convertView.findViewById(R.id.savingsListCurrentAmount);
                holder.savingsDeleted = convertView.findViewById(R.id.deleteSavingsButton);
                holder.savingsEdit = convertView.findViewById(R.id.editSavingsButton);
                convertView.setTag(holder);

            } else {
                holder = (SavingsViewHolder) convertView.getTag();
            }

            //retrieve savingsName
            holder.savingsListName.setText(savings.get(position).getSavingsName());

            //retrieve savingsGoal and format as currency
            try {
                savingsGoalS = (String.valueOf(savings.get(position).getSavingsGoal()));
                if (savingsGoalS != null && !savingsGoalS.equals("")) {
                    savingsGoalD = Double.valueOf(savingsGoalS);
                } else {
                    savingsGoalD = 0.0;
                }
                savingsGoal2 = currencyFormat.format(savingsGoalD);
                holder.savingsListGoalAmount.setText(savingsGoal2);
            } catch (NumberFormatException e) {
                holder.savingsListGoalAmount.setText(savingsGoal2);
            }

            //retrieve savingsDate
            holder.savingsListDate.setText(savings.get(position).getSavingsDate());

            //retrieve savingsAmount & format as currency
            try {
                savingsCurrentS = (String.valueOf(savings.get(position).getSavingsAmount()));
                if (savingsCurrentS != null && !savingsCurrentS.equals("")) {
                    savingsCurrentD = Double.valueOf(savingsCurrentS);
                } else {
                    savingsCurrentD = 0.0;
                }
                savingsCurrent2 = currencyFormat.format(savingsCurrentD);
                holder.savingsListCurrentAmount.setText(savingsCurrent2);
            } catch (NumberFormatException e) {
                holder.savingsListCurrentAmount.setText(savingsCurrent2);
            }

            holder.savingsDeleted.setTag(savings.get(position));
            holder.savingsEdit.setTag(savings.get(position));

            //click on pencil icon
            holder.savingsEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_savings);
                    LayoutSavings.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    expenseBudgetDbManager = new ExpenseBudgetDbManager(getApplicationContext());

                    savingsNameEntry = findViewById(R.id.savingsNameEntry);
                    savingsAmountEntry = findViewById(R.id.savingsAmountEntry);
                    savingsPercentEntry = findViewById(R.id.savingsPercentEntry);
                    savingsPaymentsEntry = findViewById(R.id.savingsPaymentsEntry);
                    savingsGoalAmountEntry = findViewById(R.id.savingsGoalAmountEntry);
                    savingsDateResult = findViewById(R.id.savingsDateResult);

                    savingsFrequencyRadioGroup = findViewById(R.id.savingsFrequencyRadioGroup);

                    savingsWeeklyRadioButton = findViewById(R.id.savingsWeeklyRadioButton);
                    savingsBiWeeklyRadioButton = findViewById(R.id.savingsBiWeeklyRadioButton);
                    savingsMonthlyRadioButton = findViewById(R.id.savingsMonthlyRadioButton);
                    savingsAnnuallyRadioButton = findViewById(R.id.savingsAnnuallyRadioButton);

                    saveSavingsButton = findViewById(R.id.saveSavingsButton);
                    saveSavingsButton.setVisibility(View.GONE);
                    updateSavingsButton = findViewById(R.id.updateSavingsButton);
                    cancelSavingsButton = findViewById(R.id.cancelSavingsButton);

                    savingsDb = (SavingsDb) holder.savingsEdit.getTag();

                    savingsNameEntry.setText(savingsDb.getSavingsName());

                    savingsAmountEntry.setText(String.valueOf(savingsDb.getSavingsAmount()));
                    savingsAmountEntry.addTextChangedListener(onChangeSavingsAmount);

                    savingsPercentEntry.setText(String.valueOf(savingsDb.getSavingsRate()));
                    savingsPercentEntry.addTextChangedListener(onChangeSavingsPercent);

                    savingsPaymentsEntry.setText(String.valueOf(savingsDb.getSavingsPayments()));
                    savingsPaymentsEntry.addTextChangedListener(onChangeSavingsPayments);

                    savingsGoalAmountEntry.setText(String.valueOf(savingsDb.getSavingsGoal()));
                    savingsGoalAmountEntry.addTextChangedListener(onChangeSavingsGoal);

                    //set radio button selections from data
                    if (savingsDb.getSavingsFrequency() == 52) {
                        savingsWeeklyRadioButton.setChecked(true);
                        savingsFrequencyS = "52";
                    } else if (savingsDb.getSavingsFrequency() == 26) {
                        savingsBiWeeklyRadioButton.setChecked(true);
                        savingsFrequencyS = "26";
                    } else if (savingsDb.getSavingsFrequency() == 12) {
                        savingsMonthlyRadioButton.setChecked(true);
                        savingsFrequencyS = "12";
                    } else if (savingsDb.getSavingsFrequency() == 1) {
                        savingsAnnuallyRadioButton.setChecked(true);
                        savingsFrequencyS = "1";
                    }

                    savingsDateResult.setText(calcSavingsDate());

                    //update db if radio buttons changed
                    savingsFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                    });

                    updateSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String[] args = new String[]{String.valueOf(findExpenseId())};
                            ContentValues values = new ContentValues();

                            values.put(DbHelper.EXPENSENAME, savingsNameEntry.getText().toString());
                            values.put(DbHelper.EXPENSEAMOUNT, Double.valueOf(savingsPaymentsEntry.getText().toString()));
                            values.put(DbHelper.EXPENSEFREQUENCY, Double.valueOf(savingsFrequencyS));

                            expenseAnnualAmount = Double.valueOf(savingsPaymentsEntry.getText().toString()) * Double.valueOf(savingsFrequencyS);

                            values.put(DbHelper.EXPENSEANNUALAMOUNT, expenseAnnualAmount);
                            values.put(DbHelper.EXPENSEAANNUALAMOUNT, expenseAnnualAmount);

                            expenseDb.update(DbHelper.EXPENSES_TABLE_NAME, values, DbHelper.ID + "=?", args);

                            savingsDb.setSavingsName(savingsNameEntry.getText().toString());
                            savingsDb.setSavingsAmount(Double.valueOf(savingsAmountEntry.getText().toString()));
                            savingsDb.setSavingsRate(Double.valueOf(savingsPercentEntry.getText().toString()));
                            savingsDb.setSavingsPayments(Double.valueOf(savingsPaymentsEntry.getText().toString()));
                            savingsDb.setSavingsFrequency(Double.valueOf(savingsFrequencyS));
                            savingsDb.setSavingsGoal(Double.valueOf(savingsGoalAmountEntry.getText().toString()));

                            savingsDbManager.updateSavings(savingsDb);
                            savingsAdapter.updateSavings(savingsDbManager.getSavings());
                            notifyDataSetChanged();

                            Toast.makeText(getBaseContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            backToSavingsScreen2 = new Intent(LayoutSavings.this, LayoutSavings.class);
                            backToSavingsScreen2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToSavingsScreen2);

                            savingsHeaderText();
                        }
                    });

                    cancelSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToSavingsScreen = new Intent(LayoutSavings.this, LayoutSavings.class);
                            backToSavingsScreen.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToSavingsScreen);
                        }
                    });
                }
            });

            //click on trash can icon
            holder.savingsDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    savingsDb = (SavingsDb) holder.savingsDeleted.getTag();
                    savingsDbManager.deleteSavings(savingsDb);

                    try {
                        String[] args = new String[]{String.valueOf(findExpenseId())};
                        expenseDb.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
                    } catch (CursorIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    savingsAdapter.updateSavings(savingsDbManager.getSavings());
                    notifyDataSetChanged();

                    savingsHeaderText();
                }
            });

            return convertView;
        }
    }

    private static class SavingsViewHolder {
        public TextView savingsListName;
        public TextView savingsListGoalAmount;
        public TextView savingsListDate;
        public TextView savingsListCurrentLabel;
        public TextView savingsListCurrentAmount;
        ImageButton savingsDeleted;
        ImageButton savingsEdit;
    }

    View.OnClickListener onClickAddSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addNewSavings = new Intent(LayoutSavings.this, AddSavings.class);
            addNewSavings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addNewSavings);
        }
    };
}
