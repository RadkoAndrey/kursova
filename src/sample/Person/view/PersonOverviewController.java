package sample.Person.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.Person.MainApp;
import sample.Person.model.Person;
import sample.Person.util.DateUtil;

public class PersonOverviewController extends MainApp{
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    // Ссылка на главное приложение
    private MainApp mainApp;

   //Конструктор, который вызывается раньше метода initialize().
    public PersonOverviewController() {
    }

   // Инициализация класса-контроллера. Этот метод вызывается автоматически после того, как fxml-файл будет загружен.
    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами
        //Метод setCellValueFactory(...) определяет, какое поле внутри класса Person будут использоваться для конкретного столбца в таблице
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        
        //  Очистка дополнительной информации об адресате
        showPersonDetails(null);

       // изменения выбора пользователем, и при изменении - отображаем дополнительную информацию об выбраном адресате
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    //Вызывается главным приложением, которое даёт на себя ссылку
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Добавление в таблицу данных из наблюдаемого списка
        personTable.setItems(mainApp.getPersonData());
    }
    
   //Заполняет все текстовые поля, отображая подробности об адресате. Если указанный адресат = null, то все текстовые поля очищаются.
    private void showPersonDetails(Person person) {
        if (person != null) {    //person — адресат типа Person или null
            // Заполняем метки информацией из объекта person.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
        } else {
            // Если Person = null, то убираем весь текст
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }
    
    //Кнопка ВИДАЛИТИ - Вызывается, когда пользователь кликает по кнопке удаления
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {  //если студент выбран, то очистить с таблицы
            personTable.getItems().remove(selectedIndex);
        } else {   //иначе....
            // Ничего не выбрано
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Видалити: Вибору немає!");
            alert.setHeaderText("Особа не обрана");
            alert.setContentText("Виберіть студента для видалення в таблиці зліва");
            
            alert.showAndWait();
        }
    }
    
   //Вызывается, когда пользователь кликает по кнопке ДОБАВИТИ...
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

   //Вызывается, когда пользователь кликает по кнопке ЗМІНИТИ..
    // где Открывает диалоговое окно для изменения выбранного студента
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
            //// Ничего не выбрано
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Змінити: Вибору немає!");
            alert.setHeaderText("Особа не обрана");
            alert.setContentText("Виберіть студента для редагування інформації в таблиці зліва");

            alert.showAndWait();
        }
    }
}