package com.jslsolucoes.nginx.admin.agent.resource.impl;

import com.jslsolucoes.file.system.FileSystemBuilder;
import com.jslsolucoes.nginx.admin.agent.config.Configuration;
import com.jslsolucoes.nginx.admin.agent.resource.impl.info.NginxInfo;
import com.jslsolucoes.nginx.admin.agent.resource.impl.info.NginxInfoDiscover;
import com.jslsolucoes.nginx.admin.agent.resource.impl.os.OperationalSystem;
import com.jslsolucoes.nginx.admin.agent.resource.impl.os.OperationalSystemInfo;
import com.jslsolucoes.nginx.admin.agent.resource.impl.status.NginxStatus;
import com.jslsolucoes.nginx.admin.agent.resource.impl.status.NginxStatusDiscover;
import com.jslsolucoes.runtime.RuntimeBuilder;
import com.jslsolucoes.runtime.RuntimeResult;
import com.jslsolucoes.template.TemplateBuilder;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@RequestScoped
public class NginxAdminResourceImpl {

	private static Logger logger = LoggerFactory.getLogger(NginxAdminResourceImpl.class);

	private NginxInfoDiscover   nginxInfoDiscover;
	private NginxStatusDiscover nginxStatusDiscover;
	private Configuration       configuration;

	@Deprecated
	public NginxAdminResourceImpl() {

	}

	@Inject
	public NginxAdminResourceImpl(NginxInfoDiscover nginxInfoDiscover, NginxStatusDiscover nginxStatusDiscover,
								  Configuration configuration) {
		this.configuration = configuration;
		this.nginxInfoDiscover = nginxInfoDiscover;
		this.nginxStatusDiscover = nginxStatusDiscover;
	}

	public NginxOperationResult configure(Integer maxPostSize, Integer rootPort, Boolean gzip) {
		try {
			applyFs();
			applyTemplate(maxPostSize, rootPort, gzip);
			return new NginxOperationResult(NginxOperationResultType.SUCCESS);
		} catch (Exception exception) {
			return new NginxOperationResult(NginxOperationResultType.ERROR, exception);
		}
	}

	private void applyTemplate(Integer maxPostSize, Integer rootPort, Boolean gzip) {
		String settings = settings();
		try (FileWriter fileWriter = new FileWriter(new File(virtualHost(), "root.conf"))) {
			TemplateBuilder.newBuilder()
					.withClasspathTemplate("/template/nginx/dynamic", "root.tpl")
					.withData("settings", settings)
					.withData("rootPort", rootPort)
					.withOutput(fileWriter).process();
		} catch (IOException e) {
			logger.error("applyTemplate root.tpl failed:", e);
			throw new RuntimeException(e);
		}

		try (FileWriter fileWriter = new FileWriter(new File(settings, "nginx.conf"))) {
			TemplateBuilder.newBuilder()
					.withClasspathTemplate("/template/nginx/dynamic", "nginx.tpl").withData("settings", settings)
					.withData("gzip", gzip).withData("maxPostSize", maxPostSize)
					.withOutput(fileWriter).process();
		} catch (IOException e) {
			logger.error("applyTemplate nginx.tpl failed:", e);
			throw new RuntimeException(e);
		}
	}

	private File virtualHost() {
		return new File(settings(), "virtual-host");
	}

	private void applyFs() {
		String setting = settings();
		initFsPrivilege(setting);
		FileSystemBuilder.newBuilder().create().withDestination(setting).execute().end().copy()
				.withClasspathResource("/template/nginx/fixed").withDestination(setting).execute().end();
		logger.info("applyFs done");
	}

	private void initFsPrivilege(String settings) {
		Validate.notEmpty(settings, "settings not set");
		RuntimeResult execute = RuntimeBuilder.newBuilder().withCommand("sudo mkdir -p " + settings).execute();
		logger.info("mkdir, settings={}, success={}, result={}", settings, execute.isSuccess(), execute.getOutput());

		execute = RuntimeBuilder.newBuilder().withCommand("sudo chmod -R 755 " + settings).execute();
		logger.info("chmod, settings={}, success={}, result={}", settings, execute.isSuccess(), execute.getOutput());

		execute = RuntimeBuilder.newBuilder()
				.withCommand("sudo chown -R " + user() + ":" + user() + settings).execute();
		logger.info("chown, settings={}, success={}, result={}", settings, execute.isSuccess(), execute.getOutput());
	}

	private String user() {
		return configuration.getApplication().getUser();
	}

	private String settings() {
		return configuration.getNginx().getSetting();
	}

	public OperationalSystemInfo os() {
		return OperationalSystem.info();
	}

	public NginxInfo info() {
		return nginxInfoDiscover.info();
	}

	public NginxStatus status() {
		return nginxStatusDiscover.status();
	}

}
