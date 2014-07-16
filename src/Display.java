import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;


@SuppressWarnings("serial")
public class Display extends JLayeredPane{
	
	
	private final Dimension WHITE_KEYS = new Dimension(35, 200);
	private final Dimension BLACK_KEYS = new Dimension(19, 133);
	private final Border SOLID_BORDER = new MatteBorder(1, 1, 1, 1, Color.BLACK);
	
	private final int WHITE_WIDTH = 35;
	private final int BLACK_WIDTH = 19;
	private final int MAX_KEYS = 88;
	private final int KEY_GROUPS = 16;

	private int index = 0;
	
	private JPanel Key_List[] = new JPanel[MAX_KEYS];

	public Display()
	{	
		
		for(int i = 0; i < KEY_GROUPS; i++)
		{
			//Creates the first three keys on the keyboard (A, A#, B). Only happens once.
			if(i == 0)
			{
				for(int x = 0; x < 3; x++)
				{
					generateKey(index, x);
					index++;
				}
			}
			//Generate the key group consisting of notes C through E
			else if(i % 2 == 1)
			{
				for(int x = 0; x < 5; x++)
				{
					generateKey(index, x);
					index++;
				}
			}
			//Generate the key group consisting of notes F through B
			else
			{
				for(int x = 0; x < 7; x++)
				{
					generateKey(index, x);
					index++;
				}
			}
		}				
	}

	//Function that adds the keys to the JLayeredPane.
	private void generateKey(int index, int generate)
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
			add(Key_List[index], 0, -1);
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
			add(Key_List[index], 1, -1);	
		}
	    //This key must be a white key and we get the previous white key's position to determine the new key's position. 
		else if (index < MAX_KEYS)
		{
			Key_List[index] = new JPanel();
			Key_List[index].setLocation(Key_List[index - 2].getX() + WHITE_WIDTH - 1, 0);
			Key_List[index].setSize(WHITE_KEYS);
			Key_List[index].setBackground(Color.WHITE);
			Key_List[index].setBorder(SOLID_BORDER);
			add(Key_List[index], 0, -1);
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
	
	public void redrawKey(int index)
	{
		Key_List[index].repaint();
	}
	
}
