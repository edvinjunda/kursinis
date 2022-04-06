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
        @ManyToMany
        private List<Book> items;
        @ManyToMany
        private List<Person> supervisingEmployees;
        @ManyToOne
        private User buyer;
        private Status status;
        private String orderNum;
        //pabaigti uzsakymo atributus


        public Cart(String orderNum, User buyer) {
                this.orderNum = orderNum;
                this.buyer = buyer;
                this.items = new ArrayList<>();
                this.supervisingEmployees = new ArrayList<>();
            }

            public Cart(String orderNum, ArrayList<Book> items, ArrayList<Person> supervisingEmployees, User buyer) {
                this.orderNum = orderNum;
                this.items = items;
                this.supervisingEmployees = supervisingEmployees;
                this.buyer = buyer;
            }

}
