package webControllers;

import books.Comment;
import com.google.gson.Gson;
import hibernateControllers.BookHibernateCtrl;
import hibernateControllers.CartHibernateCtrl;
import hibernateControllers.CommentHibernateCtrl;
import hibernateControllers.UserHibernateCtrl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import users.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

@Controller
public class CommentWebCtrl {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    CommentHibernateCtrl commentHibernateCtrl = new CommentHibernateCtrl(entityManagerFactory);
    UserHibernateCtrl userHibernateCtrl = new UserHibernateCtrl(entityManagerFactory);
    BookHibernateCtrl bookHibernateCtrl = new BookHibernateCtrl(entityManagerFactory);

    @RequestMapping(value = "comment/writeComment", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String writeComment(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        /*if (properties.getProperty("userType").equals("P")){
            Person person = new Person(properties.getProperty("login"), properties.getProperty("password"), properties.getProperty("phone_number"), Role.PERSON ,properties.getProperty("name"), properties.getProperty("surname"));
            userHibernateCtrl.createUser(person);
        }
        else if (properties.getProperty("userType").equals("C")){
            Company company = new Company(properties.getProperty("login"), properties.getProperty("password"), properties.getProperty("phone_number"), Role.COMPANY ,properties.getProperty("company_title"), properties.getProperty("address"));
            userHibernateCtrl.createUser(company);
        }*/
        //else return "Error";

        return "Success";
    }

    @RequestMapping(value = "comment/updateComment/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateComment(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Comment comment = commentHibernateCtrl.getCommentById(id);//Integer.parseInt(properties.getProperty("id"))

        return "Success";
    }

    @RequestMapping(value = "comment/removeComment/{id}", method = RequestMethod.DELETE)//netrina
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String removeComment(@PathVariable(name = "id") int id) {
        commentHibernateCtrl.removeComment(id);
        Comment comment = commentHibernateCtrl.getCommentById(id);
        if (comment == null) return "Comment deleted successfully";
        else return "Not deleted";
    }

    //?
    @RequestMapping(value = "comment/allComments")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllComments(){
        Gson gson = new Gson();
        return gson.toJson(commentHibernateCtrl.getAllComments().toString());
    }
    //?

    @RequestMapping(value = "comment/commentById/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCommentById(@PathVariable(name = "id") int id) {
        return commentHibernateCtrl.getCommentById(id).toString();
    }

}
