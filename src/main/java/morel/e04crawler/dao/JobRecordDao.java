package morel.e04crawler.dao;

import java.util.List;

import morel.e04crawler.JobRecord;

public interface JobRecordDao {
	
	public List<JobRecord> listAll();

	public List<JobRecord> listAll(String ordering);
	
	public JobRecord find(String companyId, String jobId);
	
	public int insertOrUpdate(JobRecord record);

	public boolean ensureTableExists();

	public int removeAll();
	
	public boolean rebuildTable();

}
