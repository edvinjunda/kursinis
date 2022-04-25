package fxControllers;

import books.Book;
import books.Cart;
import books.Status;
import hibernateControllers.BookHibernateCtrl;
import hibernateControllers.CartHibernateCtrl;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class CartCancelWindow {
    private Cart cart;
    @FXML
    private Button confirmButton, cancelButton;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    CartHibernateCtrl cartHibernateCtrl=new CartHibernateCtrl(entityManagerFactory);
    BookHibernateCtrl bookHibernateCtrl=new BookHibernateCtrl(entityManagerFactory);


    public void setCart(Cart cart) {
        this.cart=cart;
    }

    public void confirmCanceling(ActionEvent actionEvent){
        cart.setStatus(Status.CANCELED);
        cartHibernateCtrl.updateCart(cart);

        for(int i=0;i<cart.getItems().size();i++){
            Book tempBook=cart.getItems().get(i);
            tempBook.setInStock(tempBook.getInStock()+1);
            bookHibernateCtrl.updateBook(tempBook);
        }

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public void cancelCanceling(ActionEvent actionEvent)throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}
