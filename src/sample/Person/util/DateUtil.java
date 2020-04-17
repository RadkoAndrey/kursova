package sample.Person.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//Вспомогательные функции для работы с датами
public class DateUtil {

    //Шаблон даты, используемый для преобразования.
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    //Форматировщик даты
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern(DATE_PATTERN);

   // Возвращает полученную дату в виде хорошо отформатированной строки
    public static String format(LocalDate date) {
        if (date == null) {  //date - дата, которая будет возвращена в виде строки
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

   //Преобразует строку, которая отформатирована по правилам шаблона DATE_PATTERN в объект LocalDate
    public static LocalDate parse(String dateString) {   //dateString - дата в виде String
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;  //Возвращает null, если строка не может быть преобразована
        }
    }

   //Проверяет, является ли строка корректной датой
    public static boolean validDate(String dateString) {
       // Разобор строки(return true, если строка является корректной датой)
        return DateUtil.parse(dateString) != null;
    }
}