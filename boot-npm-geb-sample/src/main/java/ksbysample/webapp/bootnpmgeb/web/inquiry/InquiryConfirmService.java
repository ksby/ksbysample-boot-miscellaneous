package ksbysample.webapp.bootnpmgeb.web.inquiry;

import ksbysample.webapp.bootnpmgeb.dao.InquiryDataDao;
import ksbysample.webapp.bootnpmgeb.entity.InquiryData;
import ksbysample.webapp.bootnpmgeb.session.SessionData;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.ConfirmForm;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * 確認画面用 Service クラス
 */
@Service
public class InquiryConfirmService {

    private final InquiryDataDao inquiryDataDao;
    private final ModelMapper modelMapper;

    /**
     * コンストラクタ
     *
     * @param inquiryDataDao {@InquiryDataDao} オブジェクト
     * @param modelMapper    {@ModelMapper} オブジェクト
     */
    public InquiryConfirmService(InquiryDataDao inquiryDataDao
            , ModelMapper modelMapper) {
        this.inquiryDataDao = inquiryDataDao;
        this.modelMapper = modelMapper;
    }

    /**
     * 入力されたデータを INQUIRY_DATA テーブルに保存してメールを送信する
     *
     * @param sessionData {@SessionData} オブジェクト
     * @param confirmForm {@ConfirmForm} オブジェクト
     */
    public void saveToDbAndSendMail(SessionData sessionData, ConfirmForm confirmForm) {
        // INQUIRY_DATA テーブルに保存する
        InquiryData inquiryData = modelMapper.map(sessionData, InquiryData.class);
        inquiryDataDao.insert(inquiryData);

        // メールを送信する
    }

}
