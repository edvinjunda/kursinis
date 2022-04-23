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
import lombok.NoArgsConstructor;
import users.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static incorrectDataControl.IncorrectDataControl.alertMsg;


@NoArgsConstructor
public class WriteCommentWindow {
    @FXML
    public TextArea commentText;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;
    private int bookId;
    private int commentId;
    private int commenterId;
    private boolean updateComment;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    UserHibernateCtrl userHibControl = new UserHibernateCtrl(entityManagerFactory);
    BookHibernateCtrl bookHibControl = new BookHibernateCtrl(entityManagerFactory);
    CommentHibernateCtrl commentHibControl = new CommentHibernateCtrl(entityManagerFactory);


    public void setData(int bookId, int commentId, int commenterId) {
        this.bookId = bookId;
        this.commentId=commentId;
        this.commenterId = commenterId;
        updateComment=false;
    }

    public void setData(int commentId, String comment) {
        this.commentId=commentId;
        commentText.setText(comment);
        updateComment=true;
    }

    public void commentAction(ActionEvent actionEvent) {
        if(updateComment){
            updateComment();
        }
        else {
            createComment();
        }

    }

    public void createComment() {
        if(commentText.getText().isEmpty()){
            alertMsg("Comment is empty!","Enter comment.");
        }

        else {
            User user = userHibControl.getUserById(commenterId);
            if (bookId != 0) {
                Book book = bookHibControl.getBookById(bookId);

                Comment comment = new Comment(commentText.getText(), null, book, LocalDate.now(), user.getName());
                book.getComments().add(comment);
                bookHibControl.updateBook(book);
            }
            else if (commentId != 0) {
                Comment currentComment = commentHibControl.getCommentById(commentId);
                Comment reply = new Comment(commentText.getText(), currentComment, null, LocalDate.now(), user.getName());
                currentComment.getReplies().add(reply);
                commentHibControl.editComment(currentComment);
            }

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    public void updateComment () {
        if(commentText.getText().isEmpty()){
            alertMsg("Comment is empty!","Enter comment.");
        }

        else {
                Comment currentComment = commentHibControl.getCommentById(commentId);
                currentComment.setCommentText("//edited//"+commentText.getText());
                commentHibControl.editComment(currentComment);

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    public void cancelComment(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
