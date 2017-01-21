
package com.unnsvc.malmoe.mavenResolver;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.sonatype.aether.graph.Dependency;

import com.tobedevoured.naether.api.Naether;
import com.tobedevoured.naether.impl.NaetherImpl;
import com.unnsvc.malmoe.common.MalmoeUtils;

public class TestCustom {

	@Test
	public void testCustom() throws Exception {

		File repo = File.createTempFile("tmprepo", "tmprepo");
		repo.delete();
		repo.mkdirs();

		Naether naether = new NaetherImpl();
		naether.addRemoteRepository("alfresco", "default", "https://artifacts.alfresco.com/nexus/content/repositories/public/");
//		naether.addDependency("org.hibernate:hibernate-entitymanager:jar:5.2.6.Final", "test");
		naether.addDependency("org.springframework:spring-webmvc:jar:5.0.0.M4");
//		naether.addDependency("junit:junit:jar:4.12", "compile");
//		naether.resolveDependencies();
//		System.out.println(naether.getDependenciesNotation().toString());
		
		naether.resolveDependencies(false);

		printGraph(naether.getDependenciesGraph(), 0);
		printArtifacts(naether.getDependencies());	
	}

	private void printArtifacts(Set<Dependency> buildArtifacts) {

		for(Dependency a : buildArtifacts) {

			System.err.println("b:" + a + " " + a.getArtifact());
		}
	}

	@SuppressWarnings("unchecked")
	private void printGraph(Map<String, Map> dependenciesGraph, int indent) {

		for (String key : dependenciesGraph.keySet()) {

			System.err.println(MalmoeUtils.getIndents(indent) + "Key " + key);
			printGraph(dependenciesGraph.get(key), indent + 1);
		}
	}
}
