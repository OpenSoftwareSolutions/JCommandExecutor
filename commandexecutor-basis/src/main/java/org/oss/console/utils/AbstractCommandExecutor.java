package org.oss.console.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommandExecutor {
	
	public <T> boolean existsInResult(Class<T> clazz,String commandString, ResultMatcher<T> matcher) throws IOException {
		String line;
		boolean patternFound = false;
		BufferedReader reader = new BufferedReader(new InputStreamReader(executeCommand(commandString)));
		//Parsing the input stream.
		while ((line = reader.readLine()) != null) {
			T resultBean = TypedResultBean.newInstance(clazz,line.trim().split("\\s++"),true);
			if (matcher.match(resultBean,line)) {
		        patternFound = true;
		        break;				
			}
		}	
		return patternFound;
	}
	
	
	public <T> List<T> getAllResults(Class<T> clazz, String commandString, boolean appendOverlapingValuesTolastField) throws IOException {
		return getFilteredResult(clazz,commandString, new ResultMatcher<T>() {

			@Override
			public boolean match(T obj, String line) {
				return true;
			}}, appendOverlapingValuesTolastField);
	}
	
	public <T> List<T> getFilteredResult(Class<T> clazz, String commandString, ResultMatcher<T> matcher, boolean appendOverlapingValuesTolastField) throws IOException {
		List<T> result = new ArrayList<T>();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(executeCommand(commandString)));
		//Parsing the input stream.
		while ((line = reader.readLine()) != null) {
			T resultBean = TypedResultBean.newInstance(clazz,line.trim().split("\\s++"),appendOverlapingValuesTolastField);
			if (matcher.match(resultBean,line)) {
				result.add(resultBean);
			}
		}	
		return result;
	}
	
	public String execute(String commandString) throws IOException {
		String line;
		StringBuffer buf = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(executeCommand(commandString)));
		while ((line = reader.readLine()) != null) {
			buf.append(line);
		}
		return buf.toString();
	}
	
	abstract InputStream executeCommand(String commandString) throws IOException;
}
