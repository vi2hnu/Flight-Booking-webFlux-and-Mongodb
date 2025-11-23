package org.example.flightapp.model.entity;

import lombok.Data;
import org.example.flightapp.model.enums.Gender;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "Users")
@Data
public class Users {
    @Id
    private String id;
    private String name;
    private String email;
    Gender gender;
}
