package webControllers;

import books.Book;
import com.google.gson.Gson;
import hibernateControllers.BookHibernateCtrl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Properties;

@Controller
//@RequestMapping(value = "/bookInfo")
public class BookWebCtrl {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    BookHibernateCtrl bookHibernateCtrl = new BookHibernateCtrl(entityManagerFactory);


    @RequestMapping(value = "book/createBook", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createBook(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Book book = new Book(
                properties.getProperty("bookTitle"),
                properties.getProperty("description"),
                LocalDate.parse(properties.getProperty("publishDate")),
                Integer.parseInt(properties.getProperty("pageNum")),
                properties.getProperty("authors"),
                Double.parseDouble(properties.getProperty("price")),
                Integer.parseInt(properties.getProperty("inStock")));

        bookHibernateCtrl.createBook(book);

        return "Success";
    }

    @RequestMapping(value = "book/updateBook/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateBook(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Book book = bookHibernateCtrl.getBookById(id);//Integer.parseInt(properties.getProperty("id"))

        book.setBookTitle(properties.getProperty("bookTitle"));
        book.setDescription(properties.getProperty("description"));
        book.setPublishDate(LocalDate.parse(properties.getProperty("publishDate")));
        book.setPageNum(Integer.parseInt(properties.getProperty("pageNum")));
        book.setAuthors(properties.getProperty("authors"));
        book.setPrice(Double.parseDouble(properties.getProperty("price")));
        book.setInStock(Integer.parseInt(properties.getProperty("inStock")));

        bookHibernateCtrl.updateBook(book);

        return "Success";
    }

    @RequestMapping(value = "book/removeBook/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String removeBook(@PathVariable(name = "id") int id) {
        bookHibernateCtrl.removeBook(id);
        Book book = bookHibernateCtrl.getBookById(id);
        if (book == null) return "User deleted successfully";
        else return "Not deleted";
    }

    @RequestMapping(value = "book/allBooks")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllBooks(){
        Gson gson = new Gson();
        return gson.toJson(bookHibernateCtrl.getAllBooks(-1).toString());
    }

    @RequestMapping(value = "book/bookById/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getBookById(@PathVariable(name = "id") int id) {
        return bookHibernateCtrl.getBookById(id).toString();
    }
}
