package fxControllers;

import hibernateControllers.UserHibernateCtrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import users.Company;
import users.Person;
import users.Role;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import static incorrectDataControl.IncorrectDataControl.*;

public class SignUpWindow implements Initializable {
    @FXML
    public TextField loginF;
    @FXML
    public PasswordField passwordF;
    @FXML
    public PasswordField passwordRepeatF;
    @FXML
    public TextField phoneNumF;
    @FXML
    public RadioButton personRadio;
    @FXML
    public ToggleGroup userType;
    @FXML
    public RadioButton companyRadio;
    @FXML
    public TextField personNameF;
    @FXML
    public TextField personSurnameF;
    @FXML
    public TextField companyTitleF;
    @FXML
    public TextField companyAddressF;


    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    UserHibernateCtrl userHibernateCtrl = new UserHibernateCtrl(entityManagerFactory);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableFields();
    }

    public void saveAndReturn(ActionEvent actionEvent) throws SQLException, IOException {

        if(loginF.getText().isEmpty()){
            alertMsg("Login field is empty!","Enter login!");
        }

        else if(userHibernateCtrl.getUserByLogin(loginF.getText()) != null) {
            alertMsg("User with entered login already exists!","Enter other login.");
        }

        else if (loginF.getText().length()>20){
            alertMsg("Login is too long!","Max allowed length is 20.");
        }

        else if (passwordF.getText().isEmpty()){
            alertMsg("Password field is empty!","Enter password!");
        }

        else if (passwordF.getText().length()>20){
            alertMsg("Password is too long!","Max allowed length is 20.");
        }

        else if (!passwordRepeatF.getText().equals(passwordF.getText())){
            alertMsg("Repeated password doesn't match!","Enter the same password!");
        }

        else if (phoneNumF.getText().isEmpty()){
            alertMsg("Phone number field is empty!","Enter phone number!");
        }

        else if(phoneNumF.getText().length()!=8){
            alertMsg("Incorrect length.","Enter 8 digits!");
        }

        else if(containsCharactersInteger(phoneNumF.getText())){
           alertMsg("Phone number contains characters!","Remove all characters!");
       }

        else if (personNameF.getText().isEmpty() && personRadio.isSelected()){
            alertMsg("Name field is empty!","Enter name!");
        }

        else if (personNameF.getText().length()>20){
            alertMsg("Name is too long!","Max allowed length is 20.");
        }

        else if(containsDigits(personNameF.getText())){
            alertMsg("Name contains digits!","Remove digits!");
        }

        else if (personSurnameF.getText().isEmpty() && personRadio.isSelected()){
            alertMsg("Surname field is empty!","Enter surname!");
        }

        else if (personSurnameF.getText().length()>20){
            alertMsg("Surname is too long!","Max allowed length is 20.");
        }

        else if(containsDigits(personSurnameF.getText())){
            alertMsg("Surname contains digits!","Remove digits!");
        }

        else if (companyTitleF.getText().isEmpty() && companyRadio.isSelected()){
            alertMsg("Company title field is empty!","Enter company title!");
        }

        else if (companyTitleF.getText().length()>30){
            alertMsg("Company title is too long!","Max allowed length is 30.");
        }

        else if (companyAddressF.getText().isEmpty() && companyRadio.isSelected()){
            alertMsg("Address field is empty!","Enter address!");
        }

        else if (companyAddressF.getText().length()>50){
            alertMsg("Address is too long!","Max allowed length is 50.");
        }

        else {
                if (personRadio.isSelected()) {
                    Person person = new Person(loginF.getText(), passwordF.getText(), "+370" + phoneNumF.getText(), Role.PERSON, personNameF.getText(), personSurnameF.getText());  //valueOf(roleCombo.getSelectionModel().getSelectedItem().toString())
                    userHibernateCtrl.createUser(person);
                    returnToLogin();
                } else {
                    Company company = new Company(loginF.getText(), passwordF.getText(), "+370" + phoneNumF.getText(), Role.COMPANY, companyTitleF.getText(), companyAddressF.getText());  //valueOf(roleCombo.getSelectionModel().getSelectedItem().toString())
                    userHibernateCtrl.createUser(company);
                    returnToLogin();
                }

        }

    }


    public void returnToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/LoginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginF.getScene().getWindow();
        stage.setTitle("Book Shop");
        stage.setScene(scene);
        stage.show();
    }

    public void disableFields() {
        if (personRadio.isSelected()) {
            companyTitleF.setDisable(true);
            companyAddressF.setDisable(true);
            personNameF.setDisable(false);
            personSurnameF.setDisable(false);
        } else {
            personNameF.setDisable(true);
            personSurnameF.setDisable(true);
            companyTitleF.setDisable(false);
            companyAddressF.setDisable(false);
        }
    }

}

