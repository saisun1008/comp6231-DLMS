package dlms.comp.udp.util;

import dlms.comp.common.protocol.UDPProtocol;

public interface UDPNotifierIF
{
public <T> void notifyMessage(UDPProtocol<Object> message);
}
