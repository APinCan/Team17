package client;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextPane;


class WordGameClient extends JFrame 
{
	private JButton loginButton;
	private JTextField idtx;
	private JTextField pswtf;
	
	private JTextPane textPane;
	private JTextField textField;
	
	private JTextPane textPane_1;
	private JTextField textField_1;
	
	private CMClientStub m_clientStub;
	private ClientEventHandler m_eventHandler;
	
	int viewT = 0;
	String tg = "/b";
	
	class LogInPanel extends JPanel
	{
		public LogInPanel()
		{
			setLayout(null);

			loginButton = new JButton("Login");
			loginButton.setBounds(373, 122, 69, 62);
			loginButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean f = false;
					f = LogIn(idtx, pswtf);
					if(f)
					{
						viewT = 1;
						changePan();
						setTitle("Lobby");
					}
				}
			});
			add(loginButton);
			
			JLabel lblId = new JLabel("ID");
			lblId.setBounds(126, 120, 61, 16);
			add(lblId);
			
			JLabel lblPassword = new JLabel("Password");
			lblPassword.setBounds(126, 168, 61, 16);
			add(lblPassword);
			
			idtx = new JTextField();
			idtx.setBounds(216, 115, 130, 26);
			add(idtx);
			idtx.setColumns(10);
			
			pswtf = new JTextField();
			pswtf.setBounds(216, 163, 130, 26);
			add(pswtf);
			pswtf.setColumns(10);
			
			JButton btnExit = new JButton("Exit");
			btnExit.setBounds(415, 306, 117, 29);
			btnExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			add(btnExit);
		}
	}

	class LobbyPanel extends JPanel
	{
		
		
		public LobbyPanel()
		{
			setLayout(null);
			
			JScrollPane scroll = new JScrollPane (textPane, 
					   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scroll.setBounds(20, 153, 366, 167);
			getContentPane().add(scroll);
			
			textPane = new JTextPane();
			scroll.setViewportView(textPane);
			textPane.setEditable(false);
			
			add(scroll);
			
			String target [] = {"All", "Lobby"};
			
	
			JComboBox comboBox = new JComboBox(target);
			comboBox.setBounds(21, 333, 52, 27);
			comboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					Object a = e.getItem();
					comboTarget((String)a);
				}
			});
			add(comboBox);
			comboBox.setEditable(true);
			

			textField = new JTextField();
			textField.setBounds(69, 332, 322, 26);
			
			textField.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if(key == KeyEvent.VK_ENTER)
					{
						JTextField input = (JTextField)e.getSource();
						String strMessage = input.getText();
						String strTarget = getTarget();
						m_clientStub.chat(strTarget, strMessage);
						input.setText("");
						input.requestFocus();
					}
				}
			});
			add(textField);
			textField.setColumns(10);
			
			
			JButton btnRoom = new JButton("Room 1");
			btnRoom.setBounds(20, 25, 170, 98);
			btnRoom.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					if(RoomInfo(2)==true)
//					{
						m_clientStub.changeGroup("g2");
						viewT = 2;
						changePan();
						setTitle("Room 1");
						textPane.setText("");
						textPane_1.setText("");
//					}
//					else
//						JOptionPane.showMessageDialog(null, "Please enter another room!\n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
							
				}
			});
			add(btnRoom);
			
			JButton btnRoom_2 = new JButton("Room 2");
			btnRoom_2.setBounds(216, 25, 170, 98);
			btnRoom_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					if(RoomInfo(3)==true)
//					{
						m_clientStub.changeGroup("g3");
						viewT = 2;
						changePan();
						setTitle("Room 2");
						textPane.setText("");
						textPane_1.setText("");
//					}
//					else
//						JOptionPane.showMessageDialog(null, "Please enter another room!\n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
						
				}
			});
			add(btnRoom_2);
			
			JButton btnRoom_3 = new JButton("Room 3");
			btnRoom_3.setBounds(410, 25, 170, 98);
			btnRoom_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					if(RoomInfo(4)==true)
//					{
						m_clientStub.changeGroup("g4");
						viewT = 2;
						changePan();
						setTitle("Room 3");
						textPane.setText("");
						textPane_1.setText("");
