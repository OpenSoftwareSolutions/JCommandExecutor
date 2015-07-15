package org.oss.console.utils;

public interface ResultMatcher<T> {
	boolean match(T obj, String line);
}
