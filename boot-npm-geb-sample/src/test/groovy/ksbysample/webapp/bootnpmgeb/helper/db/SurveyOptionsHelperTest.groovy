package ksbysample.webapp.bootnpmgeb.helper.db

import ksbysample.webapp.bootnpmgeb.entity.SurveyOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class SurveyOptionsHelperTest extends Specification {

    @Autowired
    private SurveyOptionsHelper soh

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
