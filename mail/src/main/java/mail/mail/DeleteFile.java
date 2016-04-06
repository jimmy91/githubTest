package mail.mail;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class DeleteFile {
	
	public static void delete(){
		Configuration conf = MyConfiguration.getConf();
		try {
			FileSystem hdfs = FileSystem.get(conf);
			
			String[] strs = new String[]{"/usr/mail/content","/usr/mail/error"};
			for(String s: strs) {
				Path deleFile = new Path(s);
				boolean isDeleted = hdfs.delete(deleFile, false);
				System.out.println("Delete?" + s + "-----" + isDeleted);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		Configuration conf = MyConfiguration.getConf();
		try {
			FileSystem hdfs = FileSystem.get(conf);
			
			String[] strs = new String[]{"/usr/mail/content","/usr/mail/error"};
			for(String s: strs) {
				Path deleFile = new Path(s);
				boolean isDeleted = hdfs.delete(deleFile, false);
				System.out.println("Delete?" + s + "-----" + isDeleted);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
