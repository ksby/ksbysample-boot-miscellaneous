package ksbysample.webapp.bootnpmgeb.mapper

import ksbysample.webapp.bootnpmgeb.entity.SurveyOptions
import ksbysample.webapp.bootnpmgeb.helper.db.SurveyOptionsHelper
import ksbysample.webapp.bootnpmgeb.session.SessionData
import ksbysample.webapp.bootnpmgeb.values.JobValues
import ksbysample.webapp.bootnpmgeb.values.SexValues
import ksbysample.webapp.bootnpmgeb.values.Type1Values
import ksbysample.webapp.bootnpmgeb.values.Type2Values
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.ConfirmForm
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput01Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput02Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput03Form
import org.apache.commons.lang3.StringUtils
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.util.stream.Collectors

@SpringBootTest
class SessionData2ConfirmFormTypeMapTest extends Specification {

    @Autowired
    private ModelMapper modelMapper

    @Autowired
    private SurveyOptionsHelper soh

    InquiryInput01Form inquiryInput01Form
    InquiryInput02Form inquiryInput02Form
    InquiryInput03Form inquiryInput03Form
    SessionData sessionData

    def setup() {
        inquiryInput01Form = new InquiryInput01Form(
                lastname: "田中"
                , firstname: "太郎"
                , lastkana: "たなか"
                , firstkana: "たろう"
                , sex: SexValues.MALE.value
                , age: "30"
                , job: JobValues.EMPLOYEE.value
        )
        inquiryInput02Form = new InquiryInput02Form(
                zipcode1: "100"
                , zipcode2: "0005"
                , address: "東京都千代田区丸の内"
                , tel1: "03"
                , tel2: "1234"
                , tel3: "5678"
                , email: "test@sample.co.jp"
        )
        inquiryInput03Form = new InquiryInput03Form(
                type1: Type1Values.PRODUCT.value
                , type2: [Type2Values.CATALOGUE.value
                          , Type2Values.ESTIMATE.value
                          , Type2Values.OTHER.value]
                , inquiry: "これは\r\nテスト\r\nです。"
                , survey: soh.selectItemList("survey").stream()
                .map { SurveyOptions surveyOptions -> surveyOptions.itemValue }
                .collect()
        )
        sessionData = new SessionData(
                inquiryInput01Form: inquiryInput01Form
                , inquiryInput02Form: inquiryInput02Form
                , inquiryInput03Form: inquiryInput03Form
        )
    }

    def "全ての項目に値がセットされている場合のテスト"() {
        setup:
        ConfirmForm confirmForm = modelMapper.map(sessionData, ConfirmForm)

        expect:
        confirmForm.name == "田中　太郎"
        confirmForm.kana == "たなか　たろう"
        confirmForm.sex == SexValues.MALE.text
        confirmForm.age == "30"
        confirmForm.job == JobValues.EMPLOYEE.text
        confirmForm.zipcode == "100-0005"
        confirmForm.address == "東京都千代田区丸の内"
        confirmForm.tel == "03-1234-5678"
        confirmForm.email == "test@sample.co.jp"
        confirmForm.type1 == Type1Values.PRODUCT.text
        confirmForm.type2 == [Type2Values.CATALOGUE.text
                              , Type2Values.ESTIMATE.text
                              , Type2Values.OTHER.text].stream()
                .collect(Collectors.joining("、"))
        confirmForm.inquiry == "これは\r\nテスト\r\nです。"
        confirmForm.survey == soh.selectItemList("survey").stream()
                .map { SurveyOptions surveyOptions -> surveyOptions.itemName }
                .collect()
    }

    def "任意項目には値がセットされていない場合のテスト"() {
        setup:
        inquiryInput01Form.job = StringUtils.EMPTY
        inquiryInput02Form.tel1 = StringUtils.EMPTY
        inquiryInput02Form.tel2 = StringUtils.EMPTY
        inquiryInput02Form.tel3 = StringUtils.EMPTY
        inquiryInput03Form.survey = []
        ConfirmForm confirmForm = modelMapper.map(sessionData, ConfirmForm)

        expect:
        confirmForm.name == "田中　太郎"
        confirmForm.kana == "たなか　たろう"
        confirmForm.sex == SexValues.MALE.text
        confirmForm.age == "30"
        confirmForm.job == StringUtils.EMPTY
        confirmForm.zipcode == "100-0005"
        confirmForm.address == "東京都千代田区丸の内"
        confirmForm.tel == StringUtils.EMPTY
        confirmForm.email == "test@sample.co.jp"
        confirmForm.type1 == Type1Values.PRODUCT.text
        confirmForm.type2 == [Type2Values.CATALOGUE.text
                              , Type2Values.ESTIMATE.text
                              , Type2Values.OTHER.text].stream()
                .collect(Collectors.joining("、"))
        confirmForm.inquiry == "これは\r\nテスト\r\nです。"
        confirmForm.survey == []
    }

}
