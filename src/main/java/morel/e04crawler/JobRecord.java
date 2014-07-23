package morel.e04crawler;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class JobRecord {

	private String companyId;
	private String companyName;
	private String companyUrl;
	private String area;
	private String jobId;
	private String jobName;
	private String jobUrl;
	private String summary;
	private Date lastUpdate;
	private String jobAddress;
	private String jobCategory;
	private int period;
	private int salaryMonthLow;
	private int salaryMonthHigh;

	/**
	 * 公司名稱
	 * @return
	 */
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * 公司說明網址
	 * @return
	 */
	public String getCompanyUrl() {
		return companyUrl;
	}

	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}

	/**
	 * 工作區域
	 * @return
	 */
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 104公司ID
	 * @return
	 */
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * 104職位ID
	 * @return
	 */
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * 職位名稱
	 * @return
	 */
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * 104職位網址
	 * @return
	 */
	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

	/**
	 * 職務說明
	 * @return
	 */
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * 最近更新日期
	 * @return
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 工作地點
	 * @return
	 */
	public String getJobAddress() {
		return jobAddress;
	}

	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}

	/**
	 * 職務類別
	 * @return
	 */
	public String getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}

	/**
	 * 工作年資
	 * @return
	 */
	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * 最低月薪
	 * @return
	 */
	public int getSalaryMonthLow() {
		return salaryMonthLow;
	}

	public void setSalaryMonthLow(int salaryMonthLow) {
		this.salaryMonthLow = salaryMonthLow;
	}

	/**
	 * 最高月薪
	 * @return
	 */
	public int getSalaryMonthHigh() {
		return salaryMonthHigh;
	}

	public void setSalaryMonthHigh(int salaryMonthHigh) {
		this.salaryMonthHigh = salaryMonthHigh;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
