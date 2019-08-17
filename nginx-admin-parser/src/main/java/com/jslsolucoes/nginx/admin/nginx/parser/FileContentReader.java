package com.jslsolucoes.nginx.admin.nginx.parser;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author yizhou.xw
 * @date 2019-08-17
 **/
public class FileContentReader {

	/**
	 * remove comment lines and DOS2UNIX
	 */
	public static String content(File file) {
		try {
			return FileUtils.readFileToString(file, "UTF-8")
					.replaceAll("\\#(.*)", "")
					.replaceAll("\r\n", "\n");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

