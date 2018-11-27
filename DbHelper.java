package ca.gotchasomething.mynance;
//DbHelper

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Mynance.db";
    private static final int DATABASE_VERSION = 1;

    public static final String ID = "_id";

    public static final String INCOME_TABLE_NAME = "incomes";
    public static final String INCOMENAME = "incomeName";
    public static final String INCOMEAMOUNT = "incomeAmount";
    public static final String INCOMEFREQUENCY = "incomeFrequency";
    public static final String INCOMEANNUALAMOUNT = "incomeAnnualAmount";

    public static final String EXPENSES_TABLE_NAME = "expenses";
    public static final String EXPENSENAME = "expenseName";
    public static final String EXPENSEAMOUNT = "expenseAmount";
    public static final String EXPENSEFREQUENCY = "expenseFrequency";
    public static final String EXPENSEPRIORITY = "expensePriority";
    public static final String EXPENSEWEEKLY = "expenseWeekly";
    public static final String EXPENSEANNUALAMOUNT = "expenseAnnualAmount";
    public static final String EXPENSEAANNUALAMOUNT = "expenseAAnnualAmount";
    public static final String EXPENSEBANNUALAMOUNT = "expenseBAnnualAmount";

    public static final String DEBTS_TABLE_NAME = "debts";
    public static final String DEBTNAME = "debtName";
    public static final String DEBTAMOUNT = "debtAmount";
    public static final String DEBTRATE = "debtRate";
    public static final String DEBTPAYMENTS = "debtPayments";
    public static final String DEBTFREQUENCY = "debtFrequency";
    public static final String DEBTEND = "debtEnd";
    public static final String EXPREFKEYD = "expRefKeyD";

    public static final String SAVINGS_TABLE_NAME = "savings";
    public static final String SAVINGSNAME = "savingsName";
    public static final String SAVINGSAMOUNT = "savingsAmount";
    public static final String SAVINGSRATE = "savingsRate";
    public static final String SAVINGSPAYMENTS = "savingsPayments";
    public static final String SAVINGSFREQUENCY = "savingsFrequency";
    public static final String SAVINGSGOAL = "savingsGoal";
    public static final String SAVINGSDATE = "savingsDate";
    public static final String EXPREFKEYS = "expRefKeyS";

    public static final String MONEY_IN_TABLE_NAME = "moneyIn";
    public static final String MONEYINCAT = "moneyInCat";
    public static final String MONEYINAMOUNT = "moneyInAmount";
    public static final String MONEYINCREATEDON = "moneyInCreatedOn";

    public static final String MONEY_OUT_TABLE_NAME = "moneyOut";
    public static final String MONEYOUTCAT = "moneyOutCat";
    public static final String MONEYOUTPRIORITY= "moneyOutPriority";
    public static final String MONEYOUTAMOUNT = "moneyOutAmount";
    public static final String MONEYOUTCREATEDON = "moneyOutCreatedOn";
    public static final String MONEYOUTCC = "moneyOutCC";

    public static final String SET_UP_TABLE_NAME = "setUp";
    public static final String DEBTSDONE = "debtsDone";
    public static final String SAVINGSDONE = "savingsDone";
    public static final String BUDGETDONE = "budgetDone";
    public static final String BALANCEDONE = "balanceDone";
    public static final String BALANCEAMOUNT = "balanceAmount";
    public static final String TOURDONE = "tourDone";

    //singleton pattern
    public static DbHelper instance = null;

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    } //end of singleton pattern

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String createExpensesQuery = "CREATE TABLE " + EXPENSES_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " expenseName TEXT," +
            " expenseAmount REAL," +
            " expenseFrequency REAL," +
            " expensePriority TEXT," +
            " expenseWeekly TEXT," +
            " expenseAnnualAmount REAL NOT NULL," +
            " expenseAAnnualAmount REAL NOT NULL," +
            " expenseBAnnualAmount REAL NOT NULL)";// +
            //" CONSTRAINT unique_name UNIQUE (expenseName))";

    private static final String createIncomesQuery = "CREATE TABLE " + INCOME_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " incomeName TEXT," +
            " incomeAmount REAL," +
            " incomeFrequency REAL," +
            " incomeAnnualAmount REAL NOT NULL)";

    private static final String createDebtsQuery = "CREATE TABLE " + DEBTS_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " debtName TEXT," +
            " debtAmount REAL," +
            " debtRate REAL," +
            " debtPayments REAL," +
            " debtFrequency REAL," +
            " debtEnd TEXT," +
            " expRefKeyD INTEGER)";

    private static final String createSavingsQuery = "CREATE TABLE " + SAVINGS_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " savingsName TEXT," +
            " savingsAmount REAL," +
            " savingsRate REAL," +
            " savingsPayments REAL," +
            " savingsFrequency REAL," +
            " savingsGoal REAL," +
            " savingsDate TEXT," +
            " expRefKeyS INTEGER)";

    private static final String createMoneyInQuery = "CREATE TABLE " + MONEY_IN_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " moneyInCat TEXT," +
            " moneyInAmount REAL," +
            " moneyInCreatedOn TEXT)";

    private static final String createMoneyOutQuery = "CREATE TABLE " + MONEY_OUT_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " moneyOutCat TEXT," +
            " moneyOutPriority TEXT," +
            " moneyOutAmount REAL," +
            " moneyOutCreatedOn TEXT," +
            " moneyOutCC TEXT)";

    private static final String createSetUpQuery = "CREATE TABLE " + SET_UP_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " debtsDone INTEGER," +
            " savingsDone INTEGER," +
            " budgetDone INTEGER," +
            " balanceDone INTEGER," +
            " balanceAmount REAL," +
            " tourDone INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createExpensesQuery);
        db.execSQL(createIncomesQuery);
        db.execSQL(createDebtsQuery);
        db.execSQL(createSavingsQuery);
        db.execSQL(createMoneyInQuery);
        db.execSQL(createMoneyOutQuery);
        db.execSQL(createSetUpQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
