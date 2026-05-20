package wanted.jjsbd.lxpmvc.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jjsbd.lxpmvc.common.domain.BaseEntity;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;

@Entity
@Getter
// 1. [Unique 제약조건] 강의(Course) 내에서 순서(sequence)가 중복되지 않도록 복합 유니크 키를 설정합니다.
@Table(name = "sections", uniqueConstraints = {
        @UniqueConstraint(name = "uk_course_sequence", columnNames = {"course_id", "sequence"})
        /// sequence만 유니크키를 걸어두면 문제점: ex) sequence 1로 저장을 하면 어느 강의테이블에서도 sequence 1 만들짐 못함
        /// 그래서 복합 유니크키를 걸어둔 거임.
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section extends BaseEntity {

    private static final int MINIMUM_SEQUENCE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    private Section(Course course, String title, Integer sequence) {
        this.course = course;
        this.title = title;
        this.sequence = sequence;
    }

    public static Section createSection(Course course, String title, Integer sequence) {
        if (course == null) {
            throw new IllegalArgumentException("소속된 강의 정보는 필수입니다.");
        }
        validateSection(title, sequence);
        return new Section(course, title, sequence);
    }

    public void updateSection(String title, Integer sequence) {
        validateSection(title, sequence);
        this.title = title;
        this.sequence = sequence;
    }

    private static void validateSection(String title, Integer sequence) {
        if (title == null || title.isBlank()) {
            throw new CustomException(ErrorCode.SECTION_TITLE_REQUIRED);
        }
        if (sequence == null || sequence < MINIMUM_SEQUENCE) {
            throw new CustomException(ErrorCode.SECTION_SEQUENCE_INVALID);
        }
    }
}