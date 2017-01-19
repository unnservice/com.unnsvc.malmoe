
package com.unnsvc.malmoe.common.visitors;

import com.unnsvc.malmoe.common.MalmoeUtils;

public class SerialisationVisitor implements IVisitor {

	private int indents;
	private StringBuilder content;

	public SerialisationVisitor() {

		this.indents = 0;
		this.content = new StringBuilder();
		this.content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(System.getProperty("line.separator"));
	}

	private SerialisationVisitor(int indents, StringBuilder content) {

		this.indents = indents;
		this.content = content;
	}

	@Override
	public void startVisitable(IVisitable visitable) {

		content.append(MalmoeUtils.getIndents(indents) + "<" + visitable.serialise(true) + ">").append(System.getProperty("line.separator"));
	}

	@Override
	public void endVisitable(IVisitable visitable) {

		content.append(MalmoeUtils.getIndents(indents) + "</" + visitable.serialise(false) + ">").append(System.getProperty("line.separator"));
	}

	@Override
	public void visitable(IVisitable visitable) {

		content.append(MalmoeUtils.getIndents(indents) + "<" + visitable.serialise(true) + " />").append(System.getProperty("line.separator"));
	}

	@Override
	public IVisitor newVisitor() {

		return new SerialisationVisitor(indents + 1, content);
	}

	@Override
	public String toString() {

		return content.toString();
	}
}
