package morel.e04crawler.httpclient;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class NameValuePairBuilder {

	private List<NameValuePair> target = new ArrayList<NameValuePair>();
	
	private NameValuePairBuilder() {
		super();
	}
	
	public static NameValuePairBuilder create() {
		return new NameValuePairBuilder();
	}
	
	public NameValuePairBuilder add(String name, String value) {
		target.add(new BasicNameValuePair(name, value));
		return this;
	}
	
	public List<NameValuePair> build() {
		return target;
	}

	@Override
	public String toString() {
		return target.toString();
	}
	
}
