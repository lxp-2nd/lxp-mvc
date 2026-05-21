package wanted.jjsbd.lxpmvc.course.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@DataJpaTest //h2 인메모리 사용.
class CourseRepositoryTest {
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TestEntityManager em;

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
}
