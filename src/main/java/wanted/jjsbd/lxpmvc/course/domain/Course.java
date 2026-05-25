package wanted.jjsbd.lxpmvc.course.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Formula;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jjsbd.lxpmvc.common.domain.BaseEntity;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;

@Entity
@Getter
@Table(name = "courses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	// ✅ 팀장님 안: 강사 정보를 내 도메인 내의 VO(@Embedded)로 관리
	@Embedded
	private CourseInstructor instructorInfo;

	@Column(name = "title", nullable = false, length = 200)
	private String title;

	@Column(name = "description", length = 255)
	private String description;

	@BatchSize(size = 100)
	@OrderBy("sequence ASC")
	@OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Section> sections = new ArrayList<>();

	// 테이블의 컬럼이 생기는 것 아님. JPA
	@Formula("(SELECT COUNT(*) FROM enrollments e WHERE e.course_id = id)")
	private Integer learnerCount;

	private Course(CourseInstructor instructor, String title, String description) {
		this.instructorInfo = instructor;
		this.title = title;
		this.description = description;
	}

	public static Course createCourse(CourseInstructor instructor, String title, String description) {
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
