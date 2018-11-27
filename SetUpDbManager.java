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

public class SetUpDbManager {

    private DbHelper dbHelper;

    public SetUpDbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public List<SetUpDb> getSetUp() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.SET_UP_TABLE_NAME, null);

        List<SetUpDb> setUp = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                SetUpDb setUps = new SetUpDb(
                        cursor.getInt(cursor.getColumnIndex(DbHelper.DEBTSDONE)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.SAVINGSDONE)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.BUDGETDONE)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.BALANCEDONE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BALANCEAMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.TOURDONE)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                setUp.add(setUps); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return setUp;
    }

    public void addSetUp(SetUpDb setUp) {

        ContentValues newSetUp = new ContentValues();
        newSetUp.put(DbHelper.DEBTSDONE, setUp.getDebtsDone());
        newSetUp.put(DbHelper.SAVINGSDONE, setUp.getSavingsDone());
        newSetUp.put(DbHelper.BUDGETDONE, setUp.getBudgetDone());
        newSetUp.put(DbHelper.BALANCEDONE, setUp.getBalanceDone());
        newSetUp.put(DbHelper.BALANCEAMOUNT, setUp.getBalanceAmount());
        newSetUp.put(DbHelper.TOURDONE, setUp.getTourDone());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.SET_UP_TABLE_NAME, null, newSetUp);
    }

    public void updateSetUp(SetUpDb setUp) {

        ContentValues updateSetUp = new ContentValues();
        updateSetUp.put(DbHelper.DEBTSDONE, setUp.getDebtsDone());
        updateSetUp.put(DbHelper.SAVINGSDONE, setUp.getSavingsDone());
        updateSetUp.put(DbHelper.BUDGETDONE, setUp.getBudgetDone());
        updateSetUp.put(DbHelper.BALANCEDONE, setUp.getBalanceDone());
        updateSetUp.put(DbHelper.BALANCEAMOUNT, setUp.getBalanceAmount());
        updateSetUp.put(DbHelper.TOURDONE, setUp.getTourDone());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(setUp.getId())};

        db.update(
                DbHelper.SET_UP_TABLE_NAME,
                updateSetUp,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteSetUp(SetUpDb setUp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{String.valueOf(setUp.getId())};

        db.delete(
                DbHelper.SET_UP_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }
}
