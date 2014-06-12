package morel.e04crawler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import morel.e04crawler.dao.JobRecordDao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DataQueryer {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

	public static void main(String[] args) {
		// initialize database
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		JobRecordDao jobRecordDao = (JobRecordDao) context.getBean("jobRecordDao");
		print(jobRecordDao.listAll("companyName, jobName"));
	}

	private static void print(List<JobRecord> listAll) {
		for (JobRecord jobRecord : listAll) {
			System.out.println(format(jobRecord));
		}
	}

	private static String format(JobRecord jobRecord) {
		return escape(jobRecord.getCompanyName()) + ","
				+ escape(jobRecord.getJobName()) + ","
				+ escape(jobRecord.getArea()) + ","
				+ formatDate(jobRecord.getLastUpdate()) + ","
				+ escape(jobRecord.getJobUrl());
	}

	private static String formatDate(Date lastUpdate) {
		if (lastUpdate == null) {
			return "";
		}
		return sdf.format(lastUpdate);
	}

	private static String escape(String value) {
		return value.replace(",", "_").replace("\n", "\\n").replace("\t", "\\t");
	}
}
