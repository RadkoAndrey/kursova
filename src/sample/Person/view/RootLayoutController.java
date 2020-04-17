package sample.Person.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import sample.Person.MainApp;

import java.io.File;

// Контроллер для корневого макета
public class RootLayoutController extends MainApp {

    // Ссылка на главное приложение
    private MainApp mainApp;

    //Вызывается главным приложением, чтобы оставить ссылку на самого себя
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    //Создаёт пустую адресную книгу
    @FXML
    private void handleNew() {
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);
    }

    //Открывает FileChooser, чтобы пользователь имел возможность выбрать адресную книгу для загрузки.
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Задаём фильтр расширений
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Показываем диалог загрузки файла
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadPersonDataFromFile(file);
        }
    }

    //Сохраняет файл в файл адресатов, который в настоящее время открыт. Если файл не открыт, то отображается диалог "save as".
    @FXML
    private void handleSave() {
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null) {
            mainApp.savePersonDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    //Сохраняет файл в файл адресатов, который в настоящее время открыт.
    //Если файл не открыт, то отображается диалог "save as".
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Задаём фильтр расширений
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Показываем диалог сохранения файла
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Убедитесь, что оно имеет правильное расширение
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePersonDataToFile(file);
        }
    }

    // Открывает диалоговое окно about.
    @FXML
    private void handleAbout() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Помічник класного керівника");
    	alert.setHeaderText("Довідка");
    	alert.setContentText("Автор: Радько А., 4 курс, курсова робота 2019 року ");

    	alert.showAndWait();
    }

    // Закрывает приложение.
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    // Открывает статистику дня рождения
    @FXML
    private void handleShowBirthdayStatistics() {
      mainApp.showBirthdayStatistics();
    }


}