package i.need.it.IneedIt.utils;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Slf4j
public class EmailUtil {

    private static final String FROM_EMAIL = "ineeditshoppinglist@gmail.com";
    private static final String PASSWORD = "hfiz ishp fzoy rfnp";

    private static final String BODY = "Ineedit has a new user!";


    public static void sendEmail(String toEmail, String subject, String messageText) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Session session = Session.getInstance(props, new EmailAuthenticator(FROM_EMAIL, PASSWORD));

        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(FROM_EMAIL, "Ineedit"));

            msg.setReplyTo(InternetAddress.parse(FROM_EMAIL, false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(BODY, "UTF-8");

            msg.setSentDate(new java.util.Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);

            System.out.println("Email Sent Successfully!!");
        } catch (Exception e) {
            log.error("Email sending is unsuccessful "+ e.getMessage());
        }
    }
}

