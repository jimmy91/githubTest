package mail.mail;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class ReadContent {
	
	private static final String hdfsPath = "/usr/mail/";
	private static String[]  LEVEL = {"优秀","良好","中等","差","严重","崩溃"};
	private static int FAILEDNUM = 0;
	
	public static String getMailContent(String type){

		Configuration conf = MyConfiguration.getConf();
		String content = null;
	
		try {
			FileSystem hdfs = FileSystem.get(conf);
			Path filePath = new Path(hdfsPath+type);
			FSDataInputStream inputStream = hdfs.open(filePath);
			/*	
			byte[] bytes = null;
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			bytes = new byte[1024];
			
			byte[] finalBytes = outputStream.toByteArray();
			outputStream.close();
			content = new String(finalBytes);
			inputStream.close();
			
			int status = inputStream.read(bytes);
			while(status != -1) {
				outputStream.write(bytes);
				status = inputStream.read(bytes);
			}*/
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			FAILEDNUM = 0;
  	    	while((line = reader.readLine()) != null) {
  	    		if(type.equals("content")){
  	    			if(line.startsWith("######") || line.startsWith("=====")){
  	  	    			continue;
  	  	    		}
  	    			buffer.append(line + "</br>");
  	    		}else{
  	    			line.replace("[.*]", "");
  	    			buffer.append(line + "\n");
  	    		}
				
				if(line.startsWith(">> total Failed:")){
					String[]  temp = line.split(":");
					FAILEDNUM += Integer.valueOf(temp[temp.length-1].trim());
				}

  	    	}
			
			content = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return content;
	}
	

	public static String getSocre() {
		if(FAILEDNUM <= 10){
			return LEVEL[0];
		}
		if(FAILEDNUM <= 20){
			return LEVEL[1];
		}
		if(FAILEDNUM <= 30){
			return LEVEL[2];
		}
		if(FAILEDNUM <= 50){
			return LEVEL[3];
		}
		if(FAILEDNUM <= 1000){
			return LEVEL[4];
		}
		return LEVEL[5];
	}

	public static int getFAILEDNUM() {
		return FAILEDNUM;
	}

	public static void main(String[] args) {
		
		String line = "2016-01-28 07:27:21,412 ERROR [com.bezuo.webmagic.taobaoTmall.TaobaoTmallApiProcessor] - < 商品已下架  >>  20160128|sale_off  https://item.taobao.com/item.htm?id=522794268475&scm=1029.newlist-0.1.0&ppath=&sku=&ug=#detail>";
		System.out.println(line);
	
	//	System.out.println(getMailContent("content"));
		
	}

		
}
