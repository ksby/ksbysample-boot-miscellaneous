package ksbysample.webapp.bootnpmgeb.dao;

import ksbysample.webapp.bootnpmgeb.entity.SurveyOptions;
import ksbysample.webapp.bootnpmgeb.util.doma.ComponentAndAutowiredDomaConfig;
import org.seasar.doma.*;

import java.util.List;

/**
 *
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface SurveyOptionsDao {

    /**
     * @param groupName groupName
     * @param itemValue itemValue
     * @return the SurveyOptions entity
     */
    @Select
    SurveyOptions selectById(String groupName, String itemValue);

    /**
     * 指定されたグループ名のレコードのリストを取得する
     *
     * @param groupName グループ名
     * @return {@SurveyOptions} エンティティのリスト
     */
    @Select
    List<SurveyOptions> selectByGroupName(String groupName);

    /**
     * @param entity entity
     * @return affected rows
     */
    @Insert
    int insert(SurveyOptions entity);

    /**
     * @param entity entity
     * @return affected rows
     */
    @Update
    int update(SurveyOptions entity);

    /**
     * @param entity entity
     * @return affected rows
     */
    @Delete
    int delete(SurveyOptions entity);
}