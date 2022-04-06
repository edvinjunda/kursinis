package fxControllers;

import books.Book;
import dataBaseOperations.DataBaseOperations;
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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import static utils.Utils.*;

public class BookShopWindow implements Initializable {
    @FXML
    public HBox searchBar;
    @FXML
    public TextField searchBookF;
    @FXML
    public DatePicker searchFromDate;
    @FXML
    public DatePicker searchToDate;
    @FXML
    public TextField searchAuthorsF;
    @FXML
    public ListView<String> allBooksClient;
    //@FXML
    //public TreeView bookCommentTree;      //veliau implementuoti
    //@FXML
    //public ListView currentOrderList;     //veliau implementuoti

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
    public Button logOutButton;
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


    private int userId;

    //private static DecimalFormat df = new DecimalFormat("#.##");

    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookShop");
    UserHibernateCtrl userHibernateCtrl = new UserHibernateCtrl(entityManagerFactory);
    BookHibernateCtrl bookHibernateCtrl = new BookHibernateCtrl(entityManagerFactory);



    @Override
    public void initialize(URL location, ResourceBundle resources) { updateBookButton.setVisible(false); }

    public void setUserId(int userId) {
        this.userId = userId;
        fillTablesByUser();
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

        ClearViewFields();
        allBooksClient.getItems().clear();
        List<Book> inStockBookList = bookHibernateCtrl.getAllBooks(0);
        inStockBookList.forEach(book -> allBooksClient.getItems().add(book.getId() + ":" + book.getBookTitle()));
    }

    private Book getBookById(String id) {
        return bookHibernateCtrl.getBookById(Integer.parseInt(id));
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

    /*

    private void addTreeItem(Comment comment, TreeItem parent) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parent.getChildren().add(treeItem);
        comment.getReplies().forEach(sub -> addTreeItem(sub, treeItem));
    }

     */

    //---------------------------------ADMIN TAB LOGIC START----------------------------------------------------------//

    public void loadUsers() {
        usersTable.setEditable(true);
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
            //userEditWindow.setAdminId(userId);
            Scene scene = new Scene(parent);
            //Stage stage = (Stage) searchAuthorsF.getScene().getWindow();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit user");
            stage.setScene(scene);
            stage.show();
        }
    }

    //---------------------------------ADMIN TAB LOGIC END------------------------------------------------------------//


    //---------------------------------EMPLOYEE TAB LOGIC START----------------------------------------------------------//

    public void loadBooks() {
        employeeBookList.getItems().clear();
        List<Book> allBooksInShop = bookHibernateCtrl.getAllBooks(-1);
        allBooksInShop.forEach(book -> employeeBookList.getItems().add(book.getId() + ":" + book.getBookTitle() + " - " + book.getInStock() + " in stock"));// + "(" + (book.isAvailable() ? "AVAILABLE" : "UNAVAILABLE") + ")"));      //pakeisti su inStock tikrinimu
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

        loadBooks();
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
            loadBooks();

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
            currentBook.setBookTitle(createBookTitle.getText());
            currentBook.setDescription(createBookDescription.getText());
            currentBook.setPublishDate(createBookPublishDate.getValue());
            currentBook.setPageNum(Integer.parseInt(createBookPageNum.getText()));
            currentBook.setAuthors(createBookAuthors.getText());
            currentBook.setPrice(Math.floor(100.0*Double.parseDouble(createBookPrice.getText()))/100.0);
            currentBook.setInStock(Integer.parseInt(createBookInStock.getText()));

            bookHibernateCtrl.updateBook(currentBook);
            refresh();
            loadBooks();
        }
    }

    //---------------------------------EMPLOYEE TAB LOGIC END----------------------------------------------------------//



    //---------------------------------CUSTOMER TAB LOGIC START----------------------------------------------------------//

    public void searchBook() {
        List<Book> filteredBooks = bookHibernateCtrl.getFilteredBooks(searchBookF.getText(), searchFromDate.getValue(), searchToDate.getValue(), searchAuthorsF.getText());
        allBooksClient.getItems().clear();
        filteredBooks.forEach(book -> allBooksClient.getItems().add(book.getId() + ":" + book.getBookTitle()));
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
        }
    }

    public void createCart(ActionEvent event) {
    }
/*
    public void loadComments() {
        Book currentBook = getBookById(allBooksClient.getSelectionModel().getSelectedItem().split(":")[0]);
        bookCommentTree.setRoot(new TreeItem<String>("Comments:"));
        bookCommentTree.setShowRoot(false);
        bookCommentTree.getRoot().setExpanded(true);
        currentBook.getComments().forEach(task -> addTreeItem(task, bookCommentTree.getRoot()));
    }
    public void writeReview() throws IOException {
        //Susikurti nauja forma arba gal galima su alert
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/write-comment-page.fxml"));
        Parent parent = fxmlLoader.load();
        WriteCommentPage writeCommentPage = fxmlLoader.getController();
        writeCommentPage.setBookId(Integer.parseInt(allBooksClient.getSelectionModel().getSelectedItem().split(":")[0]), 0);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Book shop ITf");
        stage.setScene(scene);
        stage.showAndWait();
    }*/

    //---------------------------------CUSTOMER TAB LOGIC END----------------------------------------------------------//


}

