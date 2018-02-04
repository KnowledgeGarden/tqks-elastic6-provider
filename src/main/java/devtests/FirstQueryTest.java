/**
 * 
 */
package devtests;

import org.elasticsearch.action.search.SearchRequest;
import org.topicquests.es.ProviderEnvironment;
import org.topicquests.es.api.IClient;
import org.topicquests.es.api.IQueryDSL;
import org.topicquests.es.util.TextQueryUtil;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class FirstQueryTest {
	private ProviderEnvironment environment;
	private TextQueryUtil textQueryUtil;
	private final String
		INDEX = "topics",
		Q1 = "good time",
		Q2 = "So what?",
		Q3 = "good men",
		Q4 = "bogus";
	

	/**
	 * 
	 */
	public FirstQueryTest() {
		environment = new ProviderEnvironment();
		textQueryUtil = environment.getTextQueryUtil();
		String [] indices = new String [1];
		indices[0]=INDEX;
		String [] fields = new String[2];
		fields[0]="label";
		fields[1]="details";
		IResult r = textQueryUtil.queryText(Q1, 0, 5, INDEX, indices, fields);
		System.out.println("AAA "+r.getErrorString()+" | "+r.getResultObject());
		r = textQueryUtil.queryText(Q2, 0, 5, INDEX, indices, fields);
		System.out.println("BBB "+r.getErrorString()+" | "+r.getResultObject());
		r = textQueryUtil.queryText(Q3, 0, 5, INDEX, indices, fields);
		System.out.println("CCC "+r.getErrorString()+" | "+r.getResultObject());
		r = textQueryUtil.queryText(Q4, 0, 5, INDEX, indices, fields);
		System.out.println("DDD "+r.getErrorString()+" | "+r.getResultObject());
		environment.shutDown();
		System.exit(0);
	}

}
//AAA  | [{"lox":"1517766289998","language":"en","details":["For all good men to do something nice for their families."],"label":["Now is a good time!","Funky label"]}]
//BBB  | [{"lox":"1517766291967","language":"en","details":"","label":["So what?"]}]
//CCC  | [{"lox":"1517766289998","language":"en","details":["For all good men to do something nice for their families."],"label":["Now is a good time!","Funky label"]}]
//DDD  | []
