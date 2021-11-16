package com.example.pkce.helpers;

import com.example.pkce.entities.PasswordChangeToken;
import com.example.pkce.entities.RegisterToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSender {

  @Autowired private JavaMailSender mailSender;

  @Autowired private TemplateEngine templateEngine;
  @Autowired private Environment environment;

  @Async
  public void sendRegistrationEmail(RegisterToken token) throws MessagingException {

    Context context = new Context();
    String url = String.format("%s/auth/activate?id=%s",environment.getProperty("app.url"),token.getId());
    context.setVariable("url",url);

    String messageBody = templateEngine.process("registration.html",context);

    MimeMessage message = mailSender.createMimeMessage();
    message.setSubject("Aktywacja konta");

    // Create mime helper for email
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    // Set email sender
    helper.setFrom(environment.getProperty("app.email"));

    // Set email receiver
    helper.setTo(token.getEmail());

    helper.setText(messageBody,true);

    mailSender.send(message);
  }

  @Async
  public void sendPasswordChangeToken(PasswordChangeToken token) throws MessagingException {

    Context context = new Context();
    String url = String.format("%s/auth/password_change?token=%s",environment.getProperty("app.url"),token.getId());
    context.setVariable("url",url);

    String messageBody = templateEngine.process("passwordChange.html",context);

    MimeMessage message = mailSender.createMimeMessage();
    message.setSubject("Zmiana hasła");

    // Create mime helper for email
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    // Set email sender
    helper.setFrom(environment.getProperty("app.email"));

    // Set email receiver
    helper.setTo(token.getEmail());

    helper.setText(messageBody,true);

    mailSender.send(message);
  }
}
