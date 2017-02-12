package guiBlock;
/**
 * @author: Andrew Ostrosky
 * @version 1.0
 * @since 2017-01-24
 */


import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import commsCommander.IOCommander;
import fileHandling.CSVFileHandling;
import serialconnection.Datapool;
import serialconnection.DiscoverPorts;

public class GUI extends JFrame implements ActionListener, ItemListener {
	public static String  comPort = "COM1";
	public static int Baud = 115200, aquireMode = 0;
	public static double Seconds = 1,sendParameter = 1;
	public static boolean isAquiring,isConnected, fileChosen;
	public final static ImageIcon IOIOIcon = new ImageIcon("icon/IOIOI_mini.png", "IOIOI Icon");
	public static JTextField statusBar;
	
	
	public GUI()   // Constructor
	  {
		super("GDM-8251A Remote Control Console");// Title Bar
	    Container c = getContentPane();
	    c.setBackground(Color.WHITE);
	    c.setLayout(new BorderLayout());
	    c.add(new JLabel("This Part is useless!", 10));
	    statusBar = new JTextField(0);//c.getWidth()-16
	    statusBar.setBackground(new Color(245, 241, 222));
	    statusBar.setEditable(false);
	    setStatusBar();
	    getContentPane().add(statusBar, java.awt.BorderLayout.PAGE_END);
	    
	  }

	//--end of main GUI
	//begin of status bar
	
	public static void setStatusBar() {
    	String message = " ";
   	    //add points if points are used
    	if(isAquiring) message += "Aquiring | ";
    	else message += "Not aquiring | ";
    	switch(aquireMode){
    	case 0:
    		message += "Free Running Aquisition ";
    		break;
    	case 1:
    		message += "Aquiring " + (int)sendParameter + " Points | ";
    		break;
    	case 2:
    		if(sendParameter < 1) message += "Aquiring for " + (Math.round(sendParameter*60)) +" Seconds | ";
    		else message += "Aquiring for " + (int)sendParameter +" Minutes | ";
    		break;
    	default:
    		message += "Aquisition Mode Error! | ";
    		break;
    	}
        statusBar.setText(message);
    }
	
	//end status bar
	//-- beginning of UART selector method
	public static String chooseUART() {
	    ArrayList<?> choices = DiscoverPorts.listPorts();
	    if(!choices.isEmpty()){
	    Object[] elMenu = choices.toArray();
	    String input = (String) JOptionPane.showInputDialog(null, "Choose a COM (UART) port","Select COM Port", JOptionPane.QUESTION_MESSAGE, IOIOIcon , elMenu, elMenu[0]); // Initial choice
	    return input;
	    }else{ errorDialog("No Serial ports found!");
	    return "noport";
	    }
	    
	  }
	//-- ending of UART selector method
	
	
	
	
	public static void CreateGUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		JMenuBar menuBar;
    	JMenu Filemenu, Baudmenu, Helpmenu, SAmenu, ModeMenu;
    	JButton ConnectButton,COMPortButton,DisconnectButton,startStop;
    	JMenuItem menuItem;
    	JRadioButtonMenuItem rbMenuItem;
    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        GUI window = new GUI();
        
      //Create the menu bar.
        menuBar = new JMenuBar();

