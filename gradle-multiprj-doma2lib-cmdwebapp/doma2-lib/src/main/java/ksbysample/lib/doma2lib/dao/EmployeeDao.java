package ksbysample.lib.doma2lib.dao;

import ksbysample.lib.doma2lib.entity.Employee;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

import java.util.List;

/**
 */
@Dao
@ConfigAutowireable
public interface EmployeeDao {

    /**
     * @param id
     * @return the Employee entity
     */
    @Select
    Employee selectById(Integer id);

    @Select
    List<Employee> selectAll();
    
    /**
     * @param entity
     * @return affected rows
     */
    @Insert(excludeNull = true)
    int insert(Employee entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(Employee entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(Employee entity);
}