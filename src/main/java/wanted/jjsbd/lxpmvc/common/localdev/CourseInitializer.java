package wanted.jjsbd.lxpmvc.common.localdev;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.domain.CourseInstructor;
import wanted.jjsbd.lxpmvc.course.domain.Material;
import wanted.jjsbd.lxpmvc.course.domain.MaterialType;
import wanted.jjsbd.lxpmvc.course.domain.Section;
import wanted.jjsbd.lxpmvc.course.repository.CourseRepository;
import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class CourseInitializer {

	private final CourseRepository courseRepository;
	private final MemberRepository memberRepository;
	// 👉 동료가 만든 Repository 대신, JPA의 본체인 EntityManager를 직접 주입받습니다!
	private final EntityManager em;

	@Transactional
	public void initData() {
		// ==========================================
		// 1. 회원 데이터 생성 (기존과 동일)
		// ==========================================
		Member instructor = Member.createBasicMember("김강사", "instructor@example.com", "dummyPassword123!");
		Member student1 = Member.createBasicMember("학생A", "student1@example.com", "pw123!");
		Member student2 = Member.createBasicMember("학생B", "student2@example.com", "pw123!");
		Member student3 = Member.createBasicMember("학생C", "student3@example.com", "pw123!");

		memberRepository.saveAll(List.of(instructor, student1, student2, student3));

		// ==========================================
		// 2. 강의 데이터 생성 (★새로운 VO 기반으로 강사 소개글 직접 주입★)
		// ==========================================

		// 1번 강의
		CourseInstructor info1 = new CourseInstructor(
			instructor.getId(),
			instructor.getNickname(),
			"10년 차 대규모 아키텍트이자 핀테크 플랫폼 리드 개발자 김강사입니다. 실무 MVP 구축의 핵심 A부터 Z까지 짚어드립니다."
		);
		Course course1 = Course.createCourse(info1, "서비스 기획 MVP", "강의 설명 영역. 강의 목표를 확인한다.");

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

		// 2번 강의
		CourseInstructor info2 = new CourseInstructor(
			instructor.getId(),
			instructor.getNickname(),
			"자바 백엔드와 모던 프론트엔드 생태계를 모두 아우르는 풀스택 마스터 김강사입니다."
		);
		Course course2 = Course.createCourse(info2, "프론트엔드 입문", "프론트엔드 기초를 배웁니다.");

		// 3번 강의
		CourseInstructor info3 = new CourseInstructor(
			instructor.getId(),
			instructor.getNickname(),
			"스프링 부트와 최적화 인프라 아키텍처의 명가, 대한민국 백엔드의 기준을 세우는 김강사입니다."
		);
		Course course3 = Course.createCourse(info3, "스프링 부트 실전", "백엔드 개발의 모든 것.");

		List<Course> courses = new ArrayList<>(List.of(course1));
		for (int index = 1; index <= 10; index++) {
			courses.add(createCourseLikeCourse1(info1, index));
		}
		courses.addAll(List.of(course2, course3));

		courseRepository.saveAll(courses);
		// ==========================================
		// 3. 수강 신청 데이터 생성 (기존과 동일)
		// ==========================================
		Enrollment e1 = Enrollment.createEnrollment(student1, course1);
		Enrollment e2 = Enrollment.createEnrollment(student2, course1);
		Enrollment e3 = Enrollment.createEnrollment(student3, course1);
		Enrollment e4 = Enrollment.createEnrollment(student1, course2);

		List.of(e1, e2, e3, e4).forEach(em::persist);
	}

	private Course createCourseLikeCourse1(CourseInstructor instructorInfo, int index) {
		Course course = Course.createCourse(
			instructorInfo,
			"서비스 기획 MVP " + index,
			"강의 설명 영역. 강의 목표를 확인한다."
		);

		Section section1 = Section.createSection(course, "섹션 1. 강의 소개", 1);
		section1.getMaterials()
			.add(Material.createMaterial(section1, "01. 강의자료 제목", MaterialType.DOCUMENT,
				"https://example.com/course-" + index + "/1", 1));
		section1.getMaterials()
			.add(Material.createMaterial(section1, "02. 강의자료 제목", MaterialType.DOCUMENT,
				"https://example.com/course-" + index + "/2", 2));
		course.getSections().add(section1);

		Section section2 = Section.createSection(course, "섹션 2. 핵심 개념", 2);
		section2.getMaterials()
			.add(Material.createMaterial(section2, "01. 강의자료 제목", MaterialType.DOCUMENT,
				"https://example.com/course-" + index + "/3", 1));
		section2.getMaterials()
			.add(Material.createMaterial(section2, "02. 강의자료 제목", MaterialType.DOCUMENT,
				"https://example.com/course-" + index + "/4", 2));
		course.getSections().add(section2);

		return course;
	}
}
