package wanted.jjsbd.lxpmvc.course.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import wanted.jjsbd.lxpmvc.config.JpaAuditingConfig;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.domain.Material;
import wanted.jjsbd.lxpmvc.course.domain.MaterialType;
import wanted.jjsbd.lxpmvc.course.domain.Section;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class CourseRepositoryTest {
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("이중 조인(@EntityGraph)을 시도하면 MultipleBagFetchException이 발생해야 한다.")
	void triggerMultipleBagFetchException() {
		// given: 강사 생성
		Member instructor = Member.createBasicMember("일타강사", "test@test.com", "password123!");
		memberRepository.save(instructor);

		// given: 강의, 섹션 2개, 각 섹션당 자료 1개씩 생성
		Course course = Course.createCourse(instructor, "테스트 강의", "설명");

		Section section1 = Section.createSection(course, "섹션1", 1);
		section1.getMaterials().add(Material.createMaterial(section1, "자료1", MaterialType.DOCUMENT, "url1", 1));

		Section section2 = Section.createSection(course, "섹션2", 2);
		section2.getMaterials().add(Material.createMaterial(section2, "자료2", MaterialType.DOCUMENT, "url2", 1));

		course.getSections().add(section1);
		course.getSections().add(section2);

		// DB에 저장하고 영속성 컨텍스트(캐시)를 비워서, 다음 조회 시 무조건 쿼리가 날아가게 만듦
		courseRepository.saveAndFlush(course);

		// when
		// 🚨 여기서 이중 @EntityGraph 조인이 실행되며 하이버네이트가 예외를 터뜨립니다!
		System.out.println("====== 쿼리 실행 시작 ======");
		courseRepository.findByIdWithCurriculum(course.getId());
		System.out.println("====== 쿼리 실행 종료 ======");
	}
}
