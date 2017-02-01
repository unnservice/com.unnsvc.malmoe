
package com.unnsvc.malmoe.frontend.resolved;

public class GenericRepositoryResolvedRequest extends AbstractRepositoryResolvedRequest {

	private String repoRelativePath;

	public GenericRepositoryResolvedRequest(String repositoryId, String repoRelativePath) {

		super(repositoryId);
		this.repoRelativePath = repoRelativePath;
	}

	public String getRepoRelativePath() {

		return repoRelativePath;
	}
}
