package dlms.comp.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

import sequencer.comp.Multicaster;
import dlms.comp.common.protocol.UDPProtocol;
import dlms.comp.udp.util.UDPNotifierIF;

public class MulticastReceiver implements Runnable
{

	private int listeningPort = 0;
	private String multicastGroupIp = null;
	private UDPNotifierIF notifyIf = null;
	private HashMap<Integer,UDPProtocol<Object>> receivedList = null;

	public MulticastReceiver(String multicastIp, int port, UDPNotifierIF interf)
	{
		multicastGroupIp = Configuration.MULTI_CAST_INET_ADDR;
		listeningPort = Configuration.MULTI_CAST_INET_PORT;
		notifyIf= interf;
		receivedList= new HashMap<Integer, UDPProtocol<Object>>();
	}

	@Override
	public void run()
	{
		MulticastSocket clientSocket;
		try
		{
			clientSocket = new MulticastSocket(listeningPort);
			clientSocket.joinGroup(InetAddress.getByName(multicastGroupIp));

			while (true)
			{
				byte[] receiveData = new byte[1024];
				// Receive the information and print it.
				DatagramPacket msgPacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(msgPacket);

				processIncomingPacket(msgPacket);
			}
		} catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Process incoming UDP packet, and convert it to a loanProtocol object
	 * 
	 * @param <T>
	 * 
	 * @param receivePacket
	 * @return
	 * @throws ClassNotFoundException
	 */
	private <Object> UDPProtocol<Object> processIncomingPacket(DatagramPacket receivePacket)
			throws ClassNotFoundException
	{
		byte[] data = receivePacket.getData();
		UDPProtocol<Object> protocol = null;
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try
		{
			ObjectInputStream is = new ObjectInputStream(in);

			protocol = (UDPProtocol<Object>) is.readObject();
			//ok, let's check if this message is received before
			if(receivedList.containsKey(protocol.getSequencerHeader().getUUID()))
			{
				//if it's already received, then we do nothing
			}
			else
			{
				//if it's first time receiving this message, put it in the list
				receivedList.put(protocol.getSequencerHeader().getUUID(), protocol);
				//and multicast it to the group
				Multicaster.multiCastMessage(protocol);
			}
			in.close();
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return protocol;
	}

}
