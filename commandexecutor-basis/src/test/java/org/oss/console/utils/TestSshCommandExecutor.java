package org.oss.console.utils;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class TestSshCommandExecutor {
	
	@Test
	public void eclipseIsNotRunning() throws IOException {
		SshCommandExecutor execuor = getExecutor();
		boolean exists = execuor.existsInResult(ProcessInfo.class, "ps -ef", new ContainsMatcher<ProcessInfo>("eclipse"));
		execuor.disconnect();
		assertTrue("Expected eclipse running", !exists);
	}
	@Test
	public void artsIsRunning() throws IOException {
		SshCommandExecutor execuor = getExecutor();
		boolean exists = execuor.existsInResult(ProcessInfo.class,"ps -ef", new ContainsMatcher<ProcessInfo>("arts"));
		execuor.disconnect();
		assertTrue("Expected eclipse running", exists);
	}
	
	@Test
	public void listProcesses() throws IOException {
		SshCommandExecutor execuor = getExecutor();
		List<ProcessInfo> list = execuor.getFilteredResult(ProcessInfo.class, "ps -ef", new CmdEndsWithMatcher("arts","nightly"),true);
		execuor.disconnect();
		for (ProcessInfo pi : list) {			
			System.out.println("ProcessID: " + pi.getPID() + ", ParentProcessId: " + pi.getPPID() + ", Command: " + pi.getCMD());
		}
	}
	
	@Test
	public void listDirectory() throws IOException {
		SshCommandExecutor execuor = getExecutor();
		List<DirInfo> list = execuor.getFilteredResult(DirInfo.class, "ls -l installations/nightly/Instances/",new NotStartsWith<DirInfo>("total"),false);
		execuor.disconnect();
		for (DirInfo pi : list) {			
			System.out.println("Name: " + pi.getName() );
		}
	}
	
	private SshCommandExecutor getExecutor() throws IOException {
		return new SshCommandExecutor("172.16.130.25","testuser","x");
	}
	
	private static interface ProcessInfo {
		@SuppressWarnings("unused")
		String FIELD_ORDER = "UID,PID,PPID,C,STIME,TTY,TIME,CMD";
		String getUID();
		String getPID();
		String getPPID();
		String getC();
		String getSTIME();
		String getTTY();
		String getTIME();
		String getCMD();
	}
	
	private static interface DirInfo {
		@SuppressWarnings("unused")
		String FIELD_ORDER = "Access,Xxx,User,Group,Size,Month,Day,TimeYear,Name";
		String getAccess();
		String getXxxD();
		String getUser();
		String getGroup();
		String getSize();
		String getMonth();
		String getDay();
		String getTimeYear();
		String getName();
	}
	
	private static class CmdEndsWithMatcher implements ResultMatcher<ProcessInfo> {
		private final String filter, cmdend;
		public CmdEndsWithMatcher(String filter, String cmdend) {
			this.filter = filter;
			this.cmdend = cmdend;
		}
		@Override
		public boolean match(ProcessInfo obj, String line) {
			return line.contains(filter) && obj.getCMD().endsWith(cmdend);
		}
	}
	
	private static class ContainsMatcher<T> implements ResultMatcher<T> {
		private final String string;
		public ContainsMatcher(String string) {
			this.string = string;
		}
		@Override
		public boolean match(T obj, String line) {
			return line.contains(string);
		}
	}
	
	private static class NotStartsWith<T> implements ResultMatcher<T> {
		private final String string;
		public NotStartsWith(String string) {
			this.string = string;
		}
		@Override
		public boolean match(T obj, String line) {
			return !line.startsWith(string);
		}
	}
	
}
