/**
 * 
 */
package org.topicquests.es;

import org.topicquests.es.api.IClient;
import org.topicquests.es.api.IQueryDSL;
import org.topicquests.support.RootEnvironment;

/**
 * @author jackpark
 *
 */
public class ProviderEnvironment extends RootEnvironment {
	private IClient provider;
	private IQueryDSL dsl;
	/**
	 * 
	 */
	public ProviderEnvironment() {
		super("provider-config.xml", "logger.properties");
		provider = new ProviderClient(this);
		dsl = new QueryDSL(this);
	}

	public IClient getProvider() {
		return provider;
	}
	
	public IQueryDSL getQueryDSL() {
		return dsl;
	}
	
	public void shutDown() {
		provider.shutDown();
	}
}
