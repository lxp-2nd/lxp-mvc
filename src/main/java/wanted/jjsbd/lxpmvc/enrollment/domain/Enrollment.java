package wanted.jjsbd.lxpmvc.enrollment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jjsbd.lxpmvc.common.domain.BaseEntity;
import wanted.jjsbd.lxpmvc.common.domain.DomainValidator;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Entity(name = "enrollment")
@Table(name = "enrollments", uniqueConstraints = {
	@UniqueConstraint(name = "uk_member_course", columnNames = {"member_id", "course_id"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)    // protected 기본 생성자
public class Enrollment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enrollment_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member learner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	private Enrollment(Member member, Course course) {
		this.learner = member;
		this.course = course;
	}

	// 수강 도메인 생성(정적 팩토리 메서드)
	public static Enrollment createEnrollment(Member member, Course course) {
		// 유효성검사(null check)
		DomainValidator.validateNotNull(member);
		DomainValidator.validateNotNull(course);

		Enrollment enrollment = new Enrollment(member, course);
		return enrollment;
	}
}

