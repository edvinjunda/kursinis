package fxControllers;

import books.Book;
import books.Comment;
import hibernateControllers.BookHibernateCtrl;
import hibernateControllers.CommentHibernateCtrl;
import hibernateControllers.UserHibernateCtrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import users.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;

public class WriteCommentWindow {
    @FXML
    public TextArea commentText;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;
    private int bookId;
    private int parentCommentId;
    private int commenterId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    UserHibernateCtrl userHibControl = new UserHibernateCtrl(entityManagerFactory);
    BookHibernateCtrl bookHibControl = new BookHibernateCtrl(entityManagerFactory);
    CommentHibernateCtrl commentHibControl = new CommentHibernateCtrl(entityManagerFactory);


    public void setData(int bookId, int parentCommentId, int commenterId) {
        this.bookId = bookId;
        this.parentCommentId=parentCommentId;
        this.commenterId = commenterId;
    }

    public void saveComment(ActionEvent actionEvent) {
        Book book = bookHibControl.getBookById(bookId);
        User user = userHibControl.getUserById(commenterId);
        if(bookId!=0)
        {
            Comment comment = new Comment(commentText.getText(), null, book, LocalDate.now(), user.getName());
            book.getComments().add(comment);
            bookHibControl.updateBook(book);
        }
        else if (parentCommentId!=0)
        {
            Comment parentComment = commentHibControl.getCommentById(parentCommentId);
            Comment comment = new Comment(commentText.getText(), parentComment, null,LocalDate.now(), user.getName());
            parentComment.getReplies().add(comment);
            commentHibControl.editComment(parentComment);
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public void cancelComment(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
