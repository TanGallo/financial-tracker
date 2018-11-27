package ca.gotchasomething.mynance.data;
//Db description

public class SavingsDb {
    private String savingsName;
    private Double savingsAmount;
    private Double savingsRate;
    private Double savingsPayments;
    private Double savingsFrequency;
    private Double savingsGoal;
    private String savingsDate;
    private long expRefKeyS;
    private long id;

    public SavingsDb(
            String savingsName,
            Double savingsAmount,
            Double savingsRate,
            Double savingsPayments,
            Double savingsFrequency,
            Double savingsGoal,
            String savingsDate,
            long expRefKeyS,
            long id) {
        this.savingsName = savingsName;
        this.savingsAmount = savingsAmount;
        this.savingsRate = savingsRate;
        this.savingsPayments = savingsPayments;
        this.savingsFrequency = savingsFrequency;
        this.savingsGoal = savingsGoal;
        this.savingsDate = savingsDate;
        this.expRefKeyS = expRefKeyS;
        this.id = id;
    }

    public String getSavingsName() { return savingsName; }
    public void setSavingsName(String savingsName) { this.savingsName = savingsName; }

    public Double getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(Double savingsAmount) { this.savingsAmount = savingsAmount; }

    public Double getSavingsRate() { return savingsRate; }
    public void setSavingsRate(Double savingsRate) { this.savingsRate = savingsRate; }

    public Double getSavingsPayments() { return savingsPayments; }
    public void setSavingsPayments(Double savingsPayments) { this.savingsPayments = savingsPayments; }

    public Double getSavingsFrequency() { return savingsFrequency; }
    public void setSavingsFrequency(Double savingsFrequency) { this.savingsFrequency = savingsFrequency; }

    public Double getSavingsGoal() { return savingsGoal; }
    public void setSavingsGoal(Double savingsGoal) { this.savingsGoal = savingsGoal; }

    public String getSavingsDate() { return savingsDate; }
    public void setSavingsDate(String savingsDate) { this.savingsDate = savingsDate; }

    public long getExpRefKeyS() { return expRefKeyS; }
    public void setExpRefKeyS(long expRefKeyS) { this.expRefKeyS = expRefKeyS; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() { return getSavingsName(); }
}
