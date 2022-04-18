package webControllers;

import books.Book;
import books.Cart;
import books.Status;
import com.google.gson.Gson;
import hibernateControllers.BookHibernateCtrl;
import hibernateControllers.CartHibernateCtrl;
import hibernateControllers.UserHibernateCtrl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import users.Person;
import users.Role;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Properties;

@Controller
public class CartWebCtrl {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    CartHibernateCtrl cartHibernateCtrl = new CartHibernateCtrl(entityManagerFactory);
    UserHibernateCtrl userHibernateCtrl = new UserHibernateCtrl(entityManagerFactory);
    BookHibernateCtrl bookHibernateCtrl = new BookHibernateCtrl(entityManagerFactory);

    @RequestMapping(value = "cart/createCart", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCart(@RequestBody String request) {//neveikia
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Cart cart = new Cart(
               userHibernateCtrl.getUserById(Integer.parseInt(properties.getProperty("buyer_id"))),
                Status.IN_PROGRESS);

        String[] booksId = properties.getProperty("items").toString().split(",");
        Book currentBook= new Book();
        for(int i=0;i<booksId.length;i++){
            currentBook = bookHibernateCtrl.getBookById(Integer.parseInt(booksId[i]));
                cart.getItems().add(currentBook);
        }

        List<Person> people = userHibernateCtrl.getAllPerson();
        Person supervisingEmployee=new Person();
        for(int i=0;i<people.size();i++){
            supervisingEmployee = people.get(i);
            if(supervisingEmployee.getRole()== Role.EMPLOYEE) {
                cart.getSupervisingEmployees().add(supervisingEmployee);
            }
        }

        cartHibernateCtrl.createCart(cart);

        return "Success";
    }

    @RequestMapping(value = "cart/updateCart/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCart(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Cart cart = cartHibernateCtrl.getCartById(id);

        cart.setStatus(Status.valueOf(properties.getProperty("status")));


        cartHibernateCtrl.updateCart(cart);

        return "Success";
    }

    @RequestMapping(value = "cart/removeCart/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String removeCart(@PathVariable(name = "id") int id) {
        cartHibernateCtrl.removeCart(id);
        Cart cart = cartHibernateCtrl.getCartById(id);
        if (cart == null) return "Cart deleted successfully";
        else return "Not deleted";
    }

    @RequestMapping(value = "cart/allCarts")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllCart(){
        Gson gson = new Gson();
        return gson.toJson(cartHibernateCtrl.getAllCarts().toString());
    }

    @RequestMapping(value = "cart/cartById/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCartById(@PathVariable(name = "id") int id) {
        return cartHibernateCtrl.getCartById(id).toString();
    }

    @RequestMapping(value = "cart/cartByBuyer/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCartByBuyer(@PathVariable(name = "id") int id) {
        return cartHibernateCtrl.getCartByBuyer(userHibernateCtrl.getUserById(id)).toString();
    }
}
