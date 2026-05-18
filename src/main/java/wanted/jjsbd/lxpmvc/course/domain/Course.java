package wanted.jjsbd.lxpmvc.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jjsbd.lxpmvc.common.domain.BaseTimeEntity;
import wanted.jjsbd.lxpmvc.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "courses")
/// BaseTimeEntity를 상속받음으로써, createdAt 필드를 묵시적으로 가지게 됩니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Member instructor;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 255)
    private String description;

    /// 1. 실제 데이터를 넣는 생성자는 private으로 숨겨서, 외부에서 new Course()를 마음대로 못하게 막습니다.
    private Course(Member instructor, String title, String description) {
        this.instructor = instructor;
        this.title = title;
        this.description = description;
    }
    /// 2. 정적 팩토리 메서드 (Static Factory Method)
    public static Course createCourse(Member instructor, String title, String description) {
        // 객체 생성 시점에 필요한 비즈니스 검증(Validation)을 여기서 처리할 수 있습니다.
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("강의 제목은 필수입니다.");
        }
        if (instructor == null) {
            throw new IllegalArgumentException("강사 정보는 필수입니다.");
        }

        return new Course(instructor, title, description);
    }
    /**
     * [비즈니스 메서드 - 강의 정보 수정]
     * 외부에서 setter로 값을 마구잡이로 바꾸는 게 아니라, 의도가 명확한 메서드를 통해 데이터를 변경합니다.
     */
    public void updateInfo(String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("강의 제목은 비어있을 수 없습니다.");
        }
        this.title = title;
        this.description = description;
    }
}