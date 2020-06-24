package de.traviadan.lib.helper;

public class SysProps {
	public SysProps() {
		
	}
	

	public static String getJavaVersion() {
		return System.getProperty("java.version");
	}

	public static String getJavaClassPath() {
		return System.getProperty("java.class.path");
	}

	public static String getJavaLibraryPath() {
		return System.getProperty("java.library.path");
	}

	public static String getJavaTempDir() {
		return System.getProperty("java.io.tmpdir");
	}

	public static String getOsName() {
		return System.getProperty("os.name");
	}

	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	public static String getPathSeparator() {
		return System.getProperty("path.separator");
	}

	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}

	public static String getUserDir() {
		return System.getProperty("user.dir");
	}

	public static String getComputerName() {
		return System.getenv("COMPUTERNAME");
	}
	
	public static String getHomeDrive() {
		return System.getenv("HOMEDRIVE");
	}

	public static String getHomePath() {
		return System.getenv("HOMEPATH");
	}

	public static String getOs() {
		return System.getenv("OS");
	}

	public static String getPath() {
		return System.getenv("PATH");
	}

	public static String getPathExt() {
		return System.getenv("PATHEXT");
	}

	public static String getSystemDrive() {
		return System.getenv("SYSTEMDRIVE");
	}

	public static String getTemp() {
		return System.getenv("TEMP");
	}

	public static String getUserDomain() {
		return System.getenv("USERDOMAIN");
	}

	public static String getUserName() {
		return System.getenv("USERNAME");
	}

	public static String getUserProfile() {
		return System.getenv("USERPROFILE");
	}

	public static String getWinDir() {
		return System.getenv("WINDIR");
	}
}
