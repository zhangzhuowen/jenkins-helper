package com.navino.jenkinshelper.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 邮件发送工具类
 *
 * @author wangdongbin
 */

@Slf4j
@Component
public class MailUtil {

    // 发件人邮箱的 SMTP 服务器地址
    public static String myEmailSMTPHost = "smtp.partner.outlook.cn";

    public static Properties getMainConfig() {
        Properties props = new Properties(); // 参数配置
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.checkserveridentity", "false");
        props.put("mail.smtp.ssl.trust", myEmailSMTPHost);
        props.put("mail.smtp.host", myEmailSMTPHost);
        props.put("mail.smtp.port", "587");
        return props;
    }

    /**
     * 邮件发送
     *
     * @param eMails
     * @param content
     * @throws Exception
     */
    public static void sendEMail(List<String> eMails, String content, String emailAccount, String userName,
                                 String userPwd, String title) throws Exception {

        log.info("[sendEMail] emailAccount:" + emailAccount + " userName:" + userName + " userPwd:" + userPwd + " title:" + title);
        // 1. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(getMainConfig(),
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailAccount, userPwd);
                    }
                });
        session.setDebug(true);

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, emailAccount, eMails, content, title, userName);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        transport.connect(userName, userPwd);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    /**
     * 邮件发送 带附件
     *
     * @param eMails
     * @param content
     * @throws Exception
     */
    public static void sendEMailFile(List<String> eMails, String content, String emailAccount, String userName,
                                     String userPwd, String title, File attachment, String fileName) throws Exception {

        log.info("[sendEMailFile] emailAccount:" + emailAccount + " userName:" + userName + " userPwd:" + userPwd + " title:" + title);

        // 1. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(getMainConfig(),
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailAccount, userPwd);
                    }
                });

        session.setDebug(true);

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessageFile(session, emailAccount, eMails, content, title, userName, attachment, fileName);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        transport.connect(userName, userPwd);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session
     * @param sendMail
     * @param eMails
     * @param content
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, List<String> eMails, String content,
                                                String title, String userName) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, userName, "UTF-8"));

        // 3. To: 收件人
        InternetAddress[] mailReceive = new InternetAddress[eMails.size()];
        for (int i = 0; i < eMails.size(); i++) {
            InternetAddress internet = new InternetAddress(eMails.get(i));
            mailReceive[i] = internet;
        }
        message.setRecipients(MimeMessage.RecipientType.TO, mailReceive);

        // 4. Subject: 邮件主题
        message.setSubject(title, "UTF-8");

        // 5. Content: 邮件正文
        message.setContent(content, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }

    /**
     * 创建包含附件邮件
     *
     * @param session
     * @param sendMail
     * @param eMails
     * @param content
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessageFile(Session session, String sendMail, List<String> eMails, String content,
                                                    String title, String userName, File attachment, String fileName) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, userName, "UTF-8"));

        // 3. To: 收件人
        InternetAddress[] mailReceive = new InternetAddress[eMails.size()];
        for (int i = 0; i < eMails.size(); i++) {
            InternetAddress internet = new InternetAddress(eMails.get(i));
            mailReceive[i] = internet;
        }
        message.setRecipients(MimeMessage.RecipientType.TO, mailReceive);

        Multipart multipart = new MimeMultipart();

        // 添加邮件正文
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(content, "text/html;charset=UTF-8");
        multipart.addBodyPart(contentPart);

        // 添加附件的内容
        if (attachment != null) {
            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            //MimeUtility.encodeWord可以避免文件名乱码
            attachmentBodyPart.setFileName(fileName);
            multipart.addBodyPart(attachmentBodyPart);
        }

        // 将multipart对象放到message中
        message.setContent(multipart);

        // 4. Subject: 邮件主题
        message.setSubject(title, "UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }

}
