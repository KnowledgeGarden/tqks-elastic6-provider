/**
 * 
 */
package devtests;

import org.topicquests.es.ProviderEnvironment;

/**
 * @author jackpark
 *
 */
public class BootTest {
	private ProviderEnvironment environment;
	/**
	 * 
	 */
	public BootTest() {
		environment = new ProviderEnvironment();
		System.out.println("Foo"+environment.getProvider());
		environment.shutDown();
		System.exit(0);
	}
//Foo{NumDuplicates=0, NumShards=1, Clusters=[[localhost, 9200]], IndexNames=[[topics, mappings.json]], Model=org.topicquests.persist.json.es.ElasticSearchClusterModel}
}
