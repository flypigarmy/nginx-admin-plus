package com.jslsolucoes.nginx.admin.nginx.parser;

import com.jslsolucoes.nginx.admin.nginx.parser.directive.Directive;
import com.jslsolucoes.nginx.admin.nginx.parser.directive.VirtualHostDirective;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ServerParserTest {

	@Test
	public void parse_root_listen_port() {
		ServerParser serverParser = new ServerParser(TestFileLoader.content(
				"/data/template/nginx/test_root.conf"));

		List<Directive> directives = serverParser.parse();
		VirtualHostDirective virtualHostDirective = (VirtualHostDirective) directives.stream()
				.filter(directive -> directive instanceof VirtualHostDirective)
				.findFirst().get();

		Assert.assertEquals(8080, virtualHostDirective.getPort().intValue());
	}

	@Test
	public void accepts_root_expectTrue() {
		ServerParser serverParser = new ServerParser(TestFileLoader.content(
				"/data/template/nginx/test_root.conf"));

		Boolean accepts = serverParser.accepts();

		Assert.assertTrue(accepts);
	}
}
