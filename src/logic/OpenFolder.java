package logic;

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
	
	public void openFolder(File f) {
		String command = new String();
		if (isWindows()) {
			command = "rundll32 SHELL32.DLL,ShellExec_RunDLL " + "Explorer.exe /select," + f.getAbsolutePath();
		} else if (isMac()) {
			command = "open ";
		} else {
			return;
		}
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args){
//		try {
//			File f = new File("E:\\opencv\\opencv\\README.md.txt");
//			//Runtime.getRuntime().exec("explorer.exe " +  f.getParentFile().getAbsolutePath());
//			Runtime.getRuntime().exec(
//                    "rundll32 SHELL32.DLL,ShellExec_RunDLL " +
//                    "Explorer.exe /select," + f.getAbsolutePath());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
