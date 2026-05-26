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

	private static final String DUMMY_PASSWORD = "dummyPassword123!";

	private final CourseRepository courseRepository;
	private final MemberRepository memberRepository;
	private final EntityManager em;

	@Transactional
	public void initData() {
		Member student1 = Member.createBasicMember("학생A", "student1@example.com", "pw123!");
		Member student2 = Member.createBasicMember("학생B", "student2@example.com", "pw123!");
		Member student3 = Member.createBasicMember("학생C", "student3@example.com", "pw123!");

		List<CourseSeed> courseSeeds = createCourseSeeds();
		List<Member> instructors = createInstructors(courseSeeds);
		List<Member> members = new ArrayList<>(List.of(student1, student2, student3));
		members.addAll(instructors);

		memberRepository.saveAll(members);

		List<Course> courses = createCourses(courseSeeds, instructors);
		courseRepository.saveAll(courses);

		List.of(
			Enrollment.createEnrollment(student1, courses.get(0)),
			Enrollment.createEnrollment(student2, courses.get(0)),
			Enrollment.createEnrollment(student3, courses.get(0)),
			Enrollment.createEnrollment(student1, courses.get(1))
		).forEach(em::persist);
	}

	private List<Member> createInstructors(List<CourseSeed> courseSeeds) {
		return courseSeeds.stream()
			.map(seed -> Member.createInstructorMember(seed.instructorName(), seed.instructorEmail(), DUMMY_PASSWORD))
			.toList();
	}

	private List<Course> createCourses(List<CourseSeed> courseSeeds, List<Member> instructors) {
		List<Course> courses = new ArrayList<>();
		for (int index = 0; index < courseSeeds.size(); index++) {
			CourseSeed seed = courseSeeds.get(index);
			Member instructor = instructors.get(index);
			CourseInstructor instructorInfo = new CourseInstructor(
				instructor.getId(),
				instructor.getNickname(),
				seed.instructorIntro()
			);
			Course course = Course.createCourse(
				instructorInfo,
				seed.title(),
				seed.description(),
				getCourseThumbnailUrl(index + 1)
			);
			addSections(course, createSections(seed.focus()));
			courses.add(course);
		}
		return courses;
	}

	private void addSections(Course course, List<SectionSeed> sectionSeeds) {
		for (int sectionIndex = 0; sectionIndex < sectionSeeds.size(); sectionIndex++) {
			SectionSeed sectionSeed = sectionSeeds.get(sectionIndex);
			Section section = Section.createSection(course, sectionSeed.title(), sectionIndex + 1);

			for (int materialIndex = 0; materialIndex < sectionSeed.materials().size(); materialIndex++) {
				MaterialSeed materialSeed = sectionSeed.materials().get(materialIndex);
				section.getMaterials()
					.add(Material.createMaterial(section, materialSeed.title(), MaterialType.DOCUMENT,
						materialSeed.contentUrl(), materialIndex + 1));
			}
			course.getSections().add(section);
		}
	}

	private List<CourseSeed> createCourseSeeds() {
		return List.of(
			course("김도윤", "doyoon.kim@example.com", "B2B SaaS와 커머스 MVP를 출시해 온 프로덕트 전략가입니다.",
				"서비스 기획 MVP 실전", "아이디어 검증부터 지표 설계까지 MVP 기획 전 과정을 다룹니다.", "MVP 기획"),
			course("박서연", "seoyeon.park@example.com", "디자인 시스템과 접근성을 중심으로 제품을 만든 프론트엔드 엔지니어입니다.",
				"프론트엔드 입문: HTML, CSS, JavaScript", "웹 문서 구조, 반응형 레이아웃, 기본 인터랙션을 익힙니다.", "프론트엔드"),
			course("이준호", "junho.lee@example.com", "대규모 트래픽 서비스를 운영하며 백엔드 아키텍처를 설계했습니다.",
				"스프링 부트 실전 API 개발", "도메인 모델링, JPA, 예외 처리, 보안 설정을 실무 흐름으로 학습합니다.", "스프링 부트"),
			course("정하린", "harin.jung@example.com", "데이터 기반 CRM과 퍼널 분석을 담당한 그로스 마케터입니다.",
				"데이터로 개선하는 그로스 마케팅", "획득, 활성화, 전환, 재방문 퍼널을 데이터로 분석하고 실험합니다.", "그로스 마케팅"),
			course("최민재", "minjae.choi@example.com", "데이터 파이프라인과 분석 환경을 구축한 데이터 엔지니어입니다.",
				"SQL과 데이터 분석 입문", "조회, 집계, 조인, 리포트 작성까지 실무 SQL 분석 능력을 기릅니다.", "SQL 분석"),
			course("한유진", "yujin.han@example.com", "React와 Next.js 서비스의 성능 개선과 디자인 시스템 구축을 이끌었습니다.",
				"React 컴포넌트 설계", "상태, props, 합성 패턴으로 유지보수하기 쉬운 컴포넌트를 설계합니다.", "React 컴포넌트"),
			course("오지훈", "jihoon.oh@example.com", "클라우드 인프라와 CI/CD 파이프라인을 운영한 DevOps 엔지니어입니다.",
				"Docker와 배포 자동화", "개발 환경을 컨테이너화하고 반복 가능한 배포 파이프라인을 구성합니다.", "Docker 배포"),
			course("서민아", "mina.seo@example.com", "모바일 앱과 웹 서비스의 UX 리서치와 프로토타이핑을 담당했습니다.",
				"UX 리서치와 프로토타이핑", "사용자 문제를 발견하고 검증 가능한 프로토타입으로 해결책을 구체화합니다.", "UX 리서치"),
			course("문태현", "taehyun.moon@example.com", "백엔드 보안 진단과 인증 시스템 개선 프로젝트를 수행했습니다.",
				"웹 보안과 인증 기초", "세션, 쿠키, 접근 제어, 입력 검증 중심으로 웹 보안 기본기를 다집니다.", "웹 보안"),
			course("배수진", "sujin.bae@example.com", "고객 지원 자동화와 업무 도구 개선을 이끈 운영 기획자입니다.",
				"업무 자동화를 위한 노코드 입문", "반복 업무를 분석하고 폼, 시트, 자동화 도구로 효율화합니다.", "노코드 자동화"),
			course("강예린", "yerin.kang@example.com", "브랜드 콘텐츠와 제품 메시지를 함께 설계하는 콘텐츠 전략가입니다.",
				"콘텐츠 마케팅 카피라이팅", "제품 가치를 명확히 전달하는 메시지 구조와 채널별 글쓰기를 학습합니다.", "콘텐츠 마케팅"),
			course("윤서준", "seojun.yoon@example.com", "애자일 코치와 스크럼 마스터 역할을 수행한 프로젝트 리더입니다.",
				"애자일 프로젝트 운영", "스프린트 계획, 데일리 스크럼, 회고로 팀 실행 리듬을 만듭니다.", "애자일 운영"),
			course("임태오", "taeo.lim@example.com", "머신러닝 모델을 업무 서비스에 적용해 온 AI 실무 컨설턴트입니다.",
				"비전공자를 위한 AI 활용 기초", "AI 도구의 원리를 이해하고 업무 문제에 맞는 프롬프트를 설계합니다.", "AI 활용")
		);
	}

	private CourseSeed course(
		String instructorName,
		String instructorEmail,
		String instructorIntro,
		String title,
		String description,
		String focus
	) {
		return new CourseSeed(instructorName, instructorEmail, instructorIntro, title, description, focus);
	}

	private List<SectionSeed> createSections(String focus) {
		return List.of(
			section(focus + " 핵심 이해",
				focus + " 학습 목표와 전체 흐름",
				focus + " 실무 용어와 기본 개념",
				focus + " 체크리스트와 준비 사항"),
			section(focus + " 실무 적용",
				focus + " 요구사항 정리 실습",
				focus + " 단계별 실행 템플릿",
				focus + " 흔한 실수와 개선 방법"),
			section(focus + " 프로젝트 완성",
				focus + " 결과물 작성 가이드",
				focus + " 리뷰 기준과 품질 점검",
				focus + " 회고 리포트 작성")
		);
	}

	private SectionSeed section(String title, String firstMaterial, String secondMaterial, String thirdMaterial) {
		return new SectionSeed(title, List.of(
			material(firstMaterial),
			material(secondMaterial),
			material(thirdMaterial)
		));
	}

	private MaterialSeed material(String title) {
		return new MaterialSeed(title, "https://example.com/materials/" + title.replace(" ", "-"));
	}

	private String getCourseThumbnailUrl(int index) {
		int imageNumber = ((index - 1) % 9) + 1;
		return "/images/course" + imageNumber + ".jpg";
	}

	private record CourseSeed(
		String instructorName,
		String instructorEmail,
		String instructorIntro,
		String title,
		String description,
		String focus
	) {
	}

	private record SectionSeed(String title, List<MaterialSeed> materials) {
	}

	private record MaterialSeed(String title, String contentUrl) {
	}
}
