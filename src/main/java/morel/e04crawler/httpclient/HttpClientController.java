package morel.e04crawler.httpclient;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpClientController {

	private CloseableHttpClient client;
	private String currentHostPrefix;

	public HttpClientController() {
		super();
		this.client = HttpClients.createDefault();
	}

	public HttpResponse post(String url)
			throws UnsupportedEncodingException, ClientProtocolException,
			IOException {
		return post(url, new ArrayList<NameValuePair>());
	}

	public HttpResponse post(String url, List<NameValuePair> postParam)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException {
		// ensure postUrl and currentHostPrefix values
		url = ensureUrlFormatAndChangeCurrentHostPrefix(url);
		
		// printing post information
		System.out.println("");
		System.out.println("current hostPrefix:" + currentHostPrefix);
		System.out.println("posting to URL:" + url + " with params:" + postParam);
		
		// prepare 104 login post request
		HttpPost loginPost = new HttpPost(url);
		loginPost.setEntity(new UrlEncodedFormEntity(postParam));

		// login & test response status
		HttpResponse response = client.execute(loginPost);
		System.out.println("response status:" + response.getStatusLine());
		System.out.println("response headers:");
		System.out.println(Arrays.asList(response.getAllHeaders()));
		return response;
	}

	private String ensureUrlFormatAndChangeCurrentHostPrefix(String url) {
		if (isFullUrl(url)) {
			currentHostPrefix = changeCurrentHostPrefixIfNeeded(url);
		} else {
			if (currentHostPrefix == null) {
				throw new IllegalArgumentException("a full url should be specified at first post");
			}
			url = currentHostPrefix + url;
		}
		return url;
	}

	public HttpResponse postFormInResponse(HttpResponse r, String formName) throws IOException, Exception,
			UnsupportedEncodingException, ClientProtocolException {
		return postFormInResponse(r, formName, new ArrayList<NameValuePair>());
	}

	public HttpResponse postFormInResponse(HttpResponse r, String formName, List<NameValuePair> params) throws IOException, Exception,
			UnsupportedEncodingException, ClientProtocolException {
		// find form in response
		String respnseContent = EntityUtils.toString(r.getEntity());
		System.out.println("response content:");
		System.out.println(respnseContent);
		Document responseDom = Jsoup.parse(respnseContent);
		Elements elements = responseDom.select("form[name=" + formName
				+ "]");
		if (elements.size() != 1) {
			throw new Exception(formName
					+ " not found or more than one found, size:"
					+ elements.size());
		}
		String formAction = elements.get(0).attr("action");

		// collect params in this form as post params
		Elements inputs = elements.get(0).select("input");
		NameValuePairBuilder paramBuilder = NameValuePairBuilder.create();
		for (Element input : inputs) {
			NameValuePair param = findParam(input.attr("name"), params);
			if (param != null) {
				paramBuilder.add(param.getName(), param.getValue());
			} else {
				paramBuilder.add(input.attr("name"), input.attr("value"));
			}
		}
		
		// change currentHostPrefix (redirect)
		currentHostPrefix = changeCurrentHostPrefixIfNeeded(formAction);

		// post this form and get response
		HttpResponse redirectResponse = post(formAction, paramBuilder.build());
		return redirectResponse;
	}

	private NameValuePair findParam(String name, List<NameValuePair> params) {
		for (NameValuePair pair : params) {
			if (pair.getName().equals(name)) {
				return pair;
			}
		}
		return null;
	}

	private String changeCurrentHostPrefixIfNeeded(String url) {
		if (isFullUrl(url)) {
			Pattern p = Pattern.compile("(http[s]?://[^/]+/).*");
			Matcher m = p.matcher(url);
			m.matches();
			String redirectPrefix = m.group(1);
			System.out.println("chnage currentHostPrefix to:" + redirectPrefix);
			return redirectPrefix;
 		}
		
		if (currentHostPrefix == null) {
			throw new IllegalArgumentException("url should be full, but is" + url);
		}
		return currentHostPrefix;
	}
	
	private boolean isFullUrl(String formAction) {
		return formAction.startsWith("http://") || formAction.startsWith("https://");
	}

	public HttpResponse get(String url) throws ClientProtocolException, IOException {
		// ensure postUrl and currentHostPrefix values
		url = ensureUrlFormatAndChangeCurrentHostPrefix(url);

		// printing post information
		System.out.println("");
		System.out.println("current hostPrefix:" + currentHostPrefix);
		System.out.println("getting to URL:" + url);
		
		// prepare 104 login post request
		HttpGet get = new HttpGet(url);

		// login & test response status
		HttpResponse response = client.execute(get);
		System.out.println("response status:" + response.getStatusLine());
		System.out.println("response headers:");
		System.out.println(Arrays.asList(response.getAllHeaders()));
		return response;
	}

}
