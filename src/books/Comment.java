package books;

import books.Book;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String commentText;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true) //orphan... jeigu istriname parent komentara, tai visi replies irgi issitrina
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> replies;
    @ManyToOne
    private Comment parentComment; //jeigu jis lygus null, tai jis bus kabinamas prie knygos
    @ManyToOne
    private Book bookComment;
    private String commenterName;
    private LocalDate datePosted;

    public Comment(String commentText, Comment parentComment, Book bookComment, LocalDate datePosted, String commenterName) {
        this.commentText = commentText;
        this.parentComment = parentComment;
        this.bookComment = bookComment;
        this.datePosted = datePosted;
        this.commenterName = commenterName;
    }

    @Override
    public String toString() {
        return id + "." + commentText + " (" + "Posted by " +  commenterName + " on " + datePosted + ")";
    }
}
