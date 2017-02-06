
package com.unnsvc.malmoe.mavenResolver;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.resolution.DependencyResolutionException;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public class TestMavenResolution {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testMavenCollection() throws MalformedURLException, DependencyCollectionException, MalmoeException, DependencyResolutionException {

		MavenDependencyCollector collector = new MavenDependencyCollector();

		String groupId = "junit";
		String artifactId = "junit";
		String version = "4.12";

//		collector.addRepository(new URL("http://central.maven.org/maven2/"));
//		collector.addDependency(groupId, artifactId, null, "jar", version);
//		collector.addDependency(groupId, artifactId, "sources", "jar", version);
//		collector.addDependency(groupId, artifactId, "javadoc", "jar", version);

//		for (DependencyNode child : collector.collectDependencies().getChildren()) {
//			child.accept(new MavenDependencyDebugVisitor("collection"));
//		}

//		log.info("");
//
//		for (DependencyNode child : collector.resolveDependencies().getChildren()) {
//			child.accept(new MavenDependencyDebugVisitor("resolution"));
//		}

	}
}
