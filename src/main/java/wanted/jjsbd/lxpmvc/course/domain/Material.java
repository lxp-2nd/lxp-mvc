package wanted.jjsbd.lxpmvc.course.domain;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import wanted.jjsbd.lxpmvc.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "materials")
/// 삭제 요청 시 DELETE 대신 UPDATE 쿼리 실행
@SQLDelete(sql = "UPDATE courses SET deleted_at = NOW() WHERE id = ?")
/// 조회 요청(SELECT) 시 항상 deletedAt = false 인 것만 가져오도록 필터링
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Material extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", nullable = false, length = 20)
    private MaterialType materialType;

    // 명세서에 Not Null 조건이 없으므로 nullable 속성 제외 (기본값 true)
    @Column(name = "content_url", length = 255)
    private String contentUrl;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    private Material(Section section, String title, MaterialType materialType, String contentUrl, Integer sequence) {
        this.section = section;
        this.title = title;
        this.materialType = materialType;
        this.contentUrl = contentUrl;
        this.sequence = sequence;
    }

    public static Material createMaterial(Section section, String title, MaterialType materialType, String contentUrl, Integer sequence) {
        if (section == null) {
            throw new IllegalArgumentException("소속된 섹션 정보는 필수입니다.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("강의자료 제목은 필수입니다.");
        }
        if (materialType == null) {
            throw new IllegalArgumentException("자료 유형(VIDEO/DOCUMENT)은 필수입니다.");
        }
        if (sequence == null || sequence < 1) {
            throw new IllegalArgumentException("순서는 1 이상의 유효한 값이어야 합니다.");
        }

        return new Material(section, title, materialType, contentUrl, sequence);
    }

    public void updateMaterial(String title, MaterialType materialType, String contentUrl, Integer sequence) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("강의자료 제목은 비어있을 수 없습니다.");
        }
        if (materialType == null) {
            throw new IllegalArgumentException("자료 유형은 필수입니다.");
        }
        if (sequence == null || sequence < 1) {
            throw new IllegalArgumentException("순서는 1 이상의 유효한 값이어야 합니다.");
        }

        this.title = title;
        this.materialType = materialType;
        this.contentUrl = contentUrl;
        this.sequence = sequence;
    }
}