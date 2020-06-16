package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

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
					RprintMessage("<"+ie.getUserName()+"> : "+ie.getTalk()+"\n");
				}
				else
				{
					LprintMessage("<"+ie.getUserName()+"> : "+ie.getTalk()+"\n");
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
		
		String[] getMessage = due.getDummyInfo().split("#");
		
		if(getMessage[2].equals("startgame")) 
		{
			m_client.setFlag(true);
			RprintMessage("Game Start");
			//스타트 버튼 비활성
		}
		
		if(getMessage[2].equals("gameword")) 
		{
			if(getMessage[3].equals("firstWord"))
			{
				RprintMessage("<SERVER> : "+getMessage[4]+"\n");
			}
			
			else
			{
				if(getMessage[3]!="validmessage")
				{
					m_clientStub.chat(due.getHandlerGroup(), getMessage[3]+" is available."+"\n");
				}
				else if(getMessage[3]!="nonvalidmessage")
				{
					m_clientStub.chat(due.getHandlerGroup(), getMessage[3]+" is not valid."+"\n");
					//재입력 요구
				}
			}
		}
		
		if(getMessage[2].equals("notstartgame")) 
		{
			JOptionPane.showMessageDialog(null, "Can not start game!\n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
		}
		
		if(getMessage[2].equals("timeExpire")) 
		{
			m_client.setFlag(false);
			RprintMessage("Game Over!");
			//스타트 버튼 활성
		}	
		
		/*
		if(getMessage[2].equals("room")) 
		{
			if(getMessage[3].equals("true"))
			{
				due.setDummyInfo("true");
			}
			else
			{
				due.setDummyInfo("false");
			}
		
		}
		*/
		
		// 
		// when client press game start button, 
		// game#server#gamestart
		// game#server#gameword#firstWord#[word]
		
		// when client can not start game
		// game#server#notstartgame
		
		// when client send word to server, then server resend message to group
		// game#server#gameword#[word]
		// game#server#gameword#validmessage
		// if client word is not valid
		// game#server#gameword#[word]
		// game#server#gameword#nonvalidmessage
		
		// when client violate 5senconds rule
		// game#server#timeExpire
		
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