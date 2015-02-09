package jp.co.worksap.oss.findbugs.guava;

import java.util.Arrays;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.Method;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;

/**
 * <p>
 * Detector to check the corect usage of <code>@VisibleForTesting</code>.
 * </p>
 * 
 * @author tolina GmbH
 */
public class MisuseOfVisibleForTestingDetector extends BytecodeScanningDetector {

	/** Create the BugInstance by Supplier for testability */
	@VisibleForTesting
	Supplier<BugInstance> bugInstanceSupplier = new Supplier<BugInstance>() {
		@Override
		public BugInstance get() {
			return  new BugInstance(MisuseOfVisibleForTestingDetector.this, GUAVA_MISUSE_OF_VISIBLE_FOR_TESTING, HIGH_PRIORITY);
		}
	};
	
	private static final String VISIBLE_FOR_TESTING_ANNOTATION_PATTERN = "Lcom/google/common/annotations/VisibleForTesting;";
	private static final String GUAVA_MISUSE_OF_VISIBLE_FOR_TESTING = "GUAVA_MISUSE_OF_VISIBLE_FOR_TESTING";

	private final BugReporter reporter;

	private final static Predicate<AnnotationEntry> hasVisibleForTesting = new Predicate<AnnotationEntry>() {
		@Override
		public boolean apply(final AnnotationEntry input) {
			return VISIBLE_FOR_TESTING_ANNOTATION_PATTERN.equals(input.getAnnotationType());
		}
	};

	public MisuseOfVisibleForTestingDetector(final BugReporter reporter) {
		this.reporter = reporter;
	}

	@Override
	public void visit(final Field obj) {
		if (hasVisibleForTestingAnnotation(obj) && !isPackageVisible(obj)) {
			final BugInstance bug = bugInstanceSupplier.get();
			bug.addClass(this);
			bug.addField(this);
			reporter.reportBug(bug);
		}
	}

	/**
	 * @param obj
	 * @return <code>true</code> if {@link FieldOrMethod} is annotated with {@link VisibleForTesting}
	 */
	private boolean hasVisibleForTestingAnnotation(final FieldOrMethod obj) {
		return Iterables.tryFind(Arrays.asList(obj.getAnnotationEntries()), hasVisibleForTesting).isPresent();
	}

	@Override
	public void visit(final Method obj) {
		if (hasVisibleForTestingAnnotation(obj) && !isPackageVisible(obj)) {
			final BugInstance bug = bugInstanceSupplier.get();
			bug.addClassAndMethod(this); //.addSourceLine(this);
			reporter.reportBug(bug);
		}
	}

	/**
	 * @return <code>true</code> if visibility of specified {@link FieldOrMethod} is package-private.
	 */
	private boolean isPackageVisible(final @Nonnull FieldOrMethod entry) {
		return !(entry.isPrivate() || entry.isProtected() || entry.isPublic());
	}
}
