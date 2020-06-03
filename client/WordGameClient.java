package client;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class WordGameClient 
{
	
	private CMClientStub m_clientStub;
	private ClientEventHandler m_eventHandler;
	
	public CMClientStub getClientStub()
	{
		return m_clientStub;
	}
	
	public ClientEventHandler getClientEventHandler()
	{
		return m_eventHandler;
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		WordGameClient client = new WordGameClient();
		CMClientStub cmStub = client.getClientStub();
		cmStub.setAppEventHandler(client.getClientEventHandler());
		cmStub.startCM();
	}
	
	//login request. Set the LOGIN_SCHEME field to 1. (Refer to p35 of user guide)
	public void loginGame()
	{
		String username = null;
		String password = null;
		boolean result = false;
		Console console = System.console();
		
		System.out.print("======Request Login");
		
		System.out.print("User ID: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			username = br.readLine();
			if(console == null)
			{
				System.out.print("Password: ");
				password = br.readLine();
			}
			else
			{
				password = new String(console.readPassword("Password: "));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		result = m_clientStub.loginCM(username, password);
		
		if(result)
		{
			System.out.println("Login request success");
		}
		else
		{
			System.err.println("Login request fail");
		}
		
		System.out.println("======");
	}
	
	//Request to show the game room list
	public void gameroomList()
	{
		
	}
	
	//Join or Exit the game room.
	public void tranferGameRoom()
	{
	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		
		try
		{
			str = br.readLine();
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
		
		m_clientStub.changeGroup(str);
		System.out.println("Change group to "+str);
	}
	
	//Request to start the game.
	public void startgame()
	{

	}
	
	//Send the words.
	public void sendToserver()
	{
		//
		// example
		//
		// when client click gamestart
		String gameStartMessage = "game#sender#gamestart";
		// when client enter gameword
		String gameWordSendMessage = "game#sender#gameword#자동차";
		
		
		// 
		// example, receive from server
		//
		// when server send first word
		// game#server#firstWord#[exampleword]
		// 
		// when server check  constraint
		// is valid message = game#server#gameword#validmessage
		// is nonvalid message = game#server#gameword#nonvalidmessage
		
	}
	
	public void tranferLobby()
	{
		m_clientStub.changeGroup("g1");
		System.out.println("Move to lobby");
	}
	
	
	//Communication function.
	public void chat()
	{
		String strTarget = null;
		String strMessage = null;
		System.out.print("Target(/b, /s, /g, or /User ID): ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			strTarget = br.readLine();
			strTarget = strTarget.trim();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.print("Message: ");
		try
		{
			strMessage = br.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		m_clientStub.chat(strTarget, strMessage);
	}
	
	//logout request.
	public void logoutGame()
	{
		boolean result = false;
		
		System.out.println("====== Request logout");
		result = m_clientStub.logoutCM();
		
		if(result)
			System.out.println("Logout request success");
		else
			System.err.println("Logout request fail");
		
		System.out.println("======");
	}
	
	//current client states
		public void userStates()
		{
			CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
			CMUser myself = interInfo.getMyself();
			System.out.println("===== Current user state");
			System.out.println("ID("+myself.getName()+"),place("+myself.getCurrentGroup()+"), current state("+myself.getState()+").");
		}

}


