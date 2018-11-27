package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;

public class MoneyOutDbManager {

    private DbHelper dbHelper;

    public MoneyOutDbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public List<MoneyOutDb> getMoneyOuts() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME, null);

        List<MoneyOutDb> moneyOuts = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                MoneyOutDb moneyOut = new MoneyOutDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                moneyOuts.add(moneyOut); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return moneyOuts;
    }

    public void addMoneyOut(MoneyOutDb moneyOut) {

        ContentValues newMoneyOut = new ContentValues();
        newMoneyOut.put(DbHelper.MONEYOUTCAT, moneyOut.getMoneyOutCat());
        newMoneyOut.put(DbHelper.MONEYOUTPRIORITY, moneyOut.getMoneyOutPriority());
        newMoneyOut.put(DbHelper.MONEYOUTAMOUNT, moneyOut.getMoneyOutAmount());
        newMoneyOut.put(DbHelper.MONEYOUTCREATEDON, moneyOut.getMoneyOutCreatedOn());
        newMoneyOut.put(DbHelper.MONEYOUTCC, moneyOut.getMoneyOutCC());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.MONEY_OUT_TABLE_NAME, null, newMoneyOut);
    }

    public void updateMoneyOut(MoneyOutDb moneyOut) {

        ContentValues updateMoneyOut = new ContentValues();
        updateMoneyOut.put(DbHelper.MONEYOUTCAT, moneyOut.getMoneyOutCat());
        updateMoneyOut.put(DbHelper.MONEYOUTPRIORITY, moneyOut.getMoneyOutPriority());
        updateMoneyOut.put(DbHelper.MONEYOUTAMOUNT, moneyOut.getMoneyOutAmount());
        updateMoneyOut.put(DbHelper.MONEYOUTCREATEDON, moneyOut.getMoneyOutCreatedOn());
        updateMoneyOut.put(DbHelper.MONEYOUTCC, moneyOut.getMoneyOutCC());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(moneyOut.getId())};

        db.update(
                DbHelper.MONEY_OUT_TABLE_NAME,
                updateMoneyOut,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteMoneyOut(MoneyOutDb moneyOut) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(moneyOut.getId())};

        db.delete(
                DbHelper.MONEY_OUT_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }

    public List<MoneyOutDb> getCCTrans() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " + DbHelper.MONEYOUTCC + " = 'Y'", null);

        List<MoneyOutDb> ccTrans = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                MoneyOutDb ccTransList = new MoneyOutDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                ccTrans.add(ccTransList); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return ccTrans;
    }
}
