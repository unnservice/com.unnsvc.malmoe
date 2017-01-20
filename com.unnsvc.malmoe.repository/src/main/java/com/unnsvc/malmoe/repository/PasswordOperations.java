
package com.unnsvc.malmoe.repository;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordOperations {

	private int keySize;
	private int iterations;
	private Random random;

	public PasswordOperations(int keySize, int iterations) {

		this.keySize = keySize;
		this.iterations = iterations;
		this.random = new SecureRandom();
	}

	public byte[] generateSalt() {

		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	public String generateEncodedSalt() {

		return Base64.getEncoder().encodeToString(generateSalt());
	}

	public String hashPassword(String password, String salt) {

		try {
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), iterations, keySize);

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			SecretKey key = keyFactory.generateSecret(spec);
			return Base64.getEncoder().encodeToString(key.getEncoded());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean verifyPassword(String password, String salt, String expectedHash) {

		String hash = hashPassword(password, salt);
		return hash.equals(expectedHash);
	}
}
