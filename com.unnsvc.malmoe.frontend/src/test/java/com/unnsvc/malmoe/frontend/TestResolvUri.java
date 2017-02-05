
package com.unnsvc.malmoe.frontend;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.malmoe.common.IResolvedRequest;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.common.requests.ArtifactRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.requests.ModelRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.requests.RequestResolver;
import com.unnsvc.malmoe.repository.identity.AnonymousUser;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class TestResolvUri {

	private RequestResolver resolver;
	private IResolvedRequest resolved;

	@Before
	public void before() throws MalmoeException {

		resolver = new RequestResolver(Collections.singletonList("repo1"), new AnonymousUser());
	}

	@Test
	public void testModel() throws RhenaException, MalmoeException {

		resolved = resolver.resolveRequest("/repo1/com/test/com.test.artifact/0.0.1/" + RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
		Assert.assertTrue(resolved instanceof ModelRepositoryResolvedRequest);
	}
	
	@Test
	public void testArtifact() throws RhenaException, MalmoeException {

		resolved = resolver.resolveRequest("/repo1/com/test/com.test.artifact/0.0.1/test/artifact.jar");
		Assert.assertTrue(resolved instanceof ArtifactRepositoryResolvedRequest);
	}
}
