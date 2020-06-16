package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import kr.ac.konkuk.ccslab.cm.entity.CMGroup;
import kr.ac.konkuk.ccslab.cm.entity.CMSession;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMInterestEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class WordGameEventHandler implements CMAppEventHandler {
	private CMServerStub m_serverStub;
	private boolean[] gameStartFlags = new boolean[10];
	private WordConstraintsChecker[] checker = new WordConstraintsChecker[10];
	private TimerThread[] timerThread = new TimerThread[10];
	private boolean[] timerFlags = new boolean[10];
	private int[] groupMembers = new int[10];
	
	public WordGameEventHandler(CMServerStub serverStub) {
		m_serverStub = serverStub;
		Arrays.fill(gameStartFlags, false);
		Arrays.fill(timerFlags, false);
		Arrays.fill(groupMembers, 0);
		for(int i=1; i<10; i++) {
			this.checker[i]= new WordConstraintsChecker(new WordDB());
		}
	}
	
	@Override
	public void processEvent(CMEvent cme) {
		// TODO Auto-generated method stub
		switch(cme.getType()) {
			case CMInfo.CM_SESSION_EVENT:
				processSessionEvent(cme);
				break;
				
			case CMInfo.CM_DUMMY_EVENT:
				processDummyEvent(cme);
				break;
				
			case CMInfo.CM_INTEREST_EVENT:
				processInterestEvent(cme);
				break;
			
			default:
				return;
		}
	}
	
	public void sendToClient(CMDummyEvent sendToClient, CMDummyEvent receiveFromServer) {
		m_serverStub.send(sendToClient, receiveFromServer.getSender());
		
//		ex.
//		send(game#server#gamestart, [dst : which client send to gamestart message to server])
	}
	
	private void processSessionEvent(CMEvent cme) {
		CMSessionEvent se = (CMSessionEvent) cme;
		CMConfigurationInfo confInfo = m_serverStub.getCMInfo().getConfigurationInfo();
		
		switch(se.getID()) {
//			call when user login to server
			case CMSessionEvent.LOGIN:
				System.out.println("PR, ["+se.getUserName()+"] request login.");
//				if LGIN_SCHEME==1 using authentication
				if(confInfo.isLoginScheme()) {
//					login process
//					if login fail == m_serverStub.replyEvent(se, 0);
//					if login success == m_serverStub.replyEvent(se, 1);
				}
				
				break;					
//			if clinet log out to server
			case CMSessionEvent.LOGOUT:
				System.out.println("PR, "+se.getUserName()+" log out");
				break;
				
			default:
				return;
		}
	}

	private void processDummyEvent(CMEvent cme) {
		CMDummyEvent due = (CMDummyEvent) cme;
		CMDummyEvent sendDue = new CMDummyEvent();
//		CMDummyEvent otherCDue = new CMDummyEvent();
		String dummySendMessage = null;
		
		System.out.println("PR, session("+due.getHandlerSession()+") group("+due.getHandlerGroup()+")");
		System.out.println("PR, dummy msg: "+due.getDummyInfo());
		String[] getMessage = due.getDummyInfo().split("#");
		
//		in gameroom
		if(getMessage[0].equals("game")) {
			CMInteractionInfo interInfo = m_serverStub.getCMInfo().getInteractionInfo();
			CMSession session = interInfo.findSession("session1");
			Iterator<CMGroup> iter = session.getGroupList().iterator();
			int groupIdx=0;
			
			while(iter.hasNext()) {
				CMGroup gInfo = iter.next();
				
				if(gInfo.getGroupName().equals(due.getHandlerGroup())) {
					if(gInfo.getGroupUsers().getMemberNum()==2) {
						groupMembers[groupIdx]=2;
						break;
					}
					else if(gInfo.getGroupUsers().getMemberNum()==1) {
						groupMembers[groupIdx]=1;
						break;
					}
				}
				
				groupIdx++;
			}

			if(getMessage[2].equals("room")) {
				if(groupMembers[groupIdx] < 2 && gameStartFlags[groupIdx]==false) {			
					dummySendMessage = "game#server#room#true";
				}
				else {
					dummySendMessage = "game#server#room#false";
				}
				
				sendDue.setID(222);
				sendDue.setHandlerGroup("SERVER");
				sendDue.setHandlerSession("SERVER");
				sendDue.setDummyInfo(dummySendMessage);
				m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
				System.out.println("PR, "+dummySendMessage+" is send");
	
			}
//			if get game start message
			else if(getMessage[2].equals("gamestart")) {
//				if not start game yet
				if(gameStartFlags[groupIdx]==false) {
					CMDummyEvent firstWordDue = new CMDummyEvent();
					String firstWord;					
					dummySendMessage = "game#server#startgame";
					
					sendDue.setHandlerGroup("SERVER");
					sendDue.setHandlerSession("SERVER");
					sendDue.setDummyInfo(dummySendMessage);
//					sendToClient(sendDue, due);
//					send game start message to group
					m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());

					dummySendMessage = "game#server#firstWord#";
					firstWord = checker[groupIdx].getString();
				
					dummySendMessage = dummySendMessage+firstWord;
					firstWordDue.setHandlerGroup("SERVER");
					firstWordDue.setHandlerSession("SERVER");
					firstWordDue.setDummyInfo(dummySendMessage);
//					sendToClient(firstWordDue, due);
//					send first word to group
					m_serverStub.cast(firstWordDue, due.getHandlerSession(), due.getHandlerGroup());
					
					gameStartFlags[groupIdx]=true;
					System.out.println("PR, start game");	
				}
//				game is already start
				else {
					dummySendMessage = "game#server#notstartgame";
					sendDue.setHandlerGroup("SERVER");
					sendDue.setHandlerSession("SERVER");
					sendDue.setDummyInfo(dummySendMessage);
					m_serverStub.send(sendDue, due.getSender());
					
					System.out.println("PR, can not start game");
				}
				
				
			}
//			normal game message, server must check constraints
			else if(getMessage[2].equals("gameword")) {
				boolean isValid=false;
				isValid = checker[groupIdx].checkConstraints(getMessage[3]);
				System.out.println("PR, "+getMessage[3]+" is "+isValid);
				sendDue.setHandlerGroup("SERVER");
				sendDue.setHandlerSession("SERVER");
				
//				player with another client
				if(groupMembers[groupIdx]==2) {
//					if receive word pass constraint
					if(isValid) {
//						if timer is not activate
						if(!timerFlags[groupIdx]) {
							System.out.println("PR, timer is not alived");
							
							timerThread[groupIdx] = new TimerThread(groupIdx, m_serverStub, due.getHandlerGroup());
							dummySendMessage="game#serever#gameword"+getMessage[3];
							sendDue.setDummyInfo(dummySendMessage);
//							if game word pass constraints then re-send to group
							m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
							
//							resend word and activate timer
							timerThread[groupIdx].start();
							timerFlags[groupIdx] = true;
						}
//						if timer is activate, re-activate timer
						else {
							System.out.println("PR, timer is alived");
							timerThread[groupIdx].interrupt();
							
							dummySendMessage="game#serever#gameword"+getMessage[3];
							sendDue.setDummyInfo(dummySendMessage);
//							if game word pass constraints then re-send to group
							m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());

							timerThread[groupIdx] = new TimerThread(groupIdx, m_serverStub, due.getHandlerGroup());
							timerThread[groupIdx].start();
						}

						dummySendMessage="game#server#validmessage";

					}
//					receive word is non valid word, timer interrupt and timer false
					else {
						dummySendMessage="game#server#nonvalidmessage";
						
						System.out.println("PR, word is not correct");
						timerThread[groupIdx].interrupt();
						timerFlags[groupIdx] = false;
						
//						game end
						gameStartFlags[groupIdx] = false;
					}
					
//					sendToClient(sendDue, due);
//					valid or non-valid message send to group
					sendDue.setDummyInfo(dummySendMessage);
					m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
					
					System.out.println("PR, send word to client "+dummySendMessage);
				}
//				play with server
				else {
//					if receive word is valid
					if(isValid) {
//						if timer off
						if(!timerFlags[groupIdx]) {
							System.out.println("PR, timer is not alived");
						}
//						if timer on
						else {
							System.out.println("PR, timer is alived");
							timerThread[groupIdx].interrupt();
							
						}
						dummySendMessage="game#server#validmessage";
						sendDue.setDummyInfo(dummySendMessage);
//						send valid message to client
						m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
						
//						server send new word to client
						String serverWord = checker[groupIdx].getNextServerWord();
						dummySendMessage="game#server#gameword#"+serverWord;
						sendDue.setDummyInfo(dummySendMessage);
						
						m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
						timerThread[groupIdx] = new TimerThread(groupIdx, m_serverStub, due.getHandlerGroup());
						timerThread[groupIdx].start();
//						timerFlags[groupIdx] = true;
					}
//					if receive word is non-valid
					else {
						dummySendMessage="game#server#nonvalidmessage";
						sendDue.setDummyInfo(dummySendMessage);
						m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
						
//						if game word is not valid send interrupt
						System.out.println("PR, word is not correct");
						timerThread[groupIdx].interrupt();
						timerFlags[groupIdx] = false;
					}
			
					System.out.println("PR, send word to client "+dummySendMessage);
	
				}
			}
			else if(getMessage[2].equals("constraints")) {
				if(getMessage[3].equals("on")) {
					
				}
				else {
					
				}
			}
		}

		return;
	}
	
	private void processInterestEvent(CMEvent cme) {
		CMInterestEvent ie = (CMInterestEvent) cme;
		
		switch(ie.getID()) {
//		call when client change group / client call CMSessionEvent.JOIN_SESSION_ACK
			case CMInterestEvent.USER_ENTER:
//				if client move to lobby
				if(ie.getCurrentGroup().equals("g1")) {
					System.out.println("PR, ["+ie.getUserName()+"] enters lobby in session("
							+ie.getHandlerSession()+")");
				}
//				client move to other gameroom
				else {
					System.out.println("PR, ["+ie.getUserName()+"] enters group("+ie.getCurrentGroup()+") in session("
							+ie.getHandlerSession()+")");
				}
				break;
				
//		call when client leave current group
			case CMInterestEvent.USER_LEAVE:
				System.out.println("PR, ["+ie.getUserName()+"] leaves group("+ie.getCurrentGroup()+") in session("
						+ie.getHandlerSession()+")");
				break;
				
/* in server, getTalk() method using only here */
//		call when client chat / client call CMClientStub.chat(String, String).
			case CMInterestEvent.USER_TALK:
//				printing who send what talking message 
				System.out.println("PR, group("+ie.getHandlerGroup()+") ["+ie.getUserName()+"]: '"+
						ie.getTalk()+"'");
				break;
				
			default:
				return;
				
		}
	}

//	timer thread for word game
	public class TimerThread extends Thread{
		private int groupIdx;
		private CMServerStub m_serverStub;
		private String groupName;
		private CMDummyEvent event = new CMDummyEvent();
		
		TimerThread(){
		}
		
		TimerThread(int groupIdx, CMServerStub serverStub, String groupName){
			this.groupIdx = groupIdx;
			this.m_serverStub = serverStub;
			this.groupName = groupName;
			
			event.setHandlerGroup("SERVER");
			event.setHandlerSession("SERVER");
		}

		public void run() {
			// TODO Auto-generated method stub
			try {
				System.out.println("PR, thread start idx "+groupIdx);
				Thread.sleep(5000);
				String dummyMessage = "game#server#timerExpire";
				event.setDummyInfo(dummyMessage);
				
				m_serverStub.cast(event, "session1", groupName);
				System.out.println("PR, timer expired");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("PR, thread interrupted "+this.groupIdx);
//				e.printStackTrace();
			}
		}
		
	}
}