        //File Menu
        Filemenu = new JMenu("File");
        Filemenu.setMnemonic(KeyEvent.VK_F);
        Filemenu.getAccessibleContext().setAccessibleDescription("Save, New, etc.");
        menuBar.add(Filemenu);
        // Options within file menu
        menuItem = new JMenuItem("New",KeyEvent.VK_N);//new button with CTRL+N action
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Creates a New file");
        Filemenu.add(menuItem);
        menuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	//System.out.println("Placeholder for New");
        	CSVFileHandling.WriteFile(ChooseNewFile());
        	fileChosen=true;
        }});
        
        menuItem = new JMenuItem("Save",KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Saves Aquired Data");
        Filemenu.add(menuItem);
        menuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	//System.out.println("Placeholder for Save");
        	IOCommander.SRQData(false, Seconds, 0,0);
        	if(fileChosen) CSVFileHandling.SaveCSV();
        	else errorDialog("No file or changes to Save!");
        	fileChosen=false;
        }});
       /* may not implement requires too much modification of CSV Writer
        menuItem = new JMenuItem("Save As",KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Saves as a new file");
        Filemenu.add(menuItem);
        menuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	System.out.println("Placeholder for Save as");
        }});
	*/
      //Mode Menu
        ModeMenu = new JMenu("Mode");
        ModeMenu.setMnemonic(KeyEvent.VK_M);
        ModeMenu.getAccessibleContext().setAccessibleDescription("Adjusts the Aquisition Mode");
        
        //add radio buttons to mode menu
        ModeMenu.addSeparator();
        ButtonGroup modeChoice = new ButtonGroup();

        rbMenuItem = new JRadioButtonMenuItem("Free Run");
        rbMenuItem.setSelected(true);
        modeChoice.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	aquireMode = 0;
        	setStatusBar();
        	}});
        ModeMenu.add(rbMenuItem);
        
        //--
        
        rbMenuItem = new JRadioButtonMenuItem("Points");
        rbMenuItem.setSelected(true);
        modeChoice.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	aquireMode = 1;
        	String pointsInput = JOptionPane.showInputDialog("Enter the number of points you'd like to aquire");
        	if(pointsInput == null || pointsInput.isEmpty()){ errorDialog("Invalid Entry"); aquireMode =0;}
        	else sendParameter = Double.parseDouble(pointsInput);
        	setStatusBar();
        }});
        ModeMenu.add(rbMenuItem);
        
        //--
        
        rbMenuItem = new JRadioButtonMenuItem("Time");
        rbMenuItem.setSelected(true);
        modeChoice.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	aquireMode = 2;
        	String TimeInput = JOptionPane.showInputDialog("Enter length of time to aquire");
        	if(TimeInput == null || TimeInput.isEmpty()){ errorDialog("Invalid Entry"); aquireMode =0;}
        	else sendParameter = Double.parseDouble(TimeInput); 
        	setStatusBar();
        }});
        ModeMenu.add(rbMenuItem);
        
        menuBar.add(ModeMenu);
        
        
        //Baud rate menu
        Baudmenu = new JMenu("Baud Rate");
        Baudmenu.setMnemonic(KeyEvent.VK_B);
        Baudmenu.getAccessibleContext().setAccessibleDescription("Adjusts Baud Rate for your instrument");
        
        //add radio buttons to baud rate menu
        Baudmenu.addSeparator();
        ButtonGroup BaudRate = new ButtonGroup();

        rbMenuItem = new JRadioButtonMenuItem("115200");
        rbMenuItem.setSelected(true);
        BaudRate.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){Baud=115200;}});
        Baudmenu.add(rbMenuItem);
        
        //--
        
        rbMenuItem = new JRadioButtonMenuItem("57600");
        BaudRate.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){Baud=57600;}});
        Baudmenu.add(rbMenuItem);
        
        //--
        
        rbMenuItem = new JRadioButtonMenuItem("38400");
        BaudRate.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){Baud=38400;}});
        Baudmenu.add(rbMenuItem);
        
        //--
        
        rbMenuItem = new JRadioButtonMenuItem("19200");
        BaudRate.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){Baud=19200;}});
        Baudmenu.add(rbMenuItem);
        
        //--       
        
        rbMenuItem = new JRadioButtonMenuItem("9600");
        BaudRate.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){Baud=9600;}});
        Baudmenu.add(rbMenuItem);
        menuBar.add(Baudmenu);
        
        //------------------
        SAmenu = new JMenu("Aquire Rate");
        Baudmenu.setMnemonic(KeyEvent.VK_R);
        Baudmenu.getAccessibleContext().setAccessibleDescription("Adjusts Aquisition Rate from your instrument");
        
        //add radio buttons to Acquisition rate menu
        Baudmenu.addSeparator();
        ButtonGroup SARate = new ButtonGroup();

        rbMenuItem = new JRadioButtonMenuItem("1 Second");
        rbMenuItem.setSelected(true);
        SARate.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){Seconds = 1;}});
        SAmenu.add(rbMenuItem);
        
        //--
        
        rbMenuItem = new JRadioButtonMenuItem("500 Milliseconds");
        rbMenuItem.setSelected(true);
        SARate.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){Seconds = .5;}});
        SAmenu.add(rbMenuItem);
       
        //--
        rbMenuItem = new JRadioButtonMenuItem("250 MilliSeconds");
        rbMenuItem.setSelected(true);
        SARate.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){Seconds = .5;}});
        SAmenu.add(rbMenuItem);
        
        //--
        
        rbMenuItem = new JRadioButtonMenuItem("Custom Time");
        rbMenuItem.setSelected(true);
        SARate.add(rbMenuItem);
        rbMenuItem.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	String SecondsInput = JOptionPane.showInputDialog("Custom time (Seconds)");
        	if(SecondsInput == null || SecondsInput.isEmpty()) errorDialog("Invalid Entry");
        	else Seconds = Double.parseDouble(SecondsInput);
        	}});
        SAmenu.add(rbMenuItem);
        //--
        
        
        
        menuBar.add(SAmenu);
        
      //------------------
        
       COMPortButton = new JButton("COM Port");
       COMPortButton.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	comPort = chooseUART();
        	while(comPort == null){ 
        		errorDialog("A port was not chosen!");
        		comPort = chooseUART();
        	  //System.out.println("User Chose: " + comPort );
        	}
        }});	
        
        menuBar.add(COMPortButton);
        
        //--
        
      //for connect button
        ConnectButton = new JButton("Connect");
        ConnectButton.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	try {
				new Datapool().connect(comPort,Baud);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block aka me not gaf
				e1.printStackTrace();
				errorDialog("Unable to connect to the serial port " + comPort);
				isConnected = false;
			}
            System.out.println("Successfully connected to " + comPort + " @ " + Baud + " baud");
            isConnected = true;
        }});	
        
        menuBar.add(ConnectButton);
        
        //--
        
        startStop = new JButton("Start/Stop");
        startStop.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	if(isConnected && fileChosen){
        	IOCommander.SRQData(!isAquiring, Seconds,aquireMode,sendParameter);
        	System.out.println(sendParameter);
        	if(aquireMode == 1) isAquiring =  !isAquiring;
        	/*
        	 if(!isAquiring) System.out.println("Stopped");
        	else System.out.println("Started");
        	*/
        	}else if(!isConnected)errorDialog("You never connected to any serial port!");
        	else errorDialog("file was not chosen!");
        }});
        
        menuBar.add(startStop);
        
        //--
        
      /*Doesn't work!
        DisconnectButton = new JButton("Disconnect");
        DisconnectButton.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e){
        	Datapool.disconnect();
        	System.out.println("Successfully disconnected from " + comPort + " @ " + Baud + " baud");
        }});
        
        
        menuBar.add(DisconnectButton);
        */
        
        //--
        Helpmenu = new JMenu("Help");
        
        Helpmenu.setMnemonic(KeyEvent.VK_H);
        Helpmenu.getAccessibleContext().setAccessibleDescription("About and Program Help");
        menuBar.add(Helpmenu);
        menuItem = new JMenuItem("About",KeyEvent.VK_A);
        		menuItem.addActionListener(new ActionListener()
                {public void actionPerformed(ActionEvent e){
                	//System.out.println("Placeholder for About");
                	if(Desktop.isDesktopSupported())
                	{
                	  try {
						Desktop.getDesktop().browse(new URI("https://github.com/frog7227/SerialInterface")); //Shameless Help
					} catch (IOException e1) {
						// TODO Auto-generated catch block 
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block WHEN THE URL SYNTAX IS MESSED UP
						e1.printStackTrace();
					}
                	}
                }});	
        menuItem.getAccessibleContext().setAccessibleDescription("Gives Info About this Program");
        Helpmenu.add(menuItem);
      //Helpmenu.addSeparator();
        
        //--
        
        window.setJMenuBar(menuBar);
        window.setBounds(300, 300, 600, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        
        
	}
	
    public static void main (String[] args)
    {
        try
        {
        	CreateGUI();
        }
        catch ( Exception e )
        {
            System.out.println("For some reason (see below) or another I was unable to Create the GUI!");
            e.printStackTrace();
        }
    }

    public static String ChooseNewFile(){
    	JFileChooser fileChooser  = new JFileChooser();
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV","csv");
    	fileChooser.setFileFilter(filter);
    	fileChooser.setApproveButtonText("New");
    	int returnVal = fileChooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
          // System.out.println("You chose to open this file: " + fileChooser.getSelectedFile().getAbsolutePath());
        	return fileChooser.getSelectedFile().getAbsolutePath();
        }
    	
    	
    	return null;
    }
    public static void errorDialog(String Error){
    	JOptionPane.showMessageDialog(new JFrame(), Error, "Error Message",
    	        JOptionPane.ERROR_MESSAGE);
    }
    
    
    
    //these don't even do anything!
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method (not function, it's SO different...) stub
		
	}
}