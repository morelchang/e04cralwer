package morel.e04crawler.htmlunit;

import morel.e04crawler.JobRecord;

import com.gargoylesoftware.htmlunit.html.DomNode;

public interface AbsorbAction {

	public void doAbsorb(DomNode node, JobRecord record);
	
}
