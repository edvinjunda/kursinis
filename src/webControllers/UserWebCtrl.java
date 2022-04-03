package webControllers;

import hibernateControllers.BookHibernateCtrl;
import hibernateControllers.UserHibernateCtrl;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import users.Company;
import users.Person;
import users.Role;
import users.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.util.Properties;


@Controller//localhost:8081/application_context/user
@RequestMapping(value = "/userInfo")
public class UserWebCtrl {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    UserHibernateCtrl userHibernateCtrl = new UserHibernateCtrl(entityManagerFactory);

    //praveryc eta srazy a potym edicirowav astalny kod
    /*@RequestMapping(value = "/allUsers")
    @ResponseBody
    public String getAllUsers() {
        return userHibernateCtrl.getAllUsers().toString();
    }*/

        @RequestMapping(value = "/user/allUsers", method = RequestMethod.GET)
        @ResponseStatus(value = HttpStatus.OK)
        @ResponseBody
        public String getAllUsers() {
            Gson gson = new Gson();
            return gson.toJson(userHibernateCtrl.getAllUsers().toString());
        }

        @RequestMapping(value = "/user/updatePerson/{id}", method = RequestMethod.PUT)
        @ResponseStatus(value = HttpStatus.OK)
        @ResponseBody
        public String updateUser(@RequestBody String request, @PathVariable(name = "id") int id) {
            Gson gson = new Gson();
            Properties properties = gson.fromJson(request, Properties.class);

            User user = userHibernateCtrl.getPersonById(id);

            user.setName(properties.getProperty("name"));
            user.setLogin(properties.getProperty("login"));
            //pabaigsim

            //Person person = new Person(properties.getProperty("login"), properties.getProperty("psw"), properties.getProperty("name"), properties.getProperty("surname"), properties.getProperty("email"));
            userHibernateCtrl.updateUser(user);
            return "Success";
        }

        @RequestMapping(value = "/user/addPerson", method = RequestMethod.POST)
        @ResponseStatus(value = HttpStatus.OK)
        @ResponseBody
        public String addNewPerson(@RequestBody String request) {
            Gson gson = new Gson();
            Properties properties = gson.fromJson(request, Properties.class);
            Person person = new Person(properties.getProperty("login"), properties.getProperty("password"), properties.getProperty("phone_number"), Role.PERSON ,properties.getProperty("name"), properties.getProperty("surname"));
            userHibernateCtrl.createUser(person);
            return "Success";
        }

    @RequestMapping(value = "/user/addCompany", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewCompany(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Company company = new Company(properties.getProperty("login"), properties.getProperty("password"), properties.getProperty("phone number"), Role.COMPANY ,properties.getProperty("company_title"), properties.getProperty("address"));
        userHibernateCtrl.createUser(company);
        return "Success";
    }

        @RequestMapping(value = "/user/deleteUser/{id}", method = RequestMethod.DELETE)
        @ResponseStatus(value = HttpStatus.OK)
        @ResponseBody
        public String updatePerson(@PathVariable(name = "id") int id) {
            userHibernateCtrl.removeUser(id);
            //?//Patikrinti ar tikrai istryne
            return "Success";
        }

}
