/**
 * 
 */
package org.topicquests.es;
import java.util.*;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.topicquests.es.api.IQueryDSL;

/**
 * @author jackpark
 *
 */
public class QueryDSL implements IQueryDSL {
	private ProviderEnvironment environment;
	/**
	 * 
	 */
	public QueryDSL(ProviderEnvironment env) {
		environment = env;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IQueryDSL#getMatchQueryString(java.lang.String, java.lang.String)
	 */
	public SearchRequest getMatchQueryString(String key, String value, String[] indices) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(key, value));
		return new SearchRequest(indices, searchSourceBuilder);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IQueryDSL#getMatchQueryString(java.lang.String, java.lang.String, int, int)
	 */
	public SearchRequest getMatchQueryString(String key, String value, int start, int count, String[] indices) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(key, value));
		searchSourceBuilder.from(start);
		if (count > -1)
			searchSourceBuilder.size(count);
		return new SearchRequest(indices, searchSourceBuilder);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IQueryDSL#getTextQueryString(java.lang.String, int, int, java.lang.String[])
	 */
	public SearchRequest getTextQueryString(String textQuery, int start, int count, String[] indices, String field) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchPhraseQuery(field, textQuery));
		searchSourceBuilder.from(start);
		if (count > -1)
			searchSourceBuilder.size(count);
		System.out.println("QueryDSL.getTextQueryString "+searchSourceBuilder.toString());
		return new SearchRequest(indices, searchSourceBuilder);
	}

	public SearchRequest getTextQueryString(String textQuery, int start, int count, String[] indices, String[] fields) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		searchSourceBuilder.query(QueryBuilders.multiMatchQuery(textQuery, fields));
		searchSourceBuilder.from(start);
		if (count > -1)
			searchSourceBuilder.size(count);
		System.out.println("QueryDSL.getTextQueryString "+searchSourceBuilder.toString());
		return new SearchRequest(indices, searchSourceBuilder);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IQueryDSL#getFuzzyQueryString(java.lang.String, int, int, java.lang.String[])
	 * /
	public String getFuzzyQueryString(String textQuery, int start, int count, List<String> fields) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

}
