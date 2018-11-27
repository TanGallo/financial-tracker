package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;

public class SavingsDbManager {

    private DbHelper dbHelper;
    Double numberOfYearsToSavingsGoal = 0.0;
    Integer numberOfDaysToSavingsGoal = 0;
    Calendar savingsCal;
    Date savingsDateD;
    SimpleDateFormat savingsDateS;
    String savingsDate = null;

    public SavingsDbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public List<SavingsDb> getSavings() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.SAVINGS_TABLE_NAME + " ORDER BY " + DbHelper.SAVINGSGOAL + " DESC",
                null);

        List<SavingsDb> savings = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                SavingsDb saving = new SavingsDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.SAVINGSNAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSRATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSPAYMENTS)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSFREQUENCY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSGOAL)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.SAVINGSDATE)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYS)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                savings.add(saving); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return savings;
    }

    public void addSavings(SavingsDb saving) {

        ContentValues newSavings = new ContentValues();
        newSavings.put(DbHelper.SAVINGSNAME, saving.getSavingsName());
        newSavings.put(DbHelper.SAVINGSAMOUNT, saving.getSavingsAmount());
        newSavings.put(DbHelper.SAVINGSRATE, saving.getSavingsRate());
        newSavings.put(DbHelper.SAVINGSPAYMENTS, saving.getSavingsPayments());
        newSavings.put(DbHelper.SAVINGSFREQUENCY, saving.getSavingsFrequency());
        newSavings.put(DbHelper.SAVINGSGOAL, saving.getSavingsGoal());
        newSavings.put(DbHelper.SAVINGSDATE, savingsEndDate(saving));
        newSavings.put(DbHelper.EXPREFKEYS, saving.getExpRefKeyS());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.SAVINGS_TABLE_NAME, null, newSavings);
    }

    public void updateSavings(SavingsDb saving) {

        ContentValues updateSaving = new ContentValues();
        updateSaving.put(DbHelper.SAVINGSNAME, saving.getSavingsName());
        updateSaving.put(DbHelper.SAVINGSAMOUNT, saving.getSavingsAmount());
        updateSaving.put(DbHelper.SAVINGSRATE, saving.getSavingsRate());
        updateSaving.put(DbHelper.SAVINGSPAYMENTS, saving.getSavingsPayments());
        updateSaving.put(DbHelper.SAVINGSFREQUENCY, saving.getSavingsFrequency());
        updateSaving.put(DbHelper.SAVINGSGOAL, saving.getSavingsGoal());
        updateSaving.put(DbHelper.SAVINGSDATE, savingsEndDate(saving));
        updateSaving.put(DbHelper.EXPREFKEYS, saving.getExpRefKeyS());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(saving.getId())};

        db.update(
                DbHelper.SAVINGS_TABLE_NAME,
                updateSaving,
                DbHelper.ID + "=?",
                args);
    }

    public String savingsEndDate(SavingsDb saving) {

        savingsCal = Calendar.getInstance();
        //years = -(Math.log(1 - (amount * (rate / 100) / (payments * frequency))) / (frequency * Math.log(1 + ((rate / 100) / frequency))))
        numberOfYearsToSavingsGoal = -(Math.log(1 - ((saving.getSavingsGoal() - saving.getSavingsAmount()) * (saving.getSavingsRate() / 100) /
                (saving.getSavingsPayments() * saving.getSavingsFrequency()))) / (saving.getSavingsFrequency() *
                Math.log(1 + ((saving.getSavingsRate() / 100) / saving.getSavingsFrequency()))));
        numberOfDaysToSavingsGoal = (int) Math.round(numberOfYearsToSavingsGoal * 365);

        if (saving.getSavingsGoal() - saving.getSavingsAmount() <= 0) {
            savingsDate = "Goal_achieved!";

        } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE || numberOfDaysToSavingsGoal <= 0) {
            savingsDate = "Too far in the future";

        } else {

            savingsCal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
            savingsDateD = savingsCal.getTime();
            savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
            savingsDate = "Will be saved by " + savingsDateS.format(savingsDateD);
        }

        return savingsDate;
    }

    public void deleteSavings(SavingsDb saving) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(saving.getId())};

        db.delete(
                DbHelper.SAVINGS_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }
}
