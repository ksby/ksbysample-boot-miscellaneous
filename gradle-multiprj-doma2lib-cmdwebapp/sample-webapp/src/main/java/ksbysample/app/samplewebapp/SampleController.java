package ksbysample.app.samplewebapp;

import ksbysample.lib.doma2lib.dao.EmployeeDao;
import ksbysample.lib.doma2lib.entity.Employee;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sample")
public class SampleController {

    private final EmployeeDao employeeDao;

    public SampleController(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String index() {
        List<Employee> employeeList = employeeDao.selectAll();
        return employeeList.stream()
                .map(employee ->
                        String.format("name = %s, age = %d, sex = %s"
                                , employee.getName()
                                , employee.getAge()
                                , employee.getSex()))
                .collect(Collectors.joining("\n"));
    }

}
