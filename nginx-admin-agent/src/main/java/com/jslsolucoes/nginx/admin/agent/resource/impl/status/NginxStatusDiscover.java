package com.jslsolucoes.nginx.admin.agent.resource.impl.status;

import com.jslsolucoes.nginx.admin.agent.config.Configuration;
import com.jslsolucoes.nginx.admin.nginx.parser.FileContentReader;
import com.jslsolucoes.nginx.admin.nginx.parser.ServerParser;
import com.jslsolucoes.nginx.admin.nginx.parser.directive.Directive;
import com.jslsolucoes.nginx.admin.nginx.parser.directive.VirtualHostDirective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class NginxStatusDiscover {

	private static final String PATTERN = "([0-9]{1,})\\s([0-9]{1,})\\s([0-9]{1,})";
	private static       Logger logger  = LoggerFactory.getLogger(NginxStatusDiscover.class);

	private Configuration configuration;

	@Inject
	public NginxStatusDiscover(Configuration configuration) {
		this.configuration = configuration;
	}

	public NginxStatus status() {
		String response = response();
		NginxStatus nginxStatus = new NginxStatus();
		nginxStatus.setReading(reading(response));
		nginxStatus.setWriting(writing(response));
		nginxStatus.setActiveConnection(activeConnection(response));
		nginxStatus.setAccepts(accepts(response));
		nginxStatus.setWaiting(waiting(response));
		nginxStatus.setAccepts(accepts(response));
		nginxStatus.setHandled(handled(response));
		nginxStatus.setRequests(requests(response));
		return nginxStatus;
	}

	private String response() {
		Client client = ClientBuilder.newClient();
		try {
			Response response = client.target("http://localhost:" + port()).path("status").request().get();
			if (response.getStatusInfo().equals(Status.OK)) {
				return response.readEntity(String.class);
			} else {
				return "";
			}
		} catch (Exception e) {
			logger.warn("Could not read status from nginx", e);
			return "";
		} finally {
			client.close();
		}
	}

	private Integer accepts(String response) {
		Matcher accepts = Pattern.compile(PATTERN).matcher(response);
		if (accepts.find()) {
			return Integer.valueOf(accepts.group(1));
		}
		return 0;
	}

	private Integer handled(String response) {
		Matcher handled = Pattern.compile(PATTERN).matcher(response);
		if (handled.find()) {
			return Integer.valueOf(handled.group(2));
		}
		return 0;
	}

	private Integer requests(String response) {
		Matcher requests = Pattern.compile(PATTERN).matcher(response);
		if (requests.find()) {
			return Integer.valueOf(requests.group(3));
		}
		return 0;
	}

	private Integer activeConnection(String response) {
		Matcher activeConnection = Pattern.compile("Active connections:\\s([0-9]{1,})").matcher(response);
		if (activeConnection.find()) {
			return Integer.valueOf(activeConnection.group(1));
		}
		return 0;
	}

	private Integer reading(String response) {
		Matcher reading = Pattern.compile("Reading:\\s([0-9]{1,})").matcher(response);
		if (reading.find()) {
			return Integer.valueOf(reading.group(1));
		}
		return 0;
	}

	private Integer writing(String response) {
		Matcher writing = Pattern.compile("Writing:\\s([0-9]{1,})").matcher(response);
		if (writing.find()) {
			return Integer.valueOf(writing.group(1));
		}
		return 0;
	}

	private Integer waiting(String response) {
		Matcher waiting = Pattern.compile("Waiting:\\s([0-9]{1,})").matcher(response);
		if (waiting.find()) {
			return Integer.valueOf(waiting.group(1));
		}
		return 0;
	}

	/**
	 * listen port from root.conf
	 *
	 * @return listen port
	 */
	private Integer port() {
		String setting = configuration.getNginx().getSetting();
		String root = setting + "/virtual-host/root.conf";
		String content = FileContentReader.content(new File(root));
		ServerParser serverParser = new ServerParser(content);
		if (!serverParser.accepts()) {
			return 80;
		}
		List<Directive> directives = serverParser.parse();
		Directive directive = directives.stream()
				.filter(t -> t instanceof VirtualHostDirective)
				.findFirst()
				.orElse(null);
		if (directive == null) {
			return 80;
		}
		VirtualHostDirective virtualHostDirective = (VirtualHostDirective) directive;
		Integer port = virtualHostDirective.getPort();
		return port == null ? 80 : port;
	}

}
