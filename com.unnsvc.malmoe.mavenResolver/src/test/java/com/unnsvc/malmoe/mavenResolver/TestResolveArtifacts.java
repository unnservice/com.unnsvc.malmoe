
package com.unnsvc.malmoe.mavenResolver;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.ArtifactRetrievalRequest;
import com.unnsvc.malmoe.repository.retrieval.ArtifactRetrievalResult;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class TestResolveArtifacts extends AbstractMavenResolverTest {

	@Test
	public void testResolveModel() throws Exception {

		MavenRemoteResolver resolver = createResolver();

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("org.hibernate:hibernate-entitymanager:5.2.6.Final");
		IRetrievalResult result = resolver.serveRequest(new ArtifactRetrievalRequest(null, null, identifier, EExecutionType.MAIN, "hibernate-entitymanager-5.2.6.Final.jar"));
		Assert.assertEquals(ArtifactRetrievalResult.class, result.getClass());
		ArtifactRetrievalResult fileResult = (ArtifactRetrievalResult) result;
	}

}
