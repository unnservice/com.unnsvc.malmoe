
package com.unnsvc.malmoe.repository.retrieval;

import java.io.File;

public class ModelRetrievalResult extends RetrievalResult {

	private File modelFile;

	public ModelRetrievalResult(File modelFile) {

		this.modelFile = modelFile;
	}

	public File getModelFile() {

		return modelFile;
	}
}
