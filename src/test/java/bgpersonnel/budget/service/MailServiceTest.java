package bgpersonnel.budget.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("Envoi d'un mail")
    void sendMailTest() {
        String eMail = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        mailService.sendMail(eMail, subject, text);

        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage message = messageCaptor.getValue();

        Assertions.assertEquals(eMail, message.getTo()[0]);
        Assertions.assertEquals(subject, message.getSubject());
        Assertions.assertEquals(text, message.getText());
    }

}