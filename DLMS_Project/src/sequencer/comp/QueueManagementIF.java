package sequencer.comp;

import dlms.comp.common.protocol.UDPProtocol;

public interface QueueManagementIF
{
	public <Object> UDPProtocol<Object> tryToGetQueueHead();

	public void moveToSentList(UDPProtocol<Object> message);
}
