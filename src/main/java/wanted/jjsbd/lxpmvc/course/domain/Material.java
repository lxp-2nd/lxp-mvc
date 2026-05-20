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
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;

@Entity
@Getter
@Table(name = "materials")
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
            throw new CustomException(ErrorCode.MATERIAL_SECTION_REQUIRED);
        }
        validateMaterialFields(title, materialType, sequence);

        return new Material(section, title, materialType, contentUrl, sequence);
    }

    public void updateMaterial(String title, MaterialType materialType, String contentUrl, Integer sequence) {
        validateMaterialFields(title, materialType, sequence);
        this.title = title;
        this.materialType = materialType;
        this.contentUrl = contentUrl;
        this.sequence = sequence;
    }

    private static void validateMaterialFields(String title, MaterialType materialType, Integer sequence) {
        if (title == null || title.isBlank()) {
            throw new CustomException(ErrorCode.MATERIAL_TITLE_REQUIRED);
        }
        if (materialType == null) {
            throw new CustomException(ErrorCode.MATERIAL_TYPE_REQUIRED);
        }
        if (sequence == null || sequence < 1) {
            throw new CustomException(ErrorCode.MATERIAL_SEQUENCE_INVALID);
        }
    }
}