package jp.co.worksap.oss.findbugs.jpa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.umd.cs.findbugs.BugReporter;

@Ignore("test-driven-detectors4findbugs dependency is removed")
public class TableNameLengthTest {
	private BugReporter bugReporter;
	private LongTableNameDetector detector;

	@Before
	public void setup() {
		//        bugReporter = bugReporterForTesting();
		//        detector = new LongTableNameDetector(bugReporter);
	}

	@Test
	public void testShortName() throws Exception {
		//        assertNoBugsReported(ShortTableName.class, detector, bugReporter);
	}

	@Test
	public void testShortNameWithoutAnnotationParameter() throws Exception {
		//        assertNoBugsReported(ShortTableNameNoAnnotationPara.class, detector, bugReporter);
	}

	@Test
	public void testLongName() throws Exception {
		//        assertBugReported(LongTableName.class, detector, bugReporter);
	}

	@Test
	public void testLongNameWithoutAnnotationParameter() throws Exception {
		//        assertBugReported(LongTableNameWithoutAnnotationParameter.class, detector, bugReporter);
	}

	@Test
	public void testTrimPackage() {
		assertThat(detector.trimPackage("ClassName"), is(equalTo("ClassName")));
		assertThat(detector.trimPackage("jp/co/worksap/ClassName"), is(equalTo("ClassName")));
	}
}
