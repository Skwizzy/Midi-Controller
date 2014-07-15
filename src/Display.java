import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;


@SuppressWarnings("serial")
public class Display extends JFrame implements MouseListener, ActionListener {
	
	
	private final Dimension WHITE_KEYS = new Dimension(35, 200);
	private final Dimension BLACK_KEYS = new Dimension(19, 133);
	private final Border SOLID_BORDER = new MatteBorder(1, 1, 1, 1, Color.BLACK);
	
	private final int WHITE_WIDTH = 35;
	private final int BLACK_WIDTH = 19;
	private final int MAX_KEYS = 88;
	private final int KEY_GROUPS = 16;

	private int index = 0;
	
	private JPanel Key_List[] = new JPanel[MAX_KEYS];	
	private JFileChooser fc = new JFileChooser();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu, subMenu;
	private JMenuItem menuItem = new JMenuItem();
	private JRadioButtonMenuItem rbMenuItem;
	private JCheckBoxMenuItem cbMenuItem;
	
	private File midiFile;

	public Display()
	{
		JFrame window = new JFrame("Midi Keyboard Interpreter");
		JLayeredPane keys = new JLayeredPane();
		
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(menu);
		
		//a group of JMenuItems
		menuItem = new JMenuItem("Open file...",
		                         KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "Opens a .mid file for playing");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Both text and icon",
		                         new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);

		menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_D);
		menu.add(menuItem);

		//a group of radio button menu items
		menu.addSeparator();
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Another one");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		//a group of check box menu items
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Another one");
		cbMenuItem.setMnemonic(KeyEvent.VK_H);
		menu.add(cbMenuItem);

		//a submenu
		menu.addSeparator();
		subMenu = new JMenu("A submenu");
		subMenu.setMnemonic(KeyEvent.VK_S);

		menuItem = new JMenuItem("An item in the submenu");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_2, ActionEvent.ALT_MASK));
		subMenu.add(menuItem);

		menuItem = new JMenuItem("Another item");
		subMenu.add(menuItem);
		menu.add(subMenu);

		//Build second menu in the menu bar.
		menu = new JMenu("Another Menu");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription(
		        "This menu does nothing");
		menuBar.add(menu);
		
		window.add(keys);
		window.setJMenuBar(menuBar);
		window.setSize(1785, 300);
		window.setVisible(true);
		
		for(int i = 0; i < KEY_GROUPS; i++)
		{
			//Creates the first three keys on the keyboard (A, A#, B). Only happens once.
			if(i == 0)
			{
				for(int x = 0; x < 3; x++)
				{
					generateKey(index, x, keys);
					index++;
				}
			}
			//Generate the key group consisting of notes C through E
			else if(i % 2 == 1)
			{
				for(int x = 0; x < 5; x++)
				{
					generateKey(index, x, keys);
					index++;
				}
			}
			//Generate the key group consisting of notes F through B
			else
			{
				for(int x = 0; x < 7; x++)
				{
					generateKey(index, x, keys);
					index++;
				}
			}
		}	
		
		window.addMouseListener(this);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

	//Function that adds the keys to the JLayeredPane.
	private void generateKey(int index, int generate, JLayeredPane keys)
	{
		//When generate = 0, that means its the first key in the group. This key is also a white key.
	    if(generate == 0)
		{
	    	Key_List[index] = new JPanel();
	    	
	    	//Check to see if this is the first key on the keyboard and set its location to 0,0 if it is
	    	if(index == 0)
	    	{
	    		Key_List[index].setLocation(0, 0);
	    	}
	    	//If not, get the X value of the previous white key and add the key's width to determine the position of the new key. The -1 helps get rid of 
	    	//duplicate borders.
	    	else
	    	{
	    		Key_List[index].setLocation(Key_List[index - 1].getX() + WHITE_WIDTH - 1, 0);
	    	}
			
			Key_List[index].setSize(WHITE_KEYS);
			Key_List[index].setBackground(Color.WHITE);
			Key_List[index].setBorder(SOLID_BORDER);
			keys.add(Key_List[index], 0, -1);
		}
	    //If generate % 2 == 1, we want to insert a black key next
		else if(generate % 2 == 1 && index < MAX_KEYS)
		{
			Key_List[index] = new JPanel();
			
			//Get the position of the previous black key and add it with the width to determine the new key's position. 
			//The 4 was just for optimizing the position.
			Key_List[index].setLocation(Key_List[index - 1].getX() + BLACK_WIDTH + 4, 0);
			
			Key_List[index].setSize(BLACK_KEYS);
			Key_List[index].setBackground(Color.BLACK);
			Key_List[index].setBorder(SOLID_BORDER);	
			keys.add(Key_List[index], 1, -1);	
		}
	    //This key must be a white key and we get the previous white key's position to determine the new key's position. 
		else if (index < MAX_KEYS)
		{
			Key_List[index] = new JPanel();
			Key_List[index].setLocation(Key_List[index - 2].getX() + WHITE_WIDTH - 1, 0);
			Key_List[index].setSize(WHITE_KEYS);
			Key_List[index].setBackground(Color.WHITE);
			Key_List[index].setBorder(SOLID_BORDER);
			keys.add(Key_List[index], 0, -1);
		}
	}
	
	public void toggleOn_Left(int i)
	{
		//Presses down the button associated to the key pressed
		//Key_List[i].doClick();
		Key_List[i].setBackground(Color.MAGENTA);
	}
	
	public void toggleOn_Right(int i)
	{
		//Presses down the button associated to the key pressed
		//Key_List[i].doClick();
		Key_List[i].setBackground(Color.CYAN);
	}
	
	public void toggleOff(int i)
	{
		//Presses down the button associated to the key pressed
		//Key_List[i].doClick();
		if(Key_List[i].getWidth() == WHITE_WIDTH)
		{
			Key_List[i].setBackground(Color.WHITE);
		}
		else
		{
			Key_List[i].setBackground(Color.BLACK);
		}
	}
	
	public File getFile()
	{

		int retVal = fc.showOpenDialog(this);;
		
		if(retVal == JFileChooser.APPROVE_OPTION)
		{
			return fc.getSelectedFile();
		}
		else
		{
			return null;
		}
	}
	
	
	public void redrawKey(int index)
	{
		Key_List[index].repaint();
	}
		
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		System.out.println(arg0.getLocationOnScreen());
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getActionCommand() == "Open file...")
		{
			getFile();
		}
		
	}
}
