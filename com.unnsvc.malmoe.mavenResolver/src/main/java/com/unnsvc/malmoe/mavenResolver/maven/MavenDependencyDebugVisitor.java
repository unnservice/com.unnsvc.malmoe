
package com.unnsvc.malmoe.mavenResolver.maven;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.graph.DependencyVisitor;

public class MavenDependencyDebugVisitor implements DependencyVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private int indents = 0;
	private String prefix;

	public MavenDependencyDebugVisitor(String prefix) {

		this.prefix = prefix;
	}

	@Override
	public boolean visitEnter(DependencyNode node) {

		Artifact artifact = node.getDependency().getArtifact();
		log.debug(indents() + "<" + artifact + ">" + (artifact.getFile() != null ? " file:" + artifact.getFile() : ""));

		indents++;
		return true;
	}

	@Override
	public boolean visitLeave(DependencyNode node) {

		indents--;

		Artifact artifact = node.getDependency().getArtifact();
		log.debug(indents() + "</" + artifact + ">");

		return true;
	}

	private String indents() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("  ");
		}
		return prefix + ": " + sb.toString();
	}
}
