package jp.co.worksap.oss.findbugs.guava;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
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
import edu.umd.cs.findbugs.ba.ClassMember;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
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
	 * Checks, if detecting of 'default'-Visibility works
	 */
	@Test
	public void testIsDefaultVisible() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(
				bugReporter);
		final AccessibleEntity privateMethod = mock(AccessibleEntity.class);
		when(privateMethod.isPrivate()).thenReturn(true);
		assertFalse(detector.isDefaultVisible(privateMethod));

		final AccessibleEntity protectedMethod = mock(AccessibleEntity.class);
		when(protectedMethod.isProtected()).thenReturn(true);
		assertFalse(detector.isDefaultVisible(protectedMethod));

		final AccessibleEntity publicMethod = mock(AccessibleEntity.class);
		when(publicMethod.isPublic()).thenReturn(true);
		assertFalse(detector.isDefaultVisible(publicMethod));

		final AccessibleEntity defaultMethod = mock(AccessibleEntity.class);
		assertTrue(detector.isDefaultVisible(defaultMethod));
	}

	@Test
	public void testHasToCheck_Visibility() throws Exception {
		final UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);

		final ClassDescriptor currentClass = new TestClassDescriptor("a/b/F");
		final ClassDescriptor memberClass = new TestClassDescriptor("a/b/C");
	
		ClassMember entity = mock(ClassMember.class);
		when(entity.getClassDescriptor()).thenReturn(memberClass);
		
		when(entity.isPrivate()).thenReturn(true);
		assertFalse(detector.hasToCheck(entity, currentClass));

		entity = mock(ClassMember.class);
		when(entity.isProtected()).thenReturn(true);
		when(entity.getClassDescriptor()).thenReturn(memberClass);
		assertFalse(detector.hasToCheck(entity, currentClass));

		entity = mock(ClassMember.class);
		when(entity.isPublic()).thenReturn(true);
		when(entity.getClassDescriptor()).thenReturn(memberClass);
		assertFalse(detector.hasToCheck(entity, currentClass));

		entity = mock(ClassMember.class);
		when(entity.getClassDescriptor()).thenReturn(memberClass);
		assertTrue(detector.hasToCheck(entity, currentClass));
	}

	@Test
	public void testHasToCheck_CallFromClass() throws Exception {
		final UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		
		ClassDescriptor currentClass = new TestClassDescriptor("a/b/C");
		final ClassDescriptor memberClass = new TestClassDescriptor("a/b/C");
	
		final ClassMember entity = mock(ClassMember.class);
		when(entity.getClassDescriptor()).thenReturn(memberClass);

		// same class
		assertFalse(detector.hasToCheck(entity, currentClass));

		// inner class of same class
		currentClass= new TestClassDescriptor("a/b/C$1");
		assertFalse(detector.hasToCheck(entity, currentClass));

		currentClass= new TestClassDescriptor("a/b/Cxy");
		// different class in same package, name starts with member class
		assertTrue(detector.hasToCheck(entity, currentClass));

		// different class in same package
		currentClass= new TestClassDescriptor("a/b/F");
		assertTrue(detector.hasToCheck(entity, currentClass));
				
	}

	/**
	 * Checks, if detecting of the annotation works.
	 */
	@Test
	public void testCheckAnnotated() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(
				bugReporter);
		final XMethod method = mock(XMethod.class);

		detector.checkAnnotations(method.getAnnotations(), false);

		verifyZeroInteractions(bugReporter);

		final AnnotationValue someAnnotationEntry = mock(AnnotationValue.class);
		when(someAnnotationEntry.toString()).thenReturn("some other annotaion");
		when(method.getAnnotations()).thenReturn(
				Arrays.asList(someAnnotationEntry));

		detector.checkAnnotations(method.getAnnotations(), false);

		verifyZeroInteractions(bugReporter);

		final AnnotationValue visibleForTestingAnnotationEntry = mock(AnnotationValue.class);
		when(visibleForTestingAnnotationEntry.toString()).thenReturn(
				UnexpectedAccessDetector.VISIBLE_FOR_TESTING_ANNOTATION);
		when(method.getAnnotations()).thenReturn(
				Arrays.asList(someAnnotationEntry,
						visibleForTestingAnnotationEntry));

		detector.checkAnnotations(method.getAnnotations(), false);

		verify(bugReporter).reportBug(any(BugInstance.class));
	}

	/**
	 * Checks, if 'invoking' OP-Codes are detected
	 */
	@Test
	public void testIsInvoking() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(
				bugReporter);
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
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(
				bugReporter);
		assertTrue(detector.isWorkingOnAField(Constants.PUTFIELD));
		assertTrue(detector.isWorkingOnAField(Constants.GETFIELD));
	}
	
	class TestClassDescriptor extends ClassDescriptor {
		private static final long serialVersionUID = 1L;

		public TestClassDescriptor(final String classname) {
			super(classname);
		}
	}
}
