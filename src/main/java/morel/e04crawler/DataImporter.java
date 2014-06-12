package morel.e04crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;

import morel.e04crawler.dao.DataInitializer;
import morel.e04crawler.dao.JobRecordDao;
import morel.e04crawler.htmlunit.E04WebPageJobCrawler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class DataImporter {
	
	private static final String PROP_PATH = "./conf/dataImporter.properties";

	public static void main(String[] args) throws Exception {
		DataImporter importer = new DataImporter();
		importer.startImport();
	}

	public void startImport() throws IOException, MalformedURLException {
		// parameters
		boolean rebuild = false;
		String account = readProp("account");
		String password = readProp("password");
		String itemKey = readProp("itemKey");

		// initialize database
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		DataInitializer init = (DataInitializer) context.getBean("dataInitializer");
		init.init(rebuild);
		
		// fetch jobrecords
		E04WebPageJobCrawler crawler = new E04WebPageJobCrawler();
		List<JobRecord> result = crawler.fetch(account, password, itemKey, Integer.MAX_VALUE);
		
		// persist result
		JobRecordDao jobRecordDao = (JobRecordDao) context.getBean("jobRecordDao");
		for (JobRecord record : result) {
			jobRecordDao.insertOrUpdate(record);
		}
		
		System.out.println("taotal record:" + jobRecordDao.listAll().size());
	}
	
	private String readProp(String key) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(new File(PROP_PATH)));
		return prop.getProperty(key);
	}

}
