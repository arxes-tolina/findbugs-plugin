package jp.co.worksap.oss.findbugs.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NullableLongColumn {
    @Column(nullable = true)
    private long longValue;
}
