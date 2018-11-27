package ca.gotchasomething.mynance.data;
//Db description

public class MoneyOutDb {
    private String moneyOutCat;
    private String moneyOutPriority;
    private Double moneyOutAmount;
    private String moneyOutCreatedOn;
    private String moneyOutCC;
    private long id;

    public MoneyOutDb(
            String moneyOutCat,
            String moneyOutPriority,
            Double moneyOutAmount,
            String moneyOutCreatedOn,
            String moneyOutCC,
            long id) {
        this.moneyOutCat = moneyOutCat;
        this.moneyOutPriority = moneyOutPriority;
        this.moneyOutAmount = moneyOutAmount;
        this.moneyOutCreatedOn = moneyOutCreatedOn;
        this.moneyOutCC = moneyOutCC;
        this.id = id;
    }

    public String getMoneyOutCat() { return moneyOutCat; }
    public void setMoneyOutCat(String moneyOutCat) { this.moneyOutCat = moneyOutCat; }

    public String getMoneyOutPriority() { return moneyOutPriority; }
    public void setMoneyOutPriority(String moneyOutPriority) { this.moneyOutPriority = moneyOutPriority; }

    public Double getMoneyOutAmount() { return moneyOutAmount; }
    public void setMoneyOutAmount(Double moneyOutAmount) { this.moneyOutAmount = moneyOutAmount; }

    public String getMoneyOutCreatedOn() { return moneyOutCreatedOn; }
    public void setMoneyOutCreatedOn(String moneyOutCreatedOn) { this.moneyOutCreatedOn = moneyOutCreatedOn; }

    public String getMoneyOutCC() { return moneyOutCC; }
    public void setMoneyOutCC(String moneyOutCC) { this.moneyOutCC = moneyOutCC; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() { return getMoneyOutCat(); }
}
