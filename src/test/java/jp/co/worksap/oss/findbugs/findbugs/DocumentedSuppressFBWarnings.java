package jp.co.worksap.oss.findbugs.findbugs;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class DocumentedSuppressFBWarnings {
    @SuppressFBWarnings(justification = "only for unit test.")
    public void method() {
    }
}
