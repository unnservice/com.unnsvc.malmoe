
package com.unnsvc.malmoe.repository;

import org.junit.Assert;
import org.junit.Test;

public class TestPasswordOperations {

	@Test
	public void testPasswordOperatiosn() {

		PasswordOperations po = new PasswordOperations(256, 100);
		String salt = po.generateEncodedSalt();
		String password = po.hashPassword("password", salt);
		System.err.println("For reference: " + password + ":" + salt);

		Assert.assertTrue(po.verifyPassword("password", salt, po.hashPassword("password", salt)));
	}
}
