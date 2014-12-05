/*
 *  (c) tolina GmbH, 2014
 */
package jp.co.worksap.oss.findbugs.guava;

import com.google.common.annotations.VisibleForTesting;

/**
 * Class used only for {@link UnexpectedAccessDetector}-tests
 * @author tolina GmbH
 *
 */
class TestClass {

	@VisibleForTesting
	void test() {
		return;
	}

}