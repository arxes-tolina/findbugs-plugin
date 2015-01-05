package jp.co.worksap.oss.findbugs.jpa;

import static com.youdevise.fbplugins.tdd4fb.DetectorAssert.assertBugReported;
import static com.youdevise.fbplugins.tdd4fb.DetectorAssert.assertNoBugsReported;
import static com.youdevise.fbplugins.tdd4fb.DetectorAssert.bugReporterForTesting;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.umd.cs.findbugs.BugReporter;

@Ignore("test-driven-detectors4findbugs is not compatible with FB3.0")
public class ImplicitNullnessTest {
	private BugReporter bugReporter;
	private ImplicitNullnessDetector detector;

	@Before
	public void before() {
		bugReporter = bugReporterForTesting();
		detector = new ImplicitNullnessDetector(bugReporter);
	}

	@Test
	public void testExplicitNullness() throws Exception {
		assertNoBugsReported(ColumnWithNullable.class, detector, bugReporter);
	}

	@Test
	public void testImplicitNullness() throws Exception {
		assertBugReported(ColumnWithoutElement.class, detector, bugReporter);
		assertBugReported(GetterWithoutElement.class, detector, bugReporter);
	}
}
