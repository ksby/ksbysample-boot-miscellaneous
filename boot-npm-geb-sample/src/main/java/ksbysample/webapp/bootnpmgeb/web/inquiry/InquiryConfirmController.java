package ksbysample.webapp.bootnpmgeb.web.inquiry;

import ksbysample.webapp.bootnpmgeb.constants.UrlConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 確認画面用 Controller クラス
 */
@Controller
@RequestMapping("/inquiry/confirm")
public class InquiryConfirmController {

    private static final String TEMPLATE_BASE = "web/inquiry";
    private static final String TEMPLATE_CONFIRM = TEMPLATE_BASE + "/confirm";

    /**
     * 確認画面　初期表示処理
     *
     * @return 確認画面の Thymeleaf テンプレートファイルのパス
     */
    @GetMapping
    public String index() {
        return TEMPLATE_CONFIRM;
    }

    /**
     * 確認画面　「送信する」ボタンクリック時の処理
     *
     * @return 完了画面の URL
     */
    @PostMapping("/send")
    public String send(UriComponentsBuilder builder) {
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + builder.path(UrlConst.URL_INQUIRY_COMPLETE).toUriString();
    }

}
