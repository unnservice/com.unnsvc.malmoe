
package com.unnsvc.malmoe.repository.identity;

import java.util.Collections;
import java.util.List;

import com.unnsvc.malmoe.common.config.IReference;

public class AnonymousUser extends User {

	@SuppressWarnings("unchecked")
	public AnonymousUser() {

		super("anonymous", Collections.EMPTY_LIST);
	}

	public AnonymousUser(List<IReference> groupReferences) {

		super("anonymous", groupReferences);
	}

}
