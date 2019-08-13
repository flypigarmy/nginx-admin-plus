package com.jslsolucoes.nginx.admin.agent.resource.impl;

import com.jslsolucoes.template.TemplateBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;

public class NginxVirtualHostResourceImplTest {

	@Test
	public void verifyTemplateProcess() {
		StringWriter stringWriter = new StringWriter();
		TemplateBuilder.newBuilder()
				.withTemplate("${ listenPort?c },${ queueSize?c }")
				.withData("listenPort", 1234)
				.withData("queueSize", 100000L)
				.withOutput(stringWriter)
				.process();
		String processed = stringWriter.toString();
		Assert.assertEquals("1234,100000", processed);
	}
}
