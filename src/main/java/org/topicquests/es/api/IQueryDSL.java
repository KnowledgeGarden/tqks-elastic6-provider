/**
 * 
 */
package org.topicquests.es.api;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;

/*
 * @author jackpark
 *
 */
public interface IQueryDSL {

	SearchRequest getMatchQueryString(String key, String value, String[] indices);
	
	SearchRequest getMatchQueryString(String key, String value, int start, int count, String[] indices);

	SearchRequest getTextQueryString(String textQuery, int start, int count, String[] indices, String ... fields);
	
	//String getFuzzyQueryString(String textQuery, int start, int count, List<String> fields);
}
