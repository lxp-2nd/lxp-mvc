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

@DataJpaTest //h2 인메모리 사용.
class CourseRepositoryTest {
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TestEntityManager em;

	@Test
	@DisplayName("강의 제목에 검색어 포함되어 있고 삭제되지 않은 강의 목록을 페이징하여 조회한다.")
	void findByTitleContainingAndDeletedAtIsNull() {

		//given
		Member instructor = new Member();
		em.persist(instructor);

		Course course1 = Course.createCourse(instructor, "서비스 기획 MVP", "서비스 기획의 기초를 배웁니다.");
		Course course2 = Course.createCourse(instructor, "스프링 부트 입문", "백엔드 개발의 기초를 배웁니다.");
		em.persist(course1);
		em.persist(course2);

		// 영속성 컨택스트의 변경 사항을 DB에 반영하고 비워서, 캐시가 아닌 실제 쿼리가 나가는지 확인
		em.flush();
		em.clear();

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