package ksbysample.webapp.bootnpmgeb.web.inquiry;

import ksbysample.webapp.bootnpmgeb.constants.UrlConst;
import ksbysample.webapp.bootnpmgeb.helper.message.MessagesPropertiesHelper;
import ksbysample.webapp.bootnpmgeb.session.SessionData;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
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

    private static final String VALIDATE_FORM_ERROR = "InquiryInputController.validate.form.error";

    private final ModelMapper modelMapper;

    private final InquiryInput02FormValidator inquiryInput02FormValidator;

    private final Validator mvcValidator;

    private final MessagesPropertiesHelper mph;

    /**
     * コンストラクタ
     *
     * @param modelMapper                 {@link ModelMapper} オブジェクト
     * @param inquiryInput02FormValidator {@link InquiryInput02FormValidator} オブジェクト
     * @param mvcValidator                {@link Validator} オブジェクト
     * @param mph                         {@link MessagesPropertiesHelper} オブジェクト
     */
    public InquiryInputController(ModelMapper modelMapper
            , InquiryInput02FormValidator inquiryInput02FormValidator
            , Validator mvcValidator
            , MessagesPropertiesHelper mph) {
        this.modelMapper = modelMapper;
        this.inquiryInput02FormValidator = inquiryInput02FormValidator;
        this.mvcValidator = mvcValidator;
        this.mph = mph;
    }

    /**
     * inquiryInput02Form 用 InitBinder
     *
     * @param binder {@link WebDataBinder} オブジェクト
     */
    @InitBinder(value = "inquiryInput02Form")
    public void inquiryInput02FormInitBinder(WebDataBinder binder) {
        binder.addValidators(inquiryInput02FormValidator);
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
            bindingResult.getAllErrors().stream().forEach(e -> log.warn(e.getCode()));
            throw new IllegalArgumentException(mph.getMessage(VALIDATE_FORM_ERROR, null));
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
    public String input02(InquiryInput02Form inquiryInput02Form
            , SessionData sessionData) {
        // セッションに保存されているデータがある場合にはコピーする
        if (sessionData.getInquiryInput02Form() != null) {
            modelMapper.map(sessionData.getInquiryInput02Form(), inquiryInput02Form);
            inquiryInput02Form.setCopiedFromSession(true);
        }

        return TEMPLATE_INPUT02;
    }

    /**
     * 入力画面２　「前の画面へ戻る」ボタンクリック時の処理
     *
     * @return 入力画面１の URL
     */
    @PostMapping(value = "/02", params = {"move=back"})
    public String input02MoveBack(@Validated InquiryInput02Form inquiryInput02Form
            , BindingResult bindingResult
            , SessionData sessionData
            , UriComponentsBuilder builder) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream().forEach(e -> log.warn(e.getCode()));
            throw new IllegalArgumentException(mph.getMessage(VALIDATE_FORM_ERROR, null));
        }

        // 入力されたデータをセッションに保存する
        sessionData.setInquiryInput02Form(inquiryInput02Form);

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_INPUT_01).toUriString();
    }

    /**
     * 入力画面２　「次へ」ボタンクリック時の処理
     *
     * @return 入力画面３の URL
     */
    @PostMapping(value = "/02", params = {"move=next"})
    public String input02MoveNext(@Validated InquiryInput02Form inquiryInput02Form
            , BindingResult bindingResult
            , InquiryInput02FormNotEmptyRule inquiryInput02FormNotEmptyRule
            , SessionData sessionData
            , UriComponentsBuilder builder) {
        // 必須チェックをする
        mvcValidator.validate(inquiryInput02FormNotEmptyRule, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream().forEach(e -> log.warn(e.getCode()));
            throw new IllegalArgumentException(mph.getMessage(VALIDATE_FORM_ERROR, null));
        }

        // 入力されたデータをセッションに保存する
        sessionData.setInquiryInput02Form(inquiryInput02Form);

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_INPUT_03).toUriString();
    }

    /**
     * 入力画面３　初期表示処理
     *
     * @return 入力画面３の Thymeleaf テンプレートファイルのパス
     */
    @GetMapping("/03")
    public String input03(InquiryInput03Form inquiryInput03Form
            , SessionData sessionData) {
        // セッションに保存されているデータがある場合にはコピーする
        if (sessionData.getInquiryInput03Form() != null) {
            modelMapper.map(sessionData.getInquiryInput03Form(), inquiryInput03Form);
            inquiryInput03Form.setCopiedFromSession(true);
        }

        return TEMPLATE_INPUT03;
    }

    /**
     * 入力画面３　「前へ」ボタンクリック時の処理
     *
     * @return 入力画面２の URL
     */
    @PostMapping(value = "/03", params = {"move=back"})
    public String input03MoveBack(@Validated InquiryInput03Form inquiryInput03Form
            , BindingResult bindingResult
            , SessionData sessionData
            , UriComponentsBuilder builder) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream().forEach(e -> log.warn(e.getCode()));
            throw new IllegalArgumentException(mph.getMessage(VALIDATE_FORM_ERROR, null));
        }

        // 入力されたデータをセッションに保存する
        sessionData.setInquiryInput03Form(inquiryInput03Form);

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_INPUT_02).toUriString();
    }

    /**
     * 入力画面３　「確認画面へ」ボタンクリック時の処理
     *
     * @return 確認画面の URL
     */
    @PostMapping(value = "/03", params = {"move=next"})
    public String input03MoveNext(@Validated InquiryInput03Form inquiryInput03Form
            , BindingResult bindingResult
            , InquiryInput03FormNotEmptyRule inquiryInput03FormNotEmptyRule
            , SessionData sessionData
            , UriComponentsBuilder builder) {
        // 必須チェックをする
        mvcValidator.validate(inquiryInput03FormNotEmptyRule, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream().forEach(e -> log.warn(e.getCode()));
            throw new IllegalArgumentException(mph.getMessage(VALIDATE_FORM_ERROR, null));
        }

        // 入力されたデータをセッションに保存する
        sessionData.setInquiryInput03Form(inquiryInput03Form);

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_CONFIRM).toUriString();
    }

}
