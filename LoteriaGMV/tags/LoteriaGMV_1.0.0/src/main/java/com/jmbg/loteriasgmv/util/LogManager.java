package com.jmbg.loteriasgmv.util;

import android.util.Log;

public class LogManager {

	private static String TAG = Constantes.TAG;

	private static int level = Log.INFO;

	private String className;

	protected Boolean debugEnabled = true;

	/**
	 * 
	 * @param className
	 * @return
	 */
	public static LogManager getLogger(String className) {
		LogManager oldGloLogMan = new LogManager();
		oldGloLogMan.setClassName(className);

		return oldGloLogMan;
	}

	/**
	 * 
	 * @param mClass
	 * @return
	 */
	public static LogManager getLogger(Class<?> mClass) {
		return getLogger(mClass.getSimpleName());
	}

	/**
	 * 
	 * @param tag
	 */
	public static void setTag(String tag) {
		LogManager.TAG = tag;
	}

	public static String getTag() {
		return LogManager.TAG;
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void verbose(String msg) {
		if (!debugEnabled) {
			return;
		}

		if (level < Log.VERBOSE) {
			return;
		}

		Log.v(TAG, formatMsg(msg));
	}

	/**
	 * 
	 * @param msg
	 */
	public void debug(String msg) {
		debug(msg, null);
	}

	/**
	 * 
	 * @param msg
	 * @param tr
	 */
	public void debug(String msg, Throwable tr) {
		if (!debugEnabled) {
			return;
		}

		if (level > Log.DEBUG) {
			return;
		}

		if (tr == null) {
			Log.d(TAG, formatMsg(msg));
		} else {
			Log.d(TAG, formatMsg(msg), tr);
		}
	}

	/**
	 * 
	 * @param msg
	 */
	public void info(String msg) {
		if (!debugEnabled) {
			return;
		}

		if (level > Log.INFO) {
			return;
		}

		Log.i(TAG, formatMsg(msg));
	}

	/**
	 * 
	 * @param msg
	 */
	public void warn(String msg) {
		if (!debugEnabled) {
			return;
		}

		if (level > Log.WARN) {
			return;
		}

		Log.w(TAG, formatMsg(msg));
	}

	/**
	 * 
	 * @param msg
	 */
	public void error(String msg) {
		this.error(msg, null);
	}

	/**
	 * 
	 * @param msg
	 * @param tr
	 */
	public void error(String msg, Throwable tr) {
		if (!debugEnabled) {
			return;
		}

		if (level > Log.ERROR) {
			return;
		}

		if (tr == null) {
			Log.e(TAG, formatMsg(msg));
		} else {
			Log.e(TAG, formatMsg(msg), tr);
		}
	}

	/**
	 * 
	 * @param msg
	 * @return
	 */
	private String formatMsg(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append(className).append(": ").append(msg);

		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 
	 * @return
	 */
	public static int getLevel() {
		return level;
	}

	/**
	 * 
	 * @param level
	 */
	public static void setLevel(int level) {
		LogManager.level = level;
	}

	public Boolean getDebugEnabled() {
		return debugEnabled;
	}

	public void setDebugEnabled(Boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

}
