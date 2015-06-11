/*******************************************************************************
 * Copyright (c) 2015 Open Software Solutions GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0
 * which accompanies this distribution, and is available at
 *
 * Contributors:
 *     Open Software Solutions GmbH
 ******************************************************************************/
package test.org.oss.commandexecutor.dummy;

import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

public class DummyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		assertFalse(false);
	}

	@Test(expected=AssertionError.class)
	public void testStopOnFirstFailure() {
		assertFalse(true);
	}

}
