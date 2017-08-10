package ksbysample.webapp.bootnpmgeb.web.inquiry;

import ksbysample.webapp.bootnpmgeb.constants.UrlConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 入力画面１～３用 Controller クラス
 */
@Controller
@RequestMapping("/inquiry/input")
public class InquiryInputController {

    private static final String TEMPLATE_BASE = "web/inquiry";
    private static final String TEMPLATE_INPUT01 = TEMPLATE_BASE + "/input01";
    private static final String TEMPLATE_INPUT02 = TEMPLATE_BASE + "/input02";
    private static final String TEMPLATE_INPUT03 = TEMPLATE_BASE + "/input03";

    /**
     * 入力画面１　初期表示処理
     *
     * @return 入力画面１の Thymeleaf テンプレートファイルのパス
     */
    @GetMapping("/01")
    public String input01() {
        return TEMPLATE_INPUT01;
    }

    /**
     * 入力画面１　「次へ」ボタンクリック時の処理
     *
     * @return 入力画面２の URL
     */
    @PostMapping(value = "/01", params = {"move=next"})
    public String input01MoveNext(UriComponentsBuilder builder) {
        return "redirect:" + builder.path(UrlConst.URL_INQUIRY_INPUT_02).toUriString();
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
        return "redirect:" + builder.path(UrlConst.URL_INQUIRY_INPUT_01).toUriString();
    }

    /**
     * 入力画面２　「次へ」ボタンクリック時の処理
     *
     * @return 入力画面３の URL
     */
    @PostMapping(value = "/02", params = {"move=next"})
    public String input02MoveNext(UriComponentsBuilder builder) {
        return "redirect:" + builder.path(UrlConst.URL_INQUIRY_INPUT_03).toUriString();
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
        return "redirect:" + builder.path(UrlConst.URL_INQUIRY_INPUT_02).toUriString();
    }

    /**
     * 入力画面３　「確認画面へ」ボタンクリック時の処理
     *
     * @return 確認画面の URL
     */
    @PostMapping(value = "/03", params = {"move=next"})
    public String input03MoveNext(UriComponentsBuilder builder) {
        return "redirect:" + builder.path(UrlConst.URL_INQUIRY_CONFIRM).toUriString();
    }

}
