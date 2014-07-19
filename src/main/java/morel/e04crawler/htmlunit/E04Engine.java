package morel.e04crawler.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import morel.e04crawler.JobRecord;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class E04Engine {

	private static final Log logger = LogFactory.getLog(E04Engine.class);

	private static final int AVG_AJAX_TIME = 500;
	private static final int AJAX_TIME_OFFSET = 100;

	private WebClient client = new WebClient(BrowserVersion.CHROME);
	private HtmlPage loginPage;

	public String init() throws IOException, MalformedURLException {
		String loginUrl = "http://login.104.com.tw/login.cfm";
		logger.info("fetching login page:" + loginUrl + "...");
		loginPage = client.getPage(loginUrl);
		
		// retrieve captcha image for resolving
		String captchaUrl = findCaptchaUrl(loginPage);
		return captchaUrl;
	}

	public void login(String loginAccount, String loginPassword,
			String captcha) throws MalformedURLException, IOException {
		HtmlForm loginForm = loginPage.getFormByName("form1");
		
		// set login information and login
		logger.info("try to login with account:" + loginAccount + ", captcha:" + captcha + "...");
		if (captcha != null) {
			logger.debug("captcha found, setting captcha");
			loginForm.getInputByName("capcha").setValueAttribute(captcha);
		}
		loginForm.getInputByName("id_name").setValueAttribute(loginAccount);
		loginForm.getInputByName("password").setValueAttribute(loginPassword);
		
		// click to login
		HtmlInput loginButton = loginForm.getInputByValue("·|­ûµn¤J");
		if (loginButton == null) {
			logger.error("====> loign button not found!");
			throw new IllegalStateException("login button not found in login page");
		}
		HtmlPage pdaPage = loginButton.click();
		logger.info(pdaPage.asXml());
		
		// TODO: determine login failed situation by response page of click
	}
	
	public List<JobRecord> fetchJobByItemKey(String itemKey) throws IOException, MalformedURLException {
		return fetchJobByItemKey(itemKey, Integer.MAX_VALUE);
	}
	
	public List<JobRecord> fetchJobByItemKey(String itemKey, int limitPageCount)
			throws IOException, MalformedURLException {
		// fetch java related jobs
		logger.info("fetching jobDetail by itemKey:" + itemKey);
		HtmlPage javaJobsPage = client.getPage("http://pda.104.com.tw/my104/mate/list?itemNo=" + itemKey);
	
		// get total job count(first '.numeral' element)/20 as detail page count
		String jobCountValue = javaJobsPage.querySelector(".numeral").getTextContent();
		int jobCount = Integer.valueOf(jobCountValue);
		logger.info("total job count:" + jobCount);
		int totalPageCount = jobCount / 20 + ((jobCount % 20 == 0) ? (0) : (1)) - 1;
		int fetchPageCount = Math.min(totalPageCount, limitPageCount);
		
		// loop to call get_joblist_detail() javascript to load more jobs by detail page count
		client.waitForBackgroundJavaScript(2000);
		int totalWaitTime = 0;
		for (int i = 0; i < fetchPageCount; i++) {
			ScriptResult isLoadingpage = javaJobsPage.executeJavaScript("isLoadingPage");
			if (ScriptResult.isFalse(isLoadingpage)) {
				logger.info("executing get_joblist_detail() function..." + i + " time");
				javaJobsPage.executeJavaScript("get_joblist_detail()");
				totalWaitTime = 0; // rest wait time
				continue;
			}
			
			// still loading
			if (!ScriptResult.isFalse(isLoadingpage)) {
				// adjust wait time
				long waitTime = AVG_AJAX_TIME;
				if (totalWaitTime > 0) {
					waitTime = AJAX_TIME_OFFSET;
				}

				logger.info("wait " + waitTime + " miliseconds for loading");
				client.waitForBackgroundJavaScript(waitTime);
				i--;
				totalWaitTime += waitTime;
				continue;
			}
			
			// isLoadingpage undefined, page not loaded?
			if (ScriptResult.isUndefined(isLoadingpage)) {
				logger.info("wait 1000 miliseconds for undefined isLoadingpage");
				client.waitForBackgroundJavaScript(1000);
				i--;
				continue;
			}
		}
		
		// save job result with (company_id, job_id, company_name, job_name, job_summary)
		List<JobRecord> records = initRecords(jobCount);
		absorb(records, javaJobsPage.querySelectorAll("div.joblist_cont .compname_1 a"), new AbsorbAction() {
			public void doAbsorb(DomNode node, JobRecord record) {
				record.setCompanyName(node.getTextContent());
				record.setCompanyUrl(node.getAttributes().getNamedItem("href").getNodeValue());
			}
		});
		absorb(records, javaJobsPage.querySelectorAll("div.joblist_cont .jobname_1 a"), new AbsorbAction() {
			public void doAbsorb(DomNode node, JobRecord record) {
				record.setJobName(node.getTextContent());
				record.setJobUrl(node.getAttributes().getNamedItem("href").getNodeValue());
			}
		});
		absorb(records, javaJobsPage.querySelectorAll("div.joblist_cont .area"), new AbsorbAction() {
			public void doAbsorb(DomNode node, JobRecord record) {
				record.setArea(node.getTextContent());
			}
		});
		absorb(records, javaJobsPage.querySelectorAll("div.joblist_cont .joblist_summary"), new AbsorbAction() {
			public void doAbsorb(DomNode node, JobRecord record) {
				record.setSummary(node.getTextContent());
			}
		});
		absorb(records, javaJobsPage.querySelectorAll("div.joblist_cont input[name=apply]"), new AbsorbAction() {
			Pattern p = Pattern.compile("applyJob\\('(.*)', *'(.*)', *'.*'\\);");
			
			public void doAbsorb(DomNode node, JobRecord record) {
				// company_id & job_id in format: onclick:applyJob('6785172','27941103000','44524b305e64475e393a426b40363e68644424b315e6446292929292740592e2d008j52');
				record.setCompanyId(splitCompanyId(node.getAttributes().getNamedItem("onclick").getNodeValue()));
				record.setJobId(splitJobId(node.getAttributes().getNamedItem("onclick").getNodeValue()));
			}
	
			private String splitJobId(String nodeValue) {
				Matcher m = p.matcher(nodeValue);
				m.matches();
				return m.group(1);
			}
	
			private String splitCompanyId(String nodeValue) {
				Matcher m = p.matcher(nodeValue);
				m.matches();
				return m.group(2);
			}
		});
		absorb(records, javaJobsPage.querySelectorAll("div.joblist_cont .date"), new AbsorbAction() {
			public void doAbsorb(DomNode node, JobRecord record) {
				record.setLastUpdate(parseDate(node.getTextContent()));
			}

			private Date parseDate(String dateValue) {
				try {
					return DateUtils.parseDate("2014/" + dateValue, "yyyy/MM/dd");
				} catch (ParseException e) {
					throw new RuntimeException("failed to parse dateValue:" + dateValue, e);
				}
			}
		});
		
		cleanUp(records);
		return records;
	}

	public void logout() throws IOException, MalformedURLException {
		// logout
		client.getPage("http://login.104.com.tw/logout.cfm");
		client.closeAllWindows();
	}

	/**
	 * cleanup record which is not valid format
	 * 
	 * @param records
	 * @return
	 */
	private void cleanUp(List<JobRecord> records) {
		Iterator<JobRecord> it = records.iterator();
		while (it.hasNext()) {
			JobRecord jobRecord = it.next();
			if (jobRecord.getCompanyId() == null || jobRecord.getJobId() == null) {
				it.remove();
			}
		}
	}

	private void absorb(List<JobRecord> records,
			DomNodeList<DomNode> nodes, AbsorbAction action) {
		if (nodes.size() != records.size()) {
			logger.warn("warning, size does not match, nodes:" + nodes.size() + ", records:" + records.size());
			if (nodes.size() > records.size()) {
				logger.warn("!!!!!!!! nodes.size is larger than expected!");
				return;
			}
		}
		
		for (int i = 0; i < nodes.size(); i++) {
			DomNode node = nodes.get(i);
			JobRecord record = records.get(i);
			action.doAbsorb(node, record);
		}
	}

	private List<JobRecord> initRecords(int jobCount) {
		List<JobRecord> result = new ArrayList<JobRecord>();
		for (int i = 0; i < jobCount; i++) {
			result.add(new JobRecord());
			
		}
		return result;
	}

	private String findCaptchaUrl(HtmlPage loginPage) {
		logger.info("fetching captcha img...");
		client.waitForBackgroundJavaScript(5000);
		String captchaUrl = null;
		DomNode e = loginPage.querySelector(".checkImg");
		if (e != null) {
			captchaUrl = e.getAttributes().getNamedItem("src").getNodeValue();
		}
		logger.info("captcah image url:" + captchaUrl);
		return captchaUrl;
	}

}
