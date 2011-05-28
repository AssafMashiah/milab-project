//package arduino.bluetooth;
//
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import activities.MainActivity;
//import android.bluetooth.BluetoothSocket;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.widget.Toast;
//import at.abraxas.amarino.Amarino;
//import at.abraxas.amarino.AmarinoIntent;
//
//public class Comm extends BroadcastReceiver
//{
//	MainActivity activity;
//	BluetoothSocket socket;
//	InputStream in;
//	OutputStream out;
//	String remoteAddr;
//	
//	public static final String DEVICE_ADDRESS = "00:12:6F:09:6A:9B";
//
//	private boolean connected = false;
//
//	public Comm(MainActivity activity)
//	{
//		this.activity = activity;
//	}
//
//	public void connect(String address)
//	{
//		// this is how you tell Amarino to connect to a specific BT device from
//		// within your own code
//		Amarino.connect(activity, address);
//		activity.registerReceiver(this, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
//		
//		remoteAddr = address;
//		connected = true;
//	}
//
//	public void closeComm()
//	{
//		if (connected)
//		{
//			// if you connect in onStart() you must not forget to disconnect
//			// when your app is closed
//			Amarino.disconnect(activity, remoteAddr);
//			
//			// do never forget to unregister a registered receiver
//			activity.unregisterReceiver(this);
//			connected = false;
//		}
//
//	}
//
//	public void sendData(int data)
//	{
//		Amarino.sendDataToArduino(activity, DEVICE_ADDRESS, 'q', data);
//	}
//	
//	public void onReceive(Context context, Intent intent)
//	{
//		String data = null;
//		String action = intent.getAction();
//		if (!action.equals(AmarinoIntent.ACTION_RECEIVED))
//			return;
//		
//		// the device address from which the data was sent, we don't need it
//		// here but to demonstrate how you retrieve it
//		final String address = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
//		
//		// the type of data which is added to the intent
//		final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE, -1);
//		
//		// we only expect String data though, but it is better to check if
//		// really string was sent
//		// later Amarino will support different data types, so far data comes
//		// always as string and
//		// you have to parse the data to the type you have sent from Arduino,
//		// like it is shown below
//		if (dataType == AmarinoIntent.STRING_EXTRA)
//		{
//			data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
//			
//			if (data != null)
//			{
//				// Integer.parseInt(data)
//				Toast.makeText(activity, data, Toast.LENGTH_LONG);
//			}
//		}
//	}
//}
