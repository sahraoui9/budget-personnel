package bgpersonnel.budget.budget;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailService {


    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    void sendMail(Budget budget, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(budget.getUser().getEmail());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}