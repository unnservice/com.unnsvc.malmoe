
package com.unnsvc.malmoe.mavenResolver;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.sonatype.aether.resolution.DependencyRequest;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.resolution.DependencyResult;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.graph.selector.AndDependencySelector;

import com.tobedevoured.naether.aether.ValidSystemScopeDependencySelector;
import com.tobedevoured.naether.repo.LogRepositoryListener;
import com.tobedevoured.naether.repo.LogTransferListener;
import com.tobedevoured.naether.repo.ManualWagonProvider;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.common.exceptions.NotFoundMalmoeException;

/**
 * @TODO collect sources too
 * @author noname
 *
 */
public class MavenDependencyCollector {

	private Logger log = LoggerFactory.getLogger(getClass());
	private MavenRepositorySystemSession session;
	private RepositorySystem repositorySystem;
	private List<RemoteRepository> remoteRepositories;

	public MavenDependencyCollector(File mavenLocalRepository) throws Exception {

		this.remoteRepositories = new ArrayList<RemoteRepository>();

		DefaultServiceLocator locator = new DefaultServiceLocator();
		locator.setServices(WagonProvider.class, new ManualWagonProvider());
		locator.addService(RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class);

		repositorySystem = locator.getService(RepositorySystem.class);

		session = new MavenRepositorySystemSession();
		session = (MavenRepositorySystemSession) session
				.setDependencySelector(new AndDependencySelector(session.getDependencySelector(), new ValidSystemScopeDependencySelector()));
		session = (MavenRepositorySystemSession) session.setTransferListener(new LogTransferListener());
		session = (MavenRepositorySystemSession) session.setRepositoryListener(new LogRepositoryListener());

		session = (MavenRepositorySystemSession) session.setIgnoreMissingArtifactDescriptor(false);

		LocalRepository localRepo = new LocalRepository(mavenLocalRepository);
		session.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(localRepo));
	}

	public void addRepository(URL repositoryLocation) {

		remoteRepositories.add(new RemoteRepository(repositoryLocation.hashCode() + "", "default", repositoryLocation.toString()));
	}

	public DependencyNode collectDependencies(String coordinates) throws DependencyCollectionException, MalmoeException {

		try {
			CollectRequest collectRequest = createCollectRequest(coordinates);
			CollectResult collectResult = repositorySystem.collectDependencies(session, collectRequest);

			if (collectResult.getRoot().getChildren().isEmpty() || collectResult.getRoot().getChildren().size() != 1) {
				throw new MalmoeException("Resolved to (" + collectResult.getRoot().getChildren().size() + "): " + collectRequest);
			}

			return collectResult.getRoot();
		} catch (DependencyCollectionException collectionFailure) {

			throw new NotFoundMalmoeException(collectionFailure.getMessage(), collectionFailure);
		}
	}

	public DependencyNode resolveDependencies(String coordinates) throws DependencyResolutionException, MalmoeException {

		try {
			CollectRequest collectRequest = createCollectRequest(coordinates);

			DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
			DependencyResult dependencyResult = repositorySystem.resolveDependencies(session, dependencyRequest);

			if (dependencyResult.getRoot().getChildren().isEmpty() || dependencyResult.getRoot().getChildren().size() != 1) {
				throw new MalmoeException("Resolved to (" + dependencyResult.getRoot().getChildren().size() + "): " + collectRequest);
			}

			return dependencyResult.getRoot();
		} catch (DependencyResolutionException resolutionFailure) {

			log.debug(resolutionFailure.getMessage(), resolutionFailure);
			throw new NotFoundMalmoeException(resolutionFailure.getMessage());
		}
	}

	private CollectRequest createCollectRequest(String coordinates) {

		CollectRequest collectRequest = new CollectRequest();

		for (RemoteRepository remoteRepo : remoteRepositories) {
			collectRequest.addRepository(remoteRepo);
		}

		collectRequest.setDependencies(createDependency(coordinates, "compile"));
		return collectRequest;
	}

	private List<Dependency> createDependency(String notation, String scope) {

		DefaultArtifact artifact = new DefaultArtifact(notation);

		Dependency dependency = new Dependency(artifact, scope);
		return new ArrayList<Dependency>(Collections.singletonList(dependency));
	}
}
