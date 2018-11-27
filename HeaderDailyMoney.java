package ca.gotchasomething.mynance;

public class HeaderDailyMoney extends LayoutDailyMoney {
    @Override
    public Double retrieveStartingBalance() {
        return super.retrieveStartingBalance();
    }

    @Override
    public Double retrieveIncomeTotal() {
        return super.retrieveIncomeTotal();
    }

    @Override
    public Double retrieveSpentFromAccountTotal() {
        return super.retrieveSpentFromAccountTotal();
    }

    @Override
    public Double retrieveBPercentage() {
        return super.retrieveBPercentage();
    }

    @Override
    public Double retrieveBSpent() {
        return super.retrieveBSpent();
    }

    @Override
    public void dailyHeaderText() {
        super.dailyHeaderText();
    }
}
