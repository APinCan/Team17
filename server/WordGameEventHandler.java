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
//	private WordConstraintsChecker checker;
	private boolean[] gameStartFlags = new boolean[10];
	private WordConstraintsChecker[] checker = new WordConstraintsChecker[10];
	private TimerThread[] timerThread = new TimerThread[10];
	private boolean[] timerFlags = new boolean[10];
//	private String[] beforeWord = new String[10];
//	private int[] constraintsWordLength = new int[10];
//	private ArrayList<String> wordHistory = new ArrayList<String>();\
//	private WordDB wordDB;
	
	public WordGameEventHandler(CMServerStub serverStub) {
		m_serverStub = serverStub;
		Arrays.fill(gameStartFlags, false);
		Arrays.fill(timerFlags, false);
//		Arrays.fill(checker, new WordConstraintsChecker(new WordDB));
		for(int i=1; i<10; i++) {
			this.checker[i]= new WordConstraintsChecker(new WordDB());
		}
	}
	
	public WordGameEventHandler(CMServerStub serverStub, WordConstraintsChecker checker) {
		this(serverStub);
//		this.checker = checker;
//		for(int i=1; i<10; i++) {
//			this.checker[i]= new WordConstraintsChecker();
//		}
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
//			if client request requestSessionInfo()
//			case CMSessionEvent.RESPONSE_SESSION_INFO:
//				
				
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
		CMDummyEvent otherCDue = new CMDummyEvent();
		String dummySendMessage = null;
		String purpose=null;
		String sender=null;
		String message=null;
		
//		who send message(session, group)
		System.out.println("PR, session("+due.getHandlerSession()+") group("+due.getHandlerGroup()+")");
		System.out.println("PR, dummy msg: "+due.getDummyInfo());
		
		
//		String[0] = type, String[1] = sender, String[2] = message
		String[] getMessage = due.getDummyInfo().split("#");
		
//		in gameroom
		if(getMessage[0].equals("game")) {
			CMInteractionInfo interInfo = m_serverStub.getCMInfo().getInteractionInfo();
			CMSession session = interInfo.findSession("session1");
			Iterator<CMGroup> iter = session.getGroupList().iterator();
			boolean gameCanStart=false;
			int groupIdx=0;
			
			while(iter.hasNext()) {
				CMGroup gInfo = iter.next();
				
				if(gInfo.getGroupName().equals(due.getHandlerGroup())) {
//					if(gInfo.getGroupUsers().getMemberNum()==2 && gameStartFlags[groupIdx]==false) {
//						gameCanStart=true;
					if(gInfo.getGroupUsers().getMemberNum()==2) {
//						gameStartFlags[groupIdx]=true;
						break;
					}
				}
				
				groupIdx++;
			}
			
//			if get game start message
			if(getMessage[2].equals("gamestart")) {
//				if(gameCanStart) {
				if(gameStartFlags[groupIdx]==false) {
					CMDummyEvent firstWordDue = new CMDummyEvent();
					String firstString;//					
//					server send startgame message to group
//					
					dummySendMessage = "game#server#startgame";
					
					sendDue.setHandlerGroup("SERVER");
					sendDue.setHandlerSession("SERVER");
					sendDue.setDummyInfo(dummySendMessage);
//					sendToClient(sendDue, due);
					m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
//					
//					server send first word message to group
//					
					dummySendMessage = "game#server#firstWord#";
					firstString = checker[groupIdx].getFirstString();
//					server save firstWord and length constraints
//					beforeWord[groupIdx] = firstString;
//					constraintsWordLength[groupIdx] = firstString.length();
					
					dummySendMessage = dummySendMessage+firstString;
					firstWordDue.setHandlerGroup("SERVER");
					firstWordDue.setHandlerSession("SERVER");
					firstWordDue.setDummyInfo(dummySendMessage);
//					sendToClient(firstWordDue, due);
					m_serverStub.cast(firstWordDue, due.getHandlerSession(), due.getHandlerGroup());
					
					gameStartFlags[groupIdx]=true;
					System.out.println("PR, start game");	
				}
				else {
//
//					server send cannot start game meesage only to sender
//
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
//				boolean isValid=false;
				boolean isValid=true;
				isValid = checker[groupIdx].checkConstraints(getMessage[3]);
				System.out.println("PR, "+getMessage[3]+" is "+isValid);
				sendDue.setHandlerGroup("SERVER");
				sendDue.setHandlerSession("SERVER");
				
				
//				System.out.println("PR, timerTrhead alive "+timerThread[groupIdx].isAlive());
				
				if(isValid) {
//					client message sent before is valid

//					if true = timer is off then timer on
//					if(!timerThread[groupIdx].isAlive()) {
					if(!timerFlags[groupIdx]) {
						System.out.println("PR, thread not alived");
						timerThread[groupIdx] = new TimerThread(groupIdx);
						
						dummySendMessage="game#serever#gameword"+getMessage[3];
						sendDue.setDummyInfo(dummySendMessage);
						m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
						
						timerThread[groupIdx].start();
						timerFlags[groupIdx] = true;
//						System.out.println("PR, timer is alive "+timerThread[groupIdx].isAlive());
					}
//					if interrupt false = timer is on the timer off, rerun
					else {
						System.out.println("PR, server receive word");
						timerThread[groupIdx].interrupt();
						
						dummySendMessage="game#serever#gameword"+getMessage[3];
						sendDue.setDummyInfo(dummySendMessage);
						m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());

						timerThread[groupIdx] = new TimerThread(groupIdx);
						timerThread[groupIdx].start();
					}
//					for send to other group member
//					dummySendMessage="game#serever#gameword"+getMessage[3];
//					sendDue.setDummyInfo(dummySendMessage);
//					m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
					
					dummySendMessage="game#server#validmessage";

				}
				else {
//					client message sent before is non-valid
					dummySendMessage="game#server#nonvalidmessage";
					
//					if game word is not valid send interrupt
					System.out.println("PR, word is not correct");
					timerThread[groupIdx].interrupt();
					timerFlags[groupIdx] = false;
				}
				
//				sendToClient(sendDue, due);
//				message is valid or nonvalid send to group members
				sendDue.setDummyInfo(dummySendMessage);
				m_serverStub.cast(sendDue, due.getHandlerSession(), due.getHandlerGroup());
				
				System.out.println("PR, send word to client "+dummySendMessage);
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

	
	public class TimerThread extends Thread{
		private int groupIdx;
		
		TimerThread(){
		}
		
		TimerThread(int groupIdx){
			this.groupIdx = groupIdx;
		}

		public void run() {
			// TODO Auto-generated method stub
			try {
				System.out.println("PR, thread start idx "+groupIdx);
				Thread.sleep(5000);
				System.out.println("PR, timer expired");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("PR, thread interrupted "+this.groupIdx);
//				e.printStackTrace();
			}
		}
		
	}
}
