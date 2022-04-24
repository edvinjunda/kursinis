package fxControllers;

import books.Book;
import books.Cart;
import books.Comment;
import books.Status;
import hibernateControllers.CartHibernateCtrl;
import hibernateControllers.CommentHibernateCtrl;
import utils.DataBaseOperations;
import hibernateControllers.BookHibernateCtrl;
import hibernateControllers.UserHibernateCtrl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import users.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static incorrectDataControl.IncorrectDataControl.*;
import static java.lang.Integer.parseInt;

public class BookShopWindow implements Initializable {
    @FXML
    public HBox searchBar;
    @FXML
    public TextField searchBookCustF, searchAuthorsCustF;
    @FXML
    public DatePicker searchFromDateCust;
    @FXML
    public DatePicker searchToDateCust;
    @FXML
    public ListView<String> allBooksClient;
    @FXML
    public TreeView bookCommentTree;
    @FXML
    public MenuItem commentReplyButton, commentEditButton, commentDeleteButton;


    @FXML
    public ListView employeeBookList;
    @FXML
    public TextField createBookTitle;
    @FXML
    public TextArea createBookDescription;
    @FXML
    public DatePicker createBookPublishDate;
    @FXML
    public TextField createBookPageNum;
    @FXML
    public TextField createBookPrice;
    @FXML
    public TextField createBookInStock;
    @FXML
    public TextField createBookAuthors;

    @FXML
    public TextField personNameF,personSurnameF, companyTitleF, addressF, phoneNumF, loginF;
    @FXML
    public PasswordField passwordF, passwordRepeatF;
    @FXML
    public Label confirmLabel;
    @FXML
    public Button editUserButton, saveUserButton, cancelButton, logOutButton;

    @FXML
    public ListView buyerCart;
    @FXML
    public ListView employeeCartsInfo;
    @FXML
    private Tab myAccount;
    @FXML
    private Tab bookShop;
    @FXML
    public Tab adminTools;
    @FXML
    public Tab employeeTools;
    @FXML
    public TabPane tabPane;
    @FXML
    public Button addBookButton;
    @FXML
    public Button updateBookButton;
    @FXML
    TextField searchBookEmpF, searchAuthorsEmpF;
    @FXML
    DatePicker searchFromDateEmp, searchToDateEmp;

    @FXML
    private TextField bookAuthorsF;
    @FXML
    private TextArea bookDescriptionF;
    @FXML
    private TextField bookInStockF;
    @FXML
    private TextField bookPageNumF;
    @FXML
    private TextField bookPriceF;
    @FXML
    private DatePicker bookPublishDateF;
    @FXML
    private TextField bookTitleF;
    @FXML
    public ListView currentOrderList;

    @FXML
    public TableView usersTable;
    @FXML
    private TableColumn<UserTableParameters, String> colAddress;
    @FXML
    private TableColumn<UserTableParameters, String> colCompanyTitle;
    @FXML
    private TableColumn<UserTableParameters, String> colDateCreated;
    @FXML
    private TableColumn<UserTableParameters, String> colDateModified;
    @FXML
    private TableColumn<UserTableParameters, Integer> colId;
    @FXML
    private TableColumn<UserTableParameters, String> colLogin;
    @FXML
    private TableColumn<UserTableParameters, String> colName;
    @FXML
    private TableColumn<UserTableParameters, String> colPhoneNum;
    @FXML
    private TableColumn<UserTableParameters, String> colRole;
    @FXML
    private TableColumn<UserTableParameters, String> colSurname;
    @FXML
    private TableColumn<UserTableParameters, Void> actionsField;
    @FXML
    private ListView adminCartsInfo;


    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    UserHibernateCtrl userHibernateCtrl = new UserHibernateCtrl(entityManagerFactory);
    BookHibernateCtrl bookHibernateCtrl = new BookHibernateCtrl(entityManagerFactory);
    CartHibernateCtrl cartHibernateCtrl = new CartHibernateCtrl(entityManagerFactory);
    CommentHibernateCtrl commentHibernateCtrl = new CommentHibernateCtrl(entityManagerFactory);

