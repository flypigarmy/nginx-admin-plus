package com.jslsolucoes.nginx.admin.repository.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

public class UserRepositoryImplTest {

	@Test public void authenticate() {
		String sha256Hex = DigestUtils.sha256Hex("admin");

		Assert.assertEquals("8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918", sha256Hex);
	}
}
