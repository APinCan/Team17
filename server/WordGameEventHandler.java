package server;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class WordGameEventHandler implements CMAppEventHandler {
	private CMServerStub m_serverStub;
	
	public WordGameEventHandler(CMServerStub serverStub) {
		m_serverStub = serverStub;
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
			
			default:
				return;
		}
	}
	
	private void processSessionEvent(CMEvent cme) {
		CMSessionEvent se = (CMSessionEvent) cme;
		CMConfigurationInfo confInfo = m_serverStub.getCMInfo().getConfigurationInfo();
		
		switch(se.getID()) {
			case CMSessionEvent.LOGIN:
				System.out.println("["+se.getUserName()+"] request login.");
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
		System.out.println("session("+due.getHandlerSession()+") group("+due.getHandlerGroup()+")");
		System.out.println("dummy msg: "+due.getDummyInfo());
		
		return ;
	}
}
