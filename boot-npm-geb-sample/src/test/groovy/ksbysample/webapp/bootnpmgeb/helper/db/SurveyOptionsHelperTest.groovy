package ksbysample.webapp.bootnpmgeb.helper.db

import ksbysample.webapp.bootnpmgeb.dao.SurveyOptionsDao
import ksbysample.webapp.bootnpmgeb.entity.SurveyOptions
import spock.lang.Specification

class SurveyOptionsHelperTest extends Specification {

    SurveyOptionsDao surveyOptionsDao

    SurveyOptionsHelper soh

    def setup() {
        surveyOptionsDao = Stub(SurveyOptionsDao) {
            selectByGroupName("survey") >> [
                    [groupName: "survey", itemValue: "1", itemName: "選択肢１だけ長くしてみる", itemOrder: 1],
                    [groupName: "survey", itemValue: "2", itemName: "選択肢２", itemOrder: 2],
                    [groupName: "survey", itemValue: "3", itemName: "選択肢３", itemOrder: 3],
                    [groupName: "survey", itemValue: "4", itemName: "選択肢４", itemOrder: 4],
                    [groupName: "survey", itemValue: "5", itemName: "選択肢５が少し長い", itemOrder: 5],
                    [groupName: "survey", itemValue: "6", itemName: "選択肢６", itemOrder: 6],
                    [groupName: "survey", itemValue: "7", itemName: "選択肢７", itemOrder: 7],
                    [groupName: "survey", itemValue: "8", itemName: "８", itemOrder: 8]
            ]
            selectByGroupName("notexists") >> []
        }
        soh = new SurveyOptionsHelper(surveyOptionsDao)
    }

    def "登録されているグループ名を指定してselectItemListメソッドを呼ぶとリストが取得できる"() {
        setup:
        List<SurveyOptions> surveyOptionsList = soh.selectItemList("survey")

        expect:
        surveyOptionsList.size() == 8
        surveyOptionsList[0].itemValue == "1"
        surveyOptionsList[0].itemName == "選択肢１だけ長くしてみる"
        surveyOptionsList[7].itemValue == "8"
        surveyOptionsList[7].itemName == "８"
    }

    def "登録されていないグループ名を指定してselectItemListメソッドを呼ぶとIllegalArgumentExceptionがthrowされる"() {
        when:
        List<SurveyOptions> surveyOptionsList = soh.selectItemList("notexists")

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "指定されたグループ名のデータは登録されていません"
    }

}
