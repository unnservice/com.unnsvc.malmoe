
package com.unnsvc.malmoe.repository;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeConfiguration;
import com.unnsvc.malmoe.common.IRepositoryManager;
import com.unnsvc.malmoe.common.IResolvedArtifactRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.common.requests.ArtifactRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.retrieval.ArtifactRetrievalResult;
import com.unnsvc.malmoe.common.retrieval.NotFoundRetrievalResult;
import com.unnsvc.malmoe.common.visitors.SerialisationVisitor;
import com.unnsvc.malmoe.repository.config.MalmoeConfigurationParser;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class TestRepositoryManager {

	private File workspaceDirectory;
	private IIdentityManager identityManager;
	private IMalmoeConfiguration config;
	private IUser user;

	@Before
	public void before() throws MalmoeException {

		workspaceDirectory = new File("example-repository");
		config = new MalmoeConfigurationParser(workspaceDirectory);
		debugConfig(config);

		identityManager = new IdentityManager(config.getIdentityConfig());
		user = identityManager.authenticate("admin", "password");
	}

	@Test(expected = MalmoeException.class)
	public void testNotFound() throws Exception {

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.test:something:0.0.1");
		// RetrievalRequest request = new RetrievalRequest(user, "main",
		// identifier, ERequestType.MODEL);
		IResolvedArtifactRequest request = new ArtifactRepositoryResolvedRequest(user, "main", identifier, EExecutionType.ITEST, "someartifact.jar");

		IRepositoryManager manager = new RepositoryManager(workspaceDirectory, identityManager, config.getRepositoriesConfig());
		IRetrievalResult result = manager.serveRequest(request);
	}

	@Test
	public void testValidIdentifierInvalidArtifact() throws MalmoeException, RhenaException {

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("junit:junit:4.12");
		IResolvedArtifactRequest request = new ArtifactRepositoryResolvedRequest(user, "main", identifier, EExecutionType.ITEST, "unknownartifact.jar");

		IRepositoryManager manager = new RepositoryManager(workspaceDirectory, identityManager, config.getRepositoriesConfig());
		IRetrievalResult result = manager.serveRequest(request);
		Assert.assertEquals(NotFoundRetrievalResult.class, result.getClass());
	}
	
	@Test
	public void testValidIdentifierValidArtifact() throws MalmoeException, RhenaException {

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("junit:junit:4.12");
		IResolvedArtifactRequest request = new ArtifactRepositoryResolvedRequest(user, "main", identifier, EExecutionType.ITEST, "artifacts.xml");

		IRepositoryManager manager = new RepositoryManager(workspaceDirectory, identityManager, config.getRepositoriesConfig());
		IRetrievalResult result = manager.serveRequest(request);
		Assert.assertEquals(ArtifactRetrievalResult.class, result.getClass());
	}

	private void debugConfig(IMalmoeConfiguration config) {

		SerialisationVisitor visitor = new SerialisationVisitor();
		config.visit(visitor);
		System.err.println(visitor.toString());
	}
}
