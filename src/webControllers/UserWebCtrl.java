package webControllers;

import com.google.gson.GsonBuilder;
import hibernateControllers.UserHibernateCtrl;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import users.Company;
import users.Person;
import users.Role;
import users.User;
import utils.LocalDateGsonSerializer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Properties;


@Controller//localhost:8081/application_context/user
//@RequestMapping(value = "/userInfo")
public class UserWebCtrl {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    UserHibernateCtrl userHibernateCtrl = new UserHibernateCtrl(entityManagerFactory);

    @RequestMapping(value = "user/validateUser", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String validateUser(@RequestBody String userInfo) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(userInfo, Properties.class);
        User user = userHibernateCtrl.getUserByLoginData(data.getProperty("login"), data.getProperty("password"));
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateGsonSerializer());
        return builder.create().toJson(user, User.class);
    }


        @RequestMapping(value = "user/createUser", method = RequestMethod.POST)
        @ResponseStatus(value = HttpStatus.OK)
        @ResponseBody
        public String createUser(@RequestBody String request) {
            Gson gson = new Gson();
            Properties properties = gson.fromJson(request, Properties.class);
            if (properties.getProperty("userType").equals("P")){
                Person person = new Person(properties.getProperty("login"), properties.getProperty("password"), properties.getProperty("phone_number"), Role.PERSON ,properties.getProperty("name"), properties.getProperty("surname"));
                userHibernateCtrl.createUser(person);
            }
            else if (properties.getProperty("userType").equals("C")){
                Company company = new Company(properties.getProperty("login"), properties.getProperty("password"), properties.getProperty("phone_number"), Role.COMPANY ,properties.getProperty("company_title"), properties.getProperty("address"));
                userHibernateCtrl.createUser(company);
            }
            else return "Error";

            return "Success";
        }

    //perdaryti i update person, company, employee
    @RequestMapping(value = "user/updateUser/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateUser(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        User user = userHibernateCtrl.getUserById(id);//Integer.parseInt(properties.getProperty("id"))

        user.setLogin(properties.getProperty("login"));
        user.setPhoneNum(properties.getProperty("phone_number"));
        if (user.getRole()==Role.PERSON) {//properties.getProperty("userType").equals("P")
            user.setName(properties.getProperty("name"));
            user.setSurname(properties.getProperty("surname"));
            userHibernateCtrl.updateUser(user);
        }
        else if (user.getRole()==Role.COMPANY) {
            user.setCompanyTitle(properties.getProperty("company_title"));
            user.setCompanyTitle(properties.getProperty("address"));
            userHibernateCtrl.updateUser(user);
        }
        else if (user.getRole()==Role.EMPLOYEE) {
            user.setName(properties.getProperty("name"));
            user.setSurname(properties.getProperty("surname"));
            user.setCompanyTitle(properties.getProperty("address"));
            userHibernateCtrl.updateUser(user);
        }
        else return "wrong user type given or other issue";//userHibernateCtrl.updateUser(user);
        return "Success";
    }

        @RequestMapping(value = "user/removeUser/{id}", method = RequestMethod.DELETE)
        @ResponseStatus(value = HttpStatus.OK)
        @ResponseBody
        public String removeUser(@PathVariable(name = "id") int id) {
            userHibernateCtrl.removeUser(id);
            User user = userHibernateCtrl.getUserById(id);
            if (user == null) return "User deleted successfully";
            else return "Not deleted";
        }

    //?
    @RequestMapping(value = "user/allUsers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllUsers() {
        Gson gson = new Gson();
        return gson.toJson(userHibernateCtrl.getAllUsers().toString());
    }
    //?

    @RequestMapping(value = "user/allPerson", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllPerson() {
        Gson gson = new Gson();
        return gson.toJson(userHibernateCtrl.getAllPerson().toString());
    }

    @RequestMapping(value = "user/allCompany", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllCompany() {
        Gson gson = new Gson();
        return gson.toJson(userHibernateCtrl.getAllCompany().toString());
    }

    @RequestMapping(value = "user/userById/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getUserById(@PathVariable(name = "id") int id) {
            //userHibernateCtrl.getUserById(id);
        return userHibernateCtrl.getUserById(id).toString();//return "Success";
    }

    @RequestMapping(value = "user/userByLoginData", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getUserByLoginData(@PathVariable(name = "login") String login, @PathVariable(name = "password") String password) {

        userHibernateCtrl.getUserByLoginData(login, password);
        return "Success";
    }

    //?
    @RequestMapping(value = "/user/userByLogin/{login}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getUserByLoginData(@PathVariable(name = "login") String login) {

        //;
        //return "Success";
        return userHibernateCtrl.getUserByLogin(login).toString();
    }
    //?

}
