package com.bookmarkmanager.bookmarkmanager.db.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;



public class MailService{
    
    private JavaMailSender javaMailSender;
    private MimeMessage mimeMessage;
    private MimeMessageHelper messageHelper;
    
    public MailService(JavaMailSender sender) throws MessagingException{
        this.javaMailSender = sender;
        this.mimeMessage = sender.createMimeMessage();
        this.messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    }

    public void setEmail(
        String FromAddress, String ToAddress, String Subject,
        String text, boolean IsTextHTML
    ) throws MessagingException{
        messageHelper.setFrom(FromAddress);;
    }

    
    public void sendMail(){
        try{
            javaMailSender.send(mimeMessage);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    
    
    
}
