package org.oss.console.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshCommandExecutor extends AbstractCommandExecutor {

	private final JSch jsch = new JSch();
    private Session session;

    public SshCommandExecutor(String host, String user, String passwd) throws IOException {
    	try {
			this.session = jsch.getSession(user, host);
			session.setPassword(passwd);
			session.setConfig("StrictHostKeyChecking","no");
			session.connect();
		} catch (JSchException e) {
			if (session!=null) {
				session.disconnect();
			}
			throw new IOException("Error while connecting to host: " + host,e);
		}
    }
    
    public void disconnect() {
    	session.disconnect();
    }
    
	@Override
	InputStream executeCommand(String commandString) throws IOException {
		Channel channel=null;
		try {
			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(commandString);
			InputStream in = channel.getInputStream();
//			System.out.println("cmd: " + commandString);
			channel.connect();
			return new ByteArrayInputStream(readResponse(channel,in).getBytes());
		} catch (JSchException e) {
			throw new IOException("Error while executing command: " + commandString,e);
		} finally {
			if (channel != null) {
				channel.disconnect();
//				System.out.println("exit-status: " + channel.getExitStatus());
			}
		}
	}

	/**
	 * The inputstream might be at its end but when launching processes the channel does not close.
	 * Therefore a timeout counter controls when to stop reading from input.
	 * @param channel
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String readResponse(Channel channel, InputStream in)
			throws IOException {
		StringBuffer buf = new StringBuffer();
		byte[] tmp = new byte[1024];
		int timeout = 3;
		while (timeout>0) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				buf.append(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				if (in.available() > 0)
					continue;
				break;
			}
			try {
				Thread.sleep(1000);
				if (in.available()<=0) {
					timeout--;
				}
			} catch (Exception ee) {
			}
		}
		return buf.toString();
	}

}
