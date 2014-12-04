package jp.co.worksap.oss.findbugs.jpa;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.umd.cs.findbugs.BugReporter;

@Ignore("test-driven-detectors4findbugs dependency is removed")
public class ImplicitLengthTest {
	private BugReporter bugReporter;
	private ImplicitLengthDetector detector;

	@Before
	public void before() {
		//        this.bugReporter = bugReporterForTesting();
		//        this.detector = new ImplicitLengthDetector(bugReporter);
	}

	@Test
	public void testNegativeLength() throws Exception {
		//        assertBugReported(ColumnWithNegativeLength.class, detector,
		//                bugReporter, ofType("ILLEGAL_LENGTH"));
	}

	@Test
	public void testTooLongLength() throws Exception {
		//        assertBugReported(ColumnWithTooLongLength.class, detector,
		//                bugReporter, ofType("ILLEGAL_LENGTH"));
		//        assertBugReported(GetterWithTooLongLength.class, detector,
		//                bugReporter, ofType("ILLEGAL_LENGTH"));
	}

	@Test
	public void testLongLengthWithLob() throws Exception {
		//		assertNoBugsReported(ColumnWithLongLengthAndLob.class, detector, bugReporter);
		//		assertNoBugsReported(GetterWithLongLengthAndLob.class, detector, bugReporter);
	}

	@Test
	public void testExplicitLength() throws Exception {
		//		assertNoBugsReported(ColumnWithLength.class, detector, bugReporter);
	}

	@Test
	public void testImplicitLength() throws Exception {
		//		assertBugReported(ColumnWithoutElement.class, detector, bugReporter, ofType("IMPLICIT_LENGTH"));
		//		assertBugReported(GetterWithoutElement.class, detector, bugReporter, ofType("IMPLICIT_LENGTH"));
	}
}
