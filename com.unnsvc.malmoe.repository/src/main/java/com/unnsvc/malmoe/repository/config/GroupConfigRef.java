
package com.unnsvc.malmoe.repository.config;

import com.unnsvc.malmoe.common.config.IReference;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class GroupConfigRef implements IReference {

	private String ref;

	public GroupConfigRef(String ref) {

		this.ref = ref;
	}

	@Override
	public String getRef() {

		return ref;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.visitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "group" + (attrs ? " ref=\"" + ref + "\"" : "");
	}
}
