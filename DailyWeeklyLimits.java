package ca.gotchasomething.mynance.tabFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;

public class DailyWeeklyLimits extends Fragment {

    View v;
    ListView weeklyLimitListView;
    ExpenseBudgetDbManager expenseBudgetDbManager;
    WeeklyLimitsAdapter weeklyLimitsAdapter;

    public DailyWeeklyLimits() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_weekly_limits, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weeklyLimitListView = v.findViewById(R.id.weeklyLimitListView);

        expenseBudgetDbManager = new ExpenseBudgetDbManager(v.getContext());
        weeklyLimitsAdapter = new WeeklyLimitsAdapter(v.getContext(), expenseBudgetDbManager.getWeeklyLimits());
        weeklyLimitListView.setAdapter(weeklyLimitsAdapter);

    }

    //list adapter
    public class WeeklyLimitsAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        public Context context;
        public List<ExpenseBudgetDb> weeklyLimits;

        public WeeklyLimitsAdapter(
                Context context,
                List<ExpenseBudgetDb> weeklyLimits) {

            super(context, -1, weeklyLimits);

            this.context = context;
            this.weeklyLimits = weeklyLimits;
        }

        public void getWeeklyLimits(List<ExpenseBudgetDb> weeklyLimits) {
            this.weeklyLimits = weeklyLimits;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return weeklyLimits.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderWeeklyLimits holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(v.getContext()).inflate(
                        R.layout.fragment_list_daily_weekly_limits,
                        parent, false);

                holder = new ViewHolderWeeklyLimits();
                //put visual elements here and list them in ViewHolder class below
                holder.spendingCategory = convertView.findViewById(R.id.spendingCategory);
                holder.balanceRemaining = convertView.findViewById(R.id.balanceRemaining);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolderWeeklyLimits) convertView.getTag();
            }

            //retrieve spendingCategory
            holder.spendingCategory.setText(weeklyLimits.get(position).getExpenseName());

            //get balance remaining data



            return convertView;
        }
    }

    private static class ViewHolderWeeklyLimits {
        private TextView spendingCategory;
        private TextView balanceRemaining;
    }
}
