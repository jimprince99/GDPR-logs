package com.jimprince99.GRPDlogs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GdprLogs {

	private static Logger logger = null;
	private static FileHandler fileTxt;
	private static SimpleFormatter formatterTxt;
	private static String filename = null;
	private static String countryCode = "44";
	
	// Parser values
	Integer replacementCount = 0;
	private static String SEARCH_PREFIX_NATIONAL = "0";
	private static String PREFIX = "7700";


	public static void main(String[] args) {
		getLogger();
		getArgs(args);
		logger.info("File to process is " + filename);
		
		try {
		    processFile(filename);
		} catch (IOException e) {
			System.out.println("Unable to find file " + filename + " " + e.getMessage());
			logger.warning(e.getMessage());
			logger.log(Level.WARNING, "Unable to open source file " + filename, e);
			System.exit(-1);
		}
	}
	
	/**
	 * Read the file, one line at a time, and replace any MSISDNs with it's anonymous one 
	 * @param file
	 */
	public static void processFile(String file) throws IOException, IllegalArgumentException {
		if (isEmpty.test(file)) {
			throw new IllegalArgumentException("invalid filename ");
		}
		
		String outputFilename = outputFileName(file);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));

		Parser parser = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, countryCode);
		String line;
		while (((line = reader.readLine()) != null)) {
			String processesLine = parser.parse(line);
			//System.out.println("Writing " + processesLine);
			writer.write(processesLine + "\n");
		}
		
		reader.close();
		writer.close();
	}

	public static String outputFileName(String f) {
		return f + "REDACTED";
	}

	private static void getLogger() {
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		try {
			fileTxt = new FileHandler("gdpr-logs.log");
		} catch (SecurityException e) {
			System.err.println("SecurityException: Unable to open logger");
		} catch (IOException e) {
			System.err.println("IOException: Unable to open logger");
		}
		Handler[] handlers = logger.getHandlers();
		for (Handler handler : handlers) {
			logger.removeHandler(handler);
		}

		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);
	}
	
	private static void getArgs(String[] args) {
        logger.setLevel(Level.SEVERE);

		if (args.length > 2) {
			commandOptions();
		}
		for (String arg : args) {
			switch (arg) {
			case "-c":
		        countryCode = arg;
		        //System.out.println("countrycode = " + countryCode);
				break;
				
//			case "-vv":
//		        logger.setLevel(Level.INFO);
//				break;
//				
//			case "-p":
//		        partial = true;
//				break;
				
			default:
				filename = arg;
			}
		}
	}

	private static void commandOptions() {
		System.err.println("grdp : <filename>");
		System.err.println("       -c country code");
		System.exit(-1);
	}
	
	private static Predicate<String> isEmpty  = p -> {
		return (( p == null) || (p == ""));
	};

}
