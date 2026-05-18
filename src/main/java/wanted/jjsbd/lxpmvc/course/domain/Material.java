package wanted.jjsbd.lxpmvc.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import wanted.jjsbd.lxpmvc.common.domain.BaseTimeEntity;

@Entity
@Getter
@Table(name = "materials")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Material extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
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