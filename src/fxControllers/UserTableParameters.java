package fxControllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserTableParameters {
    private SimpleIntegerProperty userId = new SimpleIntegerProperty();
    private SimpleStringProperty login = new SimpleStringProperty();
    private SimpleStringProperty dateCreated = new SimpleStringProperty();
    private SimpleStringProperty dateModified = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty surname = new SimpleStringProperty();
    private SimpleStringProperty role = new SimpleStringProperty();
    private SimpleStringProperty companyTitle = new SimpleStringProperty();
    private SimpleStringProperty address = new SimpleStringProperty();
    private SimpleStringProperty phoneNum = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();

    public UserTableParameters() {
    }

    public UserTableParameters(SimpleIntegerProperty userId, SimpleStringProperty login, SimpleStringProperty dateCreated, SimpleStringProperty dateModified, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty role, SimpleStringProperty companyTitle, SimpleStringProperty address, SimpleStringProperty phoneNum) {
        this.userId = userId;
        this.login = login;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.companyTitle = companyTitle;
        this.address = address;
        this.phoneNum = phoneNum;
    }

    public int getUserId() {
        return userId.get();
    }

    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public SimpleStringProperty dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public String getDateModified() {
        return dateModified.get();
    }

    public SimpleStringProperty dateModifiedProperty() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified.set(dateModified);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getRole() {
        return role.get();
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public String getCompanyTitle() {
        return companyTitle.get();
    }

    public SimpleStringProperty companyTitleProperty() {
        return companyTitle;
    }

    public void setCompanyTitle(String companyTitle) {
        this.companyTitle.set(companyTitle);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhoneNum() {
        return phoneNum.get();
    }

    public SimpleStringProperty phoneNumProperty() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum.set(phoneNum);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
}
