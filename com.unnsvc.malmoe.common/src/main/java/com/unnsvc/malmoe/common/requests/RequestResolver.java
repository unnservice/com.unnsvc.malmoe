
package com.unnsvc.malmoe.common.requests;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.malmoe.common.IResolvedRequest;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.common.exceptions.NotFoundMalmoeException;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.Identifier;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.identity.Version;

public class RequestResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private List<String> repositoryNames;
	private IUser user;

	public RequestResolver(List<String> repositoryNames, IUser user) throws MalmoeException {

		if (repositoryNames.isEmpty()) {
			throw new MalmoeException("No repositories");
		}

		this.repositoryNames = repositoryNames;
		this.user = user;
	}

	public IResolvedRequest resolveRequest(String repoRelativePath) throws MalmoeException, RhenaException {

		String repositoryName = getRepositoryName(repoRelativePath);
		repoRelativePath = repoRelativePath.substring(repoRelativePath.indexOf(repositoryName) + repositoryName.length());

		int nrParts = repoRelativePath.split("/").length;

		IResolvedRequest resolved = null;
		if (repoRelativePath.endsWith("/")) {

			resolved = new GenericRepositoryResolvedRequest(user, repositoryName, repoRelativePath);
		} else if (repoRelativePath.endsWith(RhenaConstants.MODULE_DESCRIPTOR_FILENAME) && nrParts >= 4) {

			resolved = resolveModuleRequest(repositoryName, repoRelativePath);
		} else {

			try {
				resolved = resolveArtifactRequest(repositoryName, repoRelativePath);
			} catch (Throwable t) {
				resolved = new GenericRepositoryResolvedRequest(user, repositoryName, repoRelativePath);
			}
		}

		return resolved;
	}

	private IResolvedRequest resolveArtifactRequest(String repositoryName, String repoRelativePath) throws RhenaException {

		String[] parts = repoRelativePath.split("/");

		String artifactName = parts[parts.length - 1];
		repoRelativePath = repoRelativePath.substring(0, repoRelativePath.lastIndexOf(artifactName) - 1);

		String executionTypeStr = parts[parts.length - 2];
		EExecutionType type = EExecutionType.valueOf(executionTypeStr.toUpperCase());
		repoRelativePath = repoRelativePath.substring(0, repoRelativePath.lastIndexOf(executionTypeStr));

		ModuleIdentifier identifier = toModuleIdentifier(repoRelativePath);

		return new ArtifactRepositoryResolvedRequest(user, repositoryName, identifier, type, artifactName);
	}

	private IResolvedRequest resolveModuleRequest(String repositoryName, String repoRelativePath) throws RhenaException {

		repoRelativePath = repoRelativePath.substring(0, repoRelativePath.lastIndexOf(RhenaConstants.MODULE_DESCRIPTOR_FILENAME));
		// repoRelativePath starts and ends with /
		ModuleIdentifier identifier = toModuleIdentifier(repoRelativePath);
		return new ModelRepositoryResolvedRequest(user, repositoryName, identifier);
	}

	private ModuleIdentifier toModuleIdentifier(String repoRelativePath) throws RhenaException {

		repoRelativePath = repoRelativePath.substring(1, repoRelativePath.length() - 1);

		String[] parts = repoRelativePath.split("/");
		Version version = Version.valueOf(parts[parts.length - 1]);
		Identifier moduleName = Identifier.valueOf(parts[parts.length - 2]);

		String componentNamePathStr = repoRelativePath.substring(0, repoRelativePath.lastIndexOf(moduleName.toString()) - 1);
		Identifier componentName = Identifier.valueOf(componentNamePathStr.replace("/", "."));

		ModuleIdentifier moduleIdentifier = new ModuleIdentifier(componentName, moduleName, version);
		return moduleIdentifier;
	}

	private String getRepositoryName(String repoRelativePath) throws NotFoundMalmoeException {

		String[] parts = repoRelativePath.split("/");
		if (!repositoryNames.contains(parts[1])) {
			throw new NotFoundMalmoeException("Invalid repository name: " + parts[1]);
		}
		return parts[1];
	}
}
