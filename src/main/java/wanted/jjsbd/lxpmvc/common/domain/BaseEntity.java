package wanted.jjsbd.lxpmvc.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@MappedSuperclass
/// 1. JPA Entity 클래스들이 이 클래스를 상속할 때, 이곳에 선언된 필드들도 ####컬럼#### 으로 인식하게 합니다. (단독으로 테이블이 생성되지는 않음)
@EntityListeners(AuditingEntityListener.class)
/// 2. JPA Auditing 기능을 이 클래스에 적용하여, 엔티티의 저장/수정 이벤트를 감지하게 합니다.
public abstract class BaseEntity {

    /**
     * @CreatedDate : 엔티티가 생성되어 데이터베이스에 처음 저장될 때, 현재 시간이 자동으로 주입됩니다.
     * updatable = false : 생성일시는 처음 한 번만 저장되어야 하므로, 이후 update 쿼리 시에는 값이 변경되지 않도록 막아줍니다.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void softDeleted() {
        this.deletedAt = LocalDateTime.now();
    }


}