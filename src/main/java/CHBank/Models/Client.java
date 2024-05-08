package CHBank.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Client {
    private final StringProperty fName;
    private final StringProperty lName;
    private final StringProperty pAddress;
    private final ObjectProperty<CheckingAccount> checkingAccount;
    private final ObjectProperty<SavingsAccount> savingsAccount;
    private final ObjectProperty<LocalDate> dateCreated;

    public Client(String fName, String lName, String pAddress, CheckingAccount checkingAccount, SavingsAccount savingsAccount, LocalDate dateCreated) {
        this.fName = new SimpleStringProperty(fName);
        this.lName = new SimpleStringProperty(lName);
        this.pAddress = new SimpleStringProperty(pAddress);
        this.checkingAccount = new SimpleObjectProperty<>(checkingAccount);
        this.savingsAccount = new SimpleObjectProperty<>(savingsAccount);
        this.dateCreated = new SimpleObjectProperty<>(dateCreated);
    }

    public StringProperty fNameProperty() {return fName;}
    public StringProperty lNameProperty() {return lName;}
    public StringProperty pAddressProperty() {return pAddress;}
    public ObjectProperty<CheckingAccount> checkingAccountProperty() {return checkingAccount;}
    public ObjectProperty<SavingsAccount> savingsAccountProperty() {return savingsAccount;}
    public ObjectProperty<LocalDate> dateCreatedProperty() {return dateCreated;}

}
