
package com.unnsvc.malmoe.client;

import java.io.File;

import org.junit.Test;

import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeConfiguration;
import com.unnsvc.malmoe.common.IRepositoryManager;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.common.visitors.SerialisationVisitor;
import com.unnsvc.malmoe.repository.IdentityManager;
import com.unnsvc.malmoe.repository.RepositoryManager;
import com.unnsvc.malmoe.repository.config.MalmoeConfigurationParser;
import com.unnsvc.malmoe.repository.retrieval.RetrievalRequest;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class TestRepositoryManager {

	@Test
	public void test() throws Exception {

		File workspaceDirectory = new File("example-repository");
		IMalmoeConfiguration config = new MalmoeConfigurationParser(workspaceDirectory);
		debugConfig(config);

		IIdentityManager identityManager = new IdentityManager(config.getIdentityConfig());
		IUser user = identityManager.authenticate("admin", "password");

		RetrievalRequest request = new RetrievalRequest(user, "main", ModuleIdentifier.valueOf("com.test:something:0.0.1"), EExecutionType.MAIN);

		IRepositoryManager manager = new RepositoryManager(workspaceDirectory, identityManager, config.getRepositoriesConfig());
		IRetrievalResult result = manager.serveRequest(request);
	}

	private void debugConfig(IMalmoeConfiguration config) {

		SerialisationVisitor visitor = new SerialisationVisitor();
		config.visit(visitor);
		System.err.println(visitor.toString());
	}
}
