
package com.unnsvc.malmoe.mavenResolver;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.malmoe.common.IResolvedArtifactRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.requests.ArtifactRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.retrieval.ArtifactRetrievalResult;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class TestResolveArtifacts extends AbstractMavenResolverTest {

	@Test
	public void testResolveModel() throws Exception {

		MavenRemoteResolver resolver = createResolver();

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("org.hibernate:hibernate-entitymanager:5.2.6.Final");
		IResolvedArtifactRequest request = new ArtifactRepositoryResolvedRequest(null, null, identifier, EExecutionType.MAIN, "hibernate-entitymanager-5.2.6.Final.jar");
		IRetrievalResult result = resolver.serveRequest(request);
		Assert.assertEquals(ArtifactRetrievalResult.class, result.getClass());
		ArtifactRetrievalResult fileResult = (ArtifactRetrievalResult) result;
	}

}
