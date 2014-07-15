import java.applet.Applet;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.MidiUnavailableException;

import org.jfugue.*;
import javax.sound.midi.InvalidMidiDataException;

//For Debugging.
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

public class MidiController extends Applet implements ParserListener, Receiver{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MAX_KEYS = 88;
	//The virtual keyboard mappings & on/off switches
	private static Piano Piano_map;
	
	private static Display Virtual_keys;
	
	private static DeviceThatWillTransmitMidi keyboard;
	private static Sequencer player; 
	
	public MidiController() throws MidiUnavailableException
	{
	     //Initialize built in sequencer for reading midi files.
		 player = MidiSystem.getSequencer();
		 player.getTransmitter().setReceiver(this);
		 
		//Prints out available Midi Devices. For Debugging.
		//*************************************************
		
		MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
		if (devices.length == 0) 
		{
		    System.out.println("No MIDI devices found");
		} 
		else 
		{
		    for (MidiDevice.Info dev : devices) 
		    {
		        System.out.println(dev);
		    }
		}
		
		
		try 
		{			
			//Initialize key mappings & on/off switches
			Piano_map = new Piano();
			
			//Create the Virtual Keyboard
			Virtual_keys = new Display();
			
			
			//Connect to Midi device
			keyboard = new DeviceThatWillTransmitMidi();
			keyboard.addParserListener(this);
			
			//Start the event listening
			keyboard.startListening();
			
		} 
		catch (MidiUnavailableException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public static void main(String args[]) throws MidiUnavailableException 
	{		
		//Initialize Midi device and attach the event listener.
		MidiController Controller = new MidiController();
		
		//This needs to be a menu option. Hard Coded for now
		File midiFile = Virtual_keys.getFile();
		
		try
		{
			  //Prepare file for sequencer to play.
			  InputStream ios = new BufferedInputStream(new FileInputStream(midiFile));
			  player.open();
			  player.setSequence(ios);	  
			  player.start();			  
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InvalidMidiDataException e)
		{
			e.printStackTrace();
		}
		
		while(Virtual_keys.getStatus())
		{
			
			for(int i = 0; i < MAX_KEYS; i++)
			{
				if(Piano_map.getSwitch(i) == false)
				{
					Virtual_keys.toggleOff(i);
				}
			}
		}
		
		//Mr. Clean
		keyboard.stopListening();
		player.stop();
		player.close();
		keyboard.close();
		keyboard = null;
		Piano_map = null;
		Controller = null;
	    Virtual_keys = null;
		System.exit(0);
	    
	}
	
	//Called when a Piano key is pressed. Called again when the note is released.
	@Override
	public void noteEvent(Note arg0) {
		
		//The lowest key on my keyboard has a midi value of 21. Now it can be used as an index
		int array_value = arg0.getValue() - 21;
		
		//Show that the key has been pressed
		Piano_map.flipSwitch(array_value);
		Virtual_keys.toggleOn_Right(array_value);
		
		if(Piano_map.getSwitch(array_value) == false)
		{
			Virtual_keys.repaint();
			Virtual_keys.redrawKey(array_value);
		}
		
		System.out.println("Note Pressed: " + Piano_map.getKey(array_value));
		System.out.println("Note Held: " + Piano_map.getSwitch(array_value));
	}
	
	@Override
	public void send(MidiMessage arg0, long arg1) {
		// TODO Auto-generated method stub
		byte[] message = arg0.getMessage();
		
		/*
		System.out.println(arg0.getStatus());
		for(int i = 0; i < tests.length; i++)
		{
			
			System.out.println(tests[i]);
		}
		*/
		
		if(arg0.getStatus() == 144)
		{
				int array_value = message[1] - 21;
				
				//Show that the key has been pressed
				Piano_map.flipSwitch(array_value);
				Virtual_keys.toggleOn_Right(array_value);
				
				if(Piano_map.getSwitch(array_value) == false)
				{
					Virtual_keys.repaint();
					Virtual_keys.redrawKey(array_value);
				}
				
				System.out.println("Note Pressed: " + Piano_map.getKey(array_value));
				System.out.println("Note Held: " + Piano_map.getSwitch(array_value));
		}
		else if (arg0.getStatus() == 145)
		{
			int array_value = message[1] - 21;
			
			//Show that the key has been pressed
			Piano_map.flipSwitch(array_value);
			Virtual_keys.toggleOn_Left(array_value);
			
			if(Piano_map.getSwitch(array_value) == false)
			{
				Virtual_keys.repaint();
				Virtual_keys.redrawKey(array_value);
			}
			
			System.out.println("Note Pressed: " + Piano_map.getKey(array_value));
			System.out.println("Note Held: " + Piano_map.getSwitch(array_value));
		}
	}	

//Mandatory bullshit so the compiler doesn't bitch at me

	@Override
	public void channelPressureEvent(ChannelPressure arg0) {
		// TODO Auto-generated method stub
		System.out.println("1");
		
	}

	@Override
	public void controllerEvent(Controller arg0) {
		// TODO Auto-generated method stub
		System.out.println("2");
	}

	@Override
	public void instrumentEvent(Instrument arg0) {
		// TODO Auto-generated method stub
		System.out.println("3");
	}

	@Override
	public void keySignatureEvent(KeySignature arg0) {
		// TODO Auto-generated method stub
		System.out.println("4");
	}

	@Override
	public void layerEvent(Layer arg0) {
		// TODO Auto-generated method stub
		System.out.println("5");
	}

	@Override
	public void measureEvent(Measure arg0) {
		// TODO Auto-generated method stub
		System.out.println("6");
	}

	@Override
	public void parallelNoteEvent(Note arg0) {
		// TODO Auto-generated method stub
		System.out.println("8");
	}

	@Override
	public void pitchBendEvent(PitchBend arg0) {
		// TODO Auto-generated method stub
		System.out.println("9");
	}

	@Override
	public void polyphonicPressureEvent(PolyphonicPressure arg0) {
		// TODO Auto-generated method stub
		System.out.println("10");
	}

	@Override
	public void sequentialNoteEvent(Note arg0) {
		// TODO Auto-generated method stub
		System.out.println("11");
	}

	@Override
	public void tempoEvent(Tempo arg0) {
		// TODO Auto-generated method stub
		System.out.println("12");
	}

	@Override
	public void timeEvent(Time arg0) {
		// TODO Auto-generated method stub
		//System.out.println("13");
	}

	@Override
	public void voiceEvent(Voice arg0) {
		// TODO Auto-generated method stub
		//System.out.println("14");
	}

	public void controlChange(ShortMessage arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getMessage());
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		keyboard.stopListening();
		keyboard.close();
		player.stop();
		player.close();
		keyboard = null;
		Piano_map = null;
	    Virtual_keys = null;
	}	

}
