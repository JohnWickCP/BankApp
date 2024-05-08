package CHBank.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SavingsAccount extends Account {
    private final DoubleProperty withdrawalLimit;

    public SavingsAccount(String owner, String accountNumber, double balance , double withdrawalLimit) {
        super(owner, accountNumber, balance);
        this.withdrawalLimit = new SimpleDoubleProperty(withdrawalLimit);
    }

    public DoubleProperty withdrawalLimitProperty() {return withdrawalLimit;}
    public String toString(){
        return accountNumberProperty().get();
    }
}
