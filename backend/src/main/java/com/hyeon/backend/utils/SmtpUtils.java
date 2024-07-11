package com.hyeon.backend.utils;

import com.hyeon.backend.enums.SmtpContentsType;
import jakarta.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class SmtpUtils {

  static final String FROM = "bestshop@dtonic.io";
  static final String FROMNAME = "newbest";
  static final String SMTP_USERNAME = "jmyqkmsmIaj3XDgt";
  static final String SMTP_PASSWORD = "Z34kAzIn6dclviXlLzVRIabt9pWH3wIx";

  private Properties props;

  @Value("${smtp-client.mail}")
  private String toListString;

  @Value("${smtp-client.csvFilePath}")
  private String csvFilePath;

  static final int PORT = 587;

  static final String HOST = "smtp-mail.nhncloudservice.com";

  @PostConstruct
  public void init() {
    props = System.getProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.port", PORT);
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");
  }

  /* 생성된 내용 csv에 작성 */
  private void writeCsv(File csvFile, List<String> contents) throws Exception {
    try (
      BufferedWriter bw = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(csvFile), "MS949")
      )
    ) {
      StringBuilder sb = new StringBuilder();
      for (String content : contents) {
        sb.append(content);
        sb.append("\n");
      }
      bw.write(sb.toString()); // 쓰기
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 메일의 contents 생성
  private Message createMsg(
    Session session,
    List<String> contents,
    String subject,
    String fileName,
    String body,
    SmtpContentsType contentsType
  ) throws Exception {
    MimeMessage msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress(FROM, FROMNAME));

    String[] toList = toListString.split(",");
    InternetAddress[] toAddr = new InternetAddress[toList.length];
    for (int i = 0; i < toList.length; i++) {
      toAddr[i] = new InternetAddress(toList[i]);
    }

    msg.setRecipients(Message.RecipientType.TO, toAddr);
    msg.setSubject(subject);

    File csvFile = File.createTempFile("temp", "csv");

    if (SmtpContentsType.CSV.equals(contentsType)) {
      writeCsv(csvFile, contents);
      MimeBodyPart mbp = new MimeBodyPart();

      FileDataSource fds = new FileDataSource(csvFile);
      mbp.setDataHandler(new DataHandler(fds));
      mbp.setFileName(fileName);
      Multipart mp = new MimeMultipart();
      MimeBodyPart mTextPart = new MimeBodyPart();
      mp.addBodyPart(mbp);
      mTextPart.setText(body, "UTF-8", "html");
      mp.addBodyPart(mTextPart);
      msg.setContent(mp);
    } else if (SmtpContentsType.HTML.equals(contentsType)) {
      msg.setContent(body, "text/html;charset=euc-kr");
    }
    return msg;
  }

  //
  public void sendMail(List<String> contents, SmtpContentsType contentsType)
    throws Exception {
    LocalDate date = LocalDate.now();
    String body = "";
    String subject = "";
    String fileName = "";

    subject += " - " + date.toString();

    Session session = Session.getDefaultInstance(props);
    Message msg = createMsg(
      session,
      contents,
      subject,
      fileName,
      body,
      contentsType
    );
    Transport transport = session.getTransport();
    try {
      log.info("Sending...");
      transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
      transport.sendMessage(msg, msg.getAllRecipients());
      log.info("Email sent!");
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      transport.close();
    }
  }
}
