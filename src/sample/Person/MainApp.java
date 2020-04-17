package sample.Person;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Person.model.Person;
import sample.Person.model.PersonListWrapper;
import sample.Person.view.BirthdayStatisticsController;
import sample.Person.view.PersonEditDialogController;
import sample.Person.view.PersonOverviewController;
import sample.Person.view.RootLayoutController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    public static Stage primaryStage;
    private BorderPane rootLayout;

    //Конструктор
    public MainApp() {
        // В качестве образца добавляем некоторые  начальные данные
        personData.add(new Person("Дмитро ", "Швайка"));
        personData.add(new Person("Влад", "Сохань"));
        personData.add(new Person("Денис", "Дуброва"));
        personData.add(new Person("Станіслав", "Кошовець"));
        personData.add(new Person("Карина", "Чмуневич"));
        personData.add(new Person("Максим", "Сніжко"));
        personData.add(new Person("Тарас", "Шутко"));
        personData.add(new Person("Андрій", "Радько"));
        personData.add(new Person("Сергій", "Степаненко"));
    }

    //Данные, в виде наблюдаемого списка адресатов
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    //Возвращает данные в виде наблюдаемого списка адресатов
    public ObservableList<Person> getPersonData() {
        return personData;
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Помічник вчителя");

        // Устанавливаем иконку приложения
        this.primaryStage.getIcons().add(new Image("file:src/images/icon_address_book.png"));
        initRootLayout();

        showPersonOverview();

    }

    //Инициализирует корневой макет
    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Отображаем сцену, содержащую корневой маке
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даём контроллеру доступ к главному прилодению
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Попытка загрузить последний открытый файл с адресатами.
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

    //Показывает в корневом макете сведения об адресатах.
    public void showPersonOverview() {
        try {
            // Загружаем сведения об адресатах
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            //Помещаем сведения об адресатах в центр корневого макета
            rootLayout.setCenter(personOverview);

            // Даём контроллеру доступ к главному приложению
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   //Открывает диалоговое окно для изменения деталей указанного студента
   // Если пользователь кликнул OK, то изменения сохраняются в предоставленном объекте студента и возвращается значение true
    public boolean showPersonEditDialog(Person person) {   //person - объект адресата, который надо изменить
        try {
            // Загружаем fxml-файл и создаём новую сцену для всплывающего диалогового окна
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage
            Stage dialogStage = new Stage();   //окно
            dialogStage.setTitle("Студент: деталі");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);  //показать окно

            // иконка диалогового окна
            dialogStage.getIcons().add(new Image("file:src/images/person.png"));

            // Передаём студента в контроллер
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();   //return true, если пользователь кликнул OK, в противном случае false
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Открывает диалоговое окно для вывода статистики дней рождений.
    public void showBirthdayStatistics() {
        try {
            // Загружает fxml-файл и создаёт новую сцену для всплывающего окна
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Статистика Днів Народжень");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаёт адресатов в контроллер
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(personData);

            // иконка диалогового окна
            dialogStage.getIcons().add(new Image("file:src/images/calendar.png"));
            
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Возвращает preference файла студентов, то есть, последний открытый файл.
       // Если preference не был найден, то возвращается null.
    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class); //preference считывается из реестра, специфичного для конкретной операционной системы.
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

   //Метод, который Задаёт путь текущему загруженному файлу. Этот путь сохраняется в реестре, специфичном для конкретной операционной системы.
    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {  // file - файл или null, чтобы удалить путь
            prefs.put("filePath", file.getPath());

            // Обновление заглавия сцены
            primaryStage.setTitle("Помічник класного керівника - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Обновление заглавия сцены
            primaryStage.setTitle("Помічник класного керівника");
        }
    }
    
    //Загружает информацию об адресатах из указанного файла
    //Где текущая информация об адресатах будет заменена
    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            // Сохраняем путь к файлу в реестре
            setPersonFilePath(file);

        } catch (Exception e) { //ловит ЛЮБОЕ исключение
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Помилка!");
        	alert.setHeaderText("Не вдалося завантажити дані");
        	alert.setContentText("Не вдалося завантажити дані з файлу:\n" + file.getPath());
        	
        	alert.showAndWait();
        }
    }

    //Сохраняет текущую информацию об адресатах в указанном файле
    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обёртываем наши данные об адресатах
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            // Сохраняем XML в файл
            m.marshal(wrapper, file);

            // Сохраняем путь к файлу в реестре
            setPersonFilePath(file);
        } catch (Exception e) { // catches ANY exception
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Помилка!");
        	alert.setHeaderText("Не вдалося зберегти дані");
        	alert.setContentText("Не вдалося зберегти дані у файл:\n" + file.getPath());
        	
        	alert.showAndWait();
        }
    }

    //Возвращает главную сцену
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}