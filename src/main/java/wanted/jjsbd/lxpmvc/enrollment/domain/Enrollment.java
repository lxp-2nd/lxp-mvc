package wanted.jjsbd.lxpmvc.enrollment.domain;

import org.hibernate.annotations.Where;

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
import lombok.ToString;
import wanted.jjsbd.lxpmvc.common.domain.BaseEntity;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Entity(name = "enrollment")
@Table(
	name = "enrollment",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uk_member_course",
			columnNames = {"member_id", "course_id"}
		)
	})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)    // protected 기본 생성자
@ToString
@Where(clause = "deleted_at IS NULL")    // 삭제일시가 null인것만(삭제된 것 제외)
public class Enrollment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enrollment_id")
	private Long enrollmentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	private Enrollment(Member member, Course course) {
		this.member = member;
		this.course = course;
	}

	// 수강 도메인 생성(정적 팩토리 메서드)
	public static Enrollment createEnrollment(Member member, Course course) {
		Enrollment enrollment = new Enrollment(member, course);
		return enrollment;
	}

	@Override
	public void delete() {
		super.delete();
		// 수강 삭제시 필요 비즈니스 로직 있을 때 활용
	}

/*
	// JUnit Test 코드
	public int add(int num1, int num2) {
		return num1 + num2;
	}
 */
}
