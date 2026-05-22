package wanted.jjsbd.lxpmvc.course.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wanted.jjsbd.lxpmvc.course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	Page<Course> findByTitleContainingAndDeletedAtIsNull(String title, Pageable pageable);

	// TODO: 수정해야함 "방법 고안중: Course, Section 엔티티에 배치사이즈 설정후 @EntityGraph 삭제하고 JPQL로 조인 쿼리 보내기.
	@EntityGraph(attributePaths = {"sections", "sections.materials"})
	@Query("SELECT c FROM Course c WHERE c.id = :id and c.deletedAt is null ")
	Optional<Course> findByIdWithCurriculum(@Param("id") Long id);
}
