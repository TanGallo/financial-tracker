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

public class DebtDbManager {

    private DbHelper dbHelper;
    Double numberOfYearsToPayDebt = 0.0;
    Integer numberOfDaysToPayDebt = 0;
    Calendar cal;
    Date debtEndD;
    SimpleDateFormat debtEndS;
    String debtEnd = null;

    public DebtDbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public List<DebtDb> getDebts() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.DEBTS_TABLE_NAME + " ORDER BY " + DbHelper.DEBTRATE + " DESC",
                null);

        List<DebtDb> debts = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                DebtDb debt = new DebtDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.DEBTNAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTRATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTPAYMENTS)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTFREQUENCY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.DEBTEND)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYD)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                debts.add(debt); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return debts;
    }

    public void addDebt(DebtDb debt) {

        ContentValues newDebt = new ContentValues();
        newDebt.put(DbHelper.DEBTNAME, debt.getDebtName());
        newDebt.put(DbHelper.DEBTAMOUNT, debt.getDebtAmount());
        newDebt.put(DbHelper.DEBTRATE, debt.getDebtRate());
        newDebt.put(DbHelper.DEBTPAYMENTS, debt.getDebtPayments());
        newDebt.put(DbHelper.DEBTFREQUENCY, debt.getDebtFrequency());
        newDebt.put(DbHelper.DEBTEND, debtEndDate(debt));
        newDebt.put(DbHelper.EXPREFKEYD, debt.getExpRefKeyD());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.DEBTS_TABLE_NAME, null, newDebt);
    }

    public void updateDebt(DebtDb debt) {

        ContentValues updateDebt = new ContentValues();
        updateDebt.put(DbHelper.DEBTNAME, debt.getDebtName());
        updateDebt.put(DbHelper.DEBTAMOUNT, debt.getDebtAmount());
        updateDebt.put(DbHelper.DEBTRATE, debt.getDebtRate());
        updateDebt.put(DbHelper.DEBTPAYMENTS, debt.getDebtPayments());
        updateDebt.put(DbHelper.DEBTFREQUENCY, debt.getDebtFrequency());
        updateDebt.put(DbHelper.DEBTEND, debtEndDate(debt));
        updateDebt.put(DbHelper.EXPREFKEYD, debt.getExpRefKeyD());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(debt.getId())};

        db.update(
                DbHelper.DEBTS_TABLE_NAME,
                updateDebt,
                DbHelper.ID + "=?",
                args);
    }

    public String debtEndDate(DebtDb debt) {

        cal = Calendar.getInstance();
        //years = -(Math.log(1 - (amount * (rate / 100) / (payments * frequency))) / (frequency * Math.log(1 + ((rate / 100) / frequency))))
        numberOfYearsToPayDebt = -(Math.log(1 - (debt.getDebtAmount() * (debt.getDebtRate() / 100) /
                (debt.getDebtPayments() * debt.getDebtFrequency()))) / (debt.getDebtFrequency() *
                Math.log(1 + ((debt.getDebtRate() / 100) / debt.getDebtFrequency()))));
        numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);

        if (debt.getDebtAmount() <= 0) {
            debtEnd = "Debt paid!";

        } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfDaysToPayDebt <= 0) {
            debtEnd = "Too far in the future";

        } else {
            cal.add(Calendar.DATE, numberOfDaysToPayDebt);
            debtEndD = cal.getTime();
            debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
            debtEnd = "Will be paid by " + debtEndS.format(debtEndD);
        }

        return debtEnd;
    }

    public void deleteDebt(DebtDb debt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(debt.getId())};

        db.delete(
                DbHelper.DEBTS_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }
}
