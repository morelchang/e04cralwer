package morel.e04crawler.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import morel.e04crawler.JobRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class E04WebPageJobCrawler {
	
	private static final Log logger = LogFactory.getLog(E04WebPageJobCrawler.class);

	private E04Engine engine = new E04Engine();
	
	public List<JobRecord> fetch(String loginAccount, String loginPassword,
			String itemKey, int limitPageCount) throws IOException,
			MalformedURLException {
		// get login page
		logger.info("fetching 104 job detail by itemKey:" + itemKey);
		String captchaUrl = engine.init();
		
		String captcha = "";
		if (captchaUrl != null) {
			captcha = displayCaptchaInSwing(captchaUrl);
		}
		engine.login(loginAccount, loginPassword, captcha);
		
		List<JobRecord> records = engine.fetchJobByItemKey(itemKey, limitPageCount);
		
		engine.logout();
		return records;
	}

	private String displayCaptchaInSwing(String captchaUrl)
			throws MalformedURLException {
		String captcha;
		logger.info("captcha image found, display for resolving...");
		JFrame frame = displayCaptchaPhrase(captchaUrl);
		captcha = readInCaptcha();
		frame.setVisible(false);
		frame.dispose();
		return captcha;
	}

	@SuppressWarnings("resource")
	private String readInCaptcha() {
		logger.info("enter captcha phrase:");
		String captcha = new Scanner(System.in).nextLine();
		logger.info("captcha read:" + captcha);
		return captcha;
	}

	private JFrame displayCaptchaPhrase(String captchaUrl) throws MalformedURLException {
		JLabel label = new JLabel(new ImageIcon(new URL(captchaUrl)));
		JFrame frame = new JFrame();
		frame.add(label);
		frame.setSize(300, 400);
		frame.setVisible(true);
		return frame;
	}
	
}
