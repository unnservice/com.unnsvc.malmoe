package com.unnsvc.malmoe.common.visitors;


public interface IVisitor {

	public void startVisitable(IVisitable visitable);

	public void endVisitable(IVisitable visitable);

	/**
	 * When it's a self-closing tag
	 * @param malmoeResolverConfig
	 */
	public void visitable(IVisitable visitable);

}
