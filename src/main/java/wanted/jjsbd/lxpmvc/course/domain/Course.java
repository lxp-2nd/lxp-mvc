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

	private static final String DEFAULT_THUMBNAIL_URL = "/images/default-course.png";

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

	@Column(name = "thumbnail_url", length = 255)
	private String thumbnailUrl;

	@BatchSize(size = 100)
	@OrderBy("sequence ASC")
	@OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Section> sections = new ArrayList<>();

	// 테이블의 컬럼이 생기는 것 아님. JPA
	@Formula("(SELECT COUNT(*) FROM enrollments e WHERE e.course_id = id)")
	private Integer learnerCount;

	private Course(CourseInstructor instructor, String title, String description, String thumbnailUrl) {
		this.instructorInfo = instructor;
		this.title = title;
		this.description = description;
		this.thumbnailUrl = normalizeThumbnailUrl(thumbnailUrl);
	}

	public static Course createCourse(CourseInstructor instructor, String title, String description) {
		return createCourse(instructor, title, description, DEFAULT_THUMBNAIL_URL);
	}

	public static Course createCourse(
		CourseInstructor instructor,
		String title,
		String description,
		String thumbnailUrl
	) {
		validateCourseTitle(title);
		if (instructor == null) {
			throw new CustomException(ErrorCode.COURSE_INSTRUCTOR_REQUIRED);
		}
		return new Course(instructor, title, description, thumbnailUrl);
	}

	public void updateInfo(String title, String description) {
		validateCourseTitle(title);
		this.title = title;
		this.description = description;
	}

	public String getThumbnailUrl() {
		return normalizeThumbnailUrl(thumbnailUrl);
	}

	public void updateThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = normalizeThumbnailUrl(thumbnailUrl);
	}

	private static void validateCourseTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new CustomException(ErrorCode.COURSE_TITLE_REQUIRED);
		}
	}

	private static String normalizeThumbnailUrl(String thumbnailUrl) {
		if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
			return DEFAULT_THUMBNAIL_URL;
		}
		return thumbnailUrl;
	}
}
