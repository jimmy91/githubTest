package mail.mail;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String smtp = "smtp.sina.com.cn";// smtp服务器
		String from = "18520068513@sina.cn";// 邮件显示名称
		String to = "510014895@qq.com";// 收件人的邮件地址，必须是真实地址
		String copyto = "";// 抄送人邮件地址
		
		String content = "test";
		String subject = "test";
/*		String content = ReadContent.getMailContent("content");// 邮件内容
		if(content == null || content.equals("null") || content.isEmpty()){
			content = "线上系统日志监控出现异常！";
			DeleteFile.delete();
			return ;
		}
		String socre = ReadContent.getSocre();
		String subject = "爆款达人-线上监控状态："+socre ;// 邮件标题
		
		*/
		
		String username = "18520068513@sina.cn";// 发件人真实的账户名
		String password = "bezuo0515";// 发件人密码
		Mail.sendAndCc(smtp, from, to, copyto, subject, content, username, password);
		
		DeleteFile.delete();
	
    }
}
