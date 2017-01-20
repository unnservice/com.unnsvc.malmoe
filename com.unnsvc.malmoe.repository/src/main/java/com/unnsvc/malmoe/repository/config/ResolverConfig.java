
package com.unnsvc.malmoe.repository.config;

import java.net.URL;

import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class ResolverConfig implements IResolverConfig {

	private String resolverName;
	private URL url;

	public ResolverConfig(String resolverName, URL url) {

		this.resolverName = resolverName;
		this.url = url;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.visitable(this);
	}

	@Override
	public String getResolverName() {

		return resolverName;
	}
	
	@Override
	public URL getUrl() {

		return url;
	}

	@Override
	public String serialise(boolean attrs) {

		return "resolver:malmoe" + (attrs ? " url=\"" + url + "\"" : "");
	}
}
