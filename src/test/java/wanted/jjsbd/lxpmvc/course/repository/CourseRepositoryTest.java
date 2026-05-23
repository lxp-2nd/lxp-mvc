package wanted.jjsbd.lxpmvc.course.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import wanted.jjsbd.lxpmvc.config.JpaAuditingConfig;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.domain.CourseInstructor;
import wanted.jjsbd.lxpmvc.course.domain.Material;
import wanted.jjsbd.lxpmvc.course.domain.MaterialType;
import wanted.jjsbd.lxpmvc.course.domain.Section;
import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class CourseRepositoryTest {
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TestEntityManager em;

	@Test
	@DisplayName("이중 조인(@EntityGraph)을 시도하면 MultipleBagFetchException이 발생해야 한다.")
	void triggerMultipleBagFetchException() {
		// given: 강사 생성
		Member instructor = Member.createBasicMember("일타강사", "test@test.com", "password123!");
		memberRepository.save(instructor);

		// 👉 리팩토링: Member 객체 대신 CourseInstructor VO를 생성하여 전달합니다.
		CourseInstructor instructorInfo = new CourseInstructor(instructor.getId(), instructor.getNickname(),
			"일타강사 소개글");
		Course course = Course.createCourse(instructorInfo, "테스트 강의", "설명");

		Section section1 = Section.createSection(course, "섹션1", 1);
		section1.getMaterials().add(Material.createMaterial(section1, "자료1", MaterialType.DOCUMENT, "url1", 1));

		Section section2 = Section.createSection(course, "섹션2", 2);
		section2.getMaterials().add(Material.createMaterial(section2, "자료2", MaterialType.DOCUMENT, "url2", 1));

		course.getSections().add(section1);
		course.getSections().add(section2);

		// DB에 저장하고 영속성 컨텍스트(캐시)를 비워서, 다음 조회 시 무조건 쿼리가 날아가게 만듦
		courseRepository.saveAndFlush(course);

		// when
		System.out.println("====== 쿼리 실행 시작 ======");
		courseRepository.findByIdWithCurriculum(course.getId());
		System.out.println("====== 쿼리 실행 종료 ======");
	}

	@Test
	@DisplayName("로그인한 회원은 자신이 수강 중인 강의가 목록에서 제외되어야 한다.")
	void findAvailableCoursesForMember() {
		// =========================================================
		// 1. Given
		// 👉 리팩토링: 로직의 자연스러움을 위해 '강사'와 '학생'을 명확히 분리했습니다.
		// =========================================================
		Member instructor = Member.createBasicMember("강사", "instructor@test.com", "pw123!");
		em.persist(instructor);

		Member student = Member.createBasicMember("학생A", "student@test.com", "pw123!");
		em.persist(student);

		// 👉 리팩토링: VO를 통해 강사 정보를 주입합니다.
		CourseInstructor info = new CourseInstructor(instructor.getId(), instructor.getNickname(), "강사입니다.");
		Course course1 = Course.createCourse(info, "내가 수강한 강의", "설명");
		Course course2 = Course.createCourse(info, "내가 안 들은 강의", "설명");
		em.persist(course1);
		em.persist(course2);

		// 학생A가 course1만 수강 신청함
		Enrollment enrollment = Enrollment.createEnrollment(student, course1);
		em.persist(enrollment);

		// 영속성 컨텍스트 초기화
		em.flush();
		em.clear();

		// =========================================================
		// 2. When (실행: 학생A의 ID로 신청 가능한 강의 목록 조회)
		// =========================================================
		List<Course> result = courseRepository.findAvailableCoursesForMember("", student.getId());

		// =========================================================
		// 3. Then (검증)
		// =========================================================
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getTitle()).isEqualTo("내가 안 들은 강의");

		assertThat(result).extracting(Course::getTitle)
			.doesNotContain("내가 수강한 강의");
	}
}
