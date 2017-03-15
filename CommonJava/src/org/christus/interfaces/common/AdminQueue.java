package org.christus.interfaces.common;
import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
//import com.ibm.msg.client.wmq.common.internal.WMQStandardValidators.CaseInsentiveHashmap;
public class AdminQueue {
	/* Queue Get Disable - Input QueueName*/
	public static String queueDisable(String queueMgr, String queueName) 
	{
		String value = "SUCCESS";
		try 
		{						
			MQQueueManager qMgr = new com.ibm.mq.MQQueueManager(queueMgr);
			MQQueue queue = qMgr.accessQueue(queueName, CMQC.MQOO_SET);			
			queue.setInhibitGet(CMQC.MQQA_GET_INHIBITED);			
			queue.close();
			qMgr.close();
			value = value+":"+queueName+" Get Inhibited";		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			value = "FAIL: "+queueName+" Get Inhibited, error: "+e.getMessage();
		}
		return value;
	}

	/* Queue Get Enable -Input QueueName*/
	public static String queueEnable(String queueMgr, String queueName) 
	{
		String value = "SUCCESS";
		try 
		{						
			MQQueueManager qMgr = new com.ibm.mq.MQQueueManager(queueMgr);			
			MQQueue queue = qMgr.accessQueue(queueName, CMQC.MQOO_SET);			
			queue.setInhibitGet( CMQC.MQQA_GET_ALLOWED);			
			queue.close();
			qMgr.close();
			value = value+":"+queueName+" Get Allowed";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			value = "FAIL: "+queueName+" Get Allowed, error: "+e.getMessage();
		}
		return value;
	}

	/* Queue Get Enable -Input QueueName*/
	public static String queueDepth(String queueMgr, String queueName) 
	{
		String depth = "-1";
		try 
		{						
			MQQueueManager qMgr = new com.ibm.mq.MQQueueManager(queueMgr);			
			MQQueue queue = qMgr.accessQueue(queueName, CMQC.MQOO_INQUIRE);			
			depth = Integer.toString(queue.getCurrentDepth());		
			queue.close();
			qMgr.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return depth;
	}
}
