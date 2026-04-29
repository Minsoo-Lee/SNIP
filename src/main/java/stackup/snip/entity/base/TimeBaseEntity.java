package stackup.snip.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimeBaseEntity {

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    /**
     * Member => 같은 이메일은 삭제해도 재사용 불가
     *      왜? 이메일 = 그 사람의 정체성으로 간주
     * Answer => 삭제하면 새 row 생성 대신 기존 row를 복구하고 진행
     *      왜? 불필요한 row 생성 방지 및 동일 answer의 이력을 일관되게 관리
     */
    protected LocalDateTime deletedAt;

    protected LocalDateTime updatedAt;

    public void initUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
    }

    //== 수정일자 업데이트 ==//
    public void changeUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
