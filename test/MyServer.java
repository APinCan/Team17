package test;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class MyServer {
	private CMServerStub m_serverStub;
	private MyServerEventHandler m_eventHandler;

	public MyServer()
	{
		// �������� ��ü�� ���� �����
		// cm ������ü �̿��ϰ� ������ m_serverStub���� ���� ����
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
