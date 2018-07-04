package ksbysample.webapp.bootnpmgeb.mapper;

import com.github.rozidan.springboot.modelmapper.TypeMapConfigurer;
import ksbysample.webapp.bootnpmgeb.dao.InquiryDataDao;
import ksbysample.webapp.bootnpmgeb.entity.InquiryData;
import ksbysample.webapp.bootnpmgeb.session.SessionData;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput03Form;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.sql.Clob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * SessionData --> InquiryData データ変換用クラス
 */
@SuppressWarnings({"checkstyle:LineLength", "PMD.AvoidThrowingRawExceptionTypes"})
@Component
public class SessionData2InquiryDataTypeMap extends TypeMapConfigurer<SessionData, InquiryData> {

    private final InquiryDataDao inquiryDataDao;

    /**
     * コンストラクタ
     *
     * @param inquiryDataDao {@InquiryDataDao} オブジェクト
     */
    public SessionData2InquiryDataTypeMap(InquiryDataDao inquiryDataDao) {
        super();
        this.inquiryDataDao = inquiryDataDao;
    }

    @Override
    public void configure(TypeMap<SessionData, InquiryData> typeMap) {
        typeMap.setPreConverter(context -> {
            SessionData sessionData = context.getSource();
            InquiryInput03Form inquiryInput03Form = sessionData.getInquiryInput03Form();
            InquiryData inquiryData = context.getDestination();

            inquiryData.setType2(inquiryInput03Form.getType2().stream().collect(Collectors.joining(",")));
            Clob inquiryClob = inquiryDataDao.createClob();
            try {
                inquiryClob.setString(1, inquiryInput03Form.getInquiry());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            inquiryData.setInquiry(inquiryClob);
            inquiryData.setSurvey(inquiryInput03Form.getSurvey().stream()
                    .collect(Collectors.joining(",")));
            inquiryData.setUpdateDate(LocalDateTime.now());

            return context.getDestination();
        });

        typeMap.addMappings(mapping -> mapping.map(src -> src.getInquiryInput01Form().getLastname(), InquiryData::setLastname))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput01Form().getFirstname(), InquiryData::setFirstname))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput01Form().getLastkana(), InquiryData::setLastkana))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput01Form().getFirstkana(), InquiryData::setFirstkana))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput01Form().getSex(), InquiryData::setSex))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput01Form().getAge(), InquiryData::setAge))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput01Form().getJob(), InquiryData::setJob))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput02Form().getZipcode1(), InquiryData::setZipcode1))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput02Form().getZipcode2(), InquiryData::setZipcode2))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput02Form().getAddress(), InquiryData::setAddress))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput02Form().getTel1(), InquiryData::setTel1))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput02Form().getTel2(), InquiryData::setTel2))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput02Form().getTel3(), InquiryData::setTel3))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput02Form().getEmail(), InquiryData::setEmail))
                .addMappings(mapping -> mapping.map(src -> src.getInquiryInput03Form().getType1(), InquiryData::setType1))
                .addMappings(mapping -> mapping.skip(InquiryData::setType2))
                .addMappings(mapping -> mapping.skip(InquiryData::setInquiry))
                .addMappings(mapping -> mapping.skip(InquiryData::setSurvey))
                .addMappings(mapping -> mapping.skip(InquiryData::setUpdateDate));
    }

}
