package jp.co.worksap.oss.findbugs;

import org.junit.Ignore;
import org.junit.Test;

@Ignore("test-driven-detectors4findbugs dependency is removed")
public class ForbiddenSystemDetectorTest {

	@Test
	public void testUseSystemOutBug() throws Exception {
		//		BugReporter bugReporter = DetectorAssert.bugReporterForTesting();
		//		ForbiddenSystemClass detector = new ForbiddenSystemClass(bugReporter);
		//
		//		DetectorAssert.assertBugReported(UseSystemOut.class, detector, bugReporter);
	}

	@Test
	public void testUseSystemErrBug() throws Exception {
		//		BugReporter bugReporter = DetectorAssert.bugReporterForTesting();
		//		ForbiddenSystemClass detector = new ForbiddenSystemClass(bugReporter);
		//
		//		DetectorAssert.assertBugReported(UseSystemErr.class, detector, bugReporter);
	}

}
