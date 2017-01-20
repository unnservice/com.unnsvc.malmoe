
package com.unnsvc.malmoe.repository;

import java.io.File;

import org.junit.Test;

import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeConfiguration;
import com.unnsvc.malmoe.common.IRepositoryManager;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.visitors.SerialisationVisitor;
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

		RetrievalRequest request = new RetrievalRequest("main", ModuleIdentifier.valueOf("com.test:something:0.0.1"), EExecutionType.TEST);

		IRepositoryManager manager = new RepositoryManager(identityManager, config.getRepositoriesConfig());
		IRetrievalResult result = manager.retrieveModule(request);
	}

	private void debugConfig(IMalmoeConfiguration config) {

		SerialisationVisitor visitor = new SerialisationVisitor();
		config.visit(visitor);
		System.err.println(visitor.toString());
	}
}
