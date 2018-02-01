/**
 * 
 */
package org.topicquests.es;

import org.topicquests.support.RootEnvironment;

/**
 * @author jackpark
 *
 */
public class ProviderEnvironment extends RootEnvironment {

	/**
	 * 
	 */
	public ProviderEnvironment() {
		super("provider-config.xml", "logger.properties");
		// TODO Auto-generated constructor stub
	}

	
	public void shutDown() {
		
	}
}
