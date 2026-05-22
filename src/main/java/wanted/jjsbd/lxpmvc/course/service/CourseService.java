package wanted.jjsbd.lxpmvc.course.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	 * 강의 목록 조회 (검색 및 페이징 포함)
	 */
	public Page<CourseResponse> getCourses(CourseSearchRequest request, Pageable pageable) {
		/// 1. DTO에서 검색어(q) 거내기 (null일 경우 ""로 변환되어있음)
		String keyword = request.q();

		/// 2. Repository 호출: DB에서 강의 목록(Entity) 가져오기
		Page<Course> coursePage = courseRepository.findByTitleContainingAndDeletedAtIsNull(keyword, pageable);

		/// 3. Entity -> DTO 변환 후 반환
		return coursePage.map(CourseResponse::of);
	}

	/**
	 * 강의 상세 조회
	 * @param courseId 조회할 강의의 식별자
	 * @Request CourseDetailResponse DTO 반환
	 */
	public CourseDetailResponse getCourseDetails(Long courseId) {
		/// 강의와 연관된 섹션/자료를 한번에 조회한다.
		Course course = courseRepository.findByIdWithCurriculum(courseId)
			/// 데이터가 없거나 이미 삭제 (deletedAt != null)된 강의면 에러 빵
			.orElseThrow(() -> new CustomException(ErrorCode.COURSE_NOT_FOUND));
		return CourseDetailResponse.of(course);
	}
}
