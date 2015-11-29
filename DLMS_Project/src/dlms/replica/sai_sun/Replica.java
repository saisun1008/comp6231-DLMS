package dlms.replica.sai_sun;

import java.util.ArrayList;

import dlms.comp.common.MulticastReceiver;
import dlms.comp.common.protocol.UDPProtocol;
import dlms.comp.udp.util.UDPNotifierIF;

public class Replica implements UDPNotifierIF
{
	private ArrayList<BankServer> m_serverList;
	private MulticastReceiver multicastReceiver = null;
	private Thread multicastReceiverThread;

	public static void main(String[] args)
	{
		Replica replica = new Replica();
		replica.startReplica();
	}

	public Replica()
	{
		m_serverList = new ArrayList<BankServer>();
		for (int i = 0; i < ReplicaConfiguration.BANK_NAME_POOL.length; i++)
		{
			addServer(ReplicaConfiguration.PORT_POOL[i], ReplicaConfiguration.BANK_NAME_POOL[i]);
		}
		multicastReceiver = new MulticastReceiver(this);
		multicastReceiverThread = new Thread(multicastReceiver);
	}

	public void startReplica()
	{
		multicastReceiverThread.setName("Replica Thread");
		multicastReceiverThread.start();
	}

	/**
	 * Create one bank server and added it to the server list
	 * 
	 * @param udpPort
	 *            udp listening port of the bank server
	 * @param string
	 *            name of the bank server
	 */
	private void addServer(int udpPort, String string)
	{
		BankServer server = new BankServer(udpPort, string);
		m_serverList.add(server);
	}

	/**
	 * get bank server object from server list by its name
	 * 
	 * @param name
	 *            name of the bank server0
	 * @return bank server object
	 */
	private BankServer getServerByName(String name)
	{
		for (BankServer s : m_serverList)
		{
			if (s.getBankName().equalsIgnoreCase(name))
			{
				return s;
			}
		}
		return null;
	}

	public String openAccount(String bank, String firstName, String lastName, String emailAddress,
			String phoneNumber, String password)
	{
		// return empty string if the bank name can't be found in the server
		// list
		return getServerByName(bank) == null ? "" : getServerByName(bank).openAccount(bank,
				firstName, lastName, emailAddress, phoneNumber, password);
	}

	public String getLoan(String bank, String accountNumber, String password, double loanAmount)
	{
		// return empty string if the bank name can't be found in the server
		// list
		return getServerByName(bank) == null ? "" : getServerByName(bank).getLoan(bank,
				accountNumber, password, loanAmount);
	}

	public boolean delayPayment(String bank, String loanID, String currentDueDate, String newDueDate)
	{
		// return false if the bank name can't be found in the server list
		return getServerByName(bank) == null ? false : getServerByName(bank).delayPayment(bank,
				loanID, currentDueDate, newDueDate);
	}

	public String printCustomerInfo(String bank)
	{
		// return empty string if the bank name can't be found in the server
		// list
		return getServerByName(bank) == null ? "" : getServerByName(bank).printCustomerInfo(bank);
	}

	public String transferLoan(String LoanID, String CurrentBank, String OtherBank)
	{
		// return empty string if the bank name can't be found in the server
		// list
		return getServerByName(CurrentBank) == null ? "" : getServerByName(CurrentBank)
				.transferLoan(LoanID, CurrentBank, OtherBank);
	}

	@Override
	public void notifyMessage(UDPProtocol message)
	{
		switch (message.getClientRequest().getRequestType())
		{
		case OPEN_ACCOUNT:
			openAccount(message.getClientRequest().getCurrentBank(), message.getClientRequest()
					.getFirstName(), message.getClientRequest().getLastNmae(), message
					.getClientRequest().getEmail(), message.getClientRequest().getPhoneNum(),
					message.getClientRequest().getPassWord());
			// TODO:send reply back to FE
			break;

		case GET_LOAN:
			getLoan(message.getClientRequest().getCurrentBank(), Integer.toString(message
					.getClientRequest().getAccountId()), message.getClientRequest().getPassWord(),
					message.getClientRequest().getLoanAmount());
			// TODO:send reply back to FE
			break;
		case DELAY_LOAN:
			delayPayment(message.getClientRequest().getCurrentBank(), Integer.toString(message
					.getClientRequest().getLoanId()), message.getClientRequest()
					.getCurrentDueDate(), message.getClientRequest().getNewDueDate());
			// TODO:send reply back to FE
			break;
		case PRINT_INFO:
			printCustomerInfo(message.getClientRequest().getCurrentBank());
			// TODO:send reply back to FE
			break;
		case TRANSFER_LOAN:
			transferLoan(Integer.toString(message.getClientRequest().getLoanId()), message
					.getClientRequest().getCurrentBank(), message.getClientRequest().getOtherBank());
			break;
		default:
			System.err.println("Operation not supported");
			break;
		}
	}
}
