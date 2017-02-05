
package com.unnsvc.malmoe.common.retrieval;

import java.io.File;

public class FileRetrievalResult extends RetrievalResult {

	private File resultingFile;

	public FileRetrievalResult(File resultingFile) {

		this.resultingFile = resultingFile;
	}

	public File getFile() {

		return resultingFile;
	}
}
