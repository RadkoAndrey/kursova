package sample.Person.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.Person.model.Person;
import sample.Person.util.DateUtil;

//Если пользователь в программе выбрал кнопку "Изменить", то открываеться окно для изменения информации о студенте
public class PersonEditDialogController extends Person {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField birthdayField;


    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    //метод н пригодится, так как он вызывается автоматически
    @FXML
    private void initialize() {
    }

   //Устанавливает сцену для этого окна
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    //Задаёт студента, информацию о котором будем менять
    public void setPerson(Person person) {
        this.person = person;

        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        streetField.setText(person.getStreet());
        postalCodeField.setText(Integer.toString(person.getPostalCode()));
        cityField.setText(person.getCity());
        birthdayField.setText(DateUtil.format(person.getBirthday()));
        birthdayField.setPromptText("dd.mm.yyyy");
    }

   //Returns true - если пользователь кликнул OK, в другом случае - false.
    public boolean isOkClicked() {
        return okClicked;
    }

    //Метод, который вызывается, когда пользователь кликнул по кнопке OK...
    @FXML
    private void handleOk() {
        if (isInputValid()) {   //заменить значения на..
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setStreet(streetField.getText());
            person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
            person.setCity(cityField.getText());
            person.setBirthday(DateUtil.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();   //закрыть окно
        }
    }

    //Метод, который вызывается, когда пользователь кликнул по кнопке CANSEL...
    @FXML
    private void handleCancel() {
        dialogStage.close(); //закрыть окно
    }

    //Метод, который проверяет пользовательский ввод в текстовых полях
    private boolean isInputValid() {  //return true, если пользовательский ввод правильно введен
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "Вы не вказали ім'я студента!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "Вы не вказали прізвище студента!\n";
        }
        if (streetField.getText() == null || streetField.getText().length() == 0) {
            errorMessage += "Не вказана вулиця!\n";
        }

        if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
            errorMessage += "Не вказаний поштовий індекс!\n";
        } else {
            // преобразовуем почтовый код в int
            try {
                Integer.parseInt(postalCodeField.getText());
            } catch (NumberFormatException e) {  //если пользователь ввел почтовый код типа String(Double)
                errorMessage += "Не вірно вказаний поштовий індекс (будь-ласка, введіть ціле число)!\n";
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage += "Не вказано місто!\n";
        }

        if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
            errorMessage += "Не вказано день народження студента!\n";
        } else {
            if (!DateUtil.validDate(birthdayField.getText())) {  //если пользователь не верно указал дату дня рождения
                errorMessage += "Не вірно вказано день народження(будь-ласка, використайте формат дати dd.mm.yyyy)!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            //  Показываем сообщение об ошибке введения данных пользователем
            Alert alert = new Alert(AlertType.ERROR);  //Alert -  выводит на экран окно с сообщением и приостанавливает выполнение кода, пока пользователь не нажмёт «ОК»
            alert.initOwner(dialogStage);
            alert.setTitle("Помилка введення нових данних!");
            alert.setHeaderText("Будь-ласка, виправте недійсні поля");
            alert.setContentText(errorMessage);
            
            alert.showAndWait(); //показать окно с ошибкой
            
            return false;
        }
    }
}