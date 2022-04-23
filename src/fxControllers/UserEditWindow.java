package fxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import users.*;
import hibernateControllers.UserHibernateCtrl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static incorrectDataControl.IncorrectDataControl.*;
import static incorrectDataControl.IncorrectDataControl.alertMsg;

public class UserEditWindow implements Initializable {
    @FXML
    private TextField editAddressF;
    @FXML
    private TextField editCompanyTittleF;
    @FXML
    private TextField editLoginF;
    @FXML
    private TextField editNameF;
    @FXML
    private TextField editPhoneNumF;
    @FXML
    private TextField editSurnameF;
    @FXML
    public Button cancelButton;
    @FXML
    public Button saveEditedUser;

    User editedUser;

    /*private int adminId;

    public void setAdminId(int id){
        this.adminId=id;
    }*/

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    UserHibernateCtrl userHibernateCtrl = new UserHibernateCtrl(entityManagerFactory);

    public UserEditWindow(){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void SetFields(User user)
    {
        editPhoneNumF.setText(user.getPhoneNum().substring(4));
        editLoginF.setText(user.getLogin());

        if(user.getRole()==Role.PERSON){
            editAddressF.setDisable(true);
            editCompanyTittleF.setDisable(true);
            editNameF.setText(user.getName());
            editSurnameF.setText(user.getSurname());

            editedUser=user;
        }

        else if(user.getRole()==Role.COMPANY){
                editAddressF.setText(user.getAddress());
                editCompanyTittleF.setText(user.getCompanyTitle());
                editNameF.setDisable(true);
                editSurnameF.setDisable(true);

            editedUser=user;
        }

        else if (user.getRole()==Role.EMPLOYEE){
            editAddressF.setText(user.getAddress());
            editCompanyTittleF.setDisable(true);
            editNameF.setText(user.getName());
            editSurnameF.setText(user.getSurname());

            editedUser=user;
        }

    }


    public void cancelEditing(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    /*public void returnToBookShop(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookShopWindow.class.getResource("../view/BookShopWindow.fxml"));
        Parent parent = fxmlLoader.load();
        BookShopWindow bookShopWindow = fxmlLoader.getController();
        bookShopWindow.setUserId(adminId);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) editPhoneNumF.getScene().getWindow();
        stage.setTitle("Book Shop");
        stage.setScene(scene);
        stage.show();
    }*/

    /*public void saveAndReturnToBookShop() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(BookShopWindow.class.getResource("../view/BookShopWindow.fxml"));
        Parent parent = fxmlLoader.load();
        BookShopWindow bookShopWindow = fxmlLoader.getController();
        //bookShopWindow.setUserId(adminId);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) editPhoneNumF.getScene().getWindow();
        stage.setTitle("Book Shop");
        stage.setScene(scene);
        stage.show();
    }*/


    public void updateUser(ActionEvent actionEvent) throws IOException {
        if (editLoginF.getText().isEmpty()) {
            alertMsg("Login field is empty!", "Enter login!");
        }

        else if (editLoginF.getText().length() > 20) {
            alertMsg("Login is too long!", "Max allowed length is 20.");
        }

        else if (editPhoneNumF.getText().isEmpty()) {
            alertMsg("Phone number field is empty!", "Enter phone number!");
        }

        else if (editPhoneNumF.getText().length() != 8) {
            alertMsg("Incorrect length.", "Enter 8 digits!");
        }

        else if (containsCharactersInteger(editPhoneNumF.getText())) {
            alertMsg("Phone number contains characters!", "Remove all characters!");
        }

        else{

            editedUser.setLogin(editLoginF.getText());
            editedUser.setPhoneNum("+370"+editPhoneNumF.getText());
            editedUser.setDateModified(LocalDate.now());

            if (editedUser.getRole() == Role.PERSON) {
                if (editNameF.getText().isEmpty()) {
                    alertMsg("Name field is empty!", "Enter name!");
                }
                else if (editNameF.getText().length() > 20) {
                    alertMsg("Name is too long!", "Max allowed length is 20.");
                }

                else if (containsDigits(editNameF.getText())) {
                    alertMsg("Name contains digits!", "Remove digits!");
                }

                else if (editSurnameF.getText().isEmpty()) {
                    alertMsg("Surname field is empty!", "Enter surname!");
                }

                else if (editSurnameF.getText().length() > 20) {
                    alertMsg("Surname is too long!", "Max allowed length is 20.");
                }

                else if (containsDigits(editSurnameF.getText())) {
                    alertMsg("Surname contains digits!", "Remove digits!");
                }

                else{

                    editedUser.setName(editNameF.getText());
                    editedUser.setSurname(editSurnameF.getText());

                    userHibernateCtrl.updateUser(editedUser);

                    Stage stage = (Stage) saveEditedUser.getScene().getWindow();
                    stage.close();
                }
            }

            else if (editedUser.getRole() == Role.COMPANY) {
                if (editCompanyTittleF.getText().isEmpty()) {
                    alertMsg("Company title field is empty!", "Enter company title!");
                }

                else if (editCompanyTittleF.getText().length() > 30) {
                    alertMsg("Company title is too long!", "Max allowed length is 30.");
                }

                else if (editAddressF.getText().isEmpty()) {
                    alertMsg("Address field is empty!", "Enter address!");
                }

                else if (editAddressF.getText().length() > 50) {
                    alertMsg("Address is too long!", "Max allowed length is 50.");
                }

                else {
                    editedUser.setCompanyTitle(editCompanyTittleF.getText());
                    editedUser.setAddress(editAddressF.getText());

                    userHibernateCtrl.updateUser(editedUser);

                    Stage stage = (Stage) saveEditedUser.getScene().getWindow();
                    stage.close();
                }

            }


            else if (editedUser.getRole() == Role.EMPLOYEE) {
                if (editNameF.getText().isEmpty()) {
                    alertMsg("Name field is empty!", "Enter name!");
                }
                else if (editNameF.getText().length() > 20) {
                    alertMsg("Name is too long!", "Max allowed length is 20.");
                }

                else if (containsDigits(editNameF.getText())) {
                    alertMsg("Name contains digits!", "Remove digits!");
                }

                else if (editNameF.getText().isEmpty()) {
                    alertMsg("Surname field is empty!", "Enter surname!");
                }

                else if (editSurnameF.getText().length() > 20) {
                    alertMsg("Surname is too long!", "Max allowed length is 20.");
                }

                else if (containsDigits(editSurnameF.getText())) {
                    alertMsg("Surname contains digits!", "Remove digits!");
                }

                else if (editAddressF.getText().isEmpty()) {
                    alertMsg("Address field is empty!", "Enter address!");
                }

                else if (editAddressF.getText().length() > 50) {
                    alertMsg("Address is too long!", "Max allowed length is 50.");
                }

                else {
                editedUser.setName(editNameF.getText());
                editedUser.setSurname(editSurnameF.getText());
                editedUser.setAddress(editAddressF.getText());

                userHibernateCtrl.updateUser(editedUser);

                Stage stage = (Stage) saveEditedUser.getScene().getWindow();
                stage.close();
                }

            }

        }

    }


}
