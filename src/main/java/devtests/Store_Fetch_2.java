/**
 * 
 */
package devtests;

import org.topicquests.es.ProviderEnvironment;
import org.topicquests.es.api.IClient;
import org.topicquests.support.api.IResult;
import java.util.*;
import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class Store_Fetch_2 {
	private ProviderEnvironment environment;
	private static final String
		INDEX = "topics", //see /config/provider-config.xml
		ID = Long.toString(System.currentTimeMillis()),
		LANG = "en",
		LAB = "Now is a good time!",
		LAB2 = "So what?",
		DET	= "For all good men to do something nice for their families.";

	/**
	 * 
	 */
	public Store_Fetch_2() {
		environment = new ProviderEnvironment();
		IClient provider = environment.getProvider();
		JSONObject jo = new JSONObject();
		// got keys, see /config/mappings.json
		jo.put("lox", ID);
		jo.put("language", LANG);
		List<String> labels = new ArrayList<String>();
		labels.add(LAB);
		labels.add(LAB2);
		jo.put("label", labels);
		jo.put("details", DET);
		IResult r = provider.put(ID, INDEX, jo);
		
		
		System.out.println("Foo "+r.getErrorString());
		r = provider.get(ID, INDEX);
		System.out.println("Bar "+r.getErrorString()+" | "+r.getResultObject());
		environment.shutDown();
		System.exit(0);
	}
//Bar  | {"lox":"1517602840935","language":"en","details":"For all good men to do something nice for their families.","label":["Now is a good time!","So what?"]}

}
