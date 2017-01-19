package com.unnsvc.malmoe.common.visitors;


public interface IVisitable {

	public void visit(IVisitor visitor);

	public String serialise(boolean attrs);
}
