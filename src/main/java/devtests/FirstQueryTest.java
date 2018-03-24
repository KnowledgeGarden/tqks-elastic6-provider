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
import java.util.*;
import net.minidev.json.JSONObject;

/**
 *
 */
public class FirstQueryTest {
  private ProviderEnvironment environment;
  private TextQueryUtil textQueryUtil;
  private final String
  INDEX = "topics",
    ID = Long.toString(System.currentTimeMillis()),
    LANG = "en",
    LAB = "Now is a good time!",
    LAB2 = "So what?",
    DET	= "For all good men to do something nice for their families.",
    Q1 = "good time",
    Q2 = "So what?",
    Q3 = "good men",
    Q4 = "bogus";
	
  public FirstQueryTest() {
    System.out.println("--- First Query Test ---");
    environment = new ProviderEnvironment();
    IClient provider = environment.getProvider();
    JSONObject jo = new JSONObject();

    // got keys, see /config/mappings.json
    jo.put("lox", ID);

    List<String> labels = new ArrayList<String>();
    labels.add("Funky label");
    labels.add(LAB);
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
    jo_label = new JSONObject();
    jo_label.put(LANG, labels);

    jo = new JSONObject();
    jo.put("lox", ID2);
		
    jo.put("label", jo_label);
    jo_detail.remove(LANG);
    jo.put("details", jo_detail);
    System.out.println("JO #2" + jo);
    r = provider.put(ID2, INDEX, jo);

    System.out.println("--- calling refresh...");
    r = provider.refresh(INDEX);
    System.out.println("--- DONE calling refresh...");
    
    textQueryUtil = environment.getTextQueryUtil();
    String [] indices = new String [1];
    indices[0]=INDEX;
    String [] fields = new String[2];
    fields[0]="label.en";
    fields[1]="details.en";

    r = textQueryUtil.queryText(Q1, 0, 5, INDEX, indices, fields);
    System.out.println("AAA "+r.getErrorString()+" | "+r.getResultObject());

    r = textQueryUtil.queryText(Q2, 0, 5, INDEX, indices, fields);
    System.out.println("BBB "+r.getErrorString()+" | "+r.getResultObject());

    r = textQueryUtil.queryText(Q3, 0, 5, INDEX, indices, fields);
    System.out.println("CCC "+r.getErrorString()+" | "+r.getResultObject());

    r = textQueryUtil.queryText(Q4, 0, 5, INDEX, indices, fields);
    System.out.println("DDD "+r.getErrorString()+" | "+r.getResultObject());

    fields[0] = "lox";
    r = textQueryUtil.queryText(ID, 0, 5, INDEX, indices, fields);
    System.out.println("EEE "+r.getErrorString()+" | "+r.getResultObject());

    environment.shutDown();
  }
}
//AAA  | [{"lox":"1517766289998","language":"en","details":["For all good men to do something nice for their families."],"label":["Now is a good time!","Funky label"]}]
//BBB  | [{"lox":"1517766291967","language":"en","details":"","label":["So what?"]}]
//CCC  | [{"lox":"1517766289998","language":"en","details":["For all good men to do something nice for their families."],"label":["Now is a good time!","Funky label"]}]
//DDD  | []
