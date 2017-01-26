
package com.unnsvc.malmoe.repository.identity;

import java.util.Collections;

public class AnonymousUser extends User {

	@SuppressWarnings("unchecked")
	public AnonymousUser() {

		super("anonymous", Collections.EMPTY_LIST);
	}

}
