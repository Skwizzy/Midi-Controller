import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;


@SuppressWarnings("serial")
public class Display extends JFrame implements KeyListener, MouseListener {
	
	
	private final Dimension WHITE_KEYS = new Dimension(35, 200);
	private final Dimension BLACK_KEYS = new Dimension(19, 133);
	private final Border SOLID_BORDER = new MatteBorder(1, 1, 1, 1, Color.BLACK);
	
	private final int WHITE_WIDTH = 35;
	private final int BLACK_WIDTH = 19;
	private final int MAX_KEYS = 88;
	private final int KEY_GROUPS = 16;
	
	private boolean isrunning = true;
	private int index = 0;
	
	private JPanel C_through_E[] = new JPanel[MAX_KEYS];	
	private JFileChooser fc = new JFileChooser();

	public Display()
	{
		JFrame window = new JFrame("Midi Keyboard Interpreter");
		JLayeredPane keys = new JLayeredPane();
		
		window.add(keys);
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
		
		
		window.addKeyListener(this);
		window.addMouseListener(this);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

	//Function that adds the keys to the JLayeredPane.
	private void generateKey(int index, int generate, JLayeredPane keys)
	{
		//When generate = 0, that means its the first key in the group. This key is also a white key.
	    if(generate == 0)
		{
	    	C_through_E[index] = new JPanel();
	    	
	    	//Check to see if this is the first key on the keyboard and set its location to 0,0 if it is
	    	if(index == 0)
	    	{
	    		C_through_E[index].setLocation(0, 0);
	    	}
	    	//If not, get the X value of the previous white key and add the key's width to determine the position of the new key. The -1 helps get rid of 
	    	//duplicate borders.
	    	else
	    	{
	    		C_through_E[index].setLocation(C_through_E[index - 1].getX() + WHITE_WIDTH - 1, 0);
	    	}
			
			C_through_E[index].setSize(WHITE_KEYS);
			C_through_E[index].setBackground(Color.WHITE);
			C_through_E[index].setBorder(SOLID_BORDER);
			keys.add(C_through_E[index], 0, -1);
		}
	    //If generate % 2 == 1, we want to insert a black key next
		else if(generate % 2 == 1 && index < MAX_KEYS)
		{
			C_through_E[index] = new JPanel();
			
			//Get the position of the previous black key and add it with the width to determine the new key's position. 
			//The 4 was just for optimizing the position.
			C_through_E[index].setLocation(C_through_E[index - 1].getX() + BLACK_WIDTH + 4, 0);
			
			C_through_E[index].setSize(BLACK_KEYS);
			C_through_E[index].setBackground(Color.BLACK);
			C_through_E[index].setBorder(SOLID_BORDER);	
			keys.add(C_through_E[index], 1, -1);	
		}
	    //This key must be a white key and we get the previous white key's position to determine the new key's position. 
		else if (index < MAX_KEYS)
		{
			C_through_E[index] = new JPanel();
			C_through_E[index].setLocation(C_through_E[index - 2].getX() + WHITE_WIDTH - 1, 0);
			C_through_E[index].setSize(WHITE_KEYS);
			C_through_E[index].setBackground(Color.WHITE);
			C_through_E[index].setBorder(SOLID_BORDER);
			keys.add(C_through_E[index], 0, -1);
		}
	}
	public boolean getStatus()
	{
		return isrunning;
	}
	
	public void toggleOn_Left(int i)
	{
		//Presses down the button associated to the key pressed
		//C_through_E[i].doClick();
		C_through_E[i].setBackground(Color.MAGENTA);
	}
	
	public void toggleOn_Right(int i)
	{
		//Presses down the button associated to the key pressed
		//C_through_E[i].doClick();
		C_through_E[i].setBackground(Color.CYAN);
	}
	
	public void toggleOff(int i)
	{
		//Presses down the button associated to the key pressed
		//C_through_E[i].doClick();
		if(C_through_E[i].getWidth() == WHITE_WIDTH)
		{
			C_through_E[i].setBackground(Color.WHITE);
		}
		else
		{
			C_through_E[i].setBackground(Color.BLACK);
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
		C_through_E[index].repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		// TODO Auto-generated method stub
		
		if(e.getKeyChar() == 'q')
		{
			isrunning = false;
			System.out.println("Is_Running: False");
		}
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
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
}
