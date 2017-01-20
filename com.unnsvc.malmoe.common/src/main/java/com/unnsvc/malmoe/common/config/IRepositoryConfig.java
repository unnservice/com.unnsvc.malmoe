
package com.unnsvc.malmoe.common.config;

import com.unnsvc.malmoe.common.visitors.IVisitable;

public interface IRepositoryConfig extends IVisitable {

	public String getRepositoryName();

	public IAccessConfig getAccessConfig();
}
