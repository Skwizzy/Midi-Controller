import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class Menu extends JFrame implements ActionListener, MenuListener{
	
	private static JMenuBar menuBar = new JMenuBar();
	private JMenu menu;
	private JMenuItem menuItem = new JMenuItem();
	private static JFrame window;
	private JList device_list;
	private static MidiController Controller;
	private JDialog options;
	private JPanel tempo_panel;
	private static JButton up;
	private static JButton down;
	private static JTextField tempo;
	
	private static float song_tempo = 120;
	
	public Menu() throws MidiUnavailableException
	{
		Controller = new MidiController();
		
		window = new JFrame("Midi Keyboard Interpreter");
		
		//File menu
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("File menu");
		menuBar.add(menu);
		
		//Open File menu item
		menuItem = new JMenuItem("Open File...", KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Opens a .mid file for playing");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		//Audio Devices menu
		menu = new JMenu("Audio Devices");
		menu.setMnemonic(KeyEvent.VK_D);
		menu.getAccessibleContext().setAccessibleDescription("Selects an Audio Device");
		menu.addMenuListener(this);
		menuBar.add(menu);
		
		//Add everything to the frame
		window.add(Controller.getVirtualKeys());
		window.setJMenuBar(menuBar);
		window.add(TempoButton(), BorderLayout.PAGE_END);
		window.setSize(1785, 300);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) throws MidiUnavailableException 
	{		
		//Initialize Midi device and attach the event listener.
		Menu Interface = new Menu();
		
		//This needs to be a menu option. Hard Coded for now
		
		
		while(true)
		{
			Controller.checkKeys();
		}
		
		//Mr. Clean
		//keyboard.stopListening();
		//player.stop();
		//player.close();
		//keyboard.close();
		//keyboard = null;
		//Piano_map = null;
		//Controller = null;
	    //Virtual_keys = null;
		//System.exit(0);
	    
	}
	
	public static void updateTempo()
	{
		tempo.setText(String.format("%.2f", song_tempo));
		tempo.repaint();
		
		Controller.setTempo(song_tempo);
	}
	public static void openFile() throws MidiUnavailableException
	{
		JFileChooser fc = new JFileChooser();
		
		//Display the file chooser and get the return value for the selected file
		int retVal = fc.showOpenDialog(window);
		
		if(retVal == JFileChooser.APPROVE_OPTION)
		{
			//Reset the keyboard and start the sequencer
			window.remove(Controller.getVirtualKeys());		
			Controller.startSequencer(fc.getSelectedFile());			
			window.add(Controller.getVirtualKeys());
			
			//Get the tempo of the song and update the tempo control
			song_tempo = Controller.getTempo();
			tempo.setText(String.format("%.2f", song_tempo));
			
			//Enable tempo controls
			up.setEnabled(true);
			down.setEnabled(true);
			tempo.setEnabled(true);
			
			window.setVisible(false);
			window.setVisible(true);	
		}
	}
	
	public void setDevice()
	{
		//Creates a seperate window that lists the devices that the user can
		//choose from.
		options = new JDialog(this, "Audio Devices");
		JPanel device_pane = new JPanel();
		JButton select = new JButton("Select");
		DefaultListModel list = new DefaultListModel();
		
		if (Controller.getDeviceCount() == 0) 
		{
			JOptionPane.showMessageDialog(this,
				    "No MIDI devices found!",
				    "No MIDI Found",
				    JOptionPane.ERROR_MESSAGE);
			
			return;
		} 
		
		//Add devices to the list
		for(int i = 0; i < Controller.getDeviceCount(); i++)
		{
			list.addElement(Controller.getDeviceName(i));
		}
		
		device_list = new JList(list);
		device_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//Set currently selected audio device
		if(Controller.getSelectedDevice() != 0)
		{
			device_list.setSelectedIndex(Controller.getSelectedDevice());
		}
		
		device_list.setVisibleRowCount(7);
		
        JScrollPane listScrollPane = new JScrollPane(device_list);
        
        device_pane.add(listScrollPane, BorderLayout.CENTER);
        
        //Configure select button
        select.setActionCommand("Select");
		select.addActionListener(this);
		
		//Add things to new JDialog
		options.setSize(220, 200);
		options.add(device_pane, BorderLayout.CENTER);
		options.add(select, BorderLayout.PAGE_END);	
		options.setModal(true);
		options.toFront();
		options.setVisible(true);
		
	}
	
	public JPanel TempoButton()
	{	
		tempo_panel = new JPanel();
		up = new JButton(">");
		down = new JButton("<");
		tempo = new JTextField(String.format("%.2f",song_tempo), 4);
		
		tempo.setSize(100, up.getHeight());
		tempo.setHorizontalAlignment(JTextField.CENTER);
		
		up.addActionListener(this);
		down.addActionListener(this);
		tempo.addActionListener(this);
		
		up.setActionCommand("Tempo_Up");
		down.setActionCommand("Tempo_Down");
		tempo.setActionCommand("Tempo_Change");
		
		up.setEnabled(false);
		down.setEnabled(false);
		tempo.setEnabled(false);
		
		tempo_panel.add(down, BorderLayout.WEST);
		tempo_panel.add(tempo, BorderLayout.CENTER);
		tempo_panel.add(up, BorderLayout.EAST);

		return tempo_panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		//Called when Open File is selected.
		if(arg0.getActionCommand() == "Open File...")
		{
			if(Controller.getSelectedDevice() == -1)
			{
				JOptionPane.showMessageDialog(this,
					    "You must select an audio device!",
					    "No Device Detected",
					    JOptionPane.ERROR_MESSAGE);
				
				setDevice();
			}
			try 
			{				
				openFile();			
			} 
			catch (MidiUnavailableException e) 
			{
				JOptionPane.showMessageDialog(this,
					    "Could not open the specified file!",
					    "Failed to Open File",
					    JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		//Called when an audio device is selected.
		else if(arg0.getActionCommand() == "Select")
		{
			int index = device_list.getSelectedIndex();
			
			try 
			{
				Controller.connectToDevice(index);
				options.setVisible(false);
			} 
			catch (MidiUnavailableException e) 
			{	
				JOptionPane.showMessageDialog(this,
					    "Could not connect to the selected device!",
					    "Failed to Connect",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(arg0.getActionCommand() == "Tempo_Up")
		{
			song_tempo = song_tempo + 1.00f;
			updateTempo();
		}
		else if(arg0.getActionCommand() == "Tempo_Down")
		{
			song_tempo = song_tempo - 1.00f;
			updateTempo();
		}
		else if(arg0.getActionCommand() == "Tempo_Change")
		{
			song_tempo = Float.parseFloat(tempo.getText());
			
			if(song_tempo < 0)
			{
				song_tempo = 0;
			}
			updateTempo();
		}
	}

	@Override
	public void menuCanceled(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuDeselected(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuSelected(MenuEvent arg0)
	{	
		setDevice();
		((JMenu) arg0.getSource()).setSelected(false);
		
	}
	
	
	
}
