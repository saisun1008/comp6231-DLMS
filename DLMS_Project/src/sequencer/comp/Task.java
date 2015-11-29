package sequencer.comp;

import java.util.TimerTask;

import dlms.comp.common.Configuration;
import dlms.comp.common.protocol.UDPProtocol;

public class Task extends TimerTask
{
	private QueueManagementIF managementIF;

	public Task(QueueManagementIF interf)
	{
		managementIF = interf;
	}

	@Override
	public void run()
	{
		completeTask();
	}

	private void completeTask()
	{
		UDPProtocol<Object> message = managementIF.tryToGetQueueHead();
		if (message != null)
		{
			Multicaster.multiCastMessage(message);
			managementIF.moveToSentList(message);
		}
	}
}
