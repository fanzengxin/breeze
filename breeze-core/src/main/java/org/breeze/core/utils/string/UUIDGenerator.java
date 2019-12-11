package org.breeze.core.utils.string;

import java.util.UUID;


/**
 * UUID生成类
 *
 * @author 黑面阿呆
 *
 */
public class UUIDGenerator {

	public static String getUUID() {
		return JavaUUID();
	}

	public static String JavaUUID() {
		return UUID.randomUUID().toString();
	}
}
