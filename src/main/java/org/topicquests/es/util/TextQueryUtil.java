/**
 * 
 */
package org.topicquests.es.util;

import org.elasticsearch.action.search.SearchRequest;
import org.topicquests.es.ProviderEnvironment;
import org.topicquests.es.api.IClient;
import org.topicquests.es.api.IQueryDSL;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

import java.util.*;
/**
 * @author jackpark
 * A pojo which fetches text against multiple fields and aggregates them
 */
public class TextQueryUtil {
	private ProviderEnvironment environment;
	private IClient provider;
	private IQueryDSL dsl;

	/**
	 * 
	 */
	public TextQueryUtil(ProviderEnvironment env) {
		environment = env;
		provider = environment.getProvider();
		dsl = environment.getQueryDSL();
	}
	
	/**
	 * 
	 * @param query
	 * @param start
	 * @param count
	 * @param index
	 * @param indices
	 * @param fields
	 * @return
	 */
	public IResult queryText(String query, int start, int count, String index,
					String [] indices, String [] fields) {
		IResult result = new ResultPojo();
		Set<JSONObject> objects = new HashSet<JSONObject>();
		result.setResultObject(objects);
		IResult r;
		SearchRequest sr;		
		sr = dsl.getTextQueryString(query, start, count, indices, fields);
		r = provider.listSearch(sr, index);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		if (r.getResultObject() != null)
			objects.addAll((List<JSONObject>)r.getResultObject());				
		return result;
	}

}
