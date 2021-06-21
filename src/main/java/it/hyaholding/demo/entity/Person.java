package it.hyaholding.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("person")
public class Person {

    //indent -> crtl+alt+l
    @Id
    private String id;
    private String name;
    private String surname;
    private String address;
    private Sex sex;

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ").append(surname);
        return sb.toString();
    }
}
