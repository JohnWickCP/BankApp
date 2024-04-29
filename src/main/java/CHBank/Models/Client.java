package CHBank.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Client {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty pAddress;
    private final ObjectProperty<Account> checkingAccount;
    private final ObjectProperty<Account> savingAccount;
    private final ObjectProperty<LocalDate> dateCreated;

    public Client(String firstName, String lastName, String pAddress, Account checkingAccount, Account savingAccount, LocalDate dateCreated) {
        this.firstName = new SimpleStringProperty(this, "FirstName", firstName);
        this.lastName = new SimpleStringProperty(this, "LastName", lastName);
        this.pAddress = new SimpleStringProperty(this, "Payee Address", pAddress);
        this.savingAccount = new SimpleObjectProperty<>(this, "Saving Account", savingAccount);
        this.checkingAccount = new SimpleObjectProperty<>(this, "Checking Account", checkingAccount);
        this.dateCreated = new SimpleObjectProperty<>(this, "Date Created", dateCreated);
    }

    public StringProperty FirstName() {return firstName;}
    public StringProperty LastName() {return lastName;}
    public StringProperty pAddress() {return pAddress;}
    public ObjectProperty<Account> CheckingAccount() {return checkingAccount;}
    public ObjectProperty<Account> SavingAccount() {return savingAccount;}
    public ObjectProperty<LocalDate> DateCreated() {return dateCreated;}
}
