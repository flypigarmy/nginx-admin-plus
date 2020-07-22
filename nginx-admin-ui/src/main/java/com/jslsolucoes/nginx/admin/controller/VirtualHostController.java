package com.jslsolucoes.nginx.admin.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
import com.google.common.collect.Lists;
import com.jslsolucoes.i18n.Messages;
import com.jslsolucoes.nginx.admin.agent.NginxAgentRunner;
import com.jslsolucoes.nginx.admin.agent.model.Location;
import com.jslsolucoes.nginx.admin.agent.model.response.NginxResponse;
import com.jslsolucoes.nginx.admin.agent.model.response.virtual.host.NginxVirtualHostReadResponse;
import com.jslsolucoes.nginx.admin.error.NginxAdminRuntimeException;
import com.jslsolucoes.nginx.admin.model.*;
import com.jslsolucoes.nginx.admin.repository.ResourceIdentifierRepository;
import com.jslsolucoes.nginx.admin.repository.SslCertificateRepository;
import com.jslsolucoes.nginx.admin.repository.UpstreamRepository;
import com.jslsolucoes.nginx.admin.repository.VirtualHostRepository;
import com.jslsolucoes.nginx.admin.repository.impl.OperationResult;
import com.jslsolucoes.nginx.admin.repository.impl.OperationStatusType;
import com.jslsolucoes.tagria.lib.form.FormValidation;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
@Path("virtualHost")
public class VirtualHostController {

	private Result                       result;
	private VirtualHostRepository        virtualHostRepository;
	private UpstreamRepository           upstreamRepository;
	private SslCertificateRepository     sslCertificateRepository;
	private NginxAgentRunner             nginxAgentRunner;
	private ResourceIdentifierRepository resourceIdentifierRepository;

	@Deprecated
	public VirtualHostController() {

	}

	@Inject
	public VirtualHostController(Result result, VirtualHostRepository virtualHostRepository,
								 UpstreamRepository upstreamRepository,
								 SslCertificateRepository sslCertificateRepository,
								 NginxAgentRunner nginxAgentRunner,
								 ResourceIdentifierRepository resourceIdentifierRepository) {
		this.result = result;
		this.virtualHostRepository = virtualHostRepository;
		this.upstreamRepository = upstreamRepository;
		this.sslCertificateRepository = sslCertificateRepository;
		this.nginxAgentRunner = nginxAgentRunner;
		this.resourceIdentifierRepository = resourceIdentifierRepository;
	}

	@Path("list/{idNginx}")
	public void list(Long idNginx, boolean search, String term) {
		if (search) {
			this.result.include("virtualHostList", virtualHostRepository.searchFor(new Nginx(idNginx), term));
		} else {
			this.result.include("virtualHostList", virtualHostRepository.listAllFor(new Nginx(idNginx)));
		}
		this.result.include("nginx", new Nginx(idNginx));
	}

	@Path("form/{idNginx}")
	public void form(Long idNginx) {
		this.result.include("upstreamList", upstreamRepository.listAllFor(new Nginx(idNginx)));
		this.result.include("sslCertificateList", sslCertificateRepository.listAllFor(new Nginx(idNginx)));
		this.result.include("nginx", new Nginx(idNginx));
	}

	public void validate(Long id, Integer https, Integer listenPort, Long queueSize, String idResourceIdentifier,
						 Long idSslCertificate,
						 List<String> aliases, List<String> locations, List<Integer> queuePriorities,
						 List<String> queueHandlers, List<Long> upstreams, List<String> additionalLineses,
						 Long idNginx) {
		List<Location> locationList = locations(
				locations, queuePriorities, queueHandlers, upstreams, additionalLineses);
		this.result.use(Results.json())
				.from(FormValidation.newBuilder()
						.toUnordenedList(virtualHostRepository.validateBeforeSaveOrUpdate(new VirtualHost(id,
								https,
								listenPort,
								queueSize,
								new SslCertificate(idSslCertificate),
								new ResourceIdentifier(idResourceIdentifier),
								new Nginx(idNginx)), convert(aliases), convert(locationList, upstreams))), "errors")
				.serialize();
	}

	@Path("edit/{idNginx}/{id}")
	public void edit(Long idNginx, Long id) {
		this.result.include("isEdit", true);
		this.result.include("virtualHost", virtualHostRepository.load(new VirtualHost(id)));
		this.result.forwardTo(this).form(idNginx);
	}

	@Path("delete/{idNginx}/{id}")
	public void delete(Long idNginx, Long id) throws IOException {
		VirtualHost virtualHost = virtualHostRepository.load(new VirtualHost(id));
		NginxResponse nginxResponse = nginxAgentRunner.deleteVirtualHost(idNginx,
				virtualHost.getResourceIdentifier().getUuid());
		if (nginxResponse.success()) {
			this.result.include("operation", virtualHostRepository.delete(new VirtualHost(id)));
		} else {
			this.result.include("operation", OperationStatusType.DELETE_FAILED);
		}
		this.result.redirectTo(this).list(idNginx, false, null);
	}

