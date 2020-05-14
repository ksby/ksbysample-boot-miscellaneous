package ksbysample.webapp.bootnpmgeb.dao;

import ksbysample.webapp.bootnpmgeb.entity.InquiryData;
import ksbysample.webapp.bootnpmgeb.util.doma.ComponentAndAutowiredDomaConfig;
import org.seasar.doma.*;

import java.sql.Clob;

/**
 *
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface InquiryDataDao {

    /**
     * @param id id
     * @return the InquiryData entity
     */
    @Select
    InquiryData selectById(Integer id);

    /**
     * @param entity entity
     * @return affected rows
     */
    @Insert
    int insert(InquiryData entity);

    /**
     * @param entity entity
     * @return affected rows
     */
    @Update
    int update(InquiryData entity);

    /**
     * @param entity entity
     * @return affected rows
     */
    @Delete
    int delete(InquiryData entity);

    /**
     * Clob 生成用
     *
     * @return {@Clob} オブジェクト
     */
    @ClobFactory
    Clob createClob();

}
