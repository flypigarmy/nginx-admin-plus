<%@include file="../app/taglibs.jsp"%>
<html:view title="{title}">

	<html:block>
		<html:alert state="success" label="{virtualHost.update.success}"
			rendered="${ operation == 'UPDATE' }"></html:alert>
		<html:alert state="danger" label="{virtualHost.update.failed}"
			rendered="${ operation == 'UPDATE_FAILED' }"></html:alert>
		<html:alert state="success" label="{virtualHost.insert.success}"
			rendered="${ operation == 'INSERT' }"></html:alert>
		<html:alert state="danger" label="{virtualHost.insert.failed}"
			rendered="${ operation == 'INSERT_FAILED' }"></html:alert>
	</html:block>

	<html:block>
		<html:form action="/virtualHost/saveOrUpdate"
			label="{virtualHost.form}" validation="/virtualHost/validate">
			<html:input name="id" type="hidden" value="${ virtualHost.id }"></html:input>
			<html:input name="idNginx" type="hidden" value="${ nginx.id }"></html:input>
			
			<html:formGroup label="{virtualHost.download}" rendered="${ virtualHost != null }">
					<html:div>
						<html:link target="_blank"
							url="/virtualHost/download/${ nginx.id }/${ virtualHost.resourceIdentifier.uuid }"
							label="{virtualHost.download.file}"></html:link>
					</html:div>
			</html:formGroup>
			
			<html:formGroup>
				<html:listGroup>
					<html:listGroupItem>
						<html:input name="https" checked="${ virtualHost.https }"
							value="1" type="checkbox"></html:input>
						 <fmt:message key="virtualHost.https"></fmt:message>
					</html:listGroupItem>
				</html:listGroup>
			</html:formGroup>

			<html:formGroup label="{ssl.common.name}" required="true"
				visible="${ virtualHost.https == 1  }">
				<html:select cssClass="ssl" required="true" name="idSslCertificate"
					value="${ virtualHost.sslCertificate.id }"
					data="${ sslCertificateList }" var="sslCertificate">
					<html:option value="${ sslCertificate.id }">${ sslCertificate.commonName }</html:option>
				</html:select>
			</html:formGroup>

            <html:formGroup label="{virtualHost.listenPort}" required="true">
                <html:input value="${ virtualHost.listenPort }" name="listenPort"
                    type="number" maxLength="5"
                    placeholder="{virtualHost.listenPort.placeholder}" required="true"></html:input>
            </html:formGroup>

            <html:formGroup label="{virtualHost.queueSize}">
                <html:input value="${ virtualHost.queueSize }" name="queueSize"
                    type="number" maxLength="10"
                    placeholder="{virtualHost.queueSize.placeholder}"></html:input>
            </html:formGroup>

			<html:formGroup>
				<html:detailTable atLeast="1" data="${ virtualHost.aliases  }"
					var="virtualHostAlias" label="{virtualHost.aliases}">
					<html:detailTableColumn label="{virtualHost.alias}" required="true">
						<html:input name="aliases[]" value="${ virtualHostAlias.alias }"
							placeholder="{virtualHost.alias.placeholder}" required="true"></html:input>
					</html:detailTableColumn>
				</html:detailTable>
			</html:formGroup>

			<html:formGroup>
				<html:detailTable atLeast="1" data="${ virtualHost.locations  }"
					var="virtualHostLocation" label="{virtualHost.locations}">
					<html:detailTableColumn label="{virtualHost.location}" required="true">
						<html:input name="locations[]"
							value="${ virtualHostLocation.path }"
							placeholder="{virtualHost.location.placeholder}" required="true"></html:input>
					</html:detailTableColumn>
					<html:detailTableColumn label="{virtualHost.queuePriority}">
						<html:input name="queuePriorities[]"
							value="${ virtualHostLocation.queuePriority }"
							type="number"
							placeholder="{virtualHost.queuePriority.placeholder}"></html:input>
					</html:detailTableColumn>
					<html:detailTableColumn label="{virtualHost.queueHandler}">
						<html:input name="queueHandlers[]"
							value="${ virtualHostLocation.queueHandler }"
							placeholder="{virtualHost.queueHandler.placeholder}"></html:input>
					</html:detailTableColumn>
					<html:detailTableColumn label="{upstream.name}" required="false">
						<html:select required="false" name="upstreams[]"
							value="${ virtualHostLocation.upstream.id }"
							data="${ upstreamList }" var="upstream">
							<html:option value="${ upstream.id }">${ upstream.name }</html:option>
						</html:select>
					</html:detailTableColumn>
					<html:detailTableColumn label="{virtualHost.additionalLines}" required="false">
                        <html:textarea name="additionalLineses[]"
                            value="${ !isEdit ? 'proxy_connect_timeout 3s;' : virtualHostLocation.additionalLines }"
                            placeholder="{virtualHost.additionalLines.placeholder}" required="false"></html:textarea>
					</html:detailTableColumn>
				</html:detailTable>
			</html:formGroup>

		</html:form>
	</html:block>

	<html:jsEvent attachTo="https" event="click">
		if($(this).is(':checked')){
			$('.ssl').parent().show();
		} else {
			$('.ssl').val('').parent().hide();
		}
	</html:jsEvent>

	<html:block align="center">
		<html:link url="/virtualHost/list/${ nginx.id }" label="{back}"></html:link>
	</html:block>
</html:view>
