package wanted.jjsbd.lxpmvc.enrollment.config;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.repository.CourseRepository;
import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;
import wanted.jjsbd.lxpmvc.enrollment.repository.EnrollmentRepository;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class EnrollmentDummyDataInitService {
	private final MemberRepository memberRepository;
	private final CourseRepository courseRepository;
	private final EnrollmentRepository enrollmentRepository;

	@Transactional
	public void initData() {
		// 1. [독립 데이터] 프론트엔드 강사 세팅 (이메일 중복 방지)
		Member frontendInstructor = Member.createBasicMember("프론트일타", "frontend@example.com", "dummyPassword123!");
		memberRepository.save(frontendInstructor);

		// 2. [독립 데이터] 프론트엔드 강의 세팅
		Course reactCourse = Course.createCourse(frontendInstructor, "리액트 실전 마스터", "상태 관리부터 성능 최적화까지");
		Course nextJsCourse = Course.createCourse(frontendInstructor, "Next.js 완벽 가이드", "SSR과 SEO 정복하기");
		courseRepository.save(reactCourse);
		courseRepository.save(nextJsCourse);

		// 3. [독립 데이터] 수강생 세팅
		Member learner1 = Member.createBasicMember("열혈수강생", "learner1@example.com", "learnerPass12!");
		Member learner2 = Member.createBasicMember("초보개발자", "learner2@example.com", "learnerPass34!");
		memberRepository.save(learner1);
		memberRepository.save(learner2);

		// 4. 수강신청 세팅 (새롭게 만든 데이터들끼리만 연결)
		Enrollment enrollment1 = Enrollment.createEnrollment(learner1, reactCourse);
		Enrollment enrollment2 = Enrollment.createEnrollment(learner1, nextJsCourse);
		Enrollment enrollment3 = Enrollment.createEnrollment(learner2, reactCourse);

		enrollmentRepository.save(enrollment1);
		enrollmentRepository.save(enrollment2);
		enrollmentRepository.save(enrollment3);
	}
}
