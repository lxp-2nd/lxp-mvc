package wanted.jjsbd.lxpmvc.course.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import wanted.jjsbd.lxpmvc.course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	/// 제목(Title)에 검색어가 포함되어 있고, 활성화된 강의 목록을 페이징/정렬하여 조회
	Page<Course> findByTitleContainingAndDeletedAtIsNull(String title, Pageable pageable);

	// (신규) ID로 단일 강의 조회 (소프트 딜리트된 강의는 제외) - min 추가
	Optional<Course> findByIdAndDeletedAtIsNull(Long id);
}
