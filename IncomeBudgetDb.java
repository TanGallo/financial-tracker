package ca.gotchasomething.mynance.data;
//Db description

public class IncomeBudgetDb {
    private String incomeName;
    private Double incomeAmount;
    private Double incomeFrequency;
    private Double incomeAnnualAmount;
    private long id;

    public IncomeBudgetDb(
            String incomeName,
            Double incomeAmount,
            Double incomeFrequency,
            Double incomeAnnualAmount,
            long id) {
        this.incomeName = incomeName;
        this.incomeAmount = incomeAmount;
        this.incomeFrequency = incomeFrequency;
        this.incomeAnnualAmount = incomeAnnualAmount;
        this.id = id;
    }

    public String getIncomeName() {
        return incomeName;
    }

    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
    }

    public Double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Double getIncomeFrequency() {
        return incomeFrequency;
    }

    public void setIncomeFrequency(Double incomeFrequency) {
        this.incomeFrequency = incomeFrequency;
    }

    public Double getIncomeAnnualAmount() {
        return incomeAnnualAmount;
    }

    public void setIncomeAnnualAmount(Double incomeAnnualAmount) {
        this.incomeAnnualAmount = incomeAnnualAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getIncomeName();
    }

}
