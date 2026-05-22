package wanted.jjsbd.lxpmvc.course.config;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.domain.Material;
import wanted.jjsbd.lxpmvc.course.domain.MaterialType;
import wanted.jjsbd.lxpmvc.course.domain.Section;
import wanted.jjsbd.lxpmvc.course.repository.CourseRepository;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class DummyDataInitService {

	private final CourseRepository courseRepository;
	private final MemberRepository memberRepository;

	@Transactional // 여기서 트랜잭션 안전장치가 완벽하게 걸립니다.
	public void initData() {
		// 1. 강사 생성
		Member instructor = Member.createBasicMember("김강사", "instructor@example.com", "dummyPassword123!");
		memberRepository.save(instructor);

		Course course1 = Course.createCourse(instructor, "서비스 기획 MVP", "강의 설명 영역. 강의 목표를 확인한다.");

		// [섹션 1]
		Section section1 = Section.createSection(course1, "섹션 1. 강의 소개", 1);
		section1.getMaterials()
			.add(Material.createMaterial(section1, "01. 강의자료 제목", MaterialType.DOCUMENT, "https://example.com/1", 1));
		section1.getMaterials()
			.add(Material.createMaterial(section1, "02. 강의자료 제목", MaterialType.DOCUMENT, "https://example.com/2", 2));

		// Course에 섹션 1 추가
		course1.getSections().add(section1);

		// [섹션 2] 생성 및 자료 추가
		Section section2 = Section.createSection(course1, "섹션 2. 핵심 개념", 2);
		section2.getMaterials()
			.add(Material.createMaterial(section2, "01. 강의자료 제목", MaterialType.DOCUMENT, "https://example.com/3", 1));
		section2.getMaterials()
			.add(Material.createMaterial(section2, "02. 강의자료 제목", MaterialType.DOCUMENT, "https://example.com/4", 2));

		// Course에 섹션 2 추가
		course1.getSections().add(section2);

		// 목록용
		Course course2 = Course.createCourse(instructor, "프론트엔드 입문", "프론트엔드 기초를 배웁니다.");
		Course course3 = Course.createCourse(instructor, "스프링 부트 실전", "백엔드 개발의 모든 것.");

		// 💡 Cascade 마법: 자식 데이터(섹션, 자료)를 부모 리스트에 넣어두기만 하면,
		// 부모(Course)를 save 할 때 알아서 자식들까지 DB에 INSERT 됩니다!
		courseRepository.save(course1);
		courseRepository.save(course2);
		courseRepository.save(course3);
	}
}
