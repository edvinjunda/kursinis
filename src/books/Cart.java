package books;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import users.Person;
import users.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Cart {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @ManyToMany//(fetch = FetchType.EAGER)
        private List<Book> items;
        @ManyToMany//(fetch = FetchType.EAGER)
        private List<Person> supervisingEmployees;
        @ManyToOne
        private User buyer;
        private Status status;

            public Cart(ArrayList<Book> items, ArrayList<Person> supervisingEmployees, User buyer, Status status) {
                this.items = items;
                this.supervisingEmployees = supervisingEmployees;
                this.buyer = buyer;
                this.status=status;
            }

    public Cart(User buyer) {
        this.buyer = buyer;
        this.items = new ArrayList<>();
        this.supervisingEmployees = new ArrayList<>();
    }

    public Cart(User buyer, Status status) {
        this.buyer = buyer;
        this.status=status;
    }

/*
    public List<Person> getSupervisingEmployees(){
                return supervisingEmployees;
    }
*/

    @Override
    public String toString(){
        return id + ". Buyer id: " +
                buyer.getId() + ", status: " +
                status;
    }
}
