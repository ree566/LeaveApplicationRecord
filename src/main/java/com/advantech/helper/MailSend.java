/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 當組別異常時，發出異常訊息給使用者(呼叫此class)
 */
package com.advantech.helper;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class MailSend {

    private static MailSend instance;

    private final String mailHost;
    private final String mailPort;
    private final String mailServerAddress;
    private final String password;

    private final Properties props;
    private static final Logger log = LoggerFactory.getLogger(MailSend.class);

    private MailSend() {
        PropertiesReader properties = PropertiesReader.getInstance();

        String hostaddr = null;
        try {
            String hostSetting = properties.getMailServerLocation();
            hostaddr = new ParamChecker().checkInputVal(hostSetting) ? hostSetting : getHostAddr();
        } catch (UnknownHostException | SocketException ex) {
            log.error(ex.toString());
        }
//        mailHost = hostaddr;
        mailHost = "Relay.advantech.com.tw";
        mailPort = properties.getMailServerPort();
        mailServerAddress = properties.getMailServerUsername() + "@" + hostaddr;
        password = properties.getMailServerPassword();

        props = new Properties();
        props.setProperty("mail.smtp.host", mailHost);
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", mailPort);
    }

    public static MailSend getInstance() {
        if (instance == null) {
            instance = new MailSend();
        }
        return instance;
    }

    public boolean sendMailWithoutSender(Class cls, String to, JSONArray ccList, String subject, String mailBody) {
        return sendMailWithSender(cls, mailServerAddress, to, ccList, subject, mailBody);
    }

    public boolean sendMailWithSender(Class cls, String from, String to, JSONArray ccList, String subject, String mailBody) {
        boolean b = handleSettingsAndSendMail(from, to, ccList, subject, mailBody);
        log.info(cls.getName() + (b ? " send success" : " send fail"));
        return b;
    }

    private boolean handleSettingsAndSendMail(String from, String to, JSONArray cc, String subject, String mailBody) {
        boolean b = false;

        try {
            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailServerAddress, password);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            for (Object o : cc) {
                String mail = o.toString();
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mail));
            }

            message.setSubject(subject, "UTF-8");
            message.setSentDate(new Date());
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mailBody, "text/html;charset=UTF-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(mailHost, Integer.parseInt(mailPort), mailServerAddress, password);
            Transport.send(message);
            b = true;
        } catch (MessagingException | NumberFormatException | JSONException e) {
            log.error(e.toString());
        }
        return b;
    }

    //測試main
    public static void main(String[] arg0) {

        MailSend m = MailSend.getInstance();
        m.sendMailWithoutSender(MailSend.class, "Wei.Cheng@advantech.com.tw", new JSONArray(), "test", "test");
    }

    //Get the Host address.
    private String getHostAddr() throws UnknownHostException, SocketException {
        return InetAddress.getLocalHost().getHostAddress();  // often returns "127.0.0.1"
    }
}
