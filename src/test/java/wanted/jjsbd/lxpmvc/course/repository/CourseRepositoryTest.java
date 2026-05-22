package wanted.jjsbd.lxpmvc.course.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
	@DisplayName("강의 제목에 검색어 포함되어 있고 삭제되지 않은 강의 목록을 페이징하여 조회한다.")
	void findByTitleContainingAndDeletedAtIsNull() {

		//given
		Member instructor = Member.createBasicMember("일타강사", "instructor@example.com", "dummyPassword123!");
		memberRepository.save(instructor);

		Course course1 = Course.createCourse(instructor, "서비스 기획 MVP", "서비스 기획의 기초를 배웁니다.");
		Course course2 = Course.createCourse(instructor, "스프링 부트 입문", "백엔드 개발의 기초를 배웁니다.");

		courseRepository.save(course1);
		courseRepository.save(course2);

		// 첫번째 페이지(0), 페이지당 10개씩 가져오는 Pageable 객체 생성
		PageRequest pageRequest = PageRequest.of(0, 10);

		//when

		// "기획"이라는 키워드로 검색
		Page<Course> result = courseRepository.findByTitleContainingAndDeletedAtIsNull("기획", pageRequest);

		//then

		// "서비스 기획 MVP" 1개만 조회되어야함
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getTitle()).isEqualTo("서비스 기획 MVP");
	}

	@Test
	@DisplayName("검색어와 일치하는 강의가 없으면 빈 목록을 반환한다.")
	void findByTitleContaining_EmptyResult() {
		// given
		Member instructor = Member.createBasicMember("일타강사", "instructor@example.com", "dummyPassword123!");
		memberRepository.save(instructor);

		Course course1 = Course.createCourse(instructor, "서비스 기획 MVP", "기초를 배웁니다.");
		Course course2 = Course.createCourse(instructor, "스프링 부트 입문", "기초를 배웁니다.");
		courseRepository.save(course1);
		courseRepository.save(course2);

		PageRequest pageRequest = PageRequest.of(0, 10);

		// when: DB에 없는 "파이썬"이라는 키워드로 검색
		Page<Course> result = courseRepository.findByTitleContainingAndDeletedAtIsNull("파이썬", pageRequest);

		// then
		assertThat(result.getContent()).isEmpty(); // 데이터가 없어야 함
		assertThat(result.getTotalElements()).isEqualTo(0); // 전체 검색 결과 수 0개
		assertThat(result.getTotalPages()).isEqualTo(0); // 전체 페이지 수 0개
	}

	@Test
	@DisplayName("전체 데이터 수보다 범위를 벗어난 페이지를 요청하면 빈 목록을 반환한다.")
	void findByTitleContaining_PageOutOfBounds() {
		// given
		Member instructor = Member.createBasicMember("일타강사", "instructor@example.com", "dummyPassword123!");
		memberRepository.save(instructor);

		Course course1 = Course.createCourse(instructor, "스프링 부트 입문 1", "1편");
		Course course2 = Course.createCourse(instructor, "스프링 부트 입문 2", "2편");
		courseRepository.save(course1);
		courseRepository.save(course2);

		// 한 페이지당 10개씩 가져오는데, 2번째 페이지(index 1)를 요청함 (데이터는 총 2개뿐이므로 1페이지에 다 나옴)
		PageRequest pageRequest = PageRequest.of(1, 10);

		// when: "스프링"으로 검색
		Page<Course> result = courseRepository.findByTitleContainingAndDeletedAtIsNull("스프링", pageRequest);

		// then
		assertThat(result.getContent()).isEmpty(); // 해당 페이지에 보여줄 데이터는 없음
		assertThat(result.getNumber()).isEqualTo(1); // 현재 요청한 페이지 번호는 1
		assertThat(result.getTotalElements()).isEqualTo(2); // 하지만 전체 일치하는 데이터 수는 2개임을 보장해야 함
	}

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
