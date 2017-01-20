
package com.unnsvc.malmoe.repository.config;

import com.unnsvc.malmoe.common.config.IReference;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class RepositoryConfigRef implements IReference {

	private String name;

	public RepositoryConfigRef(String name) {

		this.name = name;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.visitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "repository" + (attrs ? " ref=\"" + name + "\"" : "");
	}

	@Override
	public String getRef() {

		return name;
	}
}
