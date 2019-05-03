package ksbysample.app.samplecmdapp;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

@Data
public class EmployeeCsvRecord {

    @Parsed(field = "name")
    private String name;

    @Parsed(field = "age")
    private Integer age;

    @Parsed(field = "sex")
    private String sex;

}
