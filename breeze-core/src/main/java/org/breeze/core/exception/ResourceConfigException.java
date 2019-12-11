package org.breeze.core.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 配置文件资源读取异常
 * @author 黑面阿呆
 *
 */
public class ResourceConfigException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 4744229594154195201L;
	Throwable nested = null;

	/**
	 * Creates new <code>DBException</code> without detail message.
	 */
	public ResourceConfigException() {
		super();
	}

	/**
	 * Constructs an <code>DBException</code> with the specified detail message.
	 *
	 * @param msg
	 *            the detail message.
	 */
	public ResourceConfigException(String msg) {
		super(msg);
	}

	/**
	 * Constructs an <code>DBException</code> with the specified detail message
	 * and nested Exception.
	 *
	 * @param msg
	 *            the detail message.
	 */
	public ResourceConfigException(String msg, Throwable nested) {
		super(msg);
		this.nested = nested;
	}

	/**
	 * Constructs an <code>DBException</code> with the specified detail message
	 * and nested Exception.
	 *
	 * @param nested
	 *            the detail message.
	 */
	public ResourceConfigException(Throwable nested) {
		super();
		this.nested = nested;
	}

	/**
	 * Returns the detail message, including the message from the nested
	 * exception if there is one.
	 */
	public String getMessage() {
		if (nested != null) {
			if (super.getMessage() == null) {
				return nested.getMessage();
			} else {
				return super.getMessage() + " (" + nested.getMessage() + ")";
			}
		} else {
			return super.getMessage();
		}
	}

	/**
	 * Returns the detail message, NOT including the message from the nested
	 * exception.
	 */
	public String getNonNestedMessage() {
		return super.getMessage();
	}

	/** Returns the nested exception if there is one, null if there is not. */
	public Throwable getNested() {
		if (nested == null) {
			return this;
		}
		return nested;
	}

	/** Prints the composite message to System.err. */
	public void printStackTrace() {
		super.printStackTrace();
		if (nested != null) {
			nested.printStackTrace();
		}
	}

	/**
	 * Prints the composite message and the embedded stack trace to the
	 * specified stream ps.
	 */
	public void printStackTrace(PrintStream ps) {
		super.printStackTrace(ps);
		if (nested != null) {
			nested.printStackTrace(ps);
		}
	}

	/**
	 * Prints the composite message and the embedded stack trace to the
	 * specified print writer pw.
	 */
	public void printStackTrace(PrintWriter pw) {
		super.printStackTrace(pw);
		if (nested != null) {
			nested.printStackTrace(pw);
		}
	}
}
