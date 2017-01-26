
package com.unnsvc.malmoe.frontend;

import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Test;

public class TestRequestPatterns {

	@Test
	public void testModelPattern() throws Exception {

		String modelRequest = "/model.xml";
		Assert.assertTrue(RequestResolver.MODEL_PATTERN.matcher(modelRequest).matches());
	}

	@Test
	public void testExecutionsPattern() throws Exception {

		String executionMainRequest = "/main/executions.xml";
		Matcher matched = RequestResolver.ARTIFACT_PATTERN.matcher(executionMainRequest);
		Assert.assertTrue(matched.find());
		Assert.assertEquals("main", matched.group("executionType"));
		Assert.assertEquals("executions.xml", matched.group("artifactName"));

		String executionTestRequest = "/test/executions.xml";
		Assert.assertTrue(RequestResolver.ARTIFACT_PATTERN.matcher(executionTestRequest).matches());

		String executionUnknownRequest = "/unknown/executions.xml";
		Assert.assertFalse(RequestResolver.ARTIFACT_PATTERN.matcher(executionUnknownRequest).matches());
	}

	@Test
	public void testArtifactResult() throws Exception {
		
		String artifactRequest = "/main/someartifact-0.0.1.jar";
		Matcher matched = RequestResolver.ARTIFACT_PATTERN.matcher(artifactRequest);
		Assert.assertTrue(matched.find());
		Assert.assertEquals("main", matched.group("executionType"));
		Assert.assertEquals("someartifact-0.0.1.jar", matched.group("artifactName"));
	}
}
