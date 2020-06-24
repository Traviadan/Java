package de.traviadan.lib.helper;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.Queue;

public class Log extends Thread {

	public enum Level {
		Debug, Info, Warning, Failure
	}
	public enum Out {
		System, File, Db
	}
	private Writer writer;

	private Queue<String> logs = new LinkedList<>();
	private String fName;
	public Log.Level lvl = Log.Level.Failure;
	public Log.Out out = Log.Out.System;
	
	public Log() {
		this("Log.log");
	}
	
	public Log(String fileName) {
		fName = fileName;
	}
	
	public void run() {
		do {
			synchronized (this.logs) {
				try {
					logs.wait();
				} catch (InterruptedException e) {
					interrupt();
				}
				printLog();
			}
		} while(!isInterrupted());
		msg("Log-Thread beendet");
		printLog();
		try {
			closeLogfile();
		} catch (IOException e) {
			System.out.println("I/O Exception beim schliessen des Logfiles: " + e.getMessage());
		}
	}
	
	private void printLog() {
		while(logs.peek() != null) {
			switch(out) {
			case File:
				try {
					getPrintWriter().write(logs.poll());
					getPrintWriter().write(System.lineSeparator());
				} catch (IOException e) {
					out = Log.Out.System;
					msg("I/O Exception beim schreiben ins Logfile: " + e.getMessage(), Log.Level.Failure);
				}
				break;
			case Db:
				break;
			default:
				System.out.println(logs.poll());
			}
		}
		try {
			getPrintWriter().flush();
		} catch (IOException e) {
			out = Log.Out.System;
			msg("I/O Exception beim flushen: " + e.getMessage(), Log.Level.Failure);
		}
	}

	private void closeLogfile() throws IOException {
		if(writer != null) {
			writer.close();
		}
	}
	private Writer getPrintWriter() throws IOException {
		if (writer == null) {
			writer = Files.newBufferedWriter( Paths.get( System.getProperty("user.dir"), fName ), StandardCharsets.UTF_8,
					StandardOpenOption.CREATE,
					StandardOpenOption.APPEND);
		}
		return writer;
	}
	
	public void msg(String msg) {
		this.msg(msg, Log.Level.Debug);
	}
	public void msg(String msg, Log.Level level) {
		if (level.compareTo(lvl) >= 0) {
			logs.add(msg);
			synchronized (this.logs) {
				this.logs.notify();
			}
		}
	}
}
