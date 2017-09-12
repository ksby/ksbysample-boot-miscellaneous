package ksbysample.webapp.bootnpmgeb.web.inquiry;

import ksbysample.webapp.bootnpmgeb.constants.UrlConst;
import ksbysample.webapp.bootnpmgeb.session.SessionData;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput01Form;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 入力画面１～３用 Controller クラス
 */
@Slf4j
@Controller
@RequestMapping("/inquiry/input")
@SessionAttributes("sessionData")
public class InquiryInputController {

    private static final String TEMPLATE_BASE = "web/inquiry";
    private static final String TEMPLATE_INPUT01 = TEMPLATE_BASE + "/input01";
    private static final String TEMPLATE_INPUT02 = TEMPLATE_BASE + "/input02";
    private static final String TEMPLATE_INPUT03 = TEMPLATE_BASE + "/input03";

    private final ModelMapper modelMapper;

    /**
     * コンストラクタ
     *
     * @param modelMapper {@link ModelMapper} オブジェクト
     */
    public InquiryInputController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * 入力画面１　初期表示処理
     *
     * @return 入力画面１の Thymeleaf テンプレートファイルのパス
     */
    @GetMapping("/01")
    public String input01(InquiryInput01Form inquiryInput01Form
            , SessionData sessionData) {
        // セッションに保存されているデータがある場合にはコピーする
        if (sessionData.getInquiryInput01Form() != null) {
            modelMapper.map(sessionData.getInquiryInput01Form(), inquiryInput01Form);
            inquiryInput01Form.setCopiedFromSession(true);
        }

        return TEMPLATE_INPUT01;
    }

    /**
     * 入力画面１　「次へ」ボタンクリック時の処理
     *
     * @return 入力画面２の URL
     */
    @PostMapping(value = "/01", params = {"move=next"})
    public String input01MoveNext(@Validated InquiryInput01Form inquiryInput01Form
            , BindingResult bindingResult
            , SessionData sessionData
            , UriComponentsBuilder builder) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("セットされるはずのないデータがセットされています");
        }

        // 入力されたデータをセッションに保存する
        sessionData.setInquiryInput01Form(inquiryInput01Form);

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_INPUT_02).toUriString();
    }

    /**
     * 入力画面２　初期表示処理
     *
     * @return 入力画面２の Thymeleaf テンプレートファイルのパス
     */
    @GetMapping("/02")
    public String input02() {
        return TEMPLATE_INPUT02;
    }

    /**
     * 入力画面２　「前へ」ボタンクリック時の処理
     *
     * @return 入力画面１の URL
     */
    @PostMapping(value = "/02", params = {"move=back"})
    public String input02MoveBack(UriComponentsBuilder builder) {
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_INPUT_01).toUriString();
    }

    /**
     * 入力画面２　「次へ」ボタンクリック時の処理
     *
     * @return 入力画面３の URL
     */
    @PostMapping(value = "/02", params = {"move=next"})
    public String input02MoveNext(UriComponentsBuilder builder) {
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_INPUT_03).toUriString();
    }

    /**
     * 入力画面３　初期表示処理
     *
     * @return 入力画面３の Thymeleaf テンプレートファイルのパス
     */
    @GetMapping("/03")
    public String input03() {
        return TEMPLATE_INPUT03;
    }

    /**
     * 入力画面３　「前へ」ボタンクリック時の処理
     *
     * @return 入力画面２の URL
     */
    @PostMapping(value = "/03", params = {"move=back"})
    public String input03MoveBack(UriComponentsBuilder builder) {
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_INPUT_02).toUriString();
    }

    /**
     * 入力画面３　「確認画面へ」ボタンクリック時の処理
     *
     * @return 確認画面の URL
     */
    @PostMapping(value = "/03", params = {"move=next"})
    public String input03MoveNext(UriComponentsBuilder builder) {
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_CONFIRM).toUriString();
    }

}
