package wanted.jjsbd.lxpmvc.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
// 1. [Unique 제약조건] 강의(Course) 내에서 순서(sequence)가 중복되지 않도록 복합 유니크 키를 설정합니다.
@Table(name = "sections", uniqueConstraints = {
        @UniqueConstraint(name = "uk_course_sequence", columnNames = {"course_id", "sequence"})
        /// sequence만 유니크키를 걸어두면 문제점: ex) sequence 1로 저장을 하면 어느 강의테이블에서도 sequence 1 만들짐 못함
        /// 그래서 복합 유니크키를 걸어둔 거임.
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    /**
     * DB 레벨에서 Foreign Key에 ON DELETE CASCADE 제약조건을 생성합니다
     * ex)
     * 기본 상태:
     * 🚨 "잠깐! 1번 강의를 부모로 삼고 있는 섹션들이 아직 살아있어! 부모를 먼저 죽일 순 없어! (에러 발생)"
     *
     * ON DELETE CASCADE 설정 시:
     * 🧹 "오케이, 1번 강의 지울게. 어? 밑에 딸린 섹션들이 있네? 내가 DB 엔진 권한으로 알아서 싹 다 지워줄게!"
     */
    @OnDelete(action = OnDeleteAction.CASCADE)
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