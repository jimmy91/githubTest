package mail.mail;

import org.apache.hadoop.conf.Configuration;

public class MyConfiguration {
	public static Configuration getConf() {
		
		String ipPart = "hdfs://192.168.31.104:9000"; // "hdfs://192.168.31.104:9000"
		
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", ipPart);
		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
	//	conf.set("mapreduce.jobhistory.address", "192.168.31.104:10020");
	//	conf.set("mapreduce.jobhistory.webapp.address", "192.168.31.104:19888");
		return conf;
	}
}
