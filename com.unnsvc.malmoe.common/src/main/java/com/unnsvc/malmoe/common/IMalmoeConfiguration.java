
package com.unnsvc.malmoe.common;

import com.unnsvc.malmoe.common.config.IIdentityConfig;
import com.unnsvc.malmoe.common.config.IRepositoriesConfig;
import com.unnsvc.malmoe.common.visitors.IVisitable;

public interface IMalmoeConfiguration extends IVisitable {

	public IIdentityConfig getIdentityConfig();

	public IRepositoriesConfig getRepositoriesConfig();

}
