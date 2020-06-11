package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.ac.konkuk.ccslab.cm.event.CMDataEvent;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMInterestEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.CMUserEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class ClientEventHandler implements CMAppEventHandler
{
	private WordGameClient m_client;
	private CMClientStub m_clientStub;
	
	private int m_nCurNumFilesPerSession;

	public ClientEventHandler(CMClientStub m_clientStub, WordGameClient WordGameClient) 
	{
		// TODO Auto-generated constructor stub
		m_client = WordGameClient;
		this.m_clientStub = m_clientStub;
	}

	@Override
	public void processEvent(CMEvent cme) {
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
			break;
			
		case CMInfo.CM_DUMMY_EVENT:
			processDummyEvent(cme);
			break;
			
		default:
			return;
		}
	}
	
	private void processSessionEvent(CMEvent cme)
	{
		CMSessionEvent se = (CMSessionEvent) cme;
		
		switch(se.getID())
		{
			
			case CMSessionEvent.SESSION_REMOVE_USER:
				LprintMessage("["+se.getUserName()+"] Log Out\n");
				break;
			
				//session message
			case CMSessionEvent.SESSION_TALK:
				LprintMessage("<"+se.getUserName()+"> : "+se.getTalk()+"\n");
				break;
			
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
				if(ie.getHandlerGroup() != "g1")
				{
					RprintMessage("<"+ie.getUserName()+"> "+ie.getTalk()+"\n");
				}
				else
				{
					LprintMessage("<"+ie.getUserName()+"> "+ie.getTalk()+"\n");
				}
				
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
				RprintMessage("["+de.getUserName()+"] enters the game.\n");
				break;
				
				//exit the game room.
			case CMDataEvent.REMOVE_USER:
				RprintMessage("["+de.getUserName()+"] leaves the game.\n");
				break;
				
			default:
				
				return;
		}
	}
	
	
	private void processDummyEvent(CMEvent cme)
	{
		CMDummyEvent due = (CMDummyEvent) cme;
		//서버이벤트핸들러에서  서버내 끝말잇기 메소드 호출
		return;
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

	
	private void LprintMessage(String strText)
	{
		m_client.LprintMessage(strText);
		
		return;
	}
	
	private void RprintMessage(String strText)
	{
		m_client.RprintMessage(strText);
		
		return;
	}
	
	

}