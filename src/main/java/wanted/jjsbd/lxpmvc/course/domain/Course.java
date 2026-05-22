package wanted.jjsbd.lxpmvc.course.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Section> sections = new ArrayList<>();

	private Course(Member instructor, String title, String description) {
		this.instructor = instructor;
		this.title = title;
		this.description = description;
	}

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
