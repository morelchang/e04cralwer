package morel.e04crawler.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface LoginOption {

	public HtmlForm fetchLoginForm(HtmlPage loginPage);

	public HtmlElement fetchLoginButton(HtmlForm loginForm);

	public String getLoginUrl();

}