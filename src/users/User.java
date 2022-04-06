package users;

import books.Cart;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
//@RequiredArgsConstructor
@Entity
public abstract class User  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String login;
    private String password;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private String phoneNum;
    private Role role;
    protected String address;
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Cart> myOrders;


    public User (){}

    public User(String login, String password, String phoneNum, Role role) {
        this.login = login;
        this.password = password;
        this.phoneNum = phoneNum;
        this.role = role;

        this.dateCreated=LocalDate.now();
        this.dateModified=LocalDate.now();
    }

    public User(String login, String password, String phoneNum, Role role, String address) {
        this.login = login;
        this.password = password;
        this.phoneNum = phoneNum;
        this.role = role;
        this.address=address;

        this.dateCreated=LocalDate.now();
        this.dateModified=LocalDate.now();
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public Role getRole() {
        return role;
    }

    public abstract String getName();

    public abstract String getSurname();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address){
        this.address=address;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }


    public abstract String getCompanyTitle();

    public abstract void setName(String text);

    public abstract void setSurname(String text);

    public abstract void setCompanyTitle(String text);

}
