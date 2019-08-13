package com.jslsolucoes.nginx.admin.nginx.parser;

import com.jslsolucoes.nginx.admin.nginx.parser.directive.Directive;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxConfParser {

	private Logger logger = LoggerFactory.getLogger(NginxConfParser.class);

	private String conf;

	private NginxConfParser() {

	}

	public static NginxConfParser newBuilder() {
		return new NginxConfParser();
	}

	public NginxConfParser withConfigurationFile(String conf) {
		this.conf = conf;
		return this;
	}

	public List<Directive> parse() {
		List<Directive> directives = new ArrayList<>();
		Matcher includes = Pattern.compile("include (.*)/(.*);").matcher(content(new File(conf)));
		while (includes.find()) {
			String directory = includes.group(1).trim();
			File include = new File(directory);
			int includeFilesCount = 0;
			int directivesCount = 0;
			if (include.exists()) {
				String pattern = includes.group(2).trim().replaceAll("\\*", "\\.\\*");
				for (File file : FileUtils.listFiles(include, new RegexFileFilter(pattern), TrueFileFilter.TRUE)) {
					for (Parser parser : parsers(content(file))) {
						if (parser.accepts()) {
							includeFilesCount++;
							List<Directive> directiveList = parser.parse();
							directives.addAll(directiveList);
							directivesCount += directiveList.size();
						}
					}
				}
			}
			logger.info("parse from include, directory={}, includeFilesCount={}, directivesCount={}",
					directory,
					includeFilesCount,
					directivesCount);
		}
		return directives;
	}

	private List<Parser> parsers(String fileContent) {
		return Arrays.asList(new UpstreamParser(fileContent), new ServerParser(fileContent));
	}

	/**
	 * remove comment lines and DOS2UNIX
	 */
	private String content(File file) {
		try {
			return FileUtils.readFileToString(file, "UTF-8")
					.replaceAll("\\#(.*)", "")
					.replaceAll("\r\n", "\n");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
