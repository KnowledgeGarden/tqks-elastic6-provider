/**
 * 
 */
package devtests;

import org.elasticsearch.action.search.SearchRequest;
import org.topicquests.es.ProviderEnvironment;
import org.topicquests.es.api.IClient;
import org.topicquests.es.api.IQueryDSL;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class FirstQueryTest {
	private ProviderEnvironment environment;
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
		IClient provider = environment.getProvider();
		IQueryDSL dsl = environment.getQueryDSL();
		String [] indices = new String [1];
		indices[0]=INDEX;
		SearchRequest req = dsl.getTextQueryString(Q1, 0, 5, indices, "label", "details");
		IResult r = provider.listSearch(req, INDEX);
		System.out.println("AAA "+r.getErrorString()+" | "+r.getResultObject());
		req = dsl.getTextQueryString(Q2, 0, 5, indices, "label", "details");
		System.out.println("BBB "+r.getErrorString()+" | "+r.getResultObject());
		req = dsl.getTextQueryString(Q3, 0, 5, indices, "label", "details");
		System.out.println("CCC "+r.getErrorString()+" | "+r.getResultObject());
		req = dsl.getTextQueryString(Q4, 0, 5, indices, "label", "details");
		System.out.println("DDD "+r.getErrorString()+" | "+r.getResultObject());
		environment.shutDown();
		System.exit(0);
	}

}
/**
AAA  | [{"lox":"1517601475435","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517598969677","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517599668073","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517528526492","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517599309189","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}]
BBB  | [{"lox":"1517601475435","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517598969677","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517599668073","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517528526492","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517599309189","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}]
CCC  | [{"lox":"1517601475435","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517598969677","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517599668073","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517528526492","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517599309189","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}]
DDD  | [{"lox":"1517601475435","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517598969677","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517599668073","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517528526492","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}, {"lox":"1517599309189","language":"en","details":"For all good men to do something nice for their families.","label":"Now is a good time!"}]
This query for DDD should not have found anything, should have returned an empty list
Since there were no mappings installed when this ran, the query is probably not functional
{
	"from": 0,
	"size": 5,
	"query": {
		"multi_match": {
			"query": "bogus",
			"fields": ["details^1.0", "label^1.0"],
			"type": "best_fields",
			"operator": "OR",
			"slop": 0,
			"prefix_length": 0,
			"max_expansions": 50,
			"zero_terms_query": "NONE",
			"auto_generate_synonyms_phrase_query": true,
			"fuzzy_transpositions": true,
			"boost": 1.0
		}
	}
}
 */
