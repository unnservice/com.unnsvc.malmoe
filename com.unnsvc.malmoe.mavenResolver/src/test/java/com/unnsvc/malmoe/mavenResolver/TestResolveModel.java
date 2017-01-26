
package com.unnsvc.malmoe.mavenResolver;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.ModelRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.RetrievalRequest;
import com.unnsvc.malmoe.resolver.ERequestType;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class TestResolveModel extends AbstractMavenResolverTest {

	@Test
	public void testResolveModel() throws Exception {

		MavenRemoteResolver resolver = createResolver();

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("org.hibernate:hibernate-entitymanager:5.2.6.Final");
		IRetrievalResult result = resolver.serveRequest(new RetrievalRequest(null, null, identifier, ERequestType.MODEL));
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