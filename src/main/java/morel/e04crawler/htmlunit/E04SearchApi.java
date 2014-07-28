package morel.e04crawler.htmlunit;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import morel.e04crawler.CodeValue;
import morel.e04crawler.JobRecord;
import morel.e04crawler.RoleType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class E04SearchApi {

	private static final Log logger = LogFactory.getLog(E04SearchApi.class);
	/**
	 * default job record fetch size
	 */
	private static final int FETCH_SIZE = 50;
	private static final String SEARCH_API_URL = "http://www.104.com.tw/i/apis/jobsearch.cfm";
	
	private List<CodeValue> cates = new ArrayList<CodeValue>();
	private List<CodeValue> areas = new ArrayList<CodeValue>();
	private List<CodeValue> inds = new ArrayList<CodeValue>();;
	private Set<RoleType> roles = new HashSet<RoleType>();

	private E04SearchApi() {
		super();
	}
	
	public static E04SearchApi create() {
		E04SearchApi api = new E04SearchApi();
		return api;
	}
	
	public List<JobRecord> search() {
		WebClient client = new WebClient(BrowserVersion.CHROME);
		List<JobRecord> result = searchByPage(client, 1);
		return result;
	}

	private List<JobRecord> searchByPage(WebClient client, int page) {
		logger.info("searching jobs page:" + page);
		String jsonResult = getJsonReuslt(client, page);
		List<JobRecord> result = new ArrayList<JobRecord>();

		// retrieve jobs in this page
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		try {
			root = mapper.readTree(jsonResult);
		} catch (IOException e) {
			logger.error("failed to read job data");
			throw new IllegalStateException(e);
		}
		Iterator<JsonNode> data = root.findValue("data").getElements();
		List<JobRecord> jobs = convertJobRecord(data);
		logger.info(jobs.size() + " jobs added as result");
		result.addAll(jobs);

		// try next page
		int totalpage = Integer.parseInt(root.path("TOTALPAGE").getTextValue());
		if (page < totalpage) {
			logger.info("totalpage:" + totalpage + ", trying to search next page");
			List<JobRecord> restJobs = searchByPage(client, page + 1);
			result.addAll(restJobs);
		}
		return result;
	}

	private String getJsonReuslt(WebClient client, int page) {
		HtmlPage response = null;
		String searchUrl = formatSearchUrl(page, FETCH_SIZE, "8");
		logger.info("retrieve jobs from API:" + searchUrl);
		try {
			response = client.getPage(searchUrl);
		} catch (IOException e) {
			logger.error(e);
			throw new IllegalStateException("failed to search E04 API, reason:" + e.getMessage());
		}

		String result = response.asText().replace("\n", "\\n");
		logger.info("search result:" + result);
		return result;
	}

	private List<JobRecord> convertJobRecord(Iterator<JsonNode> data) {
		List<JobRecord> result = new ArrayList<JobRecord>();
		while (data.hasNext()) {
			result.add(convertJobRecord(data.next()));
		}
		return result;
	}

	private JobRecord convertJobRecord(JsonNode n) {
		JobRecord result = new JobRecord();
		result.setJobId(value(n, "J"));
		result.setJobName(value(n, "JOB"));
		result.setJobUrl("http://www.104.com.tw/jobbank/cust_job/job.cfm?j=" + result.getJobId());
		result.setCompanyId(value(n, "C"));
		result.setCompanyName(value(n, "NAME"));
		result.setCompanyUrl("http://www.104.com.tw/jobbank/cust_job/introduce.cfm?j=" + result.getCompanyId());
		result.setArea(value(n, "JOB_ADDR_NO_DESCRIPT"));
		result.setSummary(value(n, "DESCRIPTION"));
		result.setLastUpdate(parseDate(value(n, "APPEAR_DATE")));
		result.setJobAddress(result.getArea() + value(n, "JOB_ADDRESS"));
		result.setJobCategory(value(n, "JOBCAT_DESCRIPT"));
		result.setPeriod(intvalue(n, "PERIOD"));
		result.setSalaryMonthLow(intvalue(n, "SAL_MONTH_LOW"));
		result.setSalaryMonthHigh(intvalue(n, "SAL_MONTH_HIGH"));
		return result;
	}

	/**
	 * get int value from node, 0 if NaN
	 * 
	 * @param n
	 * @param name
	 * @return
	 */
	private int intvalue(JsonNode n, String name) {
		return n.findValue(name).getIntValue();
	}

	private Date parseDate(String value) {
		try {
			return DateUtils.parseDate(value, "yyyyMMdd");
		} catch (ParseException e) {
			logger.error("failed to parse Date:" + value);
			return null;
		}
	}

	private String value(JsonNode n, String name) {
		return n.findValue(name).getTextValue();
	}

	private String formatSearchUrl(int page, int pagesize, String fmt) {
		return SEARCH_API_URL + "?&pgsz=" + pagesize + "&fmt=" + fmt + "&page=" + page
				+ formatCodeValues("cat", cates)
				+ formatCodeValues("area", areas)
				+ formatCodeValues("ind", inds) + formatRoleTyps("role", roles);
	}

	private String formatRoleTyps(String paramName, Set<RoleType> roles) {
		String values = "";
		for (Iterator<RoleType> it = roles.iterator(); it.hasNext();) {
			RoleType next = it.next();
			if (next == RoleType.FULL_TIME) {
				values += "1,3,";
			} else if (next == RoleType.PART_TIME) {
				values += "2,";
			}
		}
		values = StringUtils.removeEnd(values, ",");
		return String.format("&%s=%s", paramName, values);
	}

	private String formatCodeValues(String paramName, List<CodeValue> codeValues) {
		return String.format("&%s=%s", paramName, joinCodes(codeValues, ","));
	}

	private String joinCodes(List<CodeValue> codes, String sep) {
		String result = "";
		for (Iterator<CodeValue> it = codes.iterator(); it.hasNext();) {
			CodeValue codeValue = (CodeValue) it.next();
			result = result + codeValue.getCode() + (it.hasNext() ? sep : "");
		}
		return result;
	}

	public List<CodeValue> getCates() {
		return cates;
	}

	public E04SearchApi withCates(List<CodeValue> cates) {
		this.cates = cates;
		return this;
	}

	public List<CodeValue> getAreas() {
		return areas;
	}

	public E04SearchApi withAreas(List<CodeValue> areas) {
		this.areas = areas;
		return this;
	}

	public List<CodeValue> getInds() {
		return inds;
	}

	public E04SearchApi withInds(List<CodeValue> inds) {
		this.inds = inds;
		return this;
	}

	public Set<RoleType> getRoles() {
		return roles;
	}

	public E04SearchApi withRoles(Set<RoleType> roles) {
		this.roles = roles;
		return this;
	}
	
}
