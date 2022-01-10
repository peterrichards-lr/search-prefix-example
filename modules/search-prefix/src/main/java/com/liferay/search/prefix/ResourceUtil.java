package com.liferay.search.prefix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Liferay
 */
public class ResourceUtil {

	public static String readResouceAsString(String filename)
		throws UnsupportedEncodingException {

		InputStream inputStream = null;
		BufferedReader bufferedReader = null;

		try {
			ClassLoader classLoader = ResourceUtil.class.getClassLoader();

			inputStream = classLoader.getResourceAsStream(filename);

			InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, StandardCharsets.UTF_8.name());

			bufferedReader = new BufferedReader(inputStreamReader);

			Stream<String> stream = bufferedReader.lines();

			return stream.collect(Collectors.joining(System.lineSeparator()));
		}
		catch (UnsupportedEncodingException uee) {
			throw uee;
		}
		finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
			catch (IOException ioe) {
			}

			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch (IOException ioe) {
			}
		}
	}

}