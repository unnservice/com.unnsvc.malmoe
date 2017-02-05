
package com.unnsvc.malmoe.common.requests;

import com.unnsvc.malmoe.common.IUser;

public class GenericRepositoryResolvedRequest extends AbstractRepositoryResolvedRequest {

	private String repoRelativePath;

	public GenericRepositoryResolvedRequest(IUser user, String repositoryId, String repoRelativePath) {

		super(user, repositoryId);
		this.repoRelativePath = repoRelativePath;
	}

	public String getRepoRelativePath() {

		return repoRelativePath;
	}
}
