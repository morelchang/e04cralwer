package morel.e04crawler.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import morel.e04crawler.JobRecord;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class JdbcJobRecordDao extends JdbcDaoSupport implements JobRecordDao {

	private final class JobRecordMapper implements RowMapper<JobRecord> {
		public JobRecord mapRow(ResultSet rs, int index)
				throws SQLException {
			JobRecord result = new JobRecord();
			result.setCompanyId(rs.getString("companyId"));
			result.setCompanyName(rs.getString("companyName"));
			result.setArea(rs.getString("area"));
			result.setJobId(rs.getString("jobId"));
			result.setJobName(rs.getString("jobName"));
			result.setJobUrl(rs.getString("jobUrl"));
			result.setLastUpdate(rs.getDate("lastUpdate"));
			result.setCompanyUrl(rs.getString("companyUrl"));
			result.setSummary(rs.getString("summary"));
			return result;
		}
	}

	public List<JobRecord> listAll() {
		return getJdbcTemplate().query("select * from JobRecord", new JobRecordMapper());
	}

	public List<JobRecord> listAll(String ordering) {
		return getJdbcTemplate().query("select * from JobRecord order by " + ordering, new JobRecordMapper());
	}

	public JobRecord find(String companyId, String jobId) {
		List<JobRecord> result = getJdbcTemplate().query("select * from JobRecord where companyId = ? and jobId = ?"
				, new Object[] {companyId, jobId}, new JobRecordMapper());
		
		if (result.size() > 0) {
			if (result.size() > 1) {
				logger.warn("expected result 1 but is " + result.size());
			}
			return result.get(0);
		}
		return null;
	}

	public int insertOrUpdate(JobRecord record) {
		if (find(record.getCompanyId(), record.getJobId()) == null) {
			return insert(record);
		}
		return update(record);
	}

	private int update(JobRecord record) {
		logger.info("updating record:" + record);
		try {
			return getJdbcTemplate().update(
					"update JobRecord set companyName = ?, companyUrl = ?, jobName = ?, jobUrl = ?, lastUpdate = ?, summary = ?, area = ? "
					+ "where companyId = ? and jobId = ? ",
							new Object[] { record.getCompanyName(),
									record.getCompanyUrl(),
									record.getJobName(), record.getJobUrl(),
									record.getLastUpdate(),
									record.getSummary(), record.getArea(),
									record.getCompanyId(), record.getJobId() });
		} catch (DataAccessException e) {
			logger.error("failed to update jobRecord:" + record + ", reason:" + e.getMessage());
			throw e;
		}
	}

	private int insert(JobRecord record) {
		try {
			return getJdbcTemplate().update(
					"insert into JobRecord (companyId, companyName, companyUrl, jobId, jobName, jobUrl, lastUpdate, summary, area) "
							+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
							new Object[] { record.getCompanyId(),
									record.getCompanyName(),
									record.getCompanyUrl(), record.getJobId(),
									record.getJobName(), record.getJobUrl(),
									record.getLastUpdate(),
									record.getSummary(), record.getArea() });
		} catch (DataAccessException e) {
			logger.error("failed to insert jobRecord:" + record + ", reason:" + e.getMessage());
			throw e;
		}
	}

	public boolean ensureTableExists() {
		try {
			listAll();
			logger.info("table exists");
			return false;
		} catch (Exception e) {
			logger.info("exception:" + e.getMessage() + ", perhaps table does not exist, creating...");
			getJdbcTemplate().execute("create table JobRecord ("
					+ "companyId varchar(30) not null, "
					+ "companyName varchar(300), "
					+ "companyUrl varchar(300), "
					+ "jobId varchar(20) not null, "
					+ "jobName varchar(300), "
					+ "jobUrl varchar(300), "
					+ "summary varchar(4000), "
					+ "lastUpdate date, "
					+ "area varchar(100))");
			logger.info("table created");
		}
		return true;
	}

	public int removeAll() {
		return getJdbcTemplate().update("delete from JobRecord");
	}

	public boolean rebuildTable() {
		try {
			logger.info("droping table JobRecord...");
			getJdbcTemplate().execute("drop table JobRecord");
			logger.info("dropped table JobRecord");
		} catch(Exception e) {
			logger.error("failed to drop table JobRecord", e);
		}
		return ensureTableExists();
	}
	
}
