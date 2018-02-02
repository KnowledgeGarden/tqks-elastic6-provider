/**
 * 
 */
package org.topicquests.es;

import java.util.*;

import org.topicquests.es.api.IClient;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import org.topicquests.support.util.ConfigurationHelper;
import org.topicquests.support.util.LRUCache;
import org.topicquests.support.util.TextFileHandler;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 * 
 * 
 */
public class ProviderClient implements IClient {
	private ProviderEnvironment environment;
	private RestHighLevelClient client;
	private LRUCache objectCache;
	private final String _TYPE = "core";
	/**
	 * 
	 */
	public ProviderClient(ProviderEnvironment env) {
		environment = env;
		objectCache = new LRUCache(1024);
		environment.logDebug("ProviderClient-");
		setup();
		System.out.println("ProviderClient "+client);
	}

	//https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-low-usage-initialization.html
	private void setup() {
		//Object obj = environment.getProperty("Clusters");
		//List<List<String>>hx = (List<List<String>>)obj;
		//int len = hx.size();
		
		//HttpHost hosts [] = new HttpHost[len];
		//RestClient rc = null;
		//TODO BigTime -- fix this
		environment.logDebug("ProviderClient.setup-");
		client = new RestHighLevelClient(
		        RestClient.builder(
		                new HttpHost("localhost", 9200, "http")));
		environment.logDebug("ProviderClient.setup-1 "+client);
		createIndex();
		environment.logDebug("ProviderClient.setup+");
	}
	private void createIndex() {
		
		List<List<String>>indexes = (List<List<String>>)environment.getProperties().get("IndexNames");
		int len = indexes.size();
		environment.logDebug("ProviderClient.createIndex- "+indexes);
	//	List<String>indices = new ArrayList<String>();
		String numShards = environment.getStringProperty("NumShards");
		String numReplicas = environment.getStringProperty("NumDuplicates");
		int nS = Integer.parseInt(numShards);
		int nR = Integer.parseInt(numReplicas);
		String mappx;
		int foundCode = 0; //too == found, 404 == notfound
//		JSONObject mappy = null;
		String _INDEX;
		for (int i=0;i<len;i++) {
			_INDEX = indexes.get(i).get(0); // name
			mappx = indexes.get(i).get(1);  // mapping file
			mappx = getMappings(mappx);
			System.out.println("CreatingIndex "+_INDEX);
			
			try {
				Response resp = client.getLowLevelClient().performRequest("GET", "/" + _INDEX, new HashMap<String, String>(), new BasicHeader("Accep", "application/json"));
				System.out.println("CreatingIndex-1 "+resp.getStatusLine());
				foundCode = resp.getStatusLine().getStatusCode();
				if (foundCode == 404) {
					//jr = client.execute(new CreateIndex.Builder(_INDEX).build());
					createMapping(mappx, _INDEX, nS, nR);
					//createSettings(_INDEX);
					//if (jr.getErrorMessage() != null)
					//	environment.logError("JestError "+jr.getErrorMessage(), null);
				}
			} catch (Exception e) {
				environment.logError(e.getMessage(), e);
				e.printStackTrace();			
			}
		}
	}
	//https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.x/java-rest-high-put-mapping.html
	private void createMapping(String mapping, String index, int numShards, int numReplicas) {
		try {
			environment.logDebug("ProviderClient.createMapping- "+index+" "+numShards+" "+" "+numReplicas+" "+mapping);
			//CreateIndexRequest request = new CreateIndexRequest(index);
			//request.settings(Settings.builder() 
			JSONObject jo = new JSONObject();
				    jo.put("index.number_of_shards", numShards);
				    jo.put("index.number_of_replicas", numReplicas);
			
			System.out.println("ProviderClient.createMapping "+jo.toJSONString());
			StringEntity entity = new StringEntity(jo.toJSONString(), ContentType.APPLICATION_JSON);
			client.getLowLevelClient().performRequest("PUT", "/" + index, new HashMap<String, String>(), entity);
			environment.logDebug("ProviderClient.createMapping+");
		} catch (Exception e) {
			System.out.println("Ouch!");
			environment.logError(e.getMessage(), e);
			e.printStackTrace();			
		}
	}
	
