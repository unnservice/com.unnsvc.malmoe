package com.unnsvc.malmoe.repository.config;

import java.net.URL;

import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class MavenResolverConfig implements IResolverConfig {

	private URL url;

	public MavenResolverConfig(URL url) {
		
		this.url = url;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.visitable(this);
	}

}
