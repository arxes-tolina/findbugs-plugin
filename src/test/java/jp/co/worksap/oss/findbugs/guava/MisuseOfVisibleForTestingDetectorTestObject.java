package jp.co.worksap.oss.findbugs.guava;

import com.google.common.annotations.VisibleForTesting;

/**
 * 
 * @author tolina GmbH
 *
 */
public class MisuseOfVisibleForTestingDetectorTestObject {

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	public static final String PUBLICSTATICFINALSTRING = "PUBLICSTATICFINALSTRING";
	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	protected static final String PROTECTEDSTATICFINALSTRING = "PROTECTEDSTATICFINALSTRING";
	/** OK */
	@VisibleForTesting
	static final String STATICFINALSTRING = "STATICFINALSTRING";
	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	private static final String PRIVATESTATICFINALSTRING = "PRIVATESTATICFINALSTRING";

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	public static String PUBLICSTATICSTRING = "PUBLICSTATICSTRING";
	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	protected static String PROTECTEDSTATICSTRING = "PROTECTEDSTATICSTRING";
	/** OK */
	@VisibleForTesting
	static String STATICSTRING = "STATICSTRING";
	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	private static String PRIVATESTATICSTRING = "PRIVATESTATICSTRING";

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	public final String PUBLICFINALSTRING = "PUBLICFINALSTRING";
	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	protected final String PROTECTEDFINALSTRING = "PROTECTEDFINALSTRING";
	/** OK */
	@VisibleForTesting
	final String FINALSTRING = "FINALSTRING";
	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	private final String PRIVATEFINALSTRING = "PRIVATEFINALSTRING";

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	public String PUBLICSTRING = "PUBLICSTRING";
	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	protected String PROTECTEDSTRING = "PROTECTEDSTRING";
	/** OK */
	@VisibleForTesting
	String STRING = "STRING";
	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	private String PRIVATESTRING = "PRIVATESTRING";

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	public static void PublicStaticMethod() {
		// nop
	}

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	protected static void ProtectedStaticMethod() {
		// nop
	}

	/** OK */
	@VisibleForTesting
	static void StaticMethod() {
		// nop
	}

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	private static void PrivateStaticMethod() {
		// nop
	}

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	public void PublicMethod() {
		// nop
	}

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	protected void ProtectedMethod() {
		// nop
	}

	/** OK */
	@VisibleForTesting
	void Method() {
		// nop
	}

	/** MISUSE OF ANNOTATION */
	@VisibleForTesting
	private void PrivateMethod() {
		// nop
	}
}