//					}
//					else
//						JOptionPane.showMessageDialog(null, "Please enter another room!\n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
						
				}
			});
			add(btnRoom_3);
			
			JButton btnLogOut = new JButton("Log Out");
			btnLogOut.setBounds(420, 332, 80, 30);
			btnLogOut.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						LogOut();
						textPane_1.setText("");
						viewT = 0;
						changePan();
						setTitle("Log In");
				}
			});
			add(btnLogOut);
			
			JButton btnExit_1 = new JButton("Exit");
			btnExit_1.setBounds(500, 332, 80, 30);
			btnExit_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			add(btnExit_1);
		}
	}

	class RoomPanel extends JPanel
	{
		
		
		public RoomPanel()
		{
			setLayout(new BorderLayout());
	
			JScrollPane scroll = new JScrollPane (textPane_1, 
					   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			getContentPane().add(scroll);
			
			textPane_1 = new JTextPane();
			scroll.setViewportView(textPane_1);
			textPane_1.setEditable(false);
			
			add(scroll, BorderLayout.CENTER);

			textField_1 = new JTextField();
			textField_1.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if(key == KeyEvent.VK_ENTER)
					{
						JTextField input = (JTextField)e.getSource();
						String strMessage = input.getText();
						String strTarget = "/g";
						//chat
						m_clientStub.chat(strTarget, strMessage);
						//더미 이벤트
						sendToServer("game#server#gameword#firstWord#"+strMessage);
						
						input.setText("");
						input.requestFocus();
					}
				}
			});
			add(textField_1, BorderLayout.SOUTH);
			
			JPanel topButtonPanel = new JPanel();
			topButtonPanel.setBackground(new Color(220,220,220));
			topButtonPanel.setLayout(new FlowLayout());
			add(topButtonPanel, BorderLayout.NORTH);
			
			JButton btnStart = new JButton("Start");
			btnStart.setBounds(500, 342, 80, 30);
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String strTarget = "/g";
					//m_clientStub.chat(strTarget, "is ready to start.");
					//더미이벤트
					startGame();
				}
			});
			topButtonPanel.add(btnStart);
			
			JButton btnExit_2 = new JButton("Exit");
			btnExit_2.setBounds(500, 342, 80, 30);
			btnExit_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					textPane.setText("");
					m_clientStub.changeGroup("g1");		
					viewT = 1;
					changePan();
					setTitle("Lobby");
					textPane_1.setText("");
				}
			});
			topButtonPanel.add(btnExit_2);
		}
	}
	
	LogInPanel loginpan = null;
	LobbyPanel lobbypan = null;
	RoomPanel roompan = null;

	public void changePan()
	{
		
		if(viewT == 0)
		{
			getContentPane().removeAll();
			getContentPane().add(loginpan);
			revalidate();
			repaint();
		}
		else if(viewT == 1)
		{
			getContentPane().removeAll();
			getContentPane().add(lobbypan);
			revalidate();
			repaint();
		}
		else if(viewT == 2)
		{
			getContentPane().removeAll();
			getContentPane().add(roompan);
			revalidate();
			repaint();
		}
	}

	/**
	 * Create the frame.
	 */
	public WordGameClient() 
	{
		m_clientStub = new CMClientStub();
		m_eventHandler = new ClientEventHandler(m_clientStub, this);

		setTitle("Log In");
		
		loginpan = new LogInPanel();
		lobbypan = new LobbyPanel();
		roompan = new RoomPanel();
		
		this.add(this.loginpan);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, 400);
		
		boolean f = false;
		
		f = StartCM();
		
		if(f)
		{
			setVisible(true);
		}
	}

	public boolean StartCM()
	{
		boolean bRet = false;
		
		String strCurServerAddress = null;
		int nCurServerPort = -1;
		
		strCurServerAddress = m_clientStub.getServerAddress();
		nCurServerPort = m_clientStub.getServerPort();
		
		JTextField serverAddressTextField = new JTextField(strCurServerAddress);
		JTextField serverPortTextField = new JTextField(String.valueOf(nCurServerPort));
		Object msg[] = {
				"Server Address: ", serverAddressTextField,
				"Server Port: ", serverPortTextField
		};
		int option = JOptionPane.showConfirmDialog(null, msg, "Server Information", JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION) 
		{
			String strNewServerAddress = serverAddressTextField.getText();
			int nNewServerPort = Integer.parseInt(serverPortTextField.getText());
			if(!strNewServerAddress.equals(strCurServerAddress) || nNewServerPort != nCurServerPort)
				m_clientStub.setServerInfo(strNewServerAddress, nNewServerPort);
		}
		
		bRet = m_clientStub.startCM();
		
		if(!bRet)
		{
			JOptionPane.showMessageDialog(null, "CM initialization error!\n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
		}
		
		return bRet;
	}
	
	public boolean LogIn(JTextField inId, JTextField inPassword)
	{
		String id = null;
		String password = null;
		boolean br = false;
		
		id = inId.getText();
		password = inPassword.getText();
		
		br = m_clientStub.loginCM(id, password);
		
		if(!br)
		{
			JOptionPane.showMessageDialog(null, "failed the login request!\n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
		}
		
		return br;
	}
	
	public void LogOut()
	{
		boolean br = false;
		br = m_clientStub.logoutCM();
		if(br)
		{
			JOptionPane.showMessageDialog(null, "successfully sent the logout request.\n");
			viewT = 0;
			changePan();
		}
		
		else
			JOptionPane.showMessageDialog(null, "failed the login request!\n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);

		setTitle("Log In");
	}
	
	public void LprintMessage(String strText)
	{
		StyledDocument doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), strText, null);
			textPane.setCaretPosition(textPane.getDocument().getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	
	public void RprintMessage(String strText)
	{
		StyledDocument doc = textPane_1.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), strText, null);
			textPane_1.setCaretPosition(textPane_1.getDocument().getLength());

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	private void comboTarget(String str)
	{
		if(str == "All")
		{
			tg = "/b";
		}
		else if(str == "Lobby")
		{
			tg = "/g";
		}
		else
		{
			tg = "/"+str;
		}
	}
	
	private String getTarget()
	{
		return tg;
	}
	
	public CMClientStub getClientStub()
	{
		return m_clientStub;
	}
	
	public ClientEventHandler getClientEventHandler()
	{
		return m_eventHandler;
	}
	
	public void sendToServer(String input)
	{
		CMDummyEvent due = new CMDummyEvent();
		CMDummyEvent rdue = new CMDummyEvent();
		
		due.setDummyInfo(input);
		String strTarget = "SERVER";
		m_clientStub.send(due, strTarget);
		//서버이벤트핸들러 rdue.setID(222)
		//rdue = (CMDummyEvent) m_clientStub.sendrecv(due, strTarget, CMInfo.CM_DUMMY_EVENT, 222, 5000);
	}
	
	public void startGame()
	{
		CMDummyEvent due = new CMDummyEvent();
		
		due.setDummyInfo("game#server#gamestart");
		String strTarget = "SERVER";
		m_clientStub.send(due, strTarget);
	}
	
	//수정 필요. 숫자뿐만 아니라 게임 시작중인지도 함께 연산하여 리턴시켜야 함
	public boolean RoomInfo(int num)
	{
		CMDummyEvent due = new CMDummyEvent();
		CMDummyEvent rdue = new CMDummyEvent();
		String tmp = num+"";
		
		due.setID(111);
		due.setDummyInfo(tmp);
		//due.setDummyInfo(num);
		String strTarget = "SERVER";
		m_clientStub.send(due, strTarget);
		
		rdue = (CMDummyEvent) m_clientStub.sendrecv(due, strTarget, CMInfo.CM_DUMMY_EVENT, 222, 5000);
		
		String flag = rdue.getDummyInfo();
		
		if(flag == "t")
			return true;
		else
			return false;
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		
		WordGameClient client = new WordGameClient();
		CMClientStub cmStub = client.getClientStub();
		cmStub.setAppEventHandler(client.getClientEventHandler());
	}
}