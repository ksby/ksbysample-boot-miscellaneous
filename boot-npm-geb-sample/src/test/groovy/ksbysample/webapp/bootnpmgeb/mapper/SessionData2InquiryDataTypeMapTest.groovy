package ksbysample.webapp.bootnpmgeb.mapper

import ksbysample.webapp.bootnpmgeb.dao.InquiryDataDao
import ksbysample.webapp.bootnpmgeb.entity.InquiryData
import ksbysample.webapp.bootnpmgeb.entity.SurveyOptions
import ksbysample.webapp.bootnpmgeb.helper.db.SurveyOptionsHelper
import ksbysample.webapp.bootnpmgeb.session.SessionData
import ksbysample.webapp.bootnpmgeb.values.JobValues
import ksbysample.webapp.bootnpmgeb.values.SexValues
import ksbysample.webapp.bootnpmgeb.values.Type1Values
import ksbysample.webapp.bootnpmgeb.values.Type2Values
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput01Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput02Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput03Form
import org.apache.commons.lang3.StringUtils
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.StreamUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.sql.Clob
import java.util.stream.Collectors

@SpringBootTest
class SessionData2InquiryDataTypeMapTest extends Specification {

    @Autowired
    private ModelMapper modelMapper

    @Autowired
    private SurveyOptionsHelper soh

    @Autowired
    private InquiryDataDao inquiryDataDao

    InquiryInput01Form inquiryInput01Form
    InquiryInput02Form inquiryInput02Form
    InquiryInput03Form inquiryInput03Form
    SessionData sessionData
    Clob inquiryClob

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
        inquiryClob = inquiryDataDao.createClob()
        inquiryClob.setString(1, "これは\r\nテスト\r\nです。")
    }

    def "全ての項目に値がセットされている場合のテスト"() {
        setup:
        InquiryData inquiryData = modelMapper.map(sessionData, InquiryData)

        expect:
        inquiryData.lastname == "田中"
        inquiryData.firstname == "太郎"
        inquiryData.lastkana == "たなか"
        inquiryData.firstkana == "たろう"
        inquiryData.sex == SexValues.MALE.value
        inquiryData.age == 30
        inquiryData.job == JobValues.EMPLOYEE.value
        inquiryData.zipcode1 == "100"
        inquiryData.zipcode2 == "0005"
        inquiryData.address == "東京都千代田区丸の内"
        inquiryData.tel1 == "03"
        inquiryData.tel2 == "1234"
        inquiryData.tel3 == "5678"
        inquiryData.email == "test@sample.co.jp"
        inquiryData.type1 == Type1Values.PRODUCT.value
        inquiryData.type2 == [Type2Values.CATALOGUE.value
                              , Type2Values.ESTIMATE.value
                              , Type2Values.OTHER.value].stream()
                .collect(Collectors.joining(","))
        StreamUtils.copyToString(inquiryData.inquiry.asciiStream, StandardCharsets.UTF_8) ==
                StreamUtils.copyToString(inquiryClob.asciiStream, StandardCharsets.UTF_8)
        inquiryData.survey == soh.selectItemList("survey").stream()
                .map { SurveyOptions surveyOptions -> surveyOptions.itemValue }
                .collect(Collectors.joining(","))
    }

    def "任意項目には値がセットされていない場合のテスト"() {
        setup:
        inquiryInput01Form.job = StringUtils.EMPTY
        inquiryInput02Form.tel1 = StringUtils.EMPTY
        inquiryInput02Form.tel2 = StringUtils.EMPTY
        inquiryInput02Form.tel3 = StringUtils.EMPTY
        inquiryInput03Form.survey = []
        InquiryData inquiryData = modelMapper.map(sessionData, InquiryData)

        expect:
        inquiryData.lastname == "田中"
        inquiryData.firstname == "太郎"
        inquiryData.lastkana == "たなか"
        inquiryData.firstkana == "たろう"
        inquiryData.sex == SexValues.MALE.value
        inquiryData.age == 30
        inquiryData.job == StringUtils.EMPTY
        inquiryData.zipcode1 == "100"
        inquiryData.zipcode2 == "0005"
        inquiryData.address == "東京都千代田区丸の内"
        inquiryData.tel1 == StringUtils.EMPTY
        inquiryData.tel2 == StringUtils.EMPTY
        inquiryData.tel3 == StringUtils.EMPTY
        inquiryData.email == "test@sample.co.jp"
        inquiryData.type1 == Type1Values.PRODUCT.value
        inquiryData.type2 == [Type2Values.CATALOGUE.value
                              , Type2Values.ESTIMATE.value
                              , Type2Values.OTHER.value].stream()
                .collect(Collectors.joining(","))
        StreamUtils.copyToString(inquiryData.inquiry.asciiStream, StandardCharsets.UTF_8) ==
                StreamUtils.copyToString(inquiryClob.asciiStream, StandardCharsets.UTF_8)
        inquiryData.survey == StringUtils.EMPTY
    }

}
