package wanted.jjsbd.lxpmvc.course.config;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.repository.CourseRepository;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Component
@RequiredArgsConstructor
public class DummyDataInitService {

	private final CourseRepository courseRepository;
	// TODO: MemberRepository가 완성되면 EntityManager 지울 예정
	private final EntityManager em;

	@Transactional // 여기서 트랜잭션 안전장치가 완벽하게 걸립니다.
	public void initData() {
		Member instructor = new Member();
		em.persist(instructor);

		// 강의 더미 데이터 생성 및 저장
		Course course1 = Course.createCourse(instructor, "서비스 기획 MVP", "기획의 모든 것을 배웁니다.");
		Course course2 = Course.createCourse(instructor, "스프링 부트 입문", "백엔드 개발의 기초.");
		Course course3 = Course.createCourse(instructor, "실전 코딩 테스트", "알고리즘 정복하기.");

		courseRepository.save(course1);
		courseRepository.save(course2);
		courseRepository.save(course3);
	}
}
