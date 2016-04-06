package mail.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Mail {
	private MimeMessage mimeMsg;
	private Session session;
	private Properties props;
	private String username;
	private String password;
	private Multipart mp;
	public Mail(String smtp) {
		setSmtpHost(smtp);
		createMimeMessage();
	}
	public void setSmtpHost(String hostName) {
		System.out.println("设置系统属性：mail.smtp.host=" + hostName);
		if (props == null) {
			props = System.getProperties();
		}
		props.put("mail.smtp.host", hostName);
	}
	public boolean createMimeMessage() {
		try {
			System.out.println("准备获取邮件会话对象！");
			session = Session.getDefaultInstance(props, null);
		} catch (Exception e) {
			System.out.println("获取邮件会话错误！" + e);
			return false;
		}
		System.out.println("准备创建MIME邮件对象！");
		try {
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();

			return true;
		} catch (Exception e) {
			System.out.println("创建MIME邮件对象失败！" + e);
			return false;
		}
	}

	/*定义SMTP是否需要验证*/
	public void setNeedAuth(boolean need) {
		System.out.println("设置smtp身份认证：mail.smtp.auth = " + need);
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}
	public void setNamePass(String name, String pass) {
		username = name;
		password = pass;
	}

	/*定义邮件主题*/
	public boolean setSubject(String mailSubject) {
		System.out.println("定义邮件主题！");
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			System.err.println("定义邮件主题发生错误！");
			return false;
		}
	}
	
	@SuppressWarnings("restriction")
	public boolean setFile(){
		System.out.println("设置附件！");
		
		File file = new File("/home/steven/spider-server/spider-webmagic/mail/");;
    	if(!file.exists()){
    		file.mkdir();
    	}
    	
        try {
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()+"error.txt"),"UTF-8"));
			String error = ReadContent.getMailContent("error");
			printWriter.write(error);
			printWriter.flush();
			printWriter.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e1) {
			e1.printStackTrace();
		}
        
		// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
		BodyPart messageBodyPart= new MimeBodyPart();
		DataSource source = new FileDataSource(file.getAbsolutePath()+"error.txt");
		try {
			//添加附件的内容
			messageBodyPart.setDataHandler(new DataHandler(source));
			//添加附件的标题
			//这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			messageBodyPart.setFileName("=?UTF-8?B?"+enc.encode("错误异常报告.txt".getBytes())+"?=");
			mp.addBodyPart(messageBodyPart);
			
			
			//将multipart对象放到message中
			mimeMsg.setContent("" + mp, "text/html;charset=GBK");
			//保存邮件
			mimeMsg.saveChanges();
		} catch (MessagingException e) {
			System.err.println("添加发送附件失败！" + e);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/*定义邮件正文*/
	public boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("" + mailBody, "text/html;charset=GBK");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			System.err.println("定义邮件正文时发生错误！" + e);
			return false;
		}
	}

	/*设置发信人*/
	public boolean setFrom(String from) {
		System.out.println("设置发信人！");
		try {
			mimeMsg.setFrom(new InternetAddress(from)); //发信人
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*定义收信人*/
	public boolean setTo(String to) {
		if (to == null)
			return false;
		System.out.println("定义收信人！");
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*定义抄送人*/
	public boolean setCopyTo(String copyto) {
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*发送邮件模块*/
	public boolean sendOut() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			System.out.println("邮件发送中....");
			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username, password);
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
			System.out.println("发送成功！");
			transport.close();
			return true;
		} catch (Exception e) {
			System.err.println("邮件失败！" + e);
			return false;
		}
	}

	/*调用sendOut方法完成发送*/
	public static boolean sendAndCc(String smtp, String from, String to, String copyto,
		String subject, String content, String username, String password) {
		Mail theMail = new Mail(smtp);
		theMail.setNeedAuth(true); // 验证
		if (!theMail.setSubject(subject))
			return false;
		if (!theMail.setBody(content))
			return false;
		if(!theMail.setFile())
			return false;
		if (!theMail.setTo(to))
			return false;
		if (!theMail.setCopyTo(copyto))
			return false;
		if (!theMail.setFrom(from))
			return false;
		theMail.setNamePass(username, password);
		if (!theMail.sendOut())
			return false;
		return true;
	}
}