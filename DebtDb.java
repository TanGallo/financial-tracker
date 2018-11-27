package ca.gotchasomething.mynance.data;
//Db description

public class DebtDb {
    private String debtName;
    private Double debtAmount;
    private Double debtRate;
    private Double debtPayments;
    private Double debtFrequency;
    private String debtEnd;
    private long expRefKeyD;
    private long id;

    public DebtDb (
            String debtName,
            Double debtAmount,
            Double debtRate,
            Double debtPayments,
            Double debtFrequency,
            String debtEnd,
            long expRefKeyD,
            long id) {
        this.debtName = debtName;
        this.debtAmount = debtAmount;
        this.debtRate = debtRate;
        this.debtPayments = debtPayments;
        this.debtFrequency = debtFrequency;
        this.debtEnd = debtEnd;
        this.expRefKeyD = expRefKeyD;
        this.id = id;
    }

    public String getDebtName() { return debtName; }
    public void setDebtName(String debtName) { this.debtName = debtName; }

    public Double getDebtAmount() { return debtAmount; }
    public void setDebtAmount(Double debtAmount) { this.debtAmount = debtAmount; }

    public Double getDebtRate() { return debtRate; }
    public void setDebtRate(Double debtRate) { this.debtRate = debtRate; }

    public Double getDebtPayments() { return debtPayments; }
    public void setDebtPayments(Double debtPayments) { this.debtPayments = debtPayments; }

    public Double getDebtFrequency() { return debtFrequency; }
    public void setDebtFrequency(Double debtFrequency) { this.debtFrequency = debtFrequency; }

    public String getDebtEnd() { return debtEnd; }
    public void setDebtEnd(String debtEnd) { this.debtEnd = debtEnd; }

    public long getExpRefKeyD() { return expRefKeyD; }
    public void setExpRefKeyD(long expRefKeyD) { this.expRefKeyD = expRefKeyD; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() { return getDebtName(); }
}
