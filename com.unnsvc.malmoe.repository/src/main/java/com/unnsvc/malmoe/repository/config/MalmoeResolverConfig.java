
package com.unnsvc.malmoe.repository.config;

import java.net.URL;

import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class MalmoeResolverConfig implements IResolverConfig {

	private URL url;

	public MalmoeResolverConfig(URL url) {

		this.url = url;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.visitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "resolver:malmoe" + (attrs ? " url=\"" + url + "\"" : "");
	}

}
