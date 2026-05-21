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

	@EntityGraph(attributePaths = {"sections"})
	@Query("SELECT c FROM Course c WHERE c.id = :id and c.deletedAt is null ")
	Optional<Course> findByIdWithCurriculum(@Param("id") String id);
}
