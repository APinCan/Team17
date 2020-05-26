package server;

import java.util.Arrays;
import java.util.Iterator;

import kr.ac.konkuk.ccslab.cm.entity.CMGroup;
import kr.ac.konkuk.ccslab.cm.entity.CMSession;
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
	private WordConstraintsChecker checker;
	private boolean[] gameStartFlags = new boolean[10];
	private String[] gameFirstString = new String[10];
	
	public WordGameEventHandler(CMServerStub serverStub) {
		m_serverStub = serverStub;
		Arrays.fill(gameStartFlags, false);
	}
	
	public WordGameEventHandler(CMServerStub serverStub, WordConstraintsChecker checker) {
		this(serverStub);
		this.checker = checker;
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
			default:
				return;
		}
	}

	private void processDummyEvent(CMEvent cme) {
		CMDummyEvent due = (CMDummyEvent) cme;
		String message=null;
		
//		who send message(session, group)
		System.out.println("PR, session("+due.getHandlerSession()+") group("+due.getHandlerGroup()+")");
		System.out.println("PR, dummy msg: "+due.getDummyInfo());
		
		
//	
//		need test		
//		
//		if get game start message
//		split message, and get
		if(message.equals("gamestart")) {
			CMInteractionInfo interInfo = m_serverStub.getCMInfo().getInteractionInfo();
			int i=0;
//			we use only one session, session1
			CMSession session = interInfo.findSession("session1");
			Iterator<CMGroup> iter = session.getGroupList().iterator();
			
			while(iter.hasNext()) {
				CMGroup gInfo = iter.next();
				
//				find the group, equal to message sender
				if(gInfo.getGroupName()==due.getHandlerGroup()) {
//					server can start the game
					if(gInfo.getGroupUsers().getMemberNum()==2 && gameStartFlags[i]==false) {
//						start game
						System.out.println("PR, start game");
						gameStartFlags[i]=true;
					}
					else {
						System.out.println("can not start game");
					}
					
					break;
				}
				
				i++;
			}
		
		}

		return;
	}
	
	private void processInterestEvent(CMEvent cme) {
		CMInterestEvent ie = (CMInterestEvent) cme;
		
		switch(ie.getID()) {
//		call when client change group / client call CMSessionEvent.JOIN_SESSION_ACK
			case CMInterestEvent.USER_ENTER:
				System.out.println("PR, ["+ie.getUserName()+"] enters group("+ie.getCurrentGroup()+") in session("
				+ie.getHandlerSession()+")");
				
//		call when client leave current group
			case CMInterestEvent.USER_LEAVE:
				System.out.println("PR, ["+ie.getUserName()+"] leaves group("+ie.getCurrentGroup()+") in session("
						+ie.getHandlerSession()+")");
				
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
		int ret;
		
		ret = this.checker.checkConstraints(word);
//		 success
		if(ret==1) {
			
		}
//		wrong word
		else {
			
		}
	}
	

//	only call by gamestart message
	public class GameThread extends Thread{
		private WordConstraintsChecker checker;

		public GameThread(WordConstraintsChecker checker) {
			this.checker = checker;
		}
		
		public void run() {
			
		}
	}
}