    private int userId;
    private Cart cart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateBookButton.setVisible(false);
    }

    public void setUserId(int userId) {
        this.userId = userId;
        User buyer = userHibernateCtrl.getUserById(userId);
        cart=new Cart(buyer);
        fillTablesByUser();
        loadUserInfo();
    }

    @FXML
    private void fillTablesByUser() {
        User user = userHibernateCtrl.getUserById(userId);

        if (user.getRole() == Role.PERSON || user.getRole() == Role.COMPANY) {
            tabPane.getTabs().remove(employeeTools);
            tabPane.getTabs().remove(adminTools);
        }

        else if (user.getRole() == Role.EMPLOYEE) {
            tabPane.getTabs().remove(adminTools);
            }

        /*ClearViewFields();
        allBooksClient.getItems().clear();
        List<Book> inStockBookList = bookHibernateCtrl.getAllBooks(0);
        inStockBookList.forEach(book -> allBooksClient.getItems().add(book.getId() + ":" + book.getBookTitle()));*/
    }

    public void disableCommentButtons(){
        if(userHibernateCtrl.getUserById(userId).getRole()!=Role.EMPLOYEE&&userHibernateCtrl.getUserById(userId).getRole()!=Role.ADMIN){
            commentDeleteButton.setVisible(false);
            commentEditButton.setVisible(false);
        }

        else {
            commentDeleteButton.setVisible(true);
            commentEditButton.setVisible(true);
        }
    }



    public ListView searchBook(ListView bookList, TextField bookF, TextField authorsF, DatePicker fromDate, DatePicker toDate, int inStock) {
        List<Book> filteredBooks = bookHibernateCtrl.getFilteredBooks(bookF.getText(), authorsF.getText(), fromDate.getValue(),toDate.getValue(),inStock);
        bookList.getItems().clear();
        if(inStock==-1){
            filteredBooks.forEach(book -> bookList.getItems().add(book.getId() + ":" + book.getBookTitle() + " - " + book.getInStock() + " in stock"));
        }
        else {
            filteredBooks.forEach(book -> bookList.getItems().add(book.getId() + ":" + book.getBookTitle()));
        }

        return bookList;
    }

    private Book getBookById(String id) {
        return bookHibernateCtrl.getBookById(Integer.parseInt(id));
    }

    public void loadUserInfo(){
        User currentUser=userHibernateCtrl.getUserById(userId);

        personNameF.setEditable(false);
        personSurnameF.setEditable(false);
        companyTitleF.setEditable(false);
        addressF.setEditable(false);
        phoneNumF.setEditable(false);
        loginF.setEditable(false);
        passwordF.setEditable(false);

        editUserButton.setDisable(false);

        confirmLabel.setVisible(false);
        passwordRepeatF.setVisible(false);
        passwordRepeatF.clear();
        saveUserButton.setVisible(false);
        cancelButton.setVisible(false);

        //personNameF GUI TextField box is hidden under companyTittleF GUI TextField box
        if(currentUser.getRole()==Role.PERSON){
            personNameF.setText(currentUser.getName());
            personSurnameF.setText(currentUser.getSurname());
            phoneNumF.setText(currentUser.getPhoneNum().substring(4));
            loginF.setText(currentUser.getLogin());
            passwordF.setText(currentUser.getPassword());

            companyTitleF.setVisible(false);
            addressF.setVisible(false);

        }

        else if(currentUser.getRole()==Role.COMPANY){
            companyTitleF.setText(currentUser.getCompanyTitle());
            addressF.setText(currentUser.getAddress());
            phoneNumF.setText(currentUser.getPhoneNum().substring(4));
            loginF.setText(currentUser.getLogin());
            passwordF.setText(currentUser.getPassword());

            personNameF.setVisible(false);
            personSurnameF.setVisible(false);
        }

        else if(currentUser.getRole()==Role.EMPLOYEE||currentUser.getRole()==Role.ADMIN){
            personNameF.setText(currentUser.getName());
            personSurnameF.setText(currentUser.getSurname());
            addressF.setText(currentUser.getAddress());
            phoneNumF.setText(currentUser.getPhoneNum().substring(4));
            loginF.setText(currentUser.getLogin());
            passwordF.setText(currentUser.getPassword());

            companyTitleF.setVisible(false);
        }
    }

    public void editUserInfo(ActionEvent actionEvent){
        User currentUser=userHibernateCtrl.getUserById(userId);

        confirmLabel.setVisible(true);
        passwordRepeatF.setVisible(true);
        saveUserButton.setVisible(true);
        editUserButton.setDisable(true);
        cancelButton.setVisible(true);

        //personNameF GUI TextField box is hidden under companyTittleF GUI TextField box
        if(currentUser.getRole()==Role.PERSON){
            personNameF.setEditable(true);
            personSurnameF.setEditable(true);
            phoneNumF.setEditable(true);
            loginF.setEditable(true);
            passwordF.setEditable(true);

            companyTitleF.setVisible(false);
            addressF.setVisible(false);

        }

        else if(currentUser.getRole()==Role.COMPANY){
            companyTitleF.setEditable(true);
            addressF.setEditable(true);
            phoneNumF.setEditable(true);
            loginF.setEditable(true);
            passwordF.setEditable(true);

            personNameF.setVisible(false);
            personSurnameF.setVisible(false);
        }

        else if(currentUser.getRole()==Role.EMPLOYEE||currentUser.getRole()==Role.ADMIN){
            personNameF.setEditable(true);
            personSurnameF.setEditable(true);
            addressF.setEditable(true);
            phoneNumF.setEditable(true);
            loginF.setEditable(true);
            passwordF.setEditable(true);

            companyTitleF.setVisible(false);
        }
    }

    public void saveUserInfo(ActionEvent actionEvent){
        User editedUser=userHibernateCtrl.getUserById(userId);

        if (loginF.getText().isEmpty()) {
            alertMsg("Login field is empty!", "Enter login!");
        }

        /*else if(//userHibernateCtrl.getUserByLogin(loginF.getText()) != null
                //&&
                userHibernateCtrl.getUserByLogin(loginF.getText()).equals(userHibernateCtrl.getUserById(userId)))
        {
            alertMsg("User with entered login already exists!","Enter other login.");
        }*/

        else if (loginF.getText().length() > 20) {
            alertMsg("Login is too long!", "Max allowed length is 20.");
        }

        else if (passwordF.getText().isEmpty()){
            alertMsg("Password field is empty!","Enter password!");
        }

        else if (passwordF.getText().length()>20){
            alertMsg("Password is too long!","Max allowed length is 20.");
        }

        else if (!passwordRepeatF.getText().equals(passwordF.getText())){
            alertMsg("Repeated password doesn't match!","Enter the same password!");
        }

        else if (phoneNumF.getText().isEmpty()) {
            alertMsg("Phone number field is empty!", "Enter phone number!");
        }

        else if (phoneNumF.getText().length() != 8) {
            alertMsg("Incorrect length.", "Enter 8 digits!");
        }

        else if (containsCharactersInteger(phoneNumF.getText())) {
            alertMsg("Phone number contains characters!", "Remove all characters!");
        }

        else{

            editedUser.setLogin(loginF.getText());
            editedUser.setPassword(passwordF.getText());
            editedUser.setPhoneNum("+370"+phoneNumF.getText());
            editedUser.setDateModified(LocalDate.now());

            if (editedUser.getRole() == Role.PERSON) {
                if (personNameF.getText().isEmpty()) {
                    alertMsg("Name field is empty!", "Enter name!");
                }
                else if (personNameF.getText().length() > 20) {
                    alertMsg("Name is too long!", "Max allowed length is 20.");
                }

                else if (containsDigits(personNameF.getText())) {
                    alertMsg("Name contains digits!", "Remove digits!");
                }

                else if (personSurnameF.getText().isEmpty()) {
                    alertMsg("Surname field is empty!", "Enter surname!");
                }

                else if (personSurnameF.getText().length() > 20) {
                    alertMsg("Surname is too long!", "Max allowed length is 20.");
                }

                else if (containsDigits(personSurnameF.getText())) {
                    alertMsg("Surname contains digits!", "Remove digits!");
                }

                else{

                    editedUser.setName(personNameF.getText());
                    editedUser.setSurname(personSurnameF.getText());

                    userHibernateCtrl.updateUser(editedUser);
                    loadUserInfo();
                }
            }

            else if (editedUser.getRole() == Role.COMPANY) {
                if (companyTitleF.getText().isEmpty()) {
                    alertMsg("Company title field is empty!", "Enter company title!");
                }

                else if (companyTitleF.getText().length() > 30) {
                    alertMsg("Company title is too long!", "Max allowed length is 30.");
                }

                else if (addressF.getText().isEmpty()) {
                    alertMsg("Address field is empty!", "Enter address!");
                }

                else if (addressF.getText().length() > 50) {
                    alertMsg("Address is too long!", "Max allowed length is 50.");
                }

                else {
                    editedUser.setCompanyTitle(companyTitleF.getText());
                    editedUser.setAddress(addressF.getText());

                    userHibernateCtrl.updateUser(editedUser);
                    loadUserInfo();
                }

            }


            else if (editedUser.getRole() == Role.EMPLOYEE ||editedUser.getRole() == Role.ADMIN) {
                if (personNameF.getText().isEmpty()) {
                    alertMsg("Name field is empty!", "Enter name!");
                }
                else if (personNameF.getText().length() > 20) {
                    alertMsg("Name is too long!", "Max allowed length is 20.");
                }

                else if (containsDigits(personNameF.getText())) {
                    alertMsg("Name contains digits!", "Remove digits!");
                }

                else if (personNameF.getText().isEmpty()) {
                    alertMsg("Name field is empty!", "Enter name!");
                }

                else if (personSurnameF.getText().length() > 20) {
                    alertMsg("Surname is too long!", "Max allowed length is 20.");
                }

                else if (containsDigits(personSurnameF.getText())) {
                    alertMsg("Surname contains digits!", "Remove digits!");
                }

                else if (addressF.getText().isEmpty()) {
                    alertMsg("Address field is empty!", "Enter address!");
                }

                else if (addressF.getText().length() > 50) {
                    alertMsg("Address is too long!", "Max allowed length is 50.");
                }

                else {
                    editedUser.setName(personNameF.getText());
                    editedUser.setSurname(personSurnameF.getText());
                    editedUser.setAddress(addressF.getText());

                    userHibernateCtrl.updateUser(editedUser);
                    loadUserInfo();
                }

            }

        }

    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/LoginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.setTitle("Book Shop");
        stage.setScene(scene);
        stage.show();
    }

    public void ClearViewFields(){
        bookAuthorsF.clear();
        bookDescriptionF.clear();
        bookInStockF.clear();
        bookPageNumF.clear();
        bookPriceF.clear();
        bookPublishDateF.setValue(null);//
        bookTitleF.clear();
    }

    public void ClearCreateFields(){
        createBookAuthors.clear();
        createBookDescription.clear();
        createBookInStock.clear();
        createBookPageNum.clear();
        createBookPrice.clear();
        createBookPublishDate.setValue(null);
        createBookTitle.clear();
    }


    //---------------------------------ADMIN TAB LOGIC START----------------------------------------------------------//

    public void loadUsers() {
        usersTable.setEditable(false);
        usersTable.getItems().clear();

        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colDateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        colDateModified.setCellValueFactory(new PropertyValueFactory<>("dateModified"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        colLogin.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setLogin(t.getNewValue());
                    //Update e record on change
                    DataBaseOperations.updateField("login", t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).getLogin(), t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).getUserId());
                }
        );
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(
                t -> {
                    String newName = t.getNewValue();
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setName(newName);
                    //Update in db
                    DataBaseOperations.updateField("`name`", newName, t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).getUserId());
                }
        );
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colSurname.setCellFactory(TextFieldTableCell.forTableColumn());
        colSurname.setOnEditCommit(
                t -> {
                    String newSurname = t.getNewValue();
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setSurname(t.getNewValue());
                    //Update db
                    DataBaseOperations.updateField("`surname`", newSurname, t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).getUserId());
                }
        );
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colRole.setCellFactory(TextFieldTableCell.forTableColumn());
        colRole.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setRole(t.getNewValue())
        );
        colCompanyTitle.setCellValueFactory(new PropertyValueFactory<>("companyTitle"));
        colCompanyTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        colCompanyTitle.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setCompanyTitle(t.getNewValue())
        );
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        colAddress.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setAddress(t.getNewValue())
        );

        colPhoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        colPhoneNum.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhoneNum.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setPhoneNum(t.getNewValue())
        );

        Callback<TableColumn<UserTableParameters, Void>, TableCell<UserTableParameters, Void>> cellFactory = new Callback<TableColumn<UserTableParameters, Void>, TableCell<UserTableParameters, Void>>() {
            @Override
            public TableCell<UserTableParameters, Void> call(final TableColumn<UserTableParameters, Void> param) {
                final TableCell<UserTableParameters, Void> cell = new TableCell<UserTableParameters, Void>() {

                    private final Button deleteButton = new Button("Delete");
                    private final Button editButton = new Button("Edit");

                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            UserTableParameters data = getTableView().getItems().get(getIndex());

                            if(data.getRole()==Role.ADMIN.name()){}

                            else {
                                userHibernateCtrl.removeUser(data.getUserId());
                                loadUsers();
                            }
                        });

                        editButton.setOnAction((ActionEvent event) -> {
                            User user=null;
                            UserTableParameters data = getTableView().getItems().get(getIndex());

                            if(data.getRole().equals(Role.ADMIN.name())){}///////////////////////////////////////////////////////

                            else if (data.getRole().equals(Role.COMPANY.name())){
                                user=userHibernateCtrl.getCompanyById(data.getUserId());
                                try {
                                    loadUserEditWindow(user);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            else {
                                user=userHibernateCtrl.getPersonById(data.getUserId());
                                try {
                                    loadUserEditWindow(user);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) //|| user.getRole()==Role.ADMIN)
                        {
                            setGraphic(null);
                        } else {    //cell.disabledProperty();
                            HBox pane = new HBox(deleteButton, editButton);
                            setGraphic(pane);
                        }
                    }
                };
                return cell;
            }
        };

        actionsField.setCellFactory(cellFactory);


        usersTable.setEditable(true);
        List<Person> persons = userHibernateCtrl.getAllPerson();
        for (Person p : persons) {
            UserTableParameters userTableParameters = new UserTableParameters();
            userTableParameters.setUserId(p.getId());
            userTableParameters.setLogin(p.getLogin());
            userTableParameters.setDateCreated(p.getDateCreated().toString());
            userTableParameters.setDateModified(p.getDateModified().toString());
            userTableParameters.setPhoneNum(p.getPhoneNum());
            userTableParameters.setName(p.getName());
            userTableParameters.setSurname(p.getSurname());
            userTableParameters.setRole(p.getRole().toString());
            if(p.getRole()==Role.EMPLOYEE || p.getRole()==Role.ADMIN){
                userTableParameters.setAddress(p.getAddress());
            }
            data.add(userTableParameters);
        }

        List<Company> companies = userHibernateCtrl.getAllCompany();
        for (Company c : companies) {
            UserTableParameters userTableParameters = new UserTableParameters();
            userTableParameters.setUserId(c.getId());
            userTableParameters.setLogin(c.getLogin());
            userTableParameters.setDateCreated(c.getDateCreated().toString());
            userTableParameters.setDateModified(c.getDateModified().toString());
            userTableParameters.setAddress(c.getAddress());
            userTableParameters.setRole(c.getRole().toString());
            userTableParameters.setPhoneNum(c.getPhoneNum());
            userTableParameters.setCompanyTitle(c.getCompanyTitle());
            data.add(userTableParameters);
        }

        usersTable.setItems(data);
    }

    public void loadUserEditWindow(User user) throws IOException {

        if(user==null){
            alertMsg("This user is unavailable.","The user was removed.");
        }

        else {
            FXMLLoader fxmlLoader = new FXMLLoader(BookShopWindow.class.getResource("../view/UserEditWindow.fxml"));
            Parent parent = fxmlLoader.load();
            UserEditWindow userEditWindow = fxmlLoader.getController();
            userEditWindow.SetFields(user);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit user");
            stage.setScene(scene);
            stage.showAndWait();
            loadUsers();
        }
    }

    public void loadAllCartsInfo(){

        List<Cart> carts = cartHibernateCtrl.getAllCarts();
        adminCartsInfo.getItems().clear();
        carts.forEach(cart->buyerCart.getItems().add(cart.getId() + ":" + cart.getStatus()));

    }

    //---------------------------------ADMIN TAB LOGIC END------------------------------------------------------------//


    //---------------------------------EMPLOYEE TAB LOGIC START----------------------------------------------------------//

    /*public void loadBooks() {
        employeeBookList.getItems().clear();
        List<Book> allBooksInShop = bookHibernateCtrl.getAllBooks(-1);
        allBooksInShop.forEach(book -> employeeBookList.getItems().add(book.getId() + ":" + book.getBookTitle() + " - " + book.getInStock() + " in stock"));// + "(" + (book.isAvailable() ? "AVAILABLE" : "UNAVAILABLE") + ")"));      //pakeisti su inStock tikrinimu
    }*/

    public void searchBooksEmp(){
        employeeBookList=searchBook(employeeBookList,searchBookEmpF, searchAuthorsEmpF, searchFromDateEmp, searchToDateEmp, -1);
    }

    public void createBook(ActionEvent event) {

        if(createBookTitle.getText().isEmpty()){
            alertMsg("Book title field is empty!","Enter title!");
        }

        else if(createBookTitle.getText().length()>20){
            alertMsg("Book title is too long!","Enter shorter title!");
        }

        else if(createBookAuthors.getText().isEmpty()){
            alertMsg("Book authors field is empty!","Enter authors!");
        }

        else if(containsDigits(createBookAuthors.getText())){
            alertMsg("Book authors contain digits!","Remove all digits!");
        }

        else if(createBookAuthors.getText().length()>50){
            alertMsg("Book authors character number is too high!","Enter less characters!");
        }

        else if(createBookPublishDate.getValue()==null){
            alertMsg("Publish date field is empty!","Enter publish date!");
        }

        /*else if(incorrectValuesLocalDate(createBookPublishDate.getValue().toString())){
            alertMsg("Book publish date contains incorrect characters!","Enter correct date!");
        }*/

        else if(createBookPageNum.getText().isEmpty()){
            alertMsg("Book page number field is empty!","Enter page number!");
        }

        else if(containsCharactersInteger(createBookPageNum.getText())){
            alertMsg("Book page number contains characters!","Remove all characters!");
        }

        else if(createBookInStock.getText().isEmpty()){
            alertMsg("Book in stock field is empty!","Enter in stock amount!");
        }

        else if(containsCharactersInteger(createBookInStock.getText())){
            alertMsg("Book in stock field contains characters!","Remove all characters!");
        }

        else if(createBookPrice.getText().isEmpty()){
            alertMsg("Book price field is empty!","Enter price!");
        }

        else if(containsCharactersDouble(createBookPrice.getText())){
            alertMsg("Book price field contains characters!","Remove all characters!");
        }

        else if(createBookDescription.getText().isEmpty()){
            alertMsg("Book description is empty!","Write description!");
        }

        else if(createBookDescription.getText().length()>2550){
            alertMsg("Book description is too long!","Enter shorter description!");
        }

        else {
            Book book = new Book(createBookTitle.getText(),
                    createBookDescription.getText(),
                    createBookPublishDate.getValue(),
                    Integer.parseInt(createBookPageNum.getText()),
                    createBookAuthors.getText(),
                   Math.floor(100.0*Double.parseDouble(createBookPrice.getText()))/100.0,
                    Integer.parseInt(createBookInStock.getText()));

            bookHibernateCtrl.createBook(book);
            ClearCreateFields();
        }

        searchBooksEmp();
    }

    public void editBook() {
        addBookButton.setDisable(true);
        updateBookButton.setVisible(true);

        Book currentBook = getBookById(employeeBookList.getSelectionModel().getSelectedItem().toString().split(":")[0]);

        if(currentBook==null){
            refresh();
            alertMsg("This book is unavailable.","It was removed.");
        }

        else {
            createBookTitle.setText(currentBook.getBookTitle());
            createBookDescription.setText(currentBook.getDescription());
            createBookPublishDate.setValue(currentBook.getPublishDate());
            createBookPageNum.setText(String.valueOf(currentBook.getPageNum()));
            createBookAuthors.setText(currentBook.getAuthors());
            createBookPrice.setText(String.valueOf(currentBook.getPrice()));
            createBookInStock.setText(String.valueOf(currentBook.getInStock()));
        }
    }

    public void deleteBook() {
        String bookId = employeeBookList.getSelectionModel().getSelectedItem().toString().split(":")[0];
        //if(bookId==null){alertMsg("This book is unavailable.","It was removed.");} bezsmyslena bo id ne null, nu potem pa etym id ne naxodit
            bookHibernateCtrl.removeBook(Integer.parseInt(bookId));
        searchBooksEmp();

    }

    public void refresh() {
        addBookButton.setDisable(false);
        updateBookButton.setVisible(false);
        createBookTitle.clear();
        createBookDescription.clear();
        createBookPublishDate.getEditor().clear();
        createBookPageNum.clear();
        createBookAuthors.clear();
        createBookPrice.clear();
        createBookInStock.clear();
    }

    public void updateBook() {
        Book currentBook = getBookById(employeeBookList.getSelectionModel().getSelectedItem().toString().split(":")[0]);

        if(currentBook==null){
            alertMsg("This book is unavailable.","It was removed.");
        }

        else if(createBookTitle.getText().isEmpty()){
            alertMsg("Book title field is empty!","Enter title!");
        }

        else if(createBookTitle.getText().length()>20){
            alertMsg("Book title is too long!","Enter shorter title!");
        }

        else if(createBookAuthors.getText().isEmpty()){
            alertMsg("Book authors field is empty!","Enter authors!");
        }

        else if(containsDigits(createBookAuthors.getText())){
            alertMsg("Book authors contain digits!","Remove all digits!");
        }

        else if(createBookAuthors.getText().length()>50){
            alertMsg("Book authors character number is too high!","Enter less characters!");
        }

        else if(createBookPublishDate.getValue()==null){
            alertMsg("Publish date field is empty!","Enter publish date!");
        }

        else if(createBookPageNum.getText().isEmpty()){
            alertMsg("Book page number field is empty!","Enter page number!");
        }

        else if(containsCharactersInteger(createBookPageNum.getText())){
            alertMsg("Book page number contains characters!","Remove all characters!");
        }

        else if(createBookInStock.getText().isEmpty()){
            alertMsg("Book in stock field is empty!","Enter in stock amount!");
        }

        else if(containsCharactersInteger(createBookInStock.getText())){
            alertMsg("Book in stock field contains characters!","Remove all characters!");
        }

        else if(createBookPrice.getText().isEmpty()){
            alertMsg("Book price field is empty!","Enter price!");
        }

        else if(containsCharactersDouble(createBookPrice.getText())){
            alertMsg("Book price field contains characters!","Remove all characters!");
        }

        else if(createBookDescription.getText().isEmpty()){
            alertMsg("Book description is empty!","Write description!");
        }

        else if(createBookDescription.getText().length()>2550){
            alertMsg("Book description is too long!","Enter shorter description!");
        }

        else {
            currentBook.setBookTitle(createBookTitle.getText());
            currentBook.setDescription(createBookDescription.getText());
            currentBook.setPublishDate(createBookPublishDate.getValue());
            currentBook.setPageNum(Integer.parseInt(createBookPageNum.getText()));
            currentBook.setAuthors(createBookAuthors.getText());
            currentBook.setPrice(Math.floor(100.0*Double.parseDouble(createBookPrice.getText()))/100.0);
            currentBook.setInStock(Integer.parseInt(createBookInStock.getText()));

            bookHibernateCtrl.updateBook(currentBook);
            refresh();
            searchBooksEmp();
        }
    }

    public void loadCartsInfo(){ //dabaigti

        List<Cart> carts = cartHibernateCtrl.getAllCarts();//getCartBySupervisingEmp(userHibernateCtrl.getUserById(userId));
        List<Cart> empCarts=new ArrayList<>();


        for(int i=0;i<carts.size();i++){
            Cart currentCart=carts.get(i);
            List<Person> currentSupervisingEmp = currentCart.getSupervisingEmployees();
            for(int j=0;j<currentSupervisingEmp.size();j++){
                Person person=currentSupervisingEmp.get(i);
                if(person.equals(userHibernateCtrl.getUserById(userId))){
                    empCarts.add(currentCart);
                }
            }
        }

        employeeCartsInfo.getItems().clear();
        empCarts.forEach(cart->employeeCartsInfo.getItems().add(cart.getId() + ":" + cart.getStatus()));

    }


    //---------------------------------EMPLOYEE TAB LOGIC END----------------------------------------------------------//



    //---------------------------------CUSTOMER TAB LOGIC START----------------------------------------------------------//

    public void searchBooksCust(){
        ClearViewFields();
        allBooksClient=searchBook(allBooksClient,searchBookCustF, searchAuthorsCustF, searchFromDateCust, searchToDateCust,0);
    }

    public void viewBookInfo(){
        Book currentBook = getBookById(allBooksClient.getSelectionModel().getSelectedItem().toString().split(":")[0]);

        if(currentBook==null){
            alertMsg("This book is unavailable.","It was removed.");
        }

        else {
            bookTitleF.setText(currentBook.getBookTitle());
            bookAuthorsF.setText(currentBook.getAuthors());
            bookPublishDateF.setValue(currentBook.getPublishDate());
            bookPageNumF.setText(String.valueOf(currentBook.getPageNum()));
            bookInStockF.setText(String.valueOf(currentBook.getInStock()));
            bookPriceF.setText(String.valueOf(currentBook.getPrice()));
            bookDescriptionF.setText(currentBook.getDescription());

            loadComments();
        }
    }

    public void createCart(ActionEvent event) {
        if(cart.getItems().isEmpty()) {
            alertMsg("Cart is empty!","Add books to the cart.");
        }

        else {
            User buyer = userHibernateCtrl.getUserById(userId);

            /*for(int j = 0; j < cart.getItems().size(); j++)
            {
                Book obj = cart.getItems().get(j);
                obj.setInStock(obj.getInStock()-1);
                *//*if(obj.getId() == Integer.parseInt(currentOrderList.getSelectionModel().getSelectedItem().toString().split(":")[0])){
                    cart.getItems().remove(j);
                    break;
                }*//*
            }*/

            //items are set before calling this method
            List<Person> people = userHibernateCtrl.getAllPerson();
            Person supervisingEmployee=new Person();
            for(int i=0;i<people.size();i++){
                supervisingEmployee = people.get(i);
                if(supervisingEmployee.getRole()==Role.EMPLOYEE) {
                    cart.getSupervisingEmployees().add(supervisingEmployee);
                }
            }
            //cart buyer yra nustatomas pasileidus bookshopwindow langui, kur yra nustatomas userId
            cart.setStatus(Status.IN_PROGRESS);

            cartHibernateCtrl.createCart(cart);

            cart=new Cart(buyer);
            currentOrderList.getItems().clear();
        }
    }

    public void viewCartsInfo(){

        List<Cart> carts = cartHibernateCtrl.getCartByBuyer(userHibernateCtrl.getUserById(userId));
        buyerCart.getItems().clear();
        carts.forEach(cart->buyerCart.getItems().add(cart.getId() + ":" + cart.getStatus()));

    }

    public void addToCart(ActionEvent event) {
        Book currentBook = getBookById(allBooksClient.getSelectionModel().getSelectedItem().toString().split(":")[0]);

        if(currentBook==null){
            alertMsg("This book is unavailable.","It was removed.");
        }

        else if(currentBook.getInStock()==0){
            alertMsg("This book is unavailable.","It is sold out.");
        }

        else {
            cart.getItems().add(currentBook);
            currentBook.setInStock(currentBook.getInStock()-1);
            bookHibernateCtrl.updateBook(currentBook);

            currentOrderList.getItems().clear();
            List<Book> cartList = cart.getItems();
            cartList.forEach(book -> currentOrderList.getItems().add(book.getId() + ":" + book.getBookTitle()));

            /*ClearViewFields();
            allBooksClient.getItems().clear();
            List<Book> inStockBookList = bookHibernateCtrl.getAllBooks(0);
            inStockBookList.forEach(book -> allBooksClient.getItems().add(book.getId() + ":" + book.getBookTitle()));
        */
            searchBooksCust();
        }

    }

    public void removeFromCart(ActionEvent event){
        /*Book currentBook = getBookById(allBooksClient.getSelectionModel().getSelectedItem().toString().split(":")[0]);

        cart.getItems().add(currentBook);
        currentBook.setInStock(currentBook.getInStock()+1);
        bookHibernateCtrl.updateBook(currentBook);
        //eta davno byla zakamenciravana*/
        for(int j = 0; j < cart.getItems().size(); j++)
        {
            Book obj = cart.getItems().get(j);
            if(obj.getId() == Integer.parseInt(currentOrderList.getSelectionModel().getSelectedItem().toString().split(":")[0])){
                cart.getItems().remove(j);
                obj.setInStock(obj.getInStock()+1);
                bookHibernateCtrl.updateBook(obj);
                break;
            }
        }

        currentOrderList.getItems().clear();
        List<Book> cartList = cart.getItems();
        cartList.forEach(book -> currentOrderList.getItems().add(book.getId() + ":" + book.getBookTitle()));

        /*ClearViewFields();
        allBooksClient.getItems().clear();
        List<Book> inStockBookList = bookHibernateCtrl.getAllBooks(0);
        inStockBookList.forEach(book -> allBooksClient.getItems().add(book.getId() + ":" + book.getBookTitle()));
    */
        searchBooksCust();
    }

    private void addTreeItem(Comment comment, TreeItem parent) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parent.getChildren().add(treeItem);
        comment.getReplies().forEach(sub -> addTreeItem(sub, treeItem));
    }

    public void loadComments() {
        Book currentBook = getBookById(allBooksClient.getSelectionModel().getSelectedItem().toString().split(":")[0]);
        bookCommentTree.setRoot(new TreeItem<String>("Comments:"));
        bookCommentTree.setShowRoot(false);
        bookCommentTree.getRoot().setExpanded(true);
        currentBook.getComments().forEach(comment -> addTreeItem(comment, bookCommentTree.getRoot()));
    }

    public void writeComment(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/WriteCommentWindow.fxml"));
        Parent parent = fxmlLoader.load();
        WriteCommentWindow writeCommentWindow = fxmlLoader.getController();
        writeCommentWindow.setData(parseInt(allBooksClient.getSelectionModel().getSelectedItem().toString().split(":")[0]), 0, userId);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Comment window");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        loadComments();
    }

    public void editComment(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/WriteCommentWindow.fxml"));
        Parent parent = fxmlLoader.load();
        WriteCommentWindow writeCommentWindow = fxmlLoader.getController();

        String currentCommentId = (bookCommentTree.getSelectionModel().getSelectedItem().toString().split("\\.")[0]).replace("TreeItem [ value: ", "");
        Comment currentComment = commentHibernateCtrl.getCommentById(parseInt(currentCommentId));
        writeCommentWindow.setData(parseInt(currentCommentId), currentComment.getCommentText());

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Edit window");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        loadComments();

    }

    public void addReply(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/WriteCommentWindow.fxml"));
        Parent parent = fxmlLoader.load();
        WriteCommentWindow writeCommentWindow = fxmlLoader.getController();
        String currentCommentId = (bookCommentTree.getSelectionModel().getSelectedItem().toString().split("\\.")[0]).replace("TreeItem [ value: ", "");
        writeCommentWindow.setData(0, parseInt(currentCommentId), userId);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Reply window");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        loadComments();

    }

    public void deleteComment(ActionEvent actionEvent) {
        String commentId = ((bookCommentTree.getSelectionModel().getSelectedItem().toString().split("\\.")[0]).replace("TreeItem [ value: ", ""));
        Book currentBook = commentHibernateCtrl.getCommentById(Integer.parseInt(commentId)).getBookComment();
        Comment currentComment=commentHibernateCtrl.getCommentById(Integer.parseInt(commentId));

        if(currentComment.getBookComment()==null){
            Comment tempParentComment=currentComment.getParentComment();
            for(int i=0;i<tempParentComment.getReplies().size();i++) {
                Comment tempComment = tempParentComment.getReplies().get(i);

                if (tempComment.getId() == Integer.parseInt(commentId)) {
                    tempParentComment.getReplies().remove(i);
                    commentHibernateCtrl.editComment(tempParentComment);
                    break;
                }
            }
        }
        else{
            for(int i=0;i<currentBook.getComments().size();i++){
                Comment tempComment=currentBook.getComments().get(i);

                if(tempComment.getId()==Integer.parseInt(commentId)){
                    currentBook.getComments().remove(i);
                    bookHibernateCtrl.updateBook(currentBook);
                    break;
                }
            }
        }

        loadComments();
    }

    //---------------------------------CUSTOMER TAB LOGIC END----------------------------------------------------------//


}

