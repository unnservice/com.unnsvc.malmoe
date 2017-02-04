
package com.unnsvc.malmoe.frontend;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public class RequestResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private List<String> repositoryNames;

	public RequestResolver(List<String> repositoryNames) throws MalmoeException {

		if (repositoryNames.isEmpty()) {
			throw new MalmoeException("No repositories");
		}

		this.repositoryNames = repositoryNames;
	}

	public IResolvedRequest resolveRequest(String repoRelativePath) throws MalmoeException {

		String repositoryName = getRepositoryName(repoRelativePath);
		repoRelativePath = repoRelativePath.substring(repoRelativePath.indexOf(repositoryName) + repositoryName.length());

		System.err.println("in string: " + repoRelativePath);
		System.err.println("repositoryName is: " + repositoryName);

//		if (repoRelativePath.endsWith("/")) {
//
//			return new GenericRepositoryResolvedRequest(repositoryName, repoRelativePath);
//		} else {
//
//			String artifact = parts[parts.length - 1];
//			if (artifact.equals(RhenaConstants.MODULE_DESCRIPTOR_FILENAME)) {
//
//				repoRelativePath = repoRelativePath.substring(repoRelativePath.lastIndexOf(RhenaConstants.MODULE_DESCRIPTOR_FILENAME));
//				System.err.println("Model and: " + repoRelativePath);
//			} else if (parts[parts.length - 2].matches("^(main|test)$")) {
//
//				EExecutionType type = EExecutionType.valueOf(parts[parts.length - 2].toUpperCase());
//				System.err.println("Execution type: " + parts[parts.length - 2] + " artifact " + artifact);
//			}
//		}

		throw new UnsupportedOperationException("invalid repository path");
	}

	private String getRepositoryName(String repoRelativePath) throws MalmoeException {

		String[] parts = repoRelativePath.split("/");
		if (!repositoryNames.contains(parts[1])) {
			throw new MalmoeException("Invalid repository name: " + parts[1]);
		}
		return parts[1];
	}
}
