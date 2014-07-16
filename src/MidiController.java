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
import javax.swing.JLayeredPane;

@SuppressWarnings("serial")
public class MidiController implements ParserListener, Receiver{

	private final int MAX_KEYS = 88;
	private int selected_device = 0;
	
	//The virtual keyboard mappings & on/off switches
	private Piano Piano_map;
	
	private Display Virtual_keys;
	
	private DeviceThatWillTransmitMidi keyboard;
	private Sequencer player;
	
	private MidiDevice.Info[] devices;
	
	
	public MidiController() throws MidiUnavailableException
	{
				 
		//Initialize key mappings & on/off switches
		Piano_map = new Piano();
		
		//Create the Virtual Keyboard
		Virtual_keys = new Display();
		
		devices = MidiSystem.getMidiDeviceInfo();
	}
	
	public void startListening()
	{
		//Start the event listening
		keyboard.startListening();
	}
	
	public void startSequencer(File midiFile) throws MidiUnavailableException
	{
		try
		{
			  InputStream ios = new BufferedInputStream(new FileInputStream(midiFile));
			  
			  
			  if(player != null && player.isRunning())
			  {
				  player.stop();
				  resetKeys();
			  }
			  
			  if(player != null && player.isOpen())
			  {
				  player.close();
				  resetKeys();
			  }
			  
			  //Initialize built in sequencer for reading midi files.
			  player = MidiSystem.getSequencer();
			  player.getTransmitter().setReceiver(this);
			 
			  
			  //Prepare file for sequencer to play.
			 
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
	}
	
	public void connectToDevice(int index) throws MidiUnavailableException
	{
		keyboard = new DeviceThatWillTransmitMidi(devices[index]);
		keyboard.addParserListener(this);
		
		selected_device = index;
	}

	public int getSelectedDevice()
	{
		return selected_device;
	}
	
	public int getDeviceCount()
	{
		return devices.length;
	}
	
	public String getDeviceName(int index)
	{
		return devices[index].getName();
	}
	
	public void checkKeys()
	{
		for(int i = 0; i < MAX_KEYS; i++)
		{
			if(Piano_map.getSwitch(i) == false)
			{
				Virtual_keys.toggleOff(i);
			}
		}
	}
	
	public JLayeredPane getVirtualKeys()
	{
		return Virtual_keys;
	}
	
	public void resetKeys()
	{
		Virtual_keys = new Display();
		Piano_map = new Piano();
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
