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

	public static void openFolder(File f) {
		String command;
		if (isWindows()) {
			command = "rundll32 SHELL32.DLL,ShellExec_RunDLL " + "Explorer.exe /select," + f.getAbsolutePath();
		} else if (isMac()) {
			command = new String("open -R "+  f.getAbsolutePath());
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
	
	/*
	public static void main(String[] args){
		File f = new File("E:\\opencv\\opencv\\README.md.txt");
		openFolder(f);
	}
	//*/
	
	/* 
	public static void main(String[] args) {
		openFolder(new File("/Users/JJ/Desktop/CS4211\\ Final\\ Presentation.key"));
	}
	//*/
}


