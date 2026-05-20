package wanted.jjsbd.lxpmvc.course.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jjsbd.lxpmvc.common.domain.BaseEntity;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Entity
@Getter
@Table(name = "courses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseEntity {

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
        validateCourseTitle(title);
        if (instructor == null) {
            throw new CustomException(ErrorCode.COURSE_INSTRUCTOR_REQUIRED);
        }
        return new Course(instructor, title, description);
    }

    public void updateInfo(String title, String description) {
        validateCourseTitle(title);
        this.title = title;
        this.description = description;
    }
    private static void validateCourseTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new CustomException(ErrorCode.COURSE_TITLE_REQUIRED);
        }
    }
}