	@Path("download/{idNginx}/{uuid}")
	public Download download(Long idNginx, String uuid) {
		NginxResponse nginxResponse = nginxAgentRunner.readVirtualHost(idNginx, uuid);
		if (nginxResponse.success()) {
			NginxVirtualHostReadResponse nginxVirtualHostReadResponse = (NginxVirtualHostReadResponse) nginxResponse;
			return new InputStreamDownload(new ByteArrayInputStream(nginxVirtualHostReadResponse.getFileObject()
					.getContent()
					.getBytes()),
					"application/octet-stream",
					uuid + ".conf",
					true,
					nginxVirtualHostReadResponse.getFileObject().getSize());
		} else {
			throw new NginxAdminRuntimeException(Messages.getString("virtualHost.download.failed"));
		}
	}

	@Post
	public void saveOrUpdate(Long id, Integer https, Integer listenPort, Long queueSize, Long idResourceIdentifier,
							 Long idSslCertificate, List<String> aliases, List<String> locations,
							 List<Integer> queuePriorities, List<String> queueHandlers, List<Long> upstreams,
							 List<String> additionalLineses, Long idNginx) {

		SslCertificate sslCertificate = null;
		if (https == null) {
			https = 0;
		}
		if (https == 1) {
			sslCertificate = sslCertificateRepository.load(new SslCertificate(idSslCertificate));
		}
		List<Location> locationList = locations(
				locations, queuePriorities, queueHandlers, upstreams, additionalLineses);
		if (id == null) {
			ResourceIdentifier resourceIdentifier = resourceIdentifierRepository.create();
			NginxResponse nginxResponse = nginxAgentRunner.createVirtualHost(idNginx,
					resourceIdentifier.getUuid(),
					aliases,
					sslCertificate != null ? sslCertificate.getResourceIdentifierCertificate().getUuid() : null,
					https == 1,
					listenPort,
					queueSize,
					sslCertificate != null ? sslCertificate.getResourceIdentifierCertificatePrivateKey()
							.getUuid() : null,
					locationList);
			if (nginxResponse.success()) {
				OperationResult operationResult = virtualHostRepository.saveOrUpdate(new VirtualHost(id,
						https,
						listenPort,
						queueSize,
						sslCertificate,
						resourceIdentifier,
						new Nginx(idNginx)), convert(aliases), convert(locationList, upstreams));
				this.result.include("operation", operationResult.getOperationType());
				this.result.redirectTo(this).edit(idNginx, operationResult.getId());
			} else {
				this.result.include("operation", OperationStatusType.INSERT_FAILED);
				this.result.redirectTo(this).form(idNginx);
			}
		} else {
			VirtualHost virtualHost = virtualHostRepository.load(new VirtualHost(id));
			NginxResponse nginxResponse = nginxAgentRunner.updateVirtualHost(idNginx,
					virtualHost.getResourceIdentifier().getUuid(),
					aliases,
					sslCertificate != null ? sslCertificate.getResourceIdentifierCertificate().getUuid() : null,
					https == 1,
					listenPort,
					queueSize,
					sslCertificate != null ? sslCertificate.getResourceIdentifierCertificatePrivateKey()
							.getUuid() : null,
					locationList);
			if (nginxResponse.success()) {
				OperationResult operationResult = virtualHostRepository.saveOrUpdate(new VirtualHost(id,
						https,
						listenPort,
						queueSize,
						sslCertificate,
						virtualHost.getResourceIdentifier(),
						new Nginx(idNginx)), convert(aliases), convert(locationList, upstreams));
				this.result.include("operation", operationResult.getOperationType());
				this.result.redirectTo(this).edit(idNginx, operationResult.getId());
			} else {
				this.result.include("operation", OperationStatusType.UPDATE_FAILED);
				this.result.redirectTo(this).form(idNginx);
			}
		}
	}

	private List<Location> locations(List<String> locations, List<Integer> queuePriorities, List<String> queueHandlers,
									 List<Long> upstreams, List<String> additionalLineses) {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		return locations.stream().map(location -> {
			int pos = atomicInteger.getAndIncrement();
			String additionalLines = additionalLineses.get(pos);
			if (additionalLines != null) {
				additionalLines = additionalLines.replaceAll("\r\n", "\n");
			}
			return new Location(location,
					queuePriorities.get(pos),
					queueHandlers.get(pos),
					upstreamName(new Upstream(upstreams.get(pos))),
					additionalLines);
		}).collect(Collectors.toList());
	}

	private String upstreamName(Upstream upstream) {
		return upstream.getId() == null ? null : upstreamRepository.load(upstream).getName();
	}

	private List<VirtualHostLocation> convert(List<Location> locations, List<Long> upstreams) {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		return Lists.transform(locations,
				location -> new VirtualHostLocation(location.getPath(),
						location.getQueuePriority(),
						location.getQueueHandler(),
						location.getAdditionalLines(),
						upstreamFromId(upstreams.get(atomicInteger.getAndIncrement()))));
	}

	private List<VirtualHostAlias> convert(List<String> aliases) {
		return Lists.transform(aliases, VirtualHostAlias::new);
	}

	private Upstream upstreamFromId(Long id) {
		return id == null ? null : new Upstream(id);
	}
}
