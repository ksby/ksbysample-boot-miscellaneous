package ksbysample.webapp.bootnpmgeb.web.inquiry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 完了画面用 Controller クラス
 */
@Controller
@RequestMapping("/inquiry/complete")
public class InquiryCompleteController {

    private static final String TEMPLATE_BASE = "web/inquiry";
    private static final String TEMPLATE_COMPLETE = TEMPLATE_BASE + "/complete";

    /**
     * 完了画面　初期表示処理
     *
     * @return 完了画面の Thymeleaf テンプレートファイルのパス
     */
    @GetMapping
    public String index() {
        return TEMPLATE_COMPLETE;
    }

}
