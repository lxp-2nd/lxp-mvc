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
import jakarta.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import wanted.jjsbd.lxpmvc.common.domain.BaseEntity;

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
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("섹션 제목은 필수입니다.");
        }
        if (sequence == null || sequence < 1) {
            throw new IllegalArgumentException("순서는 1 이상의 유효한 값이어야 합니다.");
        }
        return new Section(course, title, sequence);
    }

    public void updateSection(String title, Integer sequence) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("섹션 제목은 비어있을 수 없습니다.");
        }
        if (sequence == null || sequence < 1) {
            throw new IllegalArgumentException("순서는 1 이상의 유효한 값이어야 합니다.");
        }

        this.title = title;
        this.sequence = sequence;
    }
}