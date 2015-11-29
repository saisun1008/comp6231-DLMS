package dlms.comp.common;

public class Configuration
{
	public enum requestType
	{
		OPEN_ACCOUNT, GET_LOAN, TRANSFER_LOAN, PRINT_INFO, DELAY_LOAN;
	}
	
	public final static String MULTI_CAST_INET_ADDR = "224.0.0.3";
	public final static int MULTI_CAST_INET_PORT = 8888;
}
