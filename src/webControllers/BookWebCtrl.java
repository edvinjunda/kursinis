package webControllers;

import hibernateControllers.BookHibernateCtrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Controller
@RequestMapping(value = "/bookInfo")
public class BookWebCtrl {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    BookHibernateCtrl bookHibernateCtrl = new BookHibernateCtrl(entityManagerFactory);

    @RequestMapping(value = "/allBooks")
    @ResponseBody
    public String getAllBooks(){
        return bookHibernateCtrl.getAllBooks(-1).toString();
    }
}
