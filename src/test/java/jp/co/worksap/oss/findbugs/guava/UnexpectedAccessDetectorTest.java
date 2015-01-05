package jp.co.worksap.oss.findbugs.guava;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.objectweb.asm.Opcodes;

import com.google.common.annotations.VisibleForTesting;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;


/**
 * @author tolina GmbH
 *
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
	 * Tests reporting a Bug when:
	 * - called Method has 'default'-visibility
	 * - called Method is annotated with {@link VisibleForTesting}
	 * @throws Exception
	 */
	@Test
	public void testVerifyVisibility() throws Exception {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		ClassDescriptor invokedClassDescriptor;
		MethodDescriptor invokedMethodDescriptor;

		invokedClassDescriptor = DescriptorFactory.createClassDescriptor(Object.class);
		invokedMethodDescriptor = DescriptorFactory.instance().getMethodDescriptor("java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false);
		detector.verifyVisibility(invokedClassDescriptor, invokedMethodDescriptor, false);

		invokedClassDescriptor = DescriptorFactory.createClassDescriptor(TestClass.class);
		invokedMethodDescriptor = DescriptorFactory.instance().getMethodDescriptor("jp/co/worksap/oss/findbugs/guava/TestClass", "test", "()V", false);

		detector.verifyVisibility(invokedClassDescriptor, invokedMethodDescriptor, false);
		verify(bugReporter).reportBug(any(BugInstance.class));
	}

	/**
	 * Tests searching for {@link Method}s of a {@link JavaClass}
	 */
	@Test
	public void testFindMethod() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		ConstantPool constant_pool = mock(ConstantPool.class);
		JavaClass bcelClass = mock(JavaClass.class);

		Method testMethod = new Method();
		int name_index = 0;
		int signature_index = 1;
		testMethod.setNameIndex(name_index);
		testMethod.setSignatureIndex(signature_index);
		testMethod.setConstantPool(constant_pool);
		Constant nameConstant = new ConstantUtf8("equals");
		Constant signatureConstant = new ConstantUtf8("(Ljava/lang/Object;)Z");

		when(constant_pool.getConstant(eq(name_index), anyByte())).thenReturn(nameConstant);
		when(constant_pool.getConstant(eq(signature_index), anyByte())).thenReturn(signatureConstant);

		Method[] methods = { testMethod };
		when(bcelClass.getMethods()).thenReturn(methods);
		when(bcelClass.getClassName()).thenReturn("java.lang.Object");

		MethodDescriptor invokedMethod = new MethodDescriptor("java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false); //   mock(MethodDescriptor.class);

		Method method = detector.findMethod(bcelClass, invokedMethod);

		assertNotNull(method);

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
