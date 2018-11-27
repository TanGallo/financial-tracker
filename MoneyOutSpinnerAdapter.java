package ca.gotchasomething.mynance.spinners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;

public class MoneyOutSpinnerAdapter extends CursorAdapter {


    TextView spinnerText;
    String expenseName;
    DbHelper moneyOutDbHelper;
    SQLiteDatabase moneyOutDbDb;
    Cursor moneyOutCursor;

    public MoneyOutSpinnerAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.spinner_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        spinnerText = view.findViewById(R.id.spinnerText);
        expenseName = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.EXPENSENAME));
        spinnerText.setText(expenseName);
    }


}
