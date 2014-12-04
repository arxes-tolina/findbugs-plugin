package jp.co.worksap.oss.findbugs.guava;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.umd.cs.findbugs.BugReporter;

@Ignore("test-driven-detectors4findbugs dependency is removed")
public class UnexpectedAccessDetectorTest {

	private UnexpectedAccessDetector detector;
	private BugReporter bugReporter;

	@Before
	public void setup() {
		//		bugReporter = bugReporterForTesting();
		//		detector = new UnexpectedAccessDetector(bugReporter);
	}

	@Test
	public void testNormalMethod() throws Exception {
		//assertNoBugsReported(ClassWhichCallsNormalMethod.class, detector, bugReporter);
	}

	@Test
	public void testCallingAnnotatedMethod() throws Exception {
		//		assertBugReported(ClassWhichCallsVisibleMethodForTesting.class, detector, bugReporter, ofType("GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING"));
	}

}
