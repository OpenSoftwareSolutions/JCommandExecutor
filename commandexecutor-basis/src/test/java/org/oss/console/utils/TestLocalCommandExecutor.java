package org.oss.console.utils;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class TestLocalCommandExecutor {
	@Test
	public void eclipseIsRunning() throws IOException {
		boolean exists = new LocalCommandExecutor().existsInResult(ProcessInfo.class, "ps -ef", new ContainsMatcher<ProcessInfo>("eclipse"));
		assertTrue("Expected eclipse running", exists);
	}
	@Test
	public void artsIsNotRunning() throws IOException {
		boolean exists = new LocalCommandExecutor().existsInResult(ProcessInfo.class,"ps -ef", new ContainsMatcher<ProcessInfo>("arts"));
		assertTrue("Expected eclipse running", !exists);
	}
	
	@Test
	public void listProcessesTyped() throws IOException {
		List<ProcessInfo> list = new LocalCommandExecutor().getFilteredResult(ProcessInfo.class, "ps -ef", new ContainsMatcher<ProcessInfo>("eclipse"),true);
		for (ProcessInfo pi : list) {
			
			System.out.println("ProcessID: " + pi.getPID() + ", ParentProcessId: " + pi.getPPID() + ", Command: " + pi.getCMD());
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
}
