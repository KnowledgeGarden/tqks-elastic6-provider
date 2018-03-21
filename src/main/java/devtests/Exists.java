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
 *
 */
public class Exists {
	private ProviderEnvironment environment;
	private static final String
		INDEX = "topics", //see /config/provider-config.xml
		ID = Long.toString(System.currentTimeMillis()),
                BAD_ID = "foo",
		LANG = "en",
		LAB = "Now is a good time!",
		LAB2 = "So what?",
		DET	= "For all good men to do something nice for their families.";

	/**
	 * 
	 */
	public Exists() {
		environment = new ProviderEnvironment();
		IClient provider = environment.getProvider();
		JSONObject jo = new JSONObject();

		// got keys, see /config/mappings.json
		jo.put("lox", ID);

		List<String> labels = new ArrayList<String>();
		labels.add(LAB);
		labels.add("Funky label");
		//labels.add(LAB2);
                JSONObject jo_label = new JSONObject();
		jo_label.put(LANG, labels);
                
		jo.put("label", jo_label);

                JSONObject jo_detail = new JSONObject();
		labels = new ArrayList<String>();
		labels.add(DET);
                jo_detail.put(LANG, labels);
		jo.put("details", jo_detail);
                System.out.println("JO #1" + jo);
                
		IResult r = provider.put(ID, INDEX, jo);
		System.out.println("Foo "+r.getErrorString());
		String ID2 = Long.toString(System.currentTimeMillis());
		labels = new ArrayList<String>();
		labels.add(LAB2);
		jo = new JSONObject();
		jo.put("lox", ID2);
		
		jo.put("label", jo_label);
                jo_detail.remove(LANG);
		jo.put("details", jo_detail);
                System.out.println("JO #2" + jo);
		r = provider.put(ID2, INDEX, jo);
		
		System.out.println("Foo2 "+r.getErrorString());
		r = provider.exists(ID, INDEX);
		System.out.println("Exists Results: " + r.getErrorString() + " | " + r.getResultObject());
		r = provider.exists(ID2, INDEX);
		System.out.println("Exists Results "  + r.getErrorString() + " | " + r.getResultObject());
		r = provider.exists(BAD_ID, INDEX);
		System.out.println("Exists Results "  + r.getErrorString() + " | " + r.getResultObject());

		environment.shutDown();
	}
/////Bar  | {"lox":"1517602840935","language":"en","details":"For all good men to do something nice for their families.","label":["Now is a good time!","So what?"]}
//Bar  | {"lox":"1517765097681","language":"en","details":["For all good men to do something nice for their families."],"label":["Now is a good time!"]}
//Bar2  | {"lox":"1517765099733","language":"en","details":"","label":["So what?"]}

}
