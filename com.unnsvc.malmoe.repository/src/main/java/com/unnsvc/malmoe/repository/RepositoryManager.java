
package com.unnsvc.malmoe.repository;

import com.unnsvc.malmoe.common.IMalmoeConfiguration;
import com.unnsvc.malmoe.common.IRepositoryManager;

public class RepositoryManager implements IRepositoryManager {

	private IMalmoeConfiguration config;

	public RepositoryManager(IMalmoeConfiguration config) {

		this.config = config;
	}
}
