package jp.co.worksap.oss.findbugs.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NullableDoubleColumn {
    @Column(nullable = true)
    private double doubleValue;
}
