package com.jslsolucoes.nginx.admin.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import com.jslsolucoes.nginx.admin.agent.NginxAgentRunner;
import com.jslsolucoes.nginx.admin.agent.model.response.NginxResponse;
import com.jslsolucoes.nginx.admin.model.Configuration;
import com.jslsolucoes.nginx.admin.model.Nginx;
import com.jslsolucoes.nginx.admin.repository.ConfigurationRepository;
import com.jslsolucoes.nginx.admin.repository.impl.OperationResult;

import javax.inject.Inject;

@Controller
@Path("configuration")
public class ConfigurationController {

	private Result                  result;
	private ConfigurationRepository configurationRepository;
	private NginxAgentRunner        nginxAgentRunner;

	@Deprecated
	public ConfigurationController() {

	}

	@Inject
	public ConfigurationController(Result result, ConfigurationRepository configurationRepository,
								   NginxAgentRunner nginxAgentRunner) {
		this.result = result;
		this.configurationRepository = configurationRepository;
		this.nginxAgentRunner = nginxAgentRunner;
	}

	@Path("edit/{idNginx}")
	public void edit(Long idNginx) {
		this.result.include("configuration", configurationRepository.loadFor(new Nginx(idNginx)));
		this.result.include("nginx", new Nginx(idNginx));
	}

	@Post
	public void saveOrUpdate(Long id, Long idNginx, Integer gzip, Integer maxPostSize, Integer rootPort) {
		NginxResponse nginxResponse = nginxAgentRunner.configure(idNginx, (gzip != null && gzip == 1),
				maxPostSize, rootPort);
		if (nginxResponse.success()) {
			OperationResult operationResult = configurationRepository
					.saveOrUpdate(new Configuration(id, (gzip == null ? 0 : gzip),
							maxPostSize, rootPort, new Nginx(idNginx)));
			this.result.include("operation", operationResult.getOperationType());
		}
		this.result.include("nginxConfigureResponse", nginxResponse);
		this.result.redirectTo(this).edit(idNginx);
	}
}
