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

public class MoneyInDbManager {

    private DbHelper dbHelper;

    public MoneyInDbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public List<MoneyInDb> getMoneyIns() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.MONEY_IN_TABLE_NAME, null);

        List<MoneyInDb> moneyIns = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                MoneyInDb moneyIn = new MoneyInDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYINCAT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYINAMOUNT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYINCREATEDON)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                moneyIns.add(0, moneyIn); //adds new items to beginning of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return moneyIns;
    }

    public void addMoneyIn(MoneyInDb moneyIn) {

        ContentValues newMoneyIn = new ContentValues();
        newMoneyIn.put(DbHelper.MONEYINCAT, moneyIn.getMoneyInCat());
        newMoneyIn.put(DbHelper.MONEYINAMOUNT, moneyIn.getMoneyInAmount());
        newMoneyIn.put(DbHelper.MONEYINCREATEDON, moneyIn.getMoneyInCreatedOn());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.MONEY_IN_TABLE_NAME, null, newMoneyIn);
    }

    public void updateMoneyIn(MoneyInDb moneyIn) {

        ContentValues updateMoneyIn = new ContentValues();
        updateMoneyIn.put(DbHelper.MONEYINCAT, moneyIn.getMoneyInCat());
        updateMoneyIn.put(DbHelper.MONEYINAMOUNT, moneyIn.getMoneyInAmount());
        updateMoneyIn.put(DbHelper.MONEYINCREATEDON, moneyIn.getMoneyInCreatedOn());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(moneyIn.getId())};

        db.update(
                DbHelper.MONEY_IN_TABLE_NAME,
                updateMoneyIn,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteMoneyIn(MoneyInDb moneyIn) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(moneyIn.getId())};

        db.delete(
                DbHelper.MONEY_IN_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }
}
