package ksbysample.webapp.bootnpmgeb.helper.db;

import ksbysample.webapp.bootnpmgeb.dao.SurveyOptionsDao;
import ksbysample.webapp.bootnpmgeb.entity.SurveyOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * SURVEY_OPTIONS テーブルデータ取得用 Helper クラス
 */
@Component("soh")
public class SurveyOptionsHelper {

    private final SurveyOptionsDao surveyOptionsDao;

    /**
     * コンストラクタ
     *
     * @param surveyOptionsDao {@SurveyOptionsDao} オブジェクト
     */
    public SurveyOptionsHelper(SurveyOptionsDao surveyOptionsDao) {
        this.surveyOptionsDao = surveyOptionsDao;
    }

    /**
     * SURVEY_OPTIONS テーブルから指定されたグループ名のリストを取得する
     *
     * @param groupName グループ名
     * @return {@SurveyOptions} オブジェクトのリスト
     */
    public List<SurveyOptions> selectItemList(String groupName) {
        List<SurveyOptions> surveyOptionsList = surveyOptionsDao.selectByGroupName(groupName);
        if (CollectionUtils.isEmpty(surveyOptionsList)) {
            throw new IllegalArgumentException("指定されたグループ名のデータは登録されていません");
        }
        return surveyOptionsList;
    }

}
