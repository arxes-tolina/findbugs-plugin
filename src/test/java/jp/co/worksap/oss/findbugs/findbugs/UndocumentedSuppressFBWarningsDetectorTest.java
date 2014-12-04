package jp.co.worksap.oss.findbugs.findbugs;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.umd.cs.findbugs.BugReporter;

@Ignore("test-driven-detectors4findbugs dependency is removed")
public class UndocumentedSuppressFBWarningsDetectorTest {

	private UndocumentedSuppressFBWarningsDetector detector;
	private BugReporter bugReporter;

	@Before
	public void setup() {
		//		bugReporter = bugReporterForTesting();
		//		detector = new UndocumentedSuppressFBWarningsDetector(bugReporter);
	}

	@Test
	public void testDocumentedClasses() throws Exception {
		//		assertNoBugsReported(DocumentedSuppressWarnings.class, detector, bugReporter);
		//		assertNoBugsReported(DocumentedSuppressFBWarnings.class, detector, bugReporter);
	}

	@Test
	public void testUndocumentedClasses() throws Exception {
		//		assertBugReported(UndocumentedSuppressWarnings.class, detector, bugReporter, ofType("FINDBUGS_UNDOCUMENTED_SUPPRESS_WARNINGS"));
		//		assertBugReported(UndocumentedSuppressFBWarnings.class, detector, bugReporter, ofType("FINDBUGS_UNDOCUMENTED_SUPPRESS_WARNINGS"));
	}

}
