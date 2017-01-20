
package com.unnsvc.malmoe.common.config;

import java.net.URL;

import com.unnsvc.malmoe.common.visitors.IVisitable;

public interface IResolverConfig extends IVisitable {

	public String getResolverName();

	public URL getUrl();

}
