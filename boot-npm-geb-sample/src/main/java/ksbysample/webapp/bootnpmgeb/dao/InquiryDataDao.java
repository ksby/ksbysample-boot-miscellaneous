package ksbysample.webapp.bootnpmgeb.dao;

import ksbysample.webapp.bootnpmgeb.entity.InquiryData;
import ksbysample.webapp.bootnpmgeb.util.doma.ComponentAndAutowiredDomaConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface InquiryDataDao {

    /**
     * @param id
     * @return the InquiryData entity
     */
    @Select
    InquiryData selectById(Integer id);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(InquiryData entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(InquiryData entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(InquiryData entity);
}