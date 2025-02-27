package BookingService.BookingService.service;

import BookingService.BookingService.dto.request.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        // Sử dụng địa chỉ email được cấu hình làm người gửi
        message.setFrom(fromEmail);
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());
        javaMailSender.send(message);
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            // Tạo nội dung HTML cho email
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; color: #333; }" +
                    ".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9; }" +
                    ".header { background-color: #ff7e9d; color: white; padding: 10px; text-align: center; border-radius: 5px 5px 0 0; }" +
                    ".content { padding: 20px; background-color: white; border-radius: 0 0 5px 5px; }" +
                    ".footer { text-align: center; font-size: 12px; color: #777; margin-top: 20px; }" +
                    ".icon { width: 50px; height: 50px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>" +
                    "<img src='https://img.icons8.com/fluency/50/000000/face-massage.png' alt='Skin Care Icon' class='icon'>" +
                    "<h2>Tư Vấn Chăm Sóc Da</h2>" +
                    "</div>" +
                    "<div class='content'>" +
                    "<p>Xin chào,</p>" +
                    "<p>" + body + "</p>" + // Nội dung từ người dùng
                    "<p>Chúng tôi rất vui được hỗ trợ bạn trong hành trình chăm sóc da. Nếu có bất kỳ thắc mắc nào, đừng ngần ngại liên hệ!</p>" +
                    "<p>Trân trọng,<br>Đội ngũ SkinCare</p>" +
                    "</div>" +
                    "<div class='footer'>" +
                    "© 2025 SkinCare Service. All rights reserved." +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true); // true = gửi dưới dạng HTML

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email thất bại: " + e.getMessage());
        }
    }

}
