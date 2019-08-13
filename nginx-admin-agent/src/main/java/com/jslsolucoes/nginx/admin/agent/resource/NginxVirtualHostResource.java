package com.jslsolucoes.nginx.admin.agent.resource;

import com.jslsolucoes.nginx.admin.agent.auth.AuthHandler;
import com.jslsolucoes.nginx.admin.agent.error.ErrorHandler;
import com.jslsolucoes.nginx.admin.agent.model.FileObject;
import com.jslsolucoes.nginx.admin.agent.model.request.virtual.host.NginxVirtualHostCreateRequest;
import com.jslsolucoes.nginx.admin.agent.model.request.virtual.host.NginxVirtualHostUpdateRequest;
import com.jslsolucoes.nginx.admin.agent.model.response.virtual.host.NginxVirtualHostCreateResponse;
import com.jslsolucoes.nginx.admin.agent.model.response.virtual.host.NginxVirtualHostDeleteResponse;
import com.jslsolucoes.nginx.admin.agent.model.response.virtual.host.NginxVirtualHostReadResponse;
import com.jslsolucoes.nginx.admin.agent.model.response.virtual.host.NginxVirtualHostUpdateResponse;
import com.jslsolucoes.nginx.admin.agent.resource.impl.NginxOperationResult;
import com.jslsolucoes.nginx.admin.agent.resource.impl.NginxVirtualHostResourceImpl;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;

@Path("virtualHost")
@ErrorHandler
@AuthHandler
@Produces(MediaType.APPLICATION_JSON)
public class NginxVirtualHostResource {

	private NginxVirtualHostResourceImpl nginxVirtualHostResourceImpl;

	@Deprecated
	public NginxVirtualHostResource() {

	}

	@Inject
	public NginxVirtualHostResource(NginxVirtualHostResourceImpl nginxVirtualHostResourceImpl) {
		this.nginxVirtualHostResourceImpl = nginxVirtualHostResourceImpl;
	}

	@POST
	public void create(NginxVirtualHostCreateRequest nginxVirtualHostCreateRequest,
					   @Suspended AsyncResponse asyncResponse, @Context UriInfo uriInfo) {
		NginxOperationResult nginxOperationResult = nginxVirtualHostResourceImpl.create(
				nginxVirtualHostCreateRequest.getUuid(),
				nginxVirtualHostCreateRequest.getHttps(),
				nginxVirtualHostCreateRequest.getQueueSize(),
				nginxVirtualHostCreateRequest.getCertificateUuid(),
				nginxVirtualHostCreateRequest.getCertificatePrivateKeyUuid(),
				nginxVirtualHostCreateRequest.getAliases(),
				nginxVirtualHostCreateRequest.getLocations());
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		uriBuilder.path(nginxVirtualHostCreateRequest.getUuid());
		asyncResponse.resume(Response.created(uriBuilder.build()).entity(
				new NginxVirtualHostCreateResponse(nginxOperationResult.getOutput(), nginxOperationResult.isSuccess()))
				.build());
	}

	@PUT
	@Path("{uuid}")
	public void update(@PathParam("uuid") String uuid, NginxVirtualHostUpdateRequest nginxVirtualHostUpdateRequest,
					   @Suspended AsyncResponse asyncResponse) {
		NginxOperationResult nginxOperationResult = nginxVirtualHostResourceImpl.update(uuid,
				nginxVirtualHostUpdateRequest.getHttps(),
				nginxVirtualHostUpdateRequest.getQueueSize(),
				nginxVirtualHostUpdateRequest.getCertificateUuid(),
				nginxVirtualHostUpdateRequest.getCertificatePrivateKeyUuid(),
				nginxVirtualHostUpdateRequest.getAliases(),
				nginxVirtualHostUpdateRequest.getLocations());
		asyncResponse.resume(Response.ok(
				new NginxVirtualHostUpdateResponse(nginxOperationResult.getOutput(), nginxOperationResult.isSuccess()))
				.build());
	}

	@DELETE
	@Path("{uuid}")
	public void delete(@PathParam("uuid") String uuid, @Suspended AsyncResponse asyncResponse) {
		NginxOperationResult nginxOperationResult = nginxVirtualHostResourceImpl.delete(uuid);
		asyncResponse.resume(Response.ok(
				new NginxVirtualHostDeleteResponse(nginxOperationResult.getOutput(), nginxOperationResult.isSuccess()))
				.build());
	}

	@GET
	@Path("{uuid}")
	public void read(@PathParam("uuid") String uuid, @Suspended AsyncResponse asyncResponse) {
		FileObject fileObject = nginxVirtualHostResourceImpl.read(uuid);
		asyncResponse.resume(Response.ok(new NginxVirtualHostReadResponse(fileObject)).build());
	}
}
