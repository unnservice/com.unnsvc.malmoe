
package com.unnsvc.malmoe.common.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.malmoe.common.MalmoeUtils;

public class SerialisationVisitor implements IVisitor {

	private int indents;
	private ISerialisationVisitorHandler handler;

	public SerialisationVisitor() {

		this(new ISerialisationVisitorHandler() {

			private Logger log = LoggerFactory.getLogger(getClass());

			@Override
			public void onLine(String line) {

				log.debug(line);
			}
		});
	}

	public SerialisationVisitor(ISerialisationVisitorHandler handler) {

		this.indents = 0;
		this.handler = handler;
		handler.onLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	}

	private SerialisationVisitor(int indents, ISerialisationVisitorHandler handler) {

		this.indents = indents;
		this.handler = handler;
	}

	@Override
	public void startVisitable(IVisitable visitable) {

		handler.onLine(MalmoeUtils.getIndents(indents) + "<" + visitable.serialise(true) + ">");
	}

	@Override
	public void endVisitable(IVisitable visitable) {

		handler.onLine(MalmoeUtils.getIndents(indents) + "</" + visitable.serialise(false) + ">");
	}

	@Override
	public void visitable(IVisitable visitable) {

		handler.onLine(MalmoeUtils.getIndents(indents) + "<" + visitable.serialise(true) + " />");
	}

	@Override
	public IVisitor newVisitor() {

		return new SerialisationVisitor(indents + 1, handler);
	}
}
