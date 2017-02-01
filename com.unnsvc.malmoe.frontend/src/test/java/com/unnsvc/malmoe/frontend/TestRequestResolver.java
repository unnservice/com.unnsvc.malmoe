
package com.unnsvc.malmoe.frontend;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.frontend.resolved.ArtifactRepositoryResolvedRequest;
import com.unnsvc.malmoe.frontend.resolved.GenericRepositoryResolvedRequest;
import com.unnsvc.malmoe.frontend.resolved.ModelRepositoryResolvedRequest;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class TestRequestResolver {

	private RequestResolver resolver;
	private IResolvedRequest resolved;

	@Before
	public void before() throws MalmoeException {

		resolver = new RequestResolver(Collections.singletonList("repo1"));
	}

	@Test
	public void testModel() throws RhenaException, MalmoeException {

		resolved = resolver.resolveRequest("/repo1/com/test/artifact/0.0.1/model.xml");
		Assert.assertTrue(resolved instanceof ModelRepositoryResolvedRequest);
	}
	
	@Test
	public void testArtifact() throws RhenaException, MalmoeException {
		
		resolved = resolver.resolveRequest("/repo1/com/test/artifact/0.0.1/main/executions.xml");
		Assert.assertTrue(resolved instanceof ArtifactRepositoryResolvedRequest);
	}
	
	@Test
	public void testOtherRepositoryRequest() throws RhenaException, MalmoeException {
		
		resolved = resolver.resolveRequest("/repo1/com/test/artifact");
		ArtifactRepositoryResolvedRequest a = (ArtifactRepositoryResolvedRequest) resolved;
		System.err.println("Resolved is " + resolved.getClass());
		Assert.assertTrue(resolved instanceof GenericRepositoryResolvedRequest);
	}
}
