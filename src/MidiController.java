import javax.sound.midi.MidiUnavailableException;

import org.jfugue.*;

//For Debugging.
//import javax.sound.midi.MidiDevice;
//import javax.sound.midi.MidiSystem;

public class MidiController implements ParserListener{
		
	//The virtual keyboard mappings & on/off switches
	private static Piano Piano_map;
	
	private static Display Virtual_keys;
	
	private static DeviceThatWillTransmitMidi keyboard;
	
	public MidiController()
	{
		//Prints out available Midi Devices. For Debugging.
		//*************************************************
		/*
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
		*/
		
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
		
		while(Virtual_keys.getStatus())
		{
			//This is the only way I can actually get the Key Listener to quit the program.
			//Not really sure why it won't exit unless I have this here.
			//if(isrunning)
			//	System.out.println("I am running!");
			//else
			//	System.out.println("I am not supposed to be alive!");				
		}
		
		//Mr. Clean
		keyboard.stopListening();
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
		Virtual_keys.toggleKeyPress(array_value);
		
		if(Piano_map.getSwitch(array_value) == false)
		{
			Virtual_keys.repaint();
			Virtual_keys.redrawKey(array_value);
		}
		
		System.out.println("Note Pressed: " + Piano_map.getKey(array_value));
		System.out.println("Note Held: " + Piano_map.getSwitch(array_value));
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

}
