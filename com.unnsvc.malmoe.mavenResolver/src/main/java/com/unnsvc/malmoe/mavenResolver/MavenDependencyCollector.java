
package com.unnsvc.malmoe.mavenResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.connector.wagon.WagonProvider;
import org.sonatype.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.graph.selector.AndDependencySelector;

import com.tobedevoured.naether.aether.ValidSystemScopeDependencySelector;
import com.tobedevoured.naether.impl.NaetherImpl;
import com.tobedevoured.naether.repo.LogRepositoryListener;
import com.tobedevoured.naether.repo.LogTransferListener;
import com.tobedevoured.naether.repo.ManualWagonProvider;

/**
 * @TODO collect sources too
 * @author noname
 *
 */
public class MavenDependencyCollector {

	private MavenRepositorySystemSession session;
	private RepositorySystem repositorySystem;

	public MavenDependencyCollector(File mavenLocalRepository) throws Exception {

		DefaultServiceLocator locator = new DefaultServiceLocator();
		locator.setServices(WagonProvider.class, new ManualWagonProvider());
		locator.addService(RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class);

		repositorySystem = locator.getService(RepositorySystem.class);

		session = new MavenRepositorySystemSession();
		session = (MavenRepositorySystemSession) session.setDependencySelector(new AndDependencySelector(session.getDependencySelector(), new ValidSystemScopeDependencySelector()));
		session = (MavenRepositorySystemSession) session.setTransferListener(new LogTransferListener());
		session = (MavenRepositorySystemSession) session.setRepositoryListener(new LogRepositoryListener());

		session = (MavenRepositorySystemSession) session.setIgnoreMissingArtifactDescriptor(false);

		LocalRepository localRepo = new LocalRepository(mavenLocalRepository);
		session.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(localRepo));
	}

	public DependencyNode collectDependencies(String coordinates, RemoteRepository... remoteRepositories) throws DependencyCollectionException {

		CollectRequest collectRequest = new CollectRequest();
		
		for (RemoteRepository remoteRepo : remoteRepositories) {
			collectRequest.addRepository(remoteRepo);
		}

		collectRequest.setDependencies(createDependency(coordinates, "compile"));
		CollectResult collectResult = repositorySystem.collectDependencies(session, collectRequest);

		return collectResult.getRoot();
	}

	private List<Dependency> createDependency(String notation, String scope) {

		DefaultArtifact artifact = new DefaultArtifact(notation);

		Dependency dependency = new Dependency(artifact, scope);
		return new ArrayList<Dependency>(Collections.singletonList(dependency));
	}
}
