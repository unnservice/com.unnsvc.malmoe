
package com.unnsvc.malmoe.common.config;

import java.util.List;

import com.unnsvc.malmoe.common.visitors.IVisitable;

public interface IUserConfig extends IVisitable {

	public String getUsername();

	public String getPassword();

	public String getSalt();

	public List<IReference> getGroupReferences();

}
