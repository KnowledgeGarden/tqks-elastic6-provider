/**
 * 
 */
package org.topicquests.es;

import org.topicquests.es.api.IClient;
import org.topicquests.es.api.IQueryDSL;
import org.topicquests.es.util.TextQueryUtil;
import org.topicquests.support.RootEnvironment;

/**
 * @author jackpark
 *
 */
public class ProviderEnvironment extends RootEnvironment {
	private IClient provider;
	private IQueryDSL dsl;
	private TextQueryUtil textQueryUtil;
	/**
	 * 
	 */
	public ProviderEnvironment() {
		super("provider-config.xml", "logger.properties");
		provider = new ProviderClient(this);
		dsl = new QueryDSL(this);
		textQueryUtil = new TextQueryUtil(this);
	}

	public IClient getProvider() {
		return provider;
	}
	
	public IQueryDSL getQueryDSL() {
		return dsl;
	}
	
	public TextQueryUtil getTextQueryUtil() {
		return textQueryUtil;
	}
	public void shutDown() {
		provider.shutDown();
	}
}
