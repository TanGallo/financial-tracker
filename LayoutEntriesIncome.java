package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.data.MoneyInDbManager;

public class LayoutEntriesIncome extends MainNavigation {

    ListView incomeEntriesListView;
    MoneyInDbManager moneyInDbManager;
    MoneyInAdapter moneyInAdapter;
    String moneyInS, moneyIn2;
    Double moneyInD, moneyInAmount;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    TextView incomeCatText;
    EditText incomeAmountEditText;
    Button cancelIncomeEntryButton, updateIncomeEntryButton;
    MoneyInDb moneyInDb;
    LinearLayout incomeEntriesEditHeader;
    Intent backToIncomeEntries, backToIncomeEntries2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_entries_income);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        incomeEntriesEditHeader = findViewById(R.id.incomeEntriesEditHeader);
        incomeEntriesEditHeader.setVisibility(View.GONE);
        incomeEntriesListView = findViewById(R.id.incomeEntriesListView);

        moneyInDbManager = new MoneyInDbManager(this);
        moneyInAdapter = new MoneyInAdapter(this, moneyInDbManager.getMoneyIns());
        incomeEntriesListView.setAdapter(moneyInAdapter);

    }

    public class MoneyInAdapter extends ArrayAdapter<MoneyInDb> {

        private Context context;
        private List<MoneyInDb> moneyIn;

        private MoneyInAdapter(
                Context context,
                List<MoneyInDb> moneyIn) {

            super(context, -1, moneyIn);

            this.context = context;
            this.moneyIn = moneyIn;
        }

        public void updateMoneyIn(List<MoneyInDb> moneyIn) {
            this.moneyIn = moneyIn;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyIn.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyInViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_entries_income,
                        parent, false);

                holder = new MoneyInViewHolder();
                holder.incomeEntriesDate = convertView.findViewById(R.id.incomeEntriesDate);
                holder.incomeEntriesCat = convertView.findViewById(R.id.incomeEntriesCat);
                holder.incomeEntriesAmount = convertView.findViewById(R.id.incomeEntriesAmount);
                holder.incomeEntriesEdit = convertView.findViewById(R.id.editIncomeEntriesButton);
                holder.incomeEntriesDelete = convertView.findViewById(R.id.deleteIncomeEntriesButton);
                convertView.setTag(holder);

            } else {
                holder = (MoneyInViewHolder) convertView.getTag();
            }

            //retrieve moneyInCreatedOn
            holder.incomeEntriesDate.setText(moneyIn.get(position).getMoneyInCreatedOn());

            //retrieve moneyInCat
            holder.incomeEntriesCat.setText(moneyIn.get(position).getMoneyInCat());

            //moneyInAmount and format as currency
            try {
                moneyInS = (String.valueOf(moneyIn.get(position).getMoneyInAmount()));
                if (moneyInS != null && !moneyInS.equals("")) {
                    moneyInD = Double.valueOf(moneyInS);
                } else {
                    moneyInD = 0.0;
                }
                moneyIn2 = currencyFormat.format(moneyInD);
                holder.incomeEntriesAmount.setText(moneyIn2);
            } catch (NumberFormatException e) {
                holder.incomeEntriesAmount.setText(moneyIn2);
            }

            holder.incomeEntriesDelete.setTag(moneyIn.get(position));
            holder.incomeEntriesEdit.setTag(moneyIn.get(position));

            //click on pencil icon
            holder.incomeEntriesEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutEntriesIncome.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    incomeEntriesEditHeader.setVisibility(View.VISIBLE);
                    incomeEntriesListView.setVisibility(View.GONE);

                    moneyInDbManager = new MoneyInDbManager(getApplicationContext());

                    incomeCatText = findViewById(R.id.incomeCatText);
                    incomeAmountEditText = findViewById(R.id.incomeAmountEditText);
                    cancelIncomeEntryButton = findViewById(R.id.cancelIncomeEntryButton);
                    updateIncomeEntryButton = findViewById(R.id.updateIncomeEntryButton);

                    moneyInDb = (MoneyInDb) holder.incomeEntriesEdit.getTag();

                    incomeCatText.setText(moneyInDb.getMoneyInCat());
                    incomeAmountEditText.setText(String.valueOf(moneyInDb.getMoneyInAmount()));

                    updateIncomeEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            moneyInDb.setMoneyInAmount(Double.valueOf(incomeAmountEditText.getText().toString()));

                            moneyInDbManager.updateMoneyIn(moneyInDb);
                            moneyInAdapter.updateMoneyIn(moneyInDbManager.getMoneyIns());
                            notifyDataSetChanged();

                            Toast.makeText(getBaseContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            incomeCatText.setVisibility(View.GONE);
                            incomeAmountEditText.setVisibility(View.GONE);
                            cancelIncomeEntryButton.setVisibility(View.GONE);
                            updateIncomeEntryButton.setVisibility(View.GONE);
                            incomeEntriesListView.setVisibility(View.VISIBLE);

                            backToIncomeEntries2 = new Intent(LayoutEntriesIncome.this, LayoutEntriesIncome.class);
                            backToIncomeEntries2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToIncomeEntries2);
                        }
                    });

                    cancelIncomeEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            incomeCatText.setVisibility(View.GONE);
                            incomeAmountEditText.setVisibility(View.GONE);
                            cancelIncomeEntryButton.setVisibility(View.GONE);
                            updateIncomeEntryButton.setVisibility(View.GONE);
                            incomeEntriesListView.setVisibility(View.VISIBLE);

                            backToIncomeEntries = new Intent(LayoutEntriesIncome.this, LayoutEntriesIncome.class);
                            backToIncomeEntries.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToIncomeEntries);
                        }
                    });
                }
            });

            //click on trash can icon
            holder.incomeEntriesDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyInDb = (MoneyInDb) holder.incomeEntriesDelete.getTag();
                    moneyInDbManager.deleteMoneyIn(moneyInDb);
                    moneyInAdapter.updateMoneyIn(moneyInDbManager.getMoneyIns());
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private static class MoneyInViewHolder {
        public TextView incomeEntriesDate;
        public TextView incomeEntriesCat;
        public TextView incomeEntriesAmount;
        ImageButton incomeEntriesEdit;
        ImageButton incomeEntriesDelete;
    }
}