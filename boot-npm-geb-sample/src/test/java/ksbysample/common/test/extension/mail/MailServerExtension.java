package ksbysample.common.test.extension.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

@Component
public class MailServerExtension {

    private GreenMail greenMail = new GreenMail(new ServerSetup(25, "localhost", ServerSetup.PROTOCOL_SMTP));

    public void start() {
        greenMail.start();
    }

    public void stop() {
        greenMail.stop();
    }

    public int getMessagesCount() {
        return greenMail.getReceivedMessages().length;
    }

    public List<MimeMessage> getMessages() {
        return Arrays.asList(greenMail.getReceivedMessages());
    }

    public MimeMessage getFirstMessage() {
        MimeMessage message = null;
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        if (receivedMessages.length > 0) {
            message = receivedMessages[0];
        }
        return message;
    }

}
