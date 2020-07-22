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
import com.jslsolucoes.nginx.admin.agent.model.Endpoint;
import com.jslsolucoes.nginx.admin.agent.model.response.NginxResponse;
import com.jslsolucoes.nginx.admin.agent.model.response.upstream.NginxUpstreamReadResponse;
import com.jslsolucoes.nginx.admin.error.NginxAdminRuntimeException;
import com.jslsolucoes.nginx.admin.model.*;
import com.jslsolucoes.nginx.admin.repository.ResourceIdentifierRepository;
import com.jslsolucoes.nginx.admin.repository.ServerRepository;
import com.jslsolucoes.nginx.admin.repository.StrategyRepository;
import com.jslsolucoes.nginx.admin.repository.UpstreamRepository;
import com.jslsolucoes.nginx.admin.repository.impl.OperationResult;
import com.jslsolucoes.nginx.admin.repository.impl.OperationStatusType;
import com.jslsolucoes.tagria.lib.form.FormValidation;
import org.apache.commons.collections.CollectionUtils;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
@Path("upstream")
public class UpstreamController {

    private Result result;
    private UpstreamRepository upstreamRepository;
    private ServerRepository serverRepository;
    private StrategyRepository strategyRepository;
    private NginxAgentRunner nginxAgentRunner;
    private ResourceIdentifierRepository resourceIdentifierRepository;

    @Deprecated
    public UpstreamController() {

    }

    @Inject
    public UpstreamController(Result result, UpstreamRepository upstreamRepository, ServerRepository serverRepository,
                              StrategyRepository strategyRepository, NginxAgentRunner nginxAgentRunner,
                              ResourceIdentifierRepository resourceIdentifierRepository) {
        this.result = result;
        this.upstreamRepository = upstreamRepository;
        this.serverRepository = serverRepository;
        this.strategyRepository = strategyRepository;
        this.nginxAgentRunner = nginxAgentRunner;
        this.resourceIdentifierRepository = resourceIdentifierRepository;
    }

    @Path("list/{idNginx}")
    public void list(Long idNginx) {
        this.result.include("upstreamList", upstreamRepository.listAllFor(new Nginx(idNginx)));
        this.result.include("nginx", new Nginx(idNginx));
    }

    @Path("form/{idNginx}")
    public void form(Long idNginx) {
        this.result.include("serverList", serverRepository.listAllFor(new Nginx(idNginx)));
        this.result.include("strategyList", strategyRepository.listAll());
        this.result.include("nginx", new Nginx(idNginx));
    }

    @Path("download/{idNginx}/{uuid}")
    public Download download(Long idNginx, String uuid) {
        NginxResponse nginxResponse = nginxAgentRunner.readUpstream(idNginx, uuid);
        if (nginxResponse.success()) {
            NginxUpstreamReadResponse nginxUpstreamReadResponse = (NginxUpstreamReadResponse) nginxResponse;
            return new InputStreamDownload(
                    new ByteArrayInputStream(nginxUpstreamReadResponse.getFileObject().getContent().getBytes()),
                    "application/octet-stream", uuid + ".conf", true,
                    nginxUpstreamReadResponse.getFileObject().getSize());
        } else {
            throw new NginxAdminRuntimeException(Messages.getString("upstream.download.failed"));
        }
    }

    public void validate(Long id, String name, String additionalLines, Long idStrategy, List<Long> servers,
                         List<Integer> ports,
                         List<String> additionalOptions,
                         Long idResourceIdentifier, Long idNginx) {
        this.result.use(Results.json())
                .from(FormValidation.newBuilder()
                                .toUnordenedList(upstreamRepository.validateBeforeSaveOrUpdate(
                                        new Upstream(id, name, additionalLines, new Strategy(idStrategy),
                                                new ResourceIdentifier(idResourceIdentifier), new Nginx(idNginx)),
                                        convert(servers, ports, additionalOptions))),
                        "errors")
                .serialize();
    }

    @Path("edit/{idNginx}/{id}")
    public void edit(Long idNginx, Long id) {
        this.result.include("isEdit", true);
        this.result.include("upstream", upstreamRepository.load(new Upstream(id)));
        this.result.forwardTo(this).form(idNginx);
    }

