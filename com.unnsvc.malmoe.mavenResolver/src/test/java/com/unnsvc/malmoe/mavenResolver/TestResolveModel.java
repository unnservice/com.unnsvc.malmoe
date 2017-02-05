
package com.unnsvc.malmoe.mavenResolver;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.malmoe.common.IResolvedArtifactRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.requests.ModelRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.retrieval.ModelRetrievalResult;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class TestResolveModel extends AbstractMavenResolverTest {

	@Test
	public void testResolveModel() throws Exception {

		MavenRemoteResolver resolver = createResolver();

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("org.hibernate:hibernate-entitymanager:5.2.6.Final");
		IResolvedArtifactRequest request = new ModelRepositoryResolvedRequest(null, "main", identifier);
		IRetrievalResult result = resolver.serveRequest(request);
		Assert.assertTrue(result instanceof ModelRetrievalResult);
		ModelRetrievalResult modelResult = (ModelRetrievalResult) result;

		try (BufferedReader reader = new BufferedReader(new FileReader(modelResult.getFile()))) {
			StringBuilder sb = new StringBuilder();
			String buff = null;
			while ((buff = reader.readLine()) != null) {
				sb.append(buff).append(RhenaConstants.LINE_SEPARATOR);
			}
			System.err.println(sb);
		}
	}

}
