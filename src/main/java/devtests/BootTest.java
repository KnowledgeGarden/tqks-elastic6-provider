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
		System.out.println("Foo"+environment.getProperties());
		environment.shutDown();
		System.exit(0);
	}

}
