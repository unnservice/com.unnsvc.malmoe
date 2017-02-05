
package com.unnsvc.malmoe.frontend;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.malmoe.common.IResolvedRequest;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.repository.identity.AnonymousUser;
import com.unnsvc.malmoe.repository.requests.ArtifactRepositoryResolvedRequest;
import com.unnsvc.malmoe.repository.requests.GenericRepositoryResolvedRequest;
import com.unnsvc.malmoe.repository.requests.ModelRepositoryResolvedRequest;
import com.unnsvc.malmoe.repository.requests.RequestResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class TestRequestResolver {

	private RequestResolver resolver;
	private IResolvedRequest resolved;

	@Before
	public void before() throws MalmoeException {

		resolver = new RequestResolver(Collections.singletonList("repo1"), new AnonymousUser());
	}

	@Test
	public void testModel() throws RhenaException, MalmoeException {

		resolved = resolver.resolveRequest("/repo1/com/test/artifact/0.0.1/module.xml");
		Assert.assertTrue(resolved instanceof ModelRepositoryResolvedRequest);
	}
	
	@Test
	public void testArtifact() throws RhenaException, MalmoeException {
		
		resolved = resolver.resolveRequest("/repo1/com/test/artifact/0.0.1/main/artifacts.xml");
		Assert.assertTrue(resolved instanceof ArtifactRepositoryResolvedRequest);
	}
	
	@Test
	public void testOtherRepositoryRequest() throws RhenaException, MalmoeException {
		
		resolved = resolver.resolveRequest("/repo1/com/test/artifact");
		GenericRepositoryResolvedRequest a = (GenericRepositoryResolvedRequest) resolved;
		System.err.println("Resolved is " + resolved.getClass());
		Assert.assertTrue(resolved instanceof GenericRepositoryResolvedRequest);
	}
}
