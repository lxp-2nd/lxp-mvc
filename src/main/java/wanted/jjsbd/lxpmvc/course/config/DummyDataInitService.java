package wanted.jjsbd.lxpmvc.course.config;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.domain.Material;
import wanted.jjsbd.lxpmvc.course.domain.MaterialType;
import wanted.jjsbd.lxpmvc.course.domain.Section;
import wanted.jjsbd.lxpmvc.course.repository.CourseRepository;
import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class DummyDataInitService {

	private final CourseRepository courseRepository;
	private final MemberRepository memberRepository;
	// 👉 동료가 만든 Repository 대신, JPA의 본체인 EntityManager를 직접 주입받습니다!
	private final EntityManager em;

	@Transactional
	public void initData() {
		// ==========================================
		// 1. 회원(강사 및 학생) 데이터 생성
		// ==========================================
		Member instructor = Member.createBasicMember("김강사", "instructor@example.com", "dummyPassword123!");

		Member student1 = Member.createBasicMember("학생A", "student1@example.com", "pw123!");
		Member student2 = Member.createBasicMember("학생B", "student2@example.com", "pw123!");
		Member student3 = Member.createBasicMember("학생C", "student3@example.com", "pw123!");

		memberRepository.saveAll(List.of(instructor, student1, student2, student3));

		// ==========================================
		// 2. 강의(Course) 데이터 생성
		// ==========================================
		Course course1 = Course.createCourse(instructor, "서비스 기획 MVP", "강의 설명 영역. 강의 목표를 확인한다.");

		Section section1 = Section.createSection(course1, "섹션 1. 강의 소개", 1);
		section1.getMaterials()
			.add(Material.createMaterial(section1, "01. 강의자료 제목", MaterialType.DOCUMENT, "https://example.com/1", 1));
		section1.getMaterials()
			.add(Material.createMaterial(section1, "02. 강의자료 제목", MaterialType.DOCUMENT, "https://example.com/2", 2));
		course1.getSections().add(section1);

		Section section2 = Section.createSection(course1, "섹션 2. 핵심 개념", 2);
		section2.getMaterials()
			.add(Material.createMaterial(section2, "01. 강의자료 제목", MaterialType.DOCUMENT, "https://example.com/3", 1));
		section2.getMaterials()
			.add(Material.createMaterial(section2, "02. 강의자료 제목", MaterialType.DOCUMENT, "https://example.com/4", 2));
		course1.getSections().add(section2);

		Course course2 = Course.createCourse(instructor, "프론트엔드 입문", "프론트엔드 기초를 배웁니다.");
		Course course3 = Course.createCourse(instructor, "스프링 부트 실전", "백엔드 개발의 모든 것.");

		courseRepository.saveAll(List.of(course1, course2, course3));

		// ==========================================
		// 3. 수강 신청(Enrollment) 데이터 생성 (EntityManager 사용)
		// ==========================================
		Enrollment e1 = Enrollment.createEnrollment(student1, course1);
		Enrollment e2 = Enrollment.createEnrollment(student2, course1);
		Enrollment e3 = Enrollment.createEnrollment(student3, course1);
		Enrollment e4 = Enrollment.createEnrollment(student1, course2);

		// 👉 Repository의 save() 역할인 em.persist()를 사용하여 직접 영속성 컨텍스트에 저장합니다.
		List.of(e1, e2, e3, e4).forEach(em::persist);
	}
}
