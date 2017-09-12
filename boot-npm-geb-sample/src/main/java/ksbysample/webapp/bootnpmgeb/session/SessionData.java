package ksbysample.webapp.bootnpmgeb.session;

import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput01Form;
import lombok.Data;

import java.io.Serializable;

/**
 * セッションデータ用クラス
 */
@Data
public class SessionData implements Serializable {

    private static final long serialVersionUID = -2673191456750655164L;

    private InquiryInput01Form inquiryInput01Form;

}
