package jp.co.worksap.oss.findbugs.guava;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.objectweb.asm.Opcodes;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;


/**
 * @author tolina GmbH
 *
 */
public class UnexpectedAccessDetectorTest {


	@Mock
	private BugReporter bugReporter;

	/**
	 * 
	 */
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		//		bugReporter = bugReporterForTesting();
	}

	@Test
	public void testNormalMethod() throws Exception {
		//assertNoBugsReported(ClassWhichCallsNormalMethod.class, detector, bugReporter);
	}

	@Test
	public void testCallingAnnotatedMethod() throws Exception {
		//		assertBugReported(ClassWhichCallsVisibleMethodForTesting.class, detector, bugReporter, ofType("GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING"));
	}


	@Test
	public void testFindMethod() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);

		JavaClass bcelClass = null;
		MethodDescriptor invokedMethod = null;

		Method method = detector.findMethod(bcelClass, invokedMethod);

	}

	/**
	 * Checks, if detecting of 'default'-Visibility works
	 */
	@Test
	public void testCheckVisibility() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		Method privateMethod = new Method();
		privateMethod.setModifiers(Opcodes.ACC_PRIVATE);
		assertFalse(detector.checkVisibility(privateMethod));

		Method protectedMethod = new Method();
		protectedMethod.setModifiers(Opcodes.ACC_PROTECTED);
		assertFalse(detector.checkVisibility(protectedMethod));

		Method publicMethod = new Method();
		publicMethod.setModifiers(Opcodes.ACC_PUBLIC);
		assertFalse(detector.checkVisibility(publicMethod));

		Method defaultVisibilityMethod = new Method();
		assertTrue(detector.checkVisibility(defaultVisibilityMethod));
	}

	/**
	 * Checks, if detecting of annotated Methods works 
	 */
	@Test
	public void testCheckAnnotated() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		Method method = new Method();
		method.setAttributes(new Attribute[] {});

		assertFalse(detector.checkAnnotated(method));

		AnnotationEntry someAnnotationEntry = mock(AnnotationEntry.class);
		when(someAnnotationEntry.getAnnotationType()).thenReturn("Lorg/junit/Test;");
		method.addAnnotationEntry(someAnnotationEntry);

		assertFalse(detector.checkAnnotated(method));

		AnnotationEntry visibleForTestingAnnotationEntry = mock(AnnotationEntry.class);
		when(visibleForTestingAnnotationEntry.getAnnotationType()).thenReturn("Lcom/google/common/annotations/VisibleForTesting;");
		method.addAnnotationEntry(visibleForTestingAnnotationEntry);

		assertTrue(detector.checkAnnotated(method));
	}

	/**
	 * Checks, if 'invoking' OP-Codes are detected
	 */
	@Test
	public void testIsInvoking() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		assertTrue(detector.isInvoking(Constants.INVOKESPECIAL));
		assertTrue(detector.isInvoking(Constants.INVOKEINTERFACE));
		assertTrue(detector.isInvoking(Constants.INVOKESTATIC));
		assertTrue(detector.isInvoking(Constants.INVOKEVIRTUAL));

	}

}
