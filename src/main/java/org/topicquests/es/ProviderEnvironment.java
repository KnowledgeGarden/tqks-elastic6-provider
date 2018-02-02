/**
 * 
 */
package org.topicquests.es;

import org.topicquests.es.api.IClient;
import org.topicquests.support.RootEnvironment;

/**
 * @author jackpark
 *
 */
public class ProviderEnvironment extends RootEnvironment {
	private IClient provider;
	/**
	 * 
	 */
	public ProviderEnvironment() {
		super("provider-config.xml", "logger.properties");
		provider = new ProviderClient(this);
	}

	public IClient getProvider() {
		return provider;
	}
	
	public void shutDown() {
		provider.shutDown();
	}
}
