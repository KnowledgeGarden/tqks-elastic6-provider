/**
 * 
 */
package org.topicquests.es.api;

import java.util.List;

import org.topicquests.support.api.IResult;
import org.elasticsearch.action.search.SearchRequest;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface IClient {

	/**
	 * Index a node into a specific <code>index</code>
	 * @param id
	 * @param index
	 * @param node
	 * @return returns integer status code
	 */
	IResult put(String id, String index, JSONObject node);
	
	/**
	 * Effectively, this is a <em>remove</em> then <em>put</em>
	 * @param id
	 * @param index
	 * @param object
	 * @param checkVersion
	 * @return
	 */
	IResult updateFullNode(String id, String index, JSONObject object, boolean checkVersion);

	/**
	 * <p>Update an already-indexed node</p>
	 * <p>NOTE: <code>object</code> is <em>not</em> a full document.
	 * Rather, it is a change script.</p>
	 * <p> IF <code>checkVersion</code> is <code>true</code>, <code>object</code>
	 * <em>must include</code> the new version value</p>
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-update.html
	 * @param id
	 * @param index
	 * @param object is a script, not a document
	 * @return
	 */
	IResult partialUpdateNode(String id, String index, JSONObject object);

	/**
	 * Delete the object identified by <code>id</code> from index identified
	 * by <code>index</code>
	 * @param id
	 * @param index
	 * @return
	 */
	IResult remove(String id, String index);
	
	/**
	 * Returns <code>true</code> if object identified by <code>id</code>
	 * exists in index identified by <code>index</code>.
	 * @param id
	 * @param index
	 * @return
	 */
	IResult exists(String id, String index);
	
	/**
	 * Return a {@link JSONObject} identified by <code>id</code> from the
	 * index identified by <code>index</code>
	 * @param id
	 * @param index
	 * @return
	 */
	IResult get(String id, String index);
	
	/**
	 * Return a collection of objects
	 * @param locators
	 * @param index
	 * @return can return an empty result; does not return <code>null</code>
	 */
	IResult multiGet(List<String>locators, String index);
	
	/**
	 * Execute the given <code>query</code>. Returns a <em>singleton</em> response
	 * @param query
	 * @param index
	 * @return can return <code>null</code>
	 */
	IResult search(SearchRequest query, String index);
	
	/**
	 * Execute the given <code>query</code>. Returns a collection as <code>List<JSONObject>
	 * @param query
	 * @param index
	 * @return can return an empty result; does not return <code>null</code>
	 */
	IResult listSearch(SearchRequest  query, String index);
	/**
	 * Perform a search on a list of query strings
	 * @param query
	 * @param index
	 * @return
	 */
	IResult multiSearch(List<String> query, String index);
	
	/**
	 * Returns Double or -1 if error
	 * @param query
	 * @param index
	 * @return
	 */
	IResult count(String query, String index);
	
	IResult refresh();
	
	void clearCache();
	
	void shutDown();
}
