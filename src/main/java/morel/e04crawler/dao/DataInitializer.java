package morel.e04crawler.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataInitializer {
	
	private static final Log logger = LogFactory.getLog(DataInitializer.class);

	public void init(boolean rebuild) {
		logger.info("initializing database");
		if (rebuild) {
			jobRecordDao.rebuildTable();
		} else {
			jobRecordDao.ensureTableExists();
		}
		logger.info("current job record count:" + jobRecordDao.listAll().size());
	}

	private JobRecordDao jobRecordDao;

	public JobRecordDao getJobRecordDao() {
		return jobRecordDao;
	}

	public void setJobRecordDao(JobRecordDao jobRecordDao) {
		this.jobRecordDao = jobRecordDao;
	}
	
}
