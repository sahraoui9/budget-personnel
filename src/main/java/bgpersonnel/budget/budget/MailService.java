package bgpersonnel.budget.budget;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {


    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String eMail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(eMail);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}