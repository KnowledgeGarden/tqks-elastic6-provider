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
public class PartialUpdateTest {
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
  public PartialUpdateTest() {
    System.out.println("--- Partial Update Test ---");
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
    System.out.println("JO #1" + jo);

    IResult r = provider.put(ID, INDEX, jo);
    System.out.println("Put result: " + r.getErrorString());

    JSONObject update_obj = new JSONObject();
    JSONObject detail_obj = new JSONObject();
    labels = new ArrayList<String>();
    labels.add(DET);
    detail_obj.put(LANG, labels);
    update_obj.put("details", detail_obj);
                
    r = provider.partialUpdateNode(ID, INDEX, update_obj);
    System.out.println("Update Results "  + r.getErrorString() + " | " + r.getResultObject());
    r = provider.get(ID, INDEX);
    System.out.println("Fetch Updated: " + r.getErrorString() + " | " + r.getResultObject());
		
    environment.shutDown();
  }
}
