
package com.unnsvc.malmoe.frontend;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.repository.identity.AnonymousUser;
import com.unnsvc.malmoe.repository.requests.RequestResolver;

/**
 * /<repoName>/<com/ponent/Name>/<moduleName>/<version>/ /model.xml
 * /main/executions.xml /test/executions.xml
 * 
 * /<repositoryId>?component=<componentName>&module=<moduleName>&version=<versionName>&executionType=<executionType>&artifactName=
 * 
 * @author noname
 *
 */
public class TestRequestPatterns {

	private RequestResolver resolver;

	@Before
	public void before() throws MalmoeException {

		resolver = new RequestResolver(Collections.singletonList("repo1"), new AnonymousUser());
	}

	@Test
	public void testModel() throws Exception {

		String modelRequest = "/repo1/com/component/com.component.artifact/0.0.1/model.xml";
		resolver.resolveRequest(modelRequest);
	}
}
