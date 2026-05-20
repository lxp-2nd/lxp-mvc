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
@NoArgsConstructor(access = AccessLevel.PROTECTED)	// protected 기본 생성자
@ToString
@Where(clause = "deleted_at IS NULL")	// 삭제일시가 null인것만(삭제된 것 제외)
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

	/*
	* BaseEntity: 생성일시(createdAt), 수정일시(modifiedAt), 삭제일시(deletedAt) - delete() Method 활용
	* */

	// 기본생성자(protected 접근자, @NoArgsConstructor 활용 생성
	// ToString(@ToString 활용)
	// Getter(@Getter 활용)

	private Enrollment(Member member, Course course) {
		this.member = member;
		this.course = course;
	}

	// 수강 도메인 자신이 해야할 일?
	// 수강을 생성한다. -> createEnrollment
	// 수강을 삭제한다.(soft delete로 DB에는 남아있음, BaseEntity delete 활용) -> delete
	// 수강을 변경한다.(해당 없음)

	// 수강 도메인 생성(정적 팩토리 메서드)
	public static Enrollment createEnrollment(Member member, Course course) {
		Enrollment enrollment = new Enrollment(member, course);
		return enrollment;
	}

	public void delete() {
		// pass
	}

	// 수강 생성 -> 정적팩토리 메서드
	// 중복 신청 방지 -> unique 제약만 있으면 되나?
	// 수강 목록 조회
	// 수강 화면 조회
	// 로그인한 회원만 강의를 신청할 수 있다.
	// 동일 수강생은 동일 강의를 중복 신청할 수 없다.
	// 장바구니에서 신청한 강의는 수강 신청 생성 후 장바구니에서 제거된다.

/*
	// JUnit Test 코드
	public int add(int num1, int num2) {
		return num1 + num2;
	}
 */
}
