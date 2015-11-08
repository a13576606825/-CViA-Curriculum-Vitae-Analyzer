package logic;

import interpreter.Utils;

import java.io.File;
import java.io.IOException;

public class OpenFolder {
	private static String OS = System.getProperty("os.name").toLowerCase();

	private static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	private static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	
	public static void openFolder(String path) {
		String command;
		if (isWindows()) {
			command = new String("explorer.exe " +path);
		} else if (isMac()) {
			command = new String("open -R "+  path);
			Utils.debug(command);
		} else {
			return;
		}
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	public static void main(String[] args) {

		openFolder("/Users/JJ/Desktop/CS4211\\ Final\\ Presentation.key");
	}
}


