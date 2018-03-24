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
    EN_LANG = "en",
    FR_LANG = "fr",
    LAB = "Now is a good time!",
    FR_LAB = "C'est un bon moment",
    LAB2 = "So what?",
    DET	= "For all good men to do something nice for their families.",
    Q1 = "good time",
    Q2 = "So what?",
    Q3 = "good men",
    Q4 = "bogus",
    Q5 = "un bon moment";
	
  public FirstQueryTest() {
    System.out.println("--- First Query Test ---");
    environment = new ProviderEnvironment();
    IClient provider = environment.getProvider();
    JSONObject jo = new JSONObject();

    // got keys, see /config/mappings.json
    jo.put("lox", ID);

    List<String> en_labels = new ArrayList<String>();
    en_labels.add("Funky label");
    en_labels.add(LAB);
    JSONObject jo_label = new JSONObject();
    jo_label.put(EN_LANG, en_labels);
                
    jo.put("label", jo_label);

    JSONObject jo_detail = new JSONObject();
    en_labels = new ArrayList<String>();
    en_labels.add(DET);
    jo_detail.put(EN_LANG, en_labels);
    jo.put("details", jo_detail);
    System.out.println("JO #1" + jo);
                
    IResult r = provider.put(ID, INDEX, jo);
    System.out.println("Foo "+r.getErrorString());
    
    String ID2 = Long.toString(System.currentTimeMillis());
    en_labels = new ArrayList<String>();
    en_labels.add(LAB2);
    jo_label = new JSONObject();
    jo_label.put(EN_LANG, en_labels);

    jo = new JSONObject();
    jo.put("lox", ID2);
		
    jo.put("label", jo_label);
    jo_detail.remove(EN_LANG);
    jo.put("details", jo_detail);
    System.out.println("JO #2" + jo);
    r = provider.put(ID2, INDEX, jo);

    String ID3 = Long.toString(System.currentTimeMillis());
    List<String> fr_labels = new ArrayList<String>();
    fr_labels.add(FR_LAB);
    jo_label = new JSONObject();
    jo_label.put(FR_LANG, fr_labels);
    jo_label.put(EN_LANG, en_labels);

    jo = new JSONObject();
    jo.put("lox", ID3);
		
    jo.put("label", jo_label);
    jo.put("details", jo_detail);
    System.out.println("JO #3" + jo);
    r = provider.put(ID3, INDEX, jo);

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

    //
    // To query labels from any language, use the wildcard:
    // fields[0]="label.*";
    //
    fields[0]="label.fr";
    r = textQueryUtil.queryText(Q5, 0, 5, INDEX, indices, fields);
    System.out.println("EEE "+r.getErrorString()+" | "+r.getResultObject());

    fields[0] = "lox";
    r = textQueryUtil.queryText(ID, 0, 5, INDEX, indices, fields);
    System.out.println("FFF "+r.getErrorString()+" | "+r.getResultObject());

    environment.shutDown();
  }
}
//AAA  | [{"lox":"1517766289998","language":"en","details":["For all good men to do something nice for their families."],"label":["Now is a good time!","Funky label"]}]
//BBB  | [{"lox":"1517766291967","language":"en","details":"","label":["So what?"]}]
//CCC  | [{"lox":"1517766289998","language":"en","details":["For all good men to do something nice for their families."],"label":["Now is a good time!","Funky label"]}]
//DDD  | []
