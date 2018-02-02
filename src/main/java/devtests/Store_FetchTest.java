/**
 * 
 */
package devtests;

import org.topicquests.es.ProviderEnvironment;
import org.topicquests.es.api.IClient;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class Store_FetchTest {
	private ProviderEnvironment environment;
	private static final String
		INDEX = "topics", //see /config/provider-config.xml
		ID = Long.toString(System.currentTimeMillis()),
		LANG = "en",
		LAB = "Now is a good time!",
		DET	= "For all good men to do something nice for their families.";

	/**
	 * 
	 */
	public Store_FetchTest() {
		environment = new ProviderEnvironment();
		IClient provider = environment.getProvider();
		JSONObject jo = new JSONObject();
		// got keys, see /config/mappings.json
		jo.put("lox", ID);
		jo.put("language", LANG);
		jo.put("label", LAB);
		jo.put("details", DET);
		IResult r = provider.put(ID, INDEX, jo);
		
		
		System.out.println("Foo "+r.getErrorString());
		//This may not work: fetching just after storing may return nothing
		r = provider.get(ID, INDEX);
		System.out.println("Bar "+r.getErrorString()+" | "+r.getResultObject());
		environment.shutDown();
		System.exit(0);
		// TODO Auto-generated constructor stub
	}
//Bar  | {"lox":"1517601475435","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}

}
