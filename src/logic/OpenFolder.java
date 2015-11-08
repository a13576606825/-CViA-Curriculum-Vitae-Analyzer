package logic;

import java.io.IOException;

public class OpenFolder {
	private static String OS = System.getProperty("os.name").toLowerCase();

	private static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	private static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}
	
	public void openFolder(String path) {
		String command = new String();
		if (isWindows()) {
			command = "explorer.exe ";
		} else if (isMac()) {
			command = "open ";
		} else {
			return;
		}
		try {
			Runtime.getRuntime().exec(command + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
