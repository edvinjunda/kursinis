package fxControllers;

import hibernateControllers.UserHibernateCtrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import users.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

import static utils.Utils.alertMsg;

public class LoginWindow {
    @FXML
    public TextField loginF;
    @FXML
    public PasswordField passwordF;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    UserHibernateCtrl userHibernateCtrl = new UserHibernateCtrl(entityManagerFactory);

    public void validateAndLoad() throws IOException {
        User user = userHibernateCtrl.getUserByLoginData(loginF.getText(), passwordF.getText());
        if (user != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/BookShopWindow.fxml"));
            Parent parent = fxmlLoader.load();          //Sito zingsnio reikia, kad galetumem pasiekti resursus susijusius su ta forma
            BookShopWindow bookShopWindow = fxmlLoader.getController();
            bookShopWindow.setUserId(user.getId());
            Scene scene = new Scene(parent);
            Stage stage = (Stage) loginF.getScene().getWindow();
            stage.setTitle("Book Shop");
            stage.setScene(scene);
            stage.show();
        } else {
            alertMsg("Login error", "Wrong login or password");
        }
    }

    public void loadSignUpWindow(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/SignUpWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginF.getScene().getWindow();
        stage.setTitle("Sign up window");
        stage.setScene(scene);
        stage.show();
    }
}
