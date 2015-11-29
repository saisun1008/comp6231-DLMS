package dlms.comp.common.protocol;

import java.io.Serializable;

public class ReplicaReplyContent<T> implements Serializable
{
	private static final long serialVersionUID = -7191281080559132917L;
	private T result = null;
	private String resultSender = null;

	public ReplicaReplyContent(T result, String sender)
	{
		this.result = result;
		resultSender = sender;
	}

	public T getResult()
	{
		return result;
	}

	public void setResult(T result)
	{
		this.result = result;
	}

	public String getResultSender()
	{
		return resultSender;
	}

	public void setResultSender(String resultSender)
	{
		this.resultSender = resultSender;
	}
}
