
package com.unnsvc.malmoe.mavenResolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.sonatype.aether.graph.DependencyNode;

import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.repository.config.ResolverConfig;
import com.unnsvc.malmoe.repository.retrieval.ModelRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.RetrievalRequest;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;


public class TestCustom {

//	@Test
	public void testCollection() throws Exception {

		MavenDependencyCollector collector = new MavenDependencyCollector(getTemporaryPath());
		DependencyNode root = collector.collectDependencies("org.hibernate:hibernate-entitymanager:jar:5.2.6.Final");
		
		System.err.println(root);
		System.err.println(root.getChildren().get(0));
	}
	
	@Test
	public void testConfig() throws Exception {
		
		IResolverConfig config = new ResolverConfig("maven", new URL("http://central.maven.org/maven2/"));
		MavenRemoteResolver resolver = new MavenRemoteResolver(config, getTemporaryPath());
		
		System.err.println("Downloading to: " + getTemporaryPath());
		
		
		ModuleIdentifier identifier = ModuleIdentifier.valueOf("org.hibernate:hibernate-entitymanager:5.2.6.Final");
		IRetrievalResult result = resolver.serveRequest(new RetrievalRequest(null, null, identifier, EExecutionType.MODEL));
		Assert.assertTrue(result instanceof ModelRetrievalResult);
		ModelRetrievalResult modelResult = (ModelRetrievalResult) result;
		
		try(BufferedReader reader = new BufferedReader(new FileReader(modelResult.getModelFile()))) {
			StringBuilder sb = new StringBuilder();
			String buff = null;
			while((buff = reader.readLine()) != null) {
				sb.append(buff).append(RhenaConstants.LINE_SEPARATOR);
			}
			System.err.println(sb);
		}
		
	}
	
	
	private File getTemporaryPath() throws IOException {
		
		File tmp = File.createTempFile("mvn", "mvn");
		tmp.delete();
		tmp.mkdirs();
		tmp.deleteOnExit();
		return tmp;
	}

}
