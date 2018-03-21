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
		List<JSONObject> labels = new ArrayList<JSONObject>();
		JSONObject ll = new JSONObject();
		ll.put("en", LAB);
		labels.add(ll);
		ll = new JSONObject();
		ll.put("en", LAB2);
		labels.add(ll);
		
		//labels.add(LAB2);
		jo.put("label", labels);
//		labels = new ArrayList<String>();
//		labels.add(DET);
//		jo.put("details", labels);
		environment.logDebug("AA "+jo.toJSONString());
		IResult r = provider.put(ID, INDEX, jo);
		System.out.println("Foo "+r.getErrorString());
		String ID2 = Long.toString(System.currentTimeMillis());
		labels = new ArrayList<JSONObject>();
		ll = new JSONObject();
		ll.put("en", "Funky label");
		labels.add(ll);
		jo = new JSONObject();
		jo.put("lox", ID2);
		
		jo.put("label", labels);
		r = provider.put(ID2, INDEX, jo);
		
		System.out.println("Foo2 "+r.getErrorString());
		r = provider.get(ID, INDEX);
		System.out.println("Bar "+r.getErrorString()+" | "+r.getResultObject());
		r = provider.get(ID2, INDEX);
		System.out.println("Bar2 "+r.getErrorString()+" | "+r.getResultObject());
		
		environment.shutDown();
		System.exit(0);
	}
/////Bar  | {"lox":"1517602840935","language":"en","details":"For all good men to do something nice for their families.","label":["Now is a good time!","So what?"]}
//Bar  | {"lox":"1517765097681","language":"en","details":["For all good men to do something nice for their families."],"label":["Now is a good time!"]}
//Bar2  | {"lox":"1517765099733","language":"en","details":"","label":["So what?"]}

}
