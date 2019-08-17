package com.jslsolucoes.nginx.admin.nginx.parser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * @author yizhou.xw
 * @date 2019-08-17
 **/
public class TestFileLoader {

	public static String content(String path) {
		try {
			return IOUtils.toString(TestFileLoader.class.getResourceAsStream(path), "UTF-8");
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
