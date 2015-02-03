package jp.co.worksap.oss.findbugs.guava;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.bcel.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.AccessibleEntity;
import edu.umd.cs.findbugs.ba.XField;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.analysis.AnnotationValue;

/**
 * @author tolina GmbH
 */
public class UnexpectedAccessDetectorTest {

	@Mock
	private BugReporter bugReporter;

	/**
	 * Init
	 */
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Checks if the visibility is checked and the annotations are loaded.
	 */
	@Test
	public void checkMethod() throws Exception {
		final UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		final XMethod invokedMethod = mock(XMethod.class);

		when(invokedMethod.isPrivate()).thenReturn(true);
		when(invokedMethod.isProtected()).thenReturn(false);
		when(invokedMethod.isPublic()).thenReturn(false);

		detector.checkMethod(invokedMethod);

		when(invokedMethod.isPrivate()).thenReturn(false);
		when(invokedMethod.isProtected()).thenReturn(true);
		when(invokedMethod.isPublic()).thenReturn(false);

		detector.checkMethod(invokedMethod);

		when(invokedMethod.isPrivate()).thenReturn(false);
		when(invokedMethod.isProtected()).thenReturn(false);
		when(invokedMethod.isPublic()).thenReturn(true);

		detector.checkMethod(invokedMethod);

		when(invokedMethod.isPrivate()).thenReturn(false);
		when(invokedMethod.isProtected()).thenReturn(false);
		when(invokedMethod.isPublic()).thenReturn(false);

		detector.checkMethod(invokedMethod);

		// This method should only be invoked when it is package protected
		verify(invokedMethod, times(1)).getAnnotations();
	}

	/**
	 * Checks if the visibility is checked and the annotations are loaded.
	 */
	@Test
	public void checkField() throws Exception {
		final UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		final XField invokedField = mock(XField.class);

		when(invokedField.isPrivate()).thenReturn(true);
		when(invokedField.isProtected()).thenReturn(false);
		when(invokedField.isPublic()).thenReturn(false);

		detector.checkField(invokedField);

		when(invokedField.isPrivate()).thenReturn(false);
		when(invokedField.isProtected()).thenReturn(true);
		when(invokedField.isPublic()).thenReturn(false);

		detector.checkField(invokedField);

		when(invokedField.isPrivate()).thenReturn(false);
		when(invokedField.isProtected()).thenReturn(false);
		when(invokedField.isPublic()).thenReturn(true);

		detector.checkField(invokedField);

		when(invokedField.isPrivate()).thenReturn(false);
		when(invokedField.isProtected()).thenReturn(false);
		when(invokedField.isPublic()).thenReturn(false);

		detector.checkField(invokedField);

		// This method should only be invoked when it is package protected
		verify(invokedField, times(1)).getAnnotations();
	}

	/**
	 * Checks, if detecting of 'default'-Visibility works
	 */
	@Test
	public void testCheckVisibility() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		final AccessibleEntity privateMethod = mock(AccessibleEntity.class);
		when(privateMethod.isPrivate()).thenReturn(true);
		assertFalse(detector.checkVisibility(privateMethod));

		final AccessibleEntity protectedMethod = mock(AccessibleEntity.class);
		when(protectedMethod.isProtected()).thenReturn(true);
		assertFalse(detector.checkVisibility(protectedMethod));

		final AccessibleEntity publicMethod = mock(AccessibleEntity.class);
		when(publicMethod.isPublic()).thenReturn(true);
		assertFalse(detector.checkVisibility(publicMethod));

		final AccessibleEntity defaultMethod = mock(AccessibleEntity.class);
		assertTrue(detector.checkVisibility(defaultMethod));
	}

	/**
	 * Checks, if detecting of the annotation works.
	 */
	@Test
	public void testCheckAnnotated() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		final XMethod method = mock(XMethod.class);

		detector.checkAnnotations(method.getAnnotations(), false);

		verifyZeroInteractions(bugReporter);

		final AnnotationValue someAnnotationEntry = mock(AnnotationValue.class);
		when(someAnnotationEntry.toString()).thenReturn("some other annotaion");
		when(method.getAnnotations()).thenReturn(Arrays.asList(someAnnotationEntry));

		detector.checkAnnotations(method.getAnnotations(), false);

		verifyZeroInteractions(bugReporter);

		final AnnotationValue visibleForTestingAnnotationEntry = mock(AnnotationValue.class);
		when(visibleForTestingAnnotationEntry.toString()).thenReturn(UnexpectedAccessDetector.VISIBLE_FOR_TESTING_ANNOTATION);
		when(method.getAnnotations()).thenReturn(Arrays.asList(someAnnotationEntry, visibleForTestingAnnotationEntry));

		detector.checkAnnotations(method.getAnnotations(), false);

		verify(bugReporter).reportBug(any(BugInstance.class));
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

	/**
	 * Checks, if 'field' OP-Codes are detected
	 */
	@Test
	public void testIsWorkingOnAField() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		assertTrue(detector.isWorkingOnAField(Constants.PUTFIELD));
		assertTrue(detector.isWorkingOnAField(Constants.GETFIELD));
	}
}
