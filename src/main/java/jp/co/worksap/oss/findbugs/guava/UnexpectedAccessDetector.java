package jp.co.worksap.oss.findbugs.guava;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.annotations.VisibleForTesting;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.AccessibleEntity;
import edu.umd.cs.findbugs.ba.ClassMember;
import edu.umd.cs.findbugs.ba.XFactory;
import edu.umd.cs.findbugs.ba.XField;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.analysis.AnnotationValue;

/**
 * <p>A detector to ensure that implementation (class in src/main/java) doesn't call
 * package-private method in other class which is annotated by {@code @VisibleForTesting}.</p>
 *
 * @author Kengo TODA (toda_k@worksap.co.jp)
 * @see com.google.common.annotations.VisibleForTesting
 */
public class UnexpectedAccessDetector extends BytecodeScanningDetector {

	private static final String GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING = "GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING";
	@VisibleForTesting
	static final String VISIBLE_FOR_TESTING_ANNOTATION = "com/google/common/annotations/VisibleForTesting:{}";

	@Nonnull
	private final BugReporter bugReporter;

	private final transient Log log = LogFactory.getLog(this.getClass());

	/**
	 * @param bugReporter
	 */
	public UnexpectedAccessDetector(final @Nonnull BugReporter bugReporter) {
		this.bugReporter = checkNotNull(bugReporter);
	}

	@Override
	public void sawOpcode(final int opcode) {
		if (isWorkingOnAField(opcode)) {
			final XField invokedField = XFactory.createReferencedXField(this);
			final ClassDescriptor currentClass = getClassDescriptor();
			if (hasToCheck(invokedField, currentClass)) {
				checkField(invokedField);
			}
		}
		if (isInvoking(opcode)) {
			final XMethod invokedMethod = XFactory.createReferencedXMethod(this);
			final ClassDescriptor currentClass = getClassDescriptor();
			if (hasToCheck(invokedMethod, currentClass)) {
				checkMethod(invokedMethod);
			}
		}
	}

	/**
	 * @param member 
	 * @param currentClass
	 * @return true, if the member is called within the same package 
	 */
	private boolean hasToCheck(final @Nonnull ClassMember member, final @Nonnull ClassDescriptor currentClass) {
		if (currentClass.equals(member.getClassDescriptor())) {
			log.debug("No check: " + member.getName() + " is called within the same class: " + currentClass.getClassName());
			// no need to check, because method is called by owner
			return false;
		} else if (!member.getPackageName().equals(currentClass.getPackageName())) {
			log.debug("No check: " + member.getName() + " is called from outside the its package. Member package: " //
					+ member.getPackageName() + ". Caller package: " + currentClass.getPackageName());
			// no need to check, because method is called by class in other package
			return false;
		}
		return true;
	}

	@VisibleForTesting
	void checkMethod(final @Nonnull XMethod invokedMethod) {
		if (checkVisibility(invokedMethod)) {
			checkAnnotations(invokedMethod.getAnnotations(), true);
		} else {
			log.debug("No check: Method " + invokedMethod.getName() + " it not package visible.");
		}
	}

	@VisibleForTesting
	void checkField(final @Nonnull XField invokedField) {
		if (checkVisibility(invokedField)) {
			checkAnnotations(invokedField.getAnnotations(), true);
		} else {
			log.debug("No check: Field " + invokedField.getName() + " it not package visible.");
		}
	}

	/**
	 * checks a collection of annotations for VisibleForTesting
	 * @param annotations from method or field
	 * @param reportCaller should report be added to {@link BugInstance}, for testing
	 */
	@VisibleForTesting
	void checkAnnotations(final Collection<AnnotationValue> annotations, final boolean reportCaller) {
		for (AnnotationValue annotationValue : annotations) {
			if (VISIBLE_FOR_TESTING_ANNOTATION.equals(annotationValue.toString())) {
				final BugInstance bug = new BugInstance(this, GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING, HIGH_PRIORITY);
				if (reportCaller) {
					bug.addClassAndMethod(this).addSourceLine(this);
				}
				bugReporter.reportBug(bug);
			}
		}
	}

	/**
	 * @return true if visibility of specified AccessibleEntity, implemented by method or field, is package-private.
	 */
	@VisibleForTesting
	boolean checkVisibility(final @Nonnull AccessibleEntity entry) {
		return !(entry.isPrivate() || entry.isProtected() || entry.isPublic());
	}

	@VisibleForTesting
	boolean isInvoking(int opcode) {
		return opcode == INVOKESPECIAL || opcode == INVOKEINTERFACE || opcode == INVOKESTATIC || opcode == INVOKEVIRTUAL;
	}

	@VisibleForTesting
	boolean isWorkingOnAField(final int opcode) {
		return opcode == GETFIELD || opcode == PUTFIELD;
	}
}
