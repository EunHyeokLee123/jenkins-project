package com.playdata.userservice.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderService {

    private final JavaMailSender mailSender;

    public String joinMain (String email) throws MessagingException {
        String setFrom = "luo1998@gmail.com";
        String id = UUID.randomUUID().toString().replace("-", "").substring(0,8);
        String toMail = email;
        String title = " 회원가입 인증 이메일";
        String content = "홈페이지 가입을 신청해 주셔서 감사합니다." +
                "<br><br>" +
                "인증 번호는 <strong>" + id+ "</strong> 입니다. <br>" +
                "해당 인증 번호를 인증번호 확인란에 기입해 주세요."; // 이메일에 삽입할 내용 (더 꾸며보세요)

        mailSend(setFrom, toMail, title, content);

        return id;
    }
    public void mailSend(String setFrom, String toMail, String title, String content) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setTo(setFrom);
        helper.setTo(toMail);
        helper.setSubject(title);
        helper.setText(content, true);
        mailSender.send(mimeMessage);

    }

    public void sendTempPasswordMail(String toEmail, String tempPassword) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(" 임시 비밀번호 안내");
        helper.setText(
                "<p>요청하신 임시 비밀번호는 다음과 같습니다:</p>" +
                        "<h3>" + tempPassword + "</h3>" +
                        "로그인 후 반드시 비밀번호를 변경해 주세요.</p>",
                true
        );

        mailSender.send(message);
    }

    public void sendAuthCode(String toEmail, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("[Playdata] 비밀번호 재설정 인증코드 안내");

        String content = """
        <html>
            <body>
                <h3>비밀번호 재설정을 위한 인증 코드입니다.</h3>
                <p>아래의 인증 코드를 입력해 주세요:</p>
                <div style="font-size: 24px; font-weight: bold; color: #2E86C1; margin-top: 10px;">
                    %s
                </div>
                <p style="margin-top: 20px; color: #555;">※ 인증 코드는 5분 동안만 유효합니다.</p>
            </body>
        </html>
    """.formatted(code);

        helper.setText(content, true); // true → HTML 사용

        mailSender.send(message);
    }

}