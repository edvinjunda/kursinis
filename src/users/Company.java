package users;

import lombok.*;

import javax.persistence.Entity;
import java.io.Serializable;

@Getter
@Setter
//@RequiredArgsConstructor
@Entity
public class Company extends User implements Serializable {
    private String companyTitle;

    public Company() {super();}

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getSurname() {
        return null;
    }

    public Company(String login, String password, String phoneNum, Role role, String companyTitle, String address) {
        super(login, password, phoneNum, role,address);
        this.companyTitle = companyTitle;
    }

    public String getCompanyTitle() {
        return companyTitle;
    }

    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public void setName(String text) {

    }

    @Override
    public void setSurname(String text) {

    }
}
