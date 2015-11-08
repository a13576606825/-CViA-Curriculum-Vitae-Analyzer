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
	
	private static final String[] prefix = {
	      "osascript",
	      "-e", "tell application \"Finder\"",
	      "-e", "activate",
	      "-e", "<openCmdGoesHere>",
	      "-e", "end tell"
	  };
	 
	  private static String buildFolderCommand(String folderPath) {
	    StringBuilder openCommand = new StringBuilder("open ");
	    String[] pathParts = folderPath.split("/");
	    for (int i = pathParts.length - 1; i > 0; i--) {
	      openCommand.append("folder \"").append(pathParts[i]).append("\" of ");
	    }
	    return openCommand.append("startup disk").toString();
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
//		openFolder( new File("/Users/JJ/Desktop/File Interchange").getAbsolutePath());
//		prefix[6] = buildFolderCommand("/Users/JJ/Desktop/CS4211 Final Presentation.key/");
//		try {
//			Runtime.getRuntime().exec(prefix).waitFor();
//		} catch (InterruptedException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		openFolder("/Users/JJ/Desktop/CS4211\\ Final\\ Presentation.key");
	}
}


