
package com.unnsvc.malmoe.mavenResolver;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.repository.config.ResolverConfig;

public abstract class AbstractMavenResolverTest {

	protected MavenRemoteResolver createResolver() throws IOException {

		File tempDir = getTemporaryPath();
		IResolverConfig config = new ResolverConfig("maven", new URL("http://central.maven.org/maven2/"));
		MavenRemoteResolver resolver = new MavenRemoteResolver(config, tempDir);
		System.err.println("Downloading to: " + tempDir);
		return resolver;
	}

	public File getTemporaryPath() throws IOException {

		File tmp = File.createTempFile("mvn", "mvn");
		tmp.delete();
		tmp.mkdirs();
		tmp.deleteOnExit();
		return tmp;
	}
}
