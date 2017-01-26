
package com.unnsvc.malmoe.frontend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.Identifier;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.identity.Version;

public class RequestResolver {

	public static final Pattern MODEL_PATTERN = Pattern.compile("/(?<repositoryId>\\w+)/(?<moduleIdentifier>.*)/model\\.xml");
	public static final Pattern ARTIFACT_PATTERN = Pattern.compile("/(?<repositoryId>\\w+)/(?<moduleIdentifier>.*)/(?<executionType>(main|test))/(?<artifactName>.*)");

	public IResolvedRequest resolveRequest(String relativePath) {

		try {
			Matcher modelMatcher = MODEL_PATTERN.matcher(relativePath);
			if (modelMatcher.find()) {
				String repositoryId = modelMatcher.group("repositoryId");
				ModuleIdentifier identifier = toModuleIdentifier(modelMatcher.group("moduleIdentifier"));
				return new ModelResolvedRequest(repositoryId, identifier);
			}

			Matcher artifactMatcher = ARTIFACT_PATTERN.matcher(relativePath);
			if (artifactMatcher.find()) {
				String repositoryId = modelMatcher.group("repositoryId");
				ModuleIdentifier identifier = toModuleIdentifier(modelMatcher.group("moduleIdentifier"));
				EExecutionType execType = EExecutionType.valueOf(artifactMatcher.group("executionType"));
				return new ArtifactResolvedRequest(repositoryId, identifier, execType, artifactMatcher.group("artifactName"));
			}
		} catch (RhenaException re) {
			
			re.printStackTrace();
			return null;
		}

		return null;
	}

	private ModuleIdentifier toModuleIdentifier(String identifierString) throws RhenaException {

		String[] parts = identifierString.split("/");
		// need at least component-module-version
		if (parts.length < 3) {
			return null;
		}

		Version version = Version.valueOf(parts[parts.length]);
		Identifier module = Identifier.valueOf(parts[parts.length - 1]);

		String componentStr = identifierString.substring(0, identifierString.lastIndexOf("/" + module + "/" + version + "/"));
		Identifier component = Identifier.valueOf(componentStr.replace("/", "."));

		return new ModuleIdentifier(component, module, version);
	}
}
