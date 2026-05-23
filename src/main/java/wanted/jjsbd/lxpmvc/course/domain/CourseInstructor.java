package wanted.jjsbd.lxpmvc.course.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable // JPA에서 다른 엔티티에 삽입될 VO임을 선언
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseInstructor {

	@Column(name = "instructor_id", nullable = false)
	private Long memberId;

	@Column(name = "instructor_name", nullable = false)
	private String name;

	@Column(name = "instructor_intro", columnDefinition = "TEXT")
	private String introduction;

	public CourseInstructor(Long memberId, String name, String introduction) {
		this.memberId = memberId;
		this.name = name;
		this.introduction = introduction;
	}
}