    @Path("delete/{idNginx}/{id}")
    public void delete(Long idNginx, Long id) {
        Upstream upstream = upstreamRepository.load(new Upstream(id));
        NginxResponse nginxResponse = nginxAgentRunner.deleteUpstream(idNginx,
                upstream.getResourceIdentifier().getUuid());
        if (nginxResponse.success()) {
            this.result.include("operation", upstreamRepository.delete(new Upstream(id)));
        } else {
            this.result.include("operation", OperationStatusType.DELETE_FAILED);
        }
        this.result.redirectTo(this).list(idNginx);
    }

    @Post
    public void saveOrUpdate(Long id, String name, String additionalLines, Long idStrategy, List<Long> servers,
                             List<Integer> ports, List<String> additionalOptions, Long idNginx) {
        if (additionalLines != null) {
            additionalLines = additionalLines.replaceAll("\r\n", "\n");
        }

        if (id == null) {
            ResourceIdentifier resourceIdentifier = resourceIdentifierRepository.create();
            NginxResponse nginxResponse = nginxAgentRunner.createUpstream(
                    idNginx, resourceIdentifier.getUuid(), name, additionalLines,
                    strategy(new Strategy(idStrategy)), endpoints(servers, ports, additionalOptions));
            if (nginxResponse.success()) {
                OperationResult operationResult = upstreamRepository.saveOrUpdate(
                        new Upstream(id, name, additionalLines, new Strategy(idStrategy), resourceIdentifier,
                                new Nginx(idNginx)), convert(servers, ports, additionalOptions));
                this.result.include("operation", operationResult.getOperationType());
                this.result.redirectTo(this).edit(idNginx, operationResult.getId());
            } else {
                this.result.include("operation", OperationStatusType.INSERT_FAILED);
                this.result.redirectTo(this).form(idNginx);
            }
        } else {
            Upstream upstream = upstreamRepository.load(new Upstream(id));
            NginxResponse nginxResponse = nginxAgentRunner.updateUpstream(
                    idNginx, upstream.getResourceIdentifier().getUuid(), name, additionalLines,
                    strategy(new Strategy(idStrategy)), endpoints(servers, ports, additionalOptions));
            if (nginxResponse.success()) {
                OperationResult operationResult = upstreamRepository.saveOrUpdate(new Upstream(id,
                                name, additionalLines, new Strategy(idStrategy), upstream.getResourceIdentifier(),
                                new Nginx(idNginx)),
                        convert(servers, ports, additionalOptions));
                this.result.include("operation", operationResult.getOperationType());
                this.result.redirectTo(this).edit(idNginx, operationResult.getId());
            } else {
                this.result.include("operation", OperationStatusType.UPDATE_FAILED);
                this.result.redirectTo(this).form(idNginx);
            }
        }

    }

    private List<Endpoint> endpoints(List<Long> servers, List<Integer> ports, List<String> additionalOptions) {
        if (CollectionUtils.isEmpty(servers)) {
            return Collections.emptyList();
        }
        AtomicInteger atomicInteger = new AtomicInteger(0);
        AtomicInteger atomicInteger2 = new AtomicInteger(0);
        return servers.stream()
                .map(idServer -> new Endpoint(server(new Server(idServer)),
                        ports.get(atomicInteger.getAndIncrement()),
                        additionalOptions.get(atomicInteger2.getAndIncrement())))
                .collect(Collectors.toList());
    }

    private String server(Server server) {
        return serverRepository.load(server).getIp();
    }

    private List<UpstreamServer> convert(List<Long> servers, List<Integer> ports, List<String> additionalOptions) {
        if (CollectionUtils.isEmpty(servers)) {
            return Collections.emptyList();
        }
        AtomicInteger atomicInteger = new AtomicInteger(0);
        AtomicInteger atomicInteger2 = new AtomicInteger(0);
        return Lists.transform(servers,
                server -> new UpstreamServer(new Server(server),
                        ports.get(atomicInteger.getAndIncrement()),
                        additionalOptions.get(atomicInteger2.getAndIncrement())));
    }

    private String strategy(Strategy strategy) {
        return strategyRepository.load(strategy).getDirective();
    }

}