	private String getMappings(String fileName) {
		TextFileHandler handler = new TextFileHandler();
		String mappings = handler.readFile(ConfigurationHelper.findPath(fileName));		
		return mappings;
	}
	
	/* (non-Javadoc)
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html
	 * @see org.topicquests.es.api.IClient#put(java.lang.String, java.lang.String, net.minidev.json.JSONObject)
	 */
	public IResult put(String id, String index, JSONObject node) {
		IResult result = new ResultPojo();
		try {
			IndexRequest request = new IndexRequest(index, _TYPE, id);
			request.source(node.toJSONString(), XContentType.JSON);
			IndexResponse indexResponse = client.index(request);
			result.setResultObject(Integer.toString(indexResponse.status().getStatus()));
		} catch (Exception e) {
			e.printStackTrace();
			environment.logError("ProviderClient.put: "+e.getMessage(), e);
			result.addErrorString(e.getMessage());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#updateFullNode(java.lang.String, java.lang.String, net.minidev.json.JSONObject, boolean)
	 */
	public IResult updateFullNode(String id, String index, JSONObject object, boolean checkVersion) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#partialUpdateNode(java.lang.String, java.lang.String, net.minidev.json.JSONObject)
	 */
	public IResult partialUpdateNode(String id, String index, JSONObject object) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#remove(java.lang.String, java.lang.String)
	 */
	public IResult remove(String id, String index) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#exists(java.lang.String, java.lang.String)
	 */
	public IResult exists(String id, String index) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}
		
	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#get(java.lang.String, java.lang.String)
	 */
	public IResult get(String id, String index) {
		IResult result = new ResultPojo();
		try {
			System.out.println("ProviderClient.get- "+id);
			GetRequest greq = new GetRequest(index, _TYPE, id);
			System.out.println("ProviderClient.get-1 "+id);
			GetResponse gres = client.get(greq, new BasicHeader("Accep", "application/json"));
			
			String json = gres.getSourceAsString();
			System.out.println("ProviderClient.get-2 "+json);
			JSONObject jo = toJSONObject(json);
			result.setResultObject(jo);
		
		} catch (Exception e) {
			e.printStackTrace();
			environment.logError("ProviderClient.get: "+e.getMessage(), e);
			result.addErrorString(e.getMessage());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#multiGet(java.util.List, java.lang.String)
	 */
	public IResult multiGet(List<String> locators, String index) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#search(java.lang.String, java.lang.String)
	 */
	public IResult search(SearchRequest query, String index) {
		IResult result = new ResultPojo();
		SearchRequest req;
		//SearchResponse searchResponse = client.
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#listSearch(java.lang.String, java.lang.String)
	 */
	public IResult listSearch(SearchRequest query, String index) {
		IResult result = new ResultPojo();
		try {
			SearchResponse searchResponse = client.search(query);
			SearchHits hits = searchResponse.getHits();
			Iterator<SearchHit> itr = hits.iterator();
			SearchHit hit;
			String json;
			List<JSONObject> vals = new ArrayList<JSONObject>();
			result.setResultObject(vals);
			while (itr.hasNext()) {
				hit = itr.next();
				json = hit.getSourceAsString();
				vals.add(toJSONObject(json));
			}
		} catch (Exception e) {
			e.printStackTrace();
			environment.logError("ProviderClient.listSearch: "+e.getMessage(), e);
			result.addErrorString(e.getMessage());
		}
		return result;
	}

	JSONObject toJSONObject(String json) throws Exception {
		JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		JSONObject jo = (JSONObject)p.parse(json);
		return jo;
	}
	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#multiSearch(java.util.List, java.lang.String)
	 */
	public IResult multiSearch(List<String> query, String index) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#count(java.lang.String, java.lang.String)
	 */
	public IResult count(String query, String index) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#refresh()
	 */
	public IResult refresh() {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.es.api.IClient#clearCache()
	 */
	public void clearCache() {
		// TODO Auto-generated method stub

	}

	public void shutDown() {
		try {
			if (client != null)
				client.close();
		} catch (Exception e) {
			e.printStackTrace();
			environment.logError(e.getMessage(), e);
		}
	}

}
