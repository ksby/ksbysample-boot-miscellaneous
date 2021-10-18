package ksbysample.webapp.bootnpmgeb.web.inquiry;

import ksbysample.webapp.bootnpmgeb.constants.UrlConst;
import ksbysample.webapp.bootnpmgeb.session.SessionData;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.ConfirmForm;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 確認画面用 Controller クラス
 */
@Controller
@RequestMapping("/inquiry/confirm")
@SessionAttributes("sessionData")
public class InquiryConfirmController {

    private static final String TEMPLATE_BASE = "web/inquiry";
    private static final String TEMPLATE_CONFIRM = TEMPLATE_BASE + "/confirm";

    private final ModelMapper modelMapper;
    private final InquiryConfirmService inquiryConfirmService;

    /**
     * コンストラクタ
     *
     * @param modelMapper           {@link ModelMapper} オブジェクト
     * @param inquiryConfirmService {@link InquiryConfirmService} オブジェクト
     */
    public InquiryConfirmController(ModelMapper modelMapper
            , InquiryConfirmService inquiryConfirmService) {
        this.modelMapper = modelMapper;
        this.inquiryConfirmService = inquiryConfirmService;
    }

    /**
     * 確認画面　初期表示処理
     *
     * @param confirmForm {@link ConfirmForm} オブジェクト
     * @param sessionData {@link SessionData} オブジェクト
     * @return 確認画面の Thymeleaf テンプレートファイルのパス
     */
    @GetMapping
    public String index(ConfirmForm confirmForm
            , SessionData sessionData) {
        modelMapper.map(sessionData, confirmForm);
        return TEMPLATE_CONFIRM;
    }

    /**
     * 確認画面　「送信する」ボタンクリック時の処理
     *
     * @param sessionData {@link SessionData} オブジェクト
     * @param builder     {@link UriComponentsBuilder} オブジェクト
     * @return 完了画面の URL
     */
    @PostMapping("/send")
    public String send(SessionData sessionData
            , UriComponentsBuilder builder) {
        ConfirmForm confirmForm = modelMapper.map(sessionData, ConfirmForm.class);
        inquiryConfirmService.saveToDbAndSendMail(sessionData, confirmForm);

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_COMPLETE).toUriString();
    }

}
