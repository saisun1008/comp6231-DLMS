package sequencer.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;

import sequencer.comp.QueueManagementIF;
import sequencer.comp.TaskExecutor;
import dlms.comp.common.protocol.ReplicaReplyContent;
import dlms.comp.common.protocol.SequencerHeader;
import dlms.comp.common.protocol.UDPProtocol;
import dlms.comp.udp.util.UDPListener;
import dlms.comp.udp.util.UDPNotifierIF;
import dlms.comp.udp.util.UDPSender;

public class Sequencer<T> implements UDPNotifierIF, Runnable, QueueManagementIF
{
	private UDPListener feMessageReceiver = null;
	private Queue<UDPProtocol<Object>> fifoQueue = new LinkedList<UDPProtocol<Object>>();
	private long uniqueIdBase = 0;
	private static int messageCounter = 0;
	private HashMap<Integer,UDPProtocol<Object>> sentList = null;
	private TaskExecutor taskExecutor = null;

	public Sequencer()
	{
		feMessageReceiver = new UDPListener(9999, this);
		uniqueIdBase = Calendar.getInstance().getTimeInMillis();
		sentList = new HashMap<Integer, UDPProtocol<Object>>();
		taskExecutor = new TaskExecutor(this);
	}
	
	public void startSequencer()
	{
		feMessageReceiver.startListening();
		taskExecutor.startExecutor();
	}

	public static void main(String[] args)
	{
		Sequencer sequencer = new Sequencer<>();
		Thread t = new Thread(sequencer);
		t.start();
		UDPProtocol<String[]> msg = new UDPProtocol<String[]>();
		msg.setReplicaReply(new ReplicaReplyContent<String[]>(new String[]
		{
				"hehe", "haha"
		}, "1"));
		try
		{
			UDPSender.sendUDPPacket("localhost", 9999, msg);
			Thread.sleep(24214214);
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public <T> void notifyMessage(UDPProtocol<Object> message)
	{
		SequencerHeader header = new SequencerHeader((int) uniqueIdBase + messageCounter);
		messageCounter++;
		message.setSequencerHeader(header);
		fifoQueue.add(message);
		String[] result = (String[]) message.getReplicaReply().getResult();
		System.out.println(result[0]);
	}

	@Override
	public void run()
	{
		while (true)
		{

		}

	}

	@Override
	public UDPProtocol<Object> tryToGetQueueHead()
	{
		return fifoQueue.poll();
	}

	@Override
	public void moveToSentList(UDPProtocol<Object> message)
	{
		sentList.put(message.getSequencerHeader().getUUID(), message);
	}

}
