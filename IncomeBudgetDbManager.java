package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;

public class IncomeBudgetDbManager {

    private DbHelper dbHelper;

    public IncomeBudgetDbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public List<IncomeBudgetDb> getIncomes() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.INCOME_TABLE_NAME + " ORDER BY " + DbHelper.INCOMEANNUALAMOUNT + " DESC",
                null);

        List<IncomeBudgetDb> incomes = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                IncomeBudgetDb income = new IncomeBudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.INCOMENAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.INCOMEAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.INCOMEFREQUENCY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.INCOMEANNUALAMOUNT)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                incomes.add(income); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return incomes;
    }

    public void addIncome(IncomeBudgetDb income) {

        ContentValues newIncome = new ContentValues();
        newIncome.put(DbHelper.INCOMENAME, income.getIncomeName());
        newIncome.put(DbHelper.INCOMEAMOUNT, income.getIncomeAmount());
        newIncome.put(DbHelper.INCOMEFREQUENCY, income.getIncomeFrequency());
        newIncome.put(DbHelper.INCOMEANNUALAMOUNT, income.getIncomeAnnualAmount());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.INCOME_TABLE_NAME, null, newIncome);
    }

    public void updateIncome(IncomeBudgetDb income) {

        ContentValues updateIncome = new ContentValues();
        updateIncome.put(DbHelper.INCOMENAME, income.getIncomeName());
        updateIncome.put(DbHelper.INCOMEAMOUNT, income.getIncomeAmount());
        updateIncome.put(DbHelper.INCOMEFREQUENCY, income.getIncomeFrequency());
        updateIncome.put(DbHelper.INCOMEANNUALAMOUNT, income.getIncomeAnnualAmount());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(income.getId())};

        db.update(
                DbHelper.INCOME_TABLE_NAME,
                updateIncome,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteIncome(IncomeBudgetDb income) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(income.getId())};

        db.delete(
                DbHelper.INCOME_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }
}
