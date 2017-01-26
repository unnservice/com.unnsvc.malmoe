
package com.unnsvc.malmoe.repository;

import java.io.File;

import com.unnsvc.malmoe.common.IAccess;
import com.unnsvc.malmoe.common.IAccessManager;
import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeRepository;
import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.repository.config.VirtualRepositoryConfig;
import com.unnsvc.malmoe.repository.retrieval.ArtifactRetrievalRequest;
import com.unnsvc.malmoe.repository.retrieval.ArtifactRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.ExecutionsRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.ModelRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.NotFoundRetrievalResult;
import com.unnsvc.malmoe.resolver.ERequestType;
import com.unnsvc.malmoe.resolver.IRemoteResolver;
import com.unnsvc.rhena.common.RhenaConstants;

/**
 * Location
 * 
 * @author noname
 *
 */
public class VirtualRepository implements IMalmoeRepository {

	private File resolverLocation;
	private IAccessManager accessManager;
	private IRemoteResolver resolver;

	public VirtualRepository(VirtualRepositoryConfig virtualConfig, File resolverLocation, IIdentityManager identityManager, IRemoteResolver resolver) {

		this.resolverLocation = resolverLocation;
		this.accessManager = new AccessManager(virtualConfig.getAccessConfig(), identityManager);
		this.resolver = resolver;
	}

	@Override
	public IRetrievalResult serveRequest(IRetrievalRequest request) throws MalmoeException {

		return accessManager.withPermissions(request, new IAccess<IRetrievalResult>() {

			public IRetrievalResult execute() throws MalmoeException {

				IRetrievalResult result = resolveLocal(request);

				if (result instanceof NotFoundRetrievalResult) {
					if (resolver != null) {
						return resolver.serveRequest(request);
					}
				}

				return result;
			}

		}, IMalmoeRepository.ACCESS_REPOSITORY_READ);
	}

	private IRetrievalResult resolveLocal(IRetrievalRequest request) throws MalmoeException {

		File groupLocation = new File(resolverLocation, request.getIdentifier().getComponentName().toString().replace(".", File.separator));
		File moduleNameLocation = new File(groupLocation, request.getIdentifier().getModuleName().toString());
		File moduleLocation = new File(moduleNameLocation, request.getIdentifier().getVersion().toString());

		if (!moduleLocation.isDirectory()) {
			return new NotFoundRetrievalResult(request);
		}

		try {

			if (request.getType().equals(ERequestType.MODEL)) {

				File modelFile = new File(moduleLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
				if (modelFile.exists()) {
					return new ModelRetrievalResult(modelFile);
				}
			} else if (request.getType().equals(ERequestType.EXECUTIONS)) {

				File executionsFile = new File(moduleLocation, RhenaConstants.EXECUTION_DESCRIPTOR_FILENAME);
				if (executionsFile.exists()) {
					return new ExecutionsRetrievalResult(executionsFile);
				}
			} else if (request instanceof ArtifactRetrievalRequest) {

				ArtifactRetrievalRequest artifactRequest = (ArtifactRetrievalRequest) request;
				File executionTypeLocation = new File(moduleLocation, artifactRequest.getExecutionType().literal());
				File artifactFile = new File(executionTypeLocation, artifactRequest.getArtifactName());
				if (artifactFile.exists()) {
					return new ArtifactRetrievalResult(artifactFile);
				}
			}

		} catch (Exception ex) {
			throw new MalmoeException("Exception while resolving " + request.getIdentifier().toString(), ex);
		}

		return new NotFoundRetrievalResult(request);
	}
}
