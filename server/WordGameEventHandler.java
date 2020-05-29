package server;

import java.util.Arrays;
import java.util.Iterator;

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
	
	public WordGameEventHandler(CMServerStub serverStub) {
		m_serverStub = serverStub;
		Arrays.fill(gameStartFlags, false);
//		Arrays.fill(checker, new WordConstraintsChecker(new WordDB));
	}
	
	public WordGameEventHandler(CMServerStub serverStub, WordConstraintsChecker checker) {
		this(serverStub);
//		this.checker = checker;
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
			int i=0;
			
			while(iter.hasNext()) {
				CMGroup gInfo = iter.next();
				
				if(gInfo.getGroupName()==due.getHandlerGroup()) {
					if(gInfo.getGroupUsers().getMemberNum()==2 && gameStartFlags[i]==false) {
						gameCanStart=true;
						
						break;
					}
				}
				
				i++;
			}
			
//			if get game start message
			if(getMessage[2].equals("gamestart")) {
				if(gameCanStart) {
					CMDummyEvent firstWordDue = new CMDummyEvent();
					gameStartFlags[i]=true;
					
					dummySendMessage = "game#server#startgame";
					
					sendDue.setHandlerGroup("SERVER");
					sendDue.setHandlerSession("SERVER");
					sendDue.setDummyInfo(dummySendMessage);
					sendToClient(sendDue, due);
					
//					server send first word
					dummySendMessage = "game#server#firstWord#";
//					dummySendMessage = dummySendMessage+FirstString;
					firstWordDue.setHandlerGroup("SERVER");
					firstWordDue.setHandlerSession("SERVER");
					firstWordDue.setDummyInfo(dummySendMessage);
					sendToClient(firstWordDue, due);
					
					System.out.println("PR, start game");	
				}
				else {
					System.out.println("PR, can not start game");
				}
				
				
			}
//			normal game message, server must check constraints
			else if(getMessage[2].equals("gameword")) {
				boolean isValid=false;
//				isValid = constraints checker(getMessage[3]);
				
				if(isValid) {
//					client message sent before is valid
					dummySendMessage="game#server#validmessage";
				}
				else {
//					client message sent before is non-valid
					dummySendMessage="game#server#nonvalidmessage";
				}
				
				sendDue.setHandlerGroup("SERVER");
				sendDue.setHandlerSession("SERVER");
				sendDue.setDummyInfo(dummySendMessage);
				sendToClient(sendDue, due);
				
				System.out.println("PR, send to client "+dummySendMessage);
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
	
//	will be used in USER_TALK
	private void wordCheck(String word) {
		boolean ret=false;

//		 success
		if(ret) {
			
		}
//		wrong word
		else {
			
		}
	}
	

//	only call by gamestart message
	public class GameThread extends Thread{
		private WordConstraintsChecker checker;
		private String firstString;
		private boolean isStop=false;
		private int turn=0; // turn=0 first player, turn=1 second player

		public GameThread(WordConstraintsChecker checker) {
			this.checker = checker;
//			firstString = this.checker.getFirstString();
		}
		
		public void setStop(boolean isStop) {
			this.isStop = isStop;
		}
		
		public void run() {
			try {
//				wait user input
				while(!isStop) {
					Thread.sleep(5000);
					turn %= 2;
				}
				System.out.println("game end");
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class TimerThread implements Runnable{

		TimerThread(){
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
