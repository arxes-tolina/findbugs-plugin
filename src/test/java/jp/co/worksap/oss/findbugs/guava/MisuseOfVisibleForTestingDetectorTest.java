package jp.co.worksap.oss.findbugs.guava;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junitx.util.PrivateAccessor;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.google.common.base.Supplier;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.visitclass.PreorderVisitor;


public class MisuseOfVisibleForTestingDetectorTest {
	
	@Mock
	private BugReporter reporter;
	@Mock
	private Supplier<BugInstance> bugInstanceSupplier;
	
	private MisuseOfVisibleForTestingDetector detector;
	
	@SuppressWarnings("javadoc")
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		detector = new MisuseOfVisibleForTestingDetector(reporter);
		detector.bugInstanceSupplier = bugInstanceSupplier;
	}
	
	/**
	 * Tests the detector with most combinations of visibility, static etc.
	 * @see MisuseOfVisibleForTestingDetectorTestObject
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {

		// get a suitable BCEL-class
		final ClassParser parser = new ClassParser(getClass().getResourceAsStream("/jp/co/worksap/oss/findbugs/guava/MisuseOfVisibleForTestingDetectorTestObject.class"), "MisuseOfVisibleForTestingDetectorTestObject");
		final JavaClass javaClass = parser.parse();
		// we want our own BugInstance-Stubs
		when(bugInstanceSupplier.get()).thenAnswer(new Answer<BugInstanceStub>() {
			public BugInstanceStub answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
				return new BugInstanceStub();
			}
		});
		
		// visit all fields
		for(final Field field : javaClass.getFields()) {
			// tell findbugs we're visiting a field
			PrivateAccessor.setField(detector, "visitingField", Boolean.TRUE);
			PrivateAccessor.setField(detector, "fieldName", field.getName());
			detector.visit(field);
			PrivateAccessor.setField(detector, "visitingField", Boolean.FALSE);
			PrivateAccessor.setField(detector, "fieldName", null);
		}
		// visit all methods
		for(final Method method : javaClass.getMethods()) {
			// tell findbugs we're visiting a method
			PrivateAccessor.setField(detector, "visitingMethod", Boolean.TRUE);
			PrivateAccessor.setField(detector, "methodName", method.getName());
			PrivateAccessor.setField(detector, "method", method);
			detector.visit(method);
			PrivateAccessor.setField(detector, "visitingMethod", Boolean.FALSE);
			PrivateAccessor.setField(detector, "methodName", null);
			PrivateAccessor.setField(detector, "method", null);
		}
		
		// 12 calls for fields
		verify(reporter, times(12)).reportBug(Matchers.argThat(new BugInstanceMatcher(1,1,0)));
		// 6 calls for Methods
		verify(reporter, times(6)).reportBug(Matchers.argThat(new BugInstanceMatcher(1,0,1)));
	
	}
	
	// BugInstances are considered equal if # of method calls are equal 
	private class BugInstanceMatcher extends BaseMatcher<BugInstance> {
		
		private int addMethod;
		private int addField;
		private int addClass;

		public BugInstanceMatcher(final int addClass, final int addField, final int addMethod) {
			this.addClass = addClass;
			this.addField = addField;
			this.addMethod = addMethod;
		}
		
		@Override
		public void describeTo(Description value) {
			value.appendText(String.format("Calls %s, %s, %s", Integer.valueOf(addClass), Integer.valueOf(addField), Integer.valueOf(addMethod)));
		}
		
		@Override
		public boolean matches(Object value) {
			BugInstanceStub instance = (BugInstanceStub) value;
			return instance.addClass==addClass && //
					instance.addField==addField && //
					instance.addMethod==addMethod;
		}
		
		
	}
	
	// count used method calls instead of using real BugInstance with all static shit
	private class BugInstanceStub extends BugInstance {
		private static final long serialVersionUID = 1L;

		int addClass;
		int addField;
		int addMethod;
		
		public BugInstanceStub() {
			super("MS_SHOULD_BE_FINAL", 0);
		}
		
		@Override
		public BugInstance addField(PreorderVisitor visitor) {
			addField++;
			return this;
		}
		
		@Override
		public BugInstance addClass(PreorderVisitor jclass) {
			addClass++;
			return this;
		}
		
		@Override
		public BugInstance addClassAndMethod(PreorderVisitor visitor) {
			addClass++;
			addMethod++;
			return this;
		}
		
		@Override
		public String toString() {
			return String.format("Calls %s, %s, %s", Integer.valueOf(addClass), Integer.valueOf(addField), Integer.valueOf(addMethod));
		}
		
	}
	
}
