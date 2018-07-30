package ksbysample.webapp.bootnpmgeb.mapper;

import com.github.rozidan.springboot.modelmapper.TypeMapConfigurer;
import ksbysample.webapp.bootnpmgeb.helper.db.SurveyOptionsHelper;
import ksbysample.webapp.bootnpmgeb.session.SessionData;
import ksbysample.webapp.bootnpmgeb.values.*;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.ConfirmForm;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput01Form;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput02Form;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput03Form;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SessionData --> ConfirmForm データ変換用クラス
 */
@Component
public class SessionData2ConfirmFormTypeMap extends TypeMapConfigurer<SessionData, ConfirmForm> {

    private final ValuesHelper vh;

    private final SurveyOptionsHelper soh;

    /**
     * コンストラクタ
     *
     * @param vh  {@ValuesHelper} オブジェクト
     * @param soh {@SurveyOptionsHelper} オブジェクト
     */
    public SessionData2ConfirmFormTypeMap(ValuesHelper vh
            , SurveyOptionsHelper soh) {
        super();
        this.vh = vh;
        this.soh = soh;
    }

    @Override
    public void configure(TypeMap<SessionData, ConfirmForm> typeMap) {
        typeMap.setPreConverter(context -> {
            SessionData sessionData = context.getSource();
            InquiryInput01Form inquiryInput01Form = sessionData.getInquiryInput01Form();
            InquiryInput02Form inquiryInput02Form = sessionData.getInquiryInput02Form();
            InquiryInput03Form inquiryInput03Form = sessionData.getInquiryInput03Form();
            ConfirmForm confirmForm = context.getDestination();

            /************************
             * 入力画面１の入力項目用
             ************************/
            confirmForm.setName(join("　"
                    , inquiryInput01Form.getLastname()
                    , inquiryInput01Form.getFirstname()));
            confirmForm.setKana(join("　"
                    , inquiryInput01Form.getLastkana()
                    , inquiryInput01Form.getFirstkana()));
            confirmForm.setSex(vh.getText(SexValues.class, inquiryInput01Form.getSex()));
            confirmForm.setAge(inquiryInput01Form.getAge());
            confirmForm.setJob(vh.getText(JobValues.class, inquiryInput01Form.getJob()));

            /************************
             * 入力画面２の入力項目用
             ************************/
            confirmForm.setZipcode(join("-"
                    , inquiryInput02Form.getZipcode1()
                    , inquiryInput02Form.getZipcode2()));
            confirmForm.setAddress(inquiryInput02Form.getAddress());
            confirmForm.setTel(join("-"
                    , inquiryInput02Form.getTel1()
                    , inquiryInput02Form.getTel2()
                    , inquiryInput02Form.getTel3()));
            confirmForm.setEmail(inquiryInput02Form.getEmail());

            /************************
             * 入力画面３の入力項目用
             ************************/
            confirmForm.setType1(vh.getText(Type1Values.class, inquiryInput03Form.getType1()));
            confirmForm.setType2(inquiryInput03Form.getType2().stream()
                    .map(type2 -> vh.getText(Type2Values.class, type2))
                    .collect(Collectors.joining("、")));
            confirmForm.setInquiry(inquiryInput03Form.getInquiry());
            Map<String, String> surveyOptionsMap = this.soh.selectItemList("survey").stream()
                    .collect(Collectors.toMap(s -> s.getItemValue(), s -> s.getItemName()));
            confirmForm.setSurvey(inquiryInput03Form.getSurvey().stream()
                    .map(survey -> surveyOptionsMap.get(survey))
                    .collect(Collectors.toList()));

            return context.getDestination();
        });

        typeMap.addMappings(mapping -> mapping.skip(ConfirmForm::setName))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setKana))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setSex))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setAge))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setJob))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setZipcode))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setAddress))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setTel))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setEmail))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setType1))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setType2))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setInquiry))
                .addMappings(mapping -> mapping.skip(ConfirmForm::setSurvey));
    }

    /**
     * 文字列を指定された区切り文字で結合する。文字列が全て空の場合には空文字列を返す。
     *
     * @param delimiter 区切り文字
     * @param arg       結合する文字列の配列
     * @return 結合した文字列
     */
    private String join(String delimiter, String... arg) {
        boolean isAllEmpty = arg == null
                || Arrays.asList(arg).stream().allMatch(str -> StringUtils.isEmpty(str));
        return isAllEmpty
                ? StringUtils.EMPTY
                : Arrays.asList(arg).stream().collect(Collectors.joining(delimiter));
    }

}
