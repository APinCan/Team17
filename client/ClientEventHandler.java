package client;

import kr.ac.konkuk.ccslab.cm.event.CMDataEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMInterestEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.CMUserEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class ClientEventHandler implements CMAppEventHandler
{
	
	private CMClientStub m_clientStub;
	
	public ClientEventHandler(CMClientStub clientStub)
	{
		m_clientStub = clientStub;
	}

	@Override
	public void processEvent(CMEvent cme) 
	{
		// TODO Auto-generated method stub
		switch(cme.getType())
		{
		case CMInfo.CM_SESSION_EVENT:
			processSessionEvent(cme);
			break;
		case CMInfo.CM_INTEREST_EVENT:
			processInterestEvent(cme);
			break;
		case CMInfo.CM_DATA_EVENT:
			processDataEvent(cme);
		default:
			return;
		}
	}
	
	private void processSessionEvent(CMEvent cme)
	{
		CMSessionEvent se = (CMSessionEvent) cme;
		
		switch(se.getID())
		{
			//login results
			case CMSessionEvent.LOGIN_ACK:
				if(se.isValidUser() == 0)
				{
					System.err.println("Login fail");
				}
				else if(se.isValidUser() == -1)
				{
					System.err.println("Already login user");
				}
				else
				{
					System.out.println("Login success");
				}
				break;
			
			case CMSessionEvent.SESSION_REMOVE_USER:
				System.err.println("["+se.getUserName()+"] Logout");
				break;
			
				//session message
			case CMSessionEvent.SESSION_TALK:
				System.out.println("<"+se.getUserName()+"> : "+se.getTalk());
			
			default:
				
				return;
			
		}
	}
	
	private void processInterestEvent(CMEvent cme)
	{
		CMInterestEvent ie = (CMInterestEvent) cme;
		
		//group message
		switch(ie.getID())
		{
			case CMInterestEvent.USER_TALK:
				System.out.println("<"+ie.getUserName()+">:"+ie.getTalk());
				break;
			
				default:
					
					return;
		}
	}
	
	private void processDataEvent(CMEvent cme)
	{
		CMDataEvent de = (CMDataEvent) cme;
		switch(de.getID())
		{
			//enter the game room.
			case CMDataEvent.NEW_USER:
				System.out.println("["+de.getUserName()+"] enter");
				break;
				
				//exit the game room.
			case CMDataEvent.REMOVE_USER:
				System.out.println("["+de.getUserName()+"] exit");
				break;
				
			default:
				
				return;
		}
	}
	
	//p69
	private void processUserEvent(CMEvent cme)
	{
		CMUserEvent ue = (CMUserEvent) cme;
		switch(ue.getID())
		{
			//blank
		}
	}

}
