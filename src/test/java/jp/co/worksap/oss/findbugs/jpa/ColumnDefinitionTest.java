package jp.co.worksap.oss.findbugs.jpa;

import static com.youdevise.fbplugins.tdd4fb.DetectorAssert.assertBugReported;
import static com.youdevise.fbplugins.tdd4fb.DetectorAssert.assertNoBugsReported;
import static com.youdevise.fbplugins.tdd4fb.DetectorAssert.bugReporterForTesting;
import static com.youdevise.fbplugins.tdd4fb.DetectorAssert.ofType;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.umd.cs.findbugs.BugReporter;

@Ignore("test-driven-detectors4findbugs is not compatible with FB3.0")
public class ColumnDefinitionTest {

	private BugReporter bugReporter;
	private ColumnDefinitionDetector detector;

	@Before
	public void setup() {
		bugReporter = bugReporterForTesting();
		detector = new ColumnDefinitionDetector(bugReporter);
	}

	@Test
	public void testNormalClass() throws Exception {
		assertNoBugsReported(ShortColumnName.class, detector, bugReporter);
	}

	@Test
	public void testWithColumnDefinition() throws Exception {
		assertBugReported(UseColumnDefinition.class, detector, bugReporter, ofType("USE_COLUMN_DEFINITION"));
	}
}
