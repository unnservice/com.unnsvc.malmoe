
package com.unnsvc.malmoe.common;

public class MalmoeUtils {

	public static final String getIndents(int nr) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nr; i++) {
			sb.append("   ");
		}
		return sb.toString();
	}
}
