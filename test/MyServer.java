package test;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class MyServer {
	private CMServerStub m_serverStub;
	private MyServerEventHandler m_eventHandler;

	public MyServer()
	{
		// 서버스텁 객체를 새로 만든다
		// cm 서버객체 이용하고 싶으면 m_serverStub으로 접근 가능
		m_serverStub = new CMServerStub();
		m_eventHandler = new MyServerEventHandler(m_serverStub);
	}
	
	public CMServerStub getServerStub()
	{
		return m_serverStub;
	}
	
	public MyServerEventHandler getServerEventHandler()
	{
		return m_eventHandler;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyServer server = new MyServer();
		
		CMServerStub cmStub = server.getServerStub();
		
		cmStub.setAppEventHandler(server.getServerEventHandler());

		cmStub.startCM();
	}

}
