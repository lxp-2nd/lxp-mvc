package wanted.jjsbd.lxpmvc.course.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.dto.CourseDetailResponse;
import wanted.jjsbd.lxpmvc.course.dto.CourseResponse;
import wanted.jjsbd.lxpmvc.course.dto.CourseSearchRequest;
import wanted.jjsbd.lxpmvc.course.repository.CourseRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
///읽기 전용 트랜잭션

public class CourseService {
	private final CourseRepository courseRepository;

	/**
	 * 강의 목록 조회
	 * @param  request 검색어 DTO
	 * @param memberId 현재 로그인한 사용자의 식별.
	 * 최종 DTO 반환
	 */
	public List<CourseResponse> getCourses(CourseSearchRequest request, Long memberId) {
		String keyword = request.q();
		List<Course> courseList;

		if (memberId == null) {/// 1. 비로그인: 전체 강의 조회
			courseList = courseRepository.findByTitleContainingAndDeletedAtIsNullOrderByCreatedAtDesc(keyword);
		} else {/// 2. 로그인: 이미 수강중인 강의 제외
			courseList = courseRepository.findAvailableCoursesForMember(keyword, memberId);
		}

		return courseList.stream()
			.map(CourseResponse::of)
			.toList();
	}

	/**
	 * 강의 상세 조회
	 * @param courseId 조회할 강의의 식별자
	 * @Request CourseDetailResponse DTO 반환
	 */
	public CourseDetailResponse getCourseDetails(Long courseId) {
		Course course = courseRepository.findByIdWithCurriculum(courseId)
			/// 데이터가 없거나 이미 삭제 (deletedAt != null)된 강의면 에러 빵
			.orElseThrow(() -> new CustomException(ErrorCode.COURSE_NOT_FOUND));
		return CourseDetailResponse.of(course);
	}
}
