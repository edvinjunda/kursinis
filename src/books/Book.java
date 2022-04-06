package books;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@Entity
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String bookTitle;
    private String description;
    private LocalDate publishDate;
    private int pageNum;
    private String authors;
    private double price;
    private int inStock;
    /*@OneToMany(mappedBy = "bookComment", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> comments;*/
    @ManyToMany(mappedBy = "items", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Cart> inOrders;

    public Book(String bookTitle, String description, LocalDate publishDate, int pageNum, String authors, double price, int inStock) {
        this.bookTitle = bookTitle;
        this.description = description;
        this.publishDate = publishDate;
        this.pageNum = pageNum;
        this.authors = authors;
        this.price = price;
        this.inStock = inStock;
    }

    public Book() {

    }


    public int getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public int getPageNum() {
        return pageNum;
    }

    public String getAuthors() {
        return authors;
    }

    public double getPrice() {
        return price;
    }

    public int getInStock() {
        return inStock;
    }


    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
