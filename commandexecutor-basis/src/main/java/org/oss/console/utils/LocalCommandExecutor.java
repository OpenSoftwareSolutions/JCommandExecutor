package org.oss.console.utils;

import java.io.IOException;
import java.io.InputStream;

public class LocalCommandExecutor extends AbstractCommandExecutor {

	@Override
	InputStream executeCommand(String commandString) throws IOException {
		Process proc = Runtime.getRuntime().exec(commandString);
		return proc.getInputStream();
	}

}
