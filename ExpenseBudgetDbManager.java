package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;

public class ExpenseBudgetDbManager {

    private DbHelper dbHelper;

    public ExpenseBudgetDbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public List<ExpenseBudgetDb> getExpense() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " ORDER BY " + DbHelper.EXPENSEAANNUALAMOUNT + " DESC",
                null);

        List<ExpenseBudgetDb> expenses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                ExpenseBudgetDb expense = new ExpenseBudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSENAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEFREQUENCY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSEPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSEWEEKLY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEANNUALAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEAANNUALAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEBANNUALAMOUNT)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                expenses.add(expense); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return expenses;
    }

    public void addExpense(ExpenseBudgetDb expense) {

        ContentValues newExpense = new ContentValues();
        newExpense.put(DbHelper.EXPENSENAME, expense.getExpenseName());
        newExpense.put(DbHelper.EXPENSEAMOUNT, expense.getExpenseAmount());
        newExpense.put(DbHelper.EXPENSEFREQUENCY, expense.getExpenseFrequency());
        newExpense.put(DbHelper.EXPENSEPRIORITY, expense.getExpensePriority());
        newExpense.put(DbHelper.EXPENSEWEEKLY, expense.getExpenseWeekly());
        newExpense.put(DbHelper.EXPENSEANNUALAMOUNT, expense.getExpenseAnnualAmount());
        newExpense.put(DbHelper.EXPENSEAANNUALAMOUNT, expense.getExpenseAAnnualAmount());
        newExpense.put(DbHelper.EXPENSEBANNUALAMOUNT, expense.getExpenseBAnnualAmount());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertOrThrow(DbHelper.EXPENSES_TABLE_NAME, null, newExpense);

    }

    public void updateExpense(ExpenseBudgetDb expense) {

        ContentValues updateExpense = new ContentValues();
        updateExpense.put(DbHelper.EXPENSENAME, expense.getExpenseName());
        updateExpense.put(DbHelper.EXPENSEAMOUNT, expense.getExpenseAmount());
        updateExpense.put(DbHelper.EXPENSEFREQUENCY, expense.getExpenseFrequency());
        updateExpense.put(DbHelper.EXPENSEPRIORITY, expense.getExpensePriority());
        updateExpense.put(DbHelper.EXPENSEWEEKLY, expense.getExpenseWeekly());
        updateExpense.put(DbHelper.EXPENSEANNUALAMOUNT, expense.getExpenseAnnualAmount());
        updateExpense.put(DbHelper.EXPENSEAANNUALAMOUNT, expense.getExpenseAAnnualAmount());
        updateExpense.put(DbHelper.EXPENSEBANNUALAMOUNT, expense.getExpenseBAnnualAmount());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(expense.getId())};

        db.update(
                DbHelper.EXPENSES_TABLE_NAME,
                updateExpense,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteExpense(ExpenseBudgetDb expense) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(expense.getId())};

        db.delete(
                DbHelper.EXPENSES_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }

    public List<ExpenseBudgetDb> getWeeklyLimits() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " WHERE " + DbHelper.EXPENSEWEEKLY +
                " = 'Y'",
                null);

        List<ExpenseBudgetDb> weeklyLimits = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                ExpenseBudgetDb weekly = new ExpenseBudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSENAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEFREQUENCY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSEPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSEWEEKLY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEANNUALAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEAANNUALAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEBANNUALAMOUNT)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                weeklyLimits.add(weekly); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return weeklyLimits;
    }
}
