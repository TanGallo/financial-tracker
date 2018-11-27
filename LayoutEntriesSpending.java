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
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;

public class LayoutEntriesSpending extends MainNavigation {

    ListView spendingEntriesListView;
    MoneyOutDbManager moneyOutDbManager;
    MoneyOutAdapter moneyOutAdapter;
    String moneyOutS, moneyOut2;
    Double moneyOutD, moneyOutAmount;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    TextView spendingCatText;
    EditText spendingAmountEditText;
    Button cancelSpendingEntryButton, updateSpendingEntryButton;
    MoneyOutDb moneyOutDb;
    LinearLayout spendingEntriesEditHeader;
    Intent backToSpendingEntries, backToSpendingEntries2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_entries_spending);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        spendingEntriesEditHeader = findViewById(R.id.spendingEntriesEditHeader);
        spendingEntriesEditHeader.setVisibility(View.GONE);
        spendingEntriesListView = findViewById(R.id.spendingEntriesListView);

        moneyOutDbManager = new MoneyOutDbManager(this);
        moneyOutAdapter = new MoneyOutAdapter(this, moneyOutDbManager.getMoneyOuts());
        spendingEntriesListView.setAdapter(moneyOutAdapter);

    }

    public class MoneyOutAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> moneyOut;

        private MoneyOutAdapter(
                Context context,
                List<MoneyOutDb> moneyOut) {

            super(context, -1, moneyOut);

            this.context = context;
            this.moneyOut = moneyOut;
        }

        public void updateMoneyOut(List<MoneyOutDb> moneyOut) {
            this.moneyOut = moneyOut;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyOut.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyOutViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_entries_spending,
                        parent, false);

                holder = new MoneyOutViewHolder();
                holder.spendingEntriesDate = convertView.findViewById(R.id.spendingEntriesDate);
                holder.spendingEntriesCat = convertView.findViewById(R.id.spendingEntriesCat);
                holder.spendingEntriesAmount = convertView.findViewById(R.id.spendingEntriesAmount);
                holder.spendingEntriesEdit = convertView.findViewById(R.id.editSpendingEntriesButton);
                holder.spendingEntriesDelete = convertView.findViewById(R.id.deleteSpendingEntriesButton);
                convertView.setTag(holder);

            } else {
                holder = (MoneyOutViewHolder) convertView.getTag();
            }

            //retrieve moneyOutCreatedOn
            holder.spendingEntriesDate.setText(moneyOut.get(position).getMoneyOutCreatedOn());

            //retrieve moneyOutCat
            holder.spendingEntriesCat.setText(moneyOut.get(position).getMoneyOutCat());

            //moneyOutAmount and format as currency
            try {
                moneyOutS = (String.valueOf(moneyOut.get(position).getMoneyOutAmount()));
                if (moneyOutS != null && !moneyOutS.equals("")) {
                    moneyOutD = Double.valueOf(moneyOutS);
                } else {
                    moneyOutD = 0.0;
                }
                moneyOut2 = currencyFormat.format(moneyOutD);
                holder.spendingEntriesAmount.setText(moneyOut2);
            } catch (NumberFormatException e) {
                holder.spendingEntriesAmount.setText(moneyOut2);
            }

            holder.spendingEntriesDelete.setTag(moneyOut.get(position));
            holder.spendingEntriesEdit.setTag(moneyOut.get(position));

            //click on pencil icon
            holder.spendingEntriesEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutEntriesSpending.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    spendingEntriesEditHeader.setVisibility(View.VISIBLE);
                    spendingEntriesListView.setVisibility(View.GONE);

                    moneyOutDbManager = new MoneyOutDbManager(getApplicationContext());

                    spendingCatText = findViewById(R.id.spendingCatText);
                    spendingAmountEditText = findViewById(R.id.spendingAmountEditText);
                    cancelSpendingEntryButton = findViewById(R.id.cancelSpendingEntryButton);
                    updateSpendingEntryButton = findViewById(R.id.updateSpendingEntryButton);

                    moneyOutDb = (MoneyOutDb) holder.spendingEntriesEdit.getTag();

                    spendingCatText.setText(moneyOutDb.getMoneyOutCat());
                    spendingAmountEditText.setText(String.valueOf(moneyOutDb.getMoneyOutAmount()));

                    updateSpendingEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            moneyOutDb.setMoneyOutAmount(Double.valueOf(spendingAmountEditText.getText().toString()));

                            moneyOutDbManager.updateMoneyOut(moneyOutDb);
                            moneyOutAdapter.updateMoneyOut(moneyOutDbManager.getMoneyOuts());
                            notifyDataSetChanged();

                            Toast.makeText(getBaseContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            spendingCatText.setVisibility(View.GONE);
                            spendingAmountEditText.setVisibility(View.GONE);
                            cancelSpendingEntryButton.setVisibility(View.GONE);
                            updateSpendingEntryButton.setVisibility(View.GONE);
                            spendingEntriesListView.setVisibility(View.VISIBLE);

                            backToSpendingEntries2 = new Intent(LayoutEntriesSpending.this, LayoutEntriesSpending.class);
                            backToSpendingEntries2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToSpendingEntries2);
                        }
                    });

                    cancelSpendingEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            spendingCatText.setVisibility(View.GONE);
                            spendingAmountEditText.setVisibility(View.GONE);
                            cancelSpendingEntryButton.setVisibility(View.GONE);
                            updateSpendingEntryButton.setVisibility(View.GONE);
                            spendingEntriesListView.setVisibility(View.VISIBLE);

                            backToSpendingEntries = new Intent(LayoutEntriesSpending.this, LayoutEntriesSpending.class);
                            backToSpendingEntries.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToSpendingEntries);
                        }
                    });
                }
            });

            //click on trash can icon
            holder.spendingEntriesDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.spendingEntriesDelete.getTag();
                    moneyOutDbManager.deleteMoneyOut(moneyOutDb);
                    moneyOutAdapter.updateMoneyOut(moneyOutDbManager.getMoneyOuts());
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private static class MoneyOutViewHolder {
        public TextView spendingEntriesDate;
        public TextView spendingEntriesCat;
        public TextView spendingEntriesAmount;
        ImageButton spendingEntriesEdit;
        ImageButton spendingEntriesDelete;
    }
}