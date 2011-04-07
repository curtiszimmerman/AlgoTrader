/*
 * Util.java
 */
package com.ib.client;

import java.util.Vector;

public class Util {
	
	public static boolean StringIsEmpty(final String str) {
		return str == null || str.length() == 0;
	}
	
	public static String NormalizeString(final String str) {
		return str != null ? str : "";
	}
	
	public static int StringCompare(final String lhs, final String rhs) {
		return Util.NormalizeString(lhs).compareTo(Util.NormalizeString(rhs));
	}
	
	public static int StringCompareIgnCase(final String lhs, final String rhs) {
		return Util.NormalizeString(lhs).compareToIgnoreCase(
		        Util.NormalizeString(rhs));
	}
	
	public static boolean VectorEqualsUnordered(final Vector lhs,
	        final Vector rhs) {
		
		if (lhs == rhs) { return true; }
		
		final int lhsCount = lhs == null ? 0 : lhs.size();
		final int rhsCount = rhs == null ? 0 : rhs.size();
		
		if (lhsCount != rhsCount) { return false; }
		
		if (lhsCount == 0) { return true; }
		
		final boolean[] matchedRhsElems = new boolean[rhsCount];
		
		for (int lhsIdx = 0; lhsIdx < lhsCount; ++lhsIdx) {
			final Object lhsElem = lhs.get(lhsIdx);
			int rhsIdx = 0;
			for (; rhsIdx < rhsCount; ++rhsIdx) {
				if (matchedRhsElems[rhsIdx]) {
					continue;
				}
				if (lhsElem.equals(rhs.get(rhsIdx))) {
					matchedRhsElems[rhsIdx] = true;
					break;
				}
			}
			if (rhsIdx >= rhsCount) {
				// no matching elem found
				return false;
			}
		}
		
		return true;
	}
	
	public static String IntMaxString(final int value) {
		return value == Integer.MAX_VALUE ? "" : "" + value;
	}
	
	public static String DoubleMaxString(final double value) {
		return value == Double.MAX_VALUE ? "" : "" + value;
	}
	
}
