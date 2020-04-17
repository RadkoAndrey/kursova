package sample.Person.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

//Вспомогательный класс для обёртывания списка адресатов.
//  Используется для сохранения списка адресатов в XML.
@XmlRootElement(name = "persons")  //@XmlRootElement - определяет имя корневого элемента.
public class PersonListWrapper {

    private List<Person> persons;

    @XmlElement(name = "person") //@XmlElement - необязательное имя, которое мы можем задать для элемента.
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}