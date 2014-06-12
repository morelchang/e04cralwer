package morel.e04crawler.httpclient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class Main {

	public static void main(String[] args) throws Exception {
		HttpClientController controller = new HttpClientController();

		// approach login page
		HttpResponse login1 = controller
				.post("http://login.104.com.tw/login.cfm");

		// extract login redirect url in response form and loginw with credential
		controller.postFormInResponse(
				login1,
				"form1",
				NameValuePairBuilder.create().add("id_name", "{account}")
						.add("password", "{password}").build());
		
		// fetch list of Java jobs - y2
		HttpResponse javaJobsResponse = controller.get("http://pda.104.com.tw/my104/mate/list?itemNo=y2");

		// login successfully!
		System.out.println(EntityUtils.toString(javaJobsResponse.getEntity(), "Big5"));
	}

}
