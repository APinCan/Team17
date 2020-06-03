package server;

import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class WordGameServer {
	private CMServerStub m_serverStub;
	private WordGameEventHandler m_eventHandler;
	private WordDB wordDB;
	private WordConstraintsChecker checker;
	
	public WordGameServer() {
		m_serverStub = new CMServerStub();
		wordDB = new WordDB();
//		checker = new WordConstraintsChecker(wordDB);
//		m_eventHandler = new WordGameEventHandler(m_serverStub, checker);
		m_eventHandler = new WordGameEventHandler(m_serverStub);
	}
	
	public CMServerStub getServerStub() {
		return m_serverStub;
	}
	
	public WordGameEventHandler getServerEvenetHandler() {
		return m_eventHandler;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WordGameServer server = new WordGameServer();
		CMServerStub cmStub = server.getServerStub();
		cmStub.setAppEventHandler(server.getServerEvenetHandler());
		cmStub.startCM();
	
	}
	
}
