package org.christus.interfaces.common;

import java.awt.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyLoggedException;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.ConfigurableService;

public class BrokerFacade {
	private static final String USERDEFINED = "UserDefined";
	private static final String CMP_LOGGED_EXCEPTION = "BrokerFacede failed to connect to broker proxy";
	private static final String CMP_PROP_NOT_INITIALIZED_EXCEPTION = "BrokerFacade failed to access configurable service";
	private static final String PROPERTY_NOT_DEFINED = "BrokerFacade failed to get configurable property: ";
	private static final String INTERRUPTED = "BrokerFacade got interrupted";

	private BrokerProxy b;
	private static final BrokerFacade INSTANCE = new BrokerFacade();

	private static void initProxy() throws ConfigManagerProxyLoggedException,
			InterruptedException {
		{
			synchronized (INSTANCE) {
				if (null == INSTANCE.b) {
					INSTANCE.b = BrokerProxy.getLocalInstance();
					while (!INSTANCE.b.hasBeenPopulatedByBroker()) {
						Thread.sleep(100);
					}
				}
			}
		}
	}
	
	public static ArrayList<String> getUserDefinedConfigurableServicePropertyArray(String configurableService, String propPattern)
	{
		ArrayList<String> arrProps = new ArrayList<String>();
		int propIndex = 0;
		String propName = getUserDefinedConfigurableServicePropertyName(configurableService, propIndex);

		Pattern r = Pattern.compile(propPattern);
		
		while(propName != "")
		{
			Matcher m = r.matcher(propName);
			if(m.find())
			{
				arrProps.add(propName);
			}
			propIndex++;
			propName = getUserDefinedConfigurableServicePropertyName(configurableService, propIndex);
		}
		
		return arrProps;
	}
	
	public static String getUserDefinedConfigurableServicePropertyName(String configurableService, int propIndex)
	{
		String PropertyName = "";
		
		try
		{
			if (null == INSTANCE.b) 
			{
				initProxy();
			}
			ConfigurableService service = INSTANCE.b.getConfigurableService(
					USERDEFINED, configurableService);
			Properties props = service.getProperties();
			Enumeration enums = props.keys();
			while(enums.hasMoreElements() && propIndex >= 0)
			{
				String pn = (String)enums.nextElement();
				if(propIndex == 0)
				{
					PropertyName = pn;
				}
				propIndex--;
			}
		}
		catch (ConfigManagerProxyLoggedException e) {
			// TODO: handle exception
		}
		catch (ConfigManagerProxyPropertyNotInitializedException e) {
			// TODO: handle exception
		}
		catch (InterruptedException e) {
			// TODO: handle exception
		}
		
		return PropertyName;
	}

	public static String getUserDefinedConfigurableServiceProperty(
			String configurableService, String property) {
		try {
			if (null == INSTANCE.b) {
				initProxy();
			}

			ConfigurableService service = INSTANCE.b.getConfigurableService(
					USERDEFINED, configurableService);
			Properties props = service.getProperties();
			String retval = (String) props.get(property);
			if (null == retval) {
				throw new RuntimeException(PROPERTY_NOT_DEFINED + property);
			}
			return retval;
		} catch (ConfigManagerProxyLoggedException ex) {
			throw new RuntimeException(CMP_LOGGED_EXCEPTION, ex);
		} catch (ConfigManagerProxyPropertyNotInitializedException ex) {
			throw new RuntimeException(CMP_PROP_NOT_INITIALIZED_EXCEPTION, ex);
		} catch (InterruptedException ex) {
			throw new RuntimeException(INTERRUPTED, ex);
		}
	}

}
