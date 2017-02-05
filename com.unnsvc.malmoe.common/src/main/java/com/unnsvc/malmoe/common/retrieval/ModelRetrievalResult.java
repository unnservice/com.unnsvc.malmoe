
package com.unnsvc.malmoe.common.retrieval;

import java.io.File;

public class ModelRetrievalResult extends FileRetrievalResult {

	public ModelRetrievalResult(File retrievalResult) {

		super(retrievalResult);
	}

	@Override
	public String toString() {

		return "ModelRetrievalResult [toString()=" + super.toString() + "]";
	}
}
