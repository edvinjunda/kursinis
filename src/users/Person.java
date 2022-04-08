package users;

import lombok.*;

import javax.persistence.Entity;
import java.io.Serializable;

@Getter
@Setter
//@RequiredArgsConstructor
@Entity
public class Person extends User implements Serializable {
    private String name;
    private String surname;

    public Person() {super();}

    public Person(String login, String password, String phoneNum, Role role, String name, String surname) {
        super(login, password, phoneNum, role);
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAddress(String address) {
        this.address=address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public void setCompanyTitle(String text) {

    }

    @Override
    public String getCompanyTitle() {
        return null;
    }


    @Override
    public String toString(){
        return "Person{" +
                "name " + name +
                ", surname " + surname +
                ", login " + getLogin() +
                '}';
    }
}
