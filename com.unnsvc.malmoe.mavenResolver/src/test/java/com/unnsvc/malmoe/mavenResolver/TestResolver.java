package com.unnsvc.malmoe.mavenResolver;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TestResolver {

	
	@Test
	public void testResolver() throws IOException {
		
//		final File repo = new File(System.getProperty("java.io.tmpdir"), "my-repo");
//		System.err.println(System.getProperty("java.io.tmpdir"));
		File repo = File.createTempFile("tmprepo", "tmprepo");
		repo.delete();
		repo.mkdirs();
		
//	    Collection<RemoteRepository> remotes = Arrays.asList(
//	      new RemoteRepository(
//	        "maven-central",
//	        "default",
//	        "http://repo1.maven.org/maven2/"
//	      )
//	    );
//	    Collection<Artifact> deps = new Aether(remotes, repo).resolve(
//	      new DefaultArtifact("junit", "junit-dep", "", "jar", "4.10"),
//	      "runtime"
//	    );
	}
}
