package wanted.jjsbd.lxpmvc.course.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import wanted.jjsbd.lxpmvc.course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	/// 제목(Title)에 검색어가 포함되어 있고, 활성화된 강의 목록을 페이징/정렬하여 조회
	Page<Course> findByTitleContainingAndDeletedAtIsNull(String title, Pageable pageable);

}
