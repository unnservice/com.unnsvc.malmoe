package com.unnsvc.malmoe.repository;

import java.io.File;

import org.junit.Test;

import com.unnsvc.malmoe.common.IMalmoeConfiguration;
import com.unnsvc.malmoe.common.visitors.SerialisationVisitor;
import com.unnsvc.malmoe.repository.config.MalmoeConfigurationParser;

public class TestRepositoryManager {

	@Test
	public void test() throws Exception {
		
		File workspaceDirectory = new File("example-repository");
		IMalmoeConfiguration config = new MalmoeConfigurationParser(workspaceDirectory);
		SerialisationVisitor visitor = new SerialisationVisitor();
		config.visit(visitor);
		System.err.println(visitor.toString());
		
		RepositoryManager manager = new RepositoryManager(config);
	}
}
