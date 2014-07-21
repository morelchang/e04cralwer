package morel.e04crawler.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * using "http://login.104.com.tw/login.cfm" as login page
 * 
 * @author morel_chang
 *
 */
public class DesktopLoginOption implements LoginOption {

	public String getLoginUrl() {
		return "http://login.104.com.tw/login.cfm";
	}

	public HtmlElement fetchLoginButton(HtmlForm loginForm) {
		return loginForm.getInputByValue("會員登入");
	}

	public HtmlForm fetchLoginForm(HtmlPage loginPage) {
		return loginPage.getFormByName("form1");
	}

}
