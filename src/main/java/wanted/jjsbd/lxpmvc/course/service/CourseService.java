package wanted.jjsbd.lxpmvc.course.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
import wanted.jjsbd.lxpmvc.course.domain.Course;
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
}
