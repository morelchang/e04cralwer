package morel.e04crawler.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * using "http://m.104.com.tw/login" as login page
 * 
 * @author morel_chang
 *
 */
public class MobileLoginOption implements LoginOption {

	public String getLoginUrl() {
		return "http://m.104.com.tw/login";
	}

	public HtmlElement fetchLoginButton(HtmlForm loginForm) {
			return (HtmlElement) loginForm.querySelector("#loginBut");
	}

	public HtmlForm fetchLoginForm(HtmlPage loginPage) {
			return loginPage.getFormByName("login");
	}

}
