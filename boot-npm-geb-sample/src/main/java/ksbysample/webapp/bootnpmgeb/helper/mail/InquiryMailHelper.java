package ksbysample.webapp.bootnpmgeb.helper.mail;

import ksbysample.webapp.bootnpmgeb.helper.freemarker.FreeMarkerHelper;
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.ConfirmForm;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * 問い合わせフォームのメール用 Helper クラス
 */
@SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
@Component
public class InquiryMailHelper {

    private static final String TEMPLATE_LOCATION_TEXTMAIL = "mail/inquirymail-body.ftlh";

    private static final String FROM_ADDR = "inquiry-form@sample.co.jp";
    private static final String TO_ADDR = "inquiry@sample.co.jp";
    private static final String SUBJECT = "問い合わせフォームからお問い合わせがありました";

    private final FreeMarkerHelper freeMarkerHelper;

    private final JavaMailSender mailSender;

    /**
     * コンストラクタ
     *
     * @param freeMarkerHelper {@link FreeMarkerHelper} オブジェクト
     * @param mailSender       {@link JavaMailSender} オブジェクト
     */
    public InquiryMailHelper(FreeMarkerHelper freeMarkerHelper
            , JavaMailSender mailSender) {
        this.freeMarkerHelper = freeMarkerHelper;
        this.mailSender = mailSender;
    }

    /**
     * {@link MimeMessage} オブジェクトを生成する
     * メール本文は入力画面１～３で入力された内容から生成する
     *
     * @param confirmForm {@link ConfirmForm} オブジェクト
     * @return {@link MimeMessage} オブジェクト
     */
    public MimeMessage createMessage(ConfirmForm confirmForm) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            message.setFrom(FROM_ADDR);
            message.setTo(TO_ADDR);
            message.setSubject(SUBJECT);
            message.setText(generateTextUsingVelocity(confirmForm), false);
            return message.getMimeMessage();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateTextUsingVelocity(ConfirmForm confirmForm) {
        Map<String, Object> model = new HashMap<>();
        model.put("confirmForm", confirmForm);
        return freeMarkerHelper.merge(TEMPLATE_LOCATION_TEXTMAIL, model);
    }

}
