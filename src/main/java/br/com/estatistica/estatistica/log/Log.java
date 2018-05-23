package br.com.estatistica.estatistica.log;

import org.apache.log4j.Logger;

public class Log {

	final static Logger logger = Logger.getLogger(Log.class);
	
	public static void logErrorWriter(String error) {
		logger.error(error);
	}

	public static void logInfoWriter(String info) {
		logger.info(info);
	}

	public static void logWarnWriter(String warn) {
		logger.warn(warn);
	}

	public static void logFatalWriter(String fatal) {
		logger.fatal(fatal);
	}
}
