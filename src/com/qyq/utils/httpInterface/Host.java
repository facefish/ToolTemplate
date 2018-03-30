package com.qyq.utils.httpInterface;

public class Host {

	
	private String host;
	  private int port = -1;

	  public String getHost() {
	    return this.host;
	  }

	  public void setHost(String host) {
	    this.host = host;
	  }

	  public int getPort() {
	    return this.port;
	  }

	  public void setPort(int port) {
	    this.port = port;
	  }

	  public Host(String host, int port)
	  {
	    this.host = host;
	    this.port = port;
	  }

	  public int hashCode()
	  {
	    
	    int result = 1;
	    result = 31 * result + ((this.host == null) ? 0 : this.host.hashCode());
	    result = 31 * result + this.port;
	    return result;
	  }

	  public boolean equals(Object obj)
	  {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (super.getClass() != obj.getClass())
	      return false;
	    Host other = (Host)obj;
	    if (this.host == null)
	    {
	      if (other.host != null)
	        return false;
	    }
	    else if (!this.host.equals(other.host)) {
	      return false;
	    }
	    return this.port == other.port;
	  }
}
