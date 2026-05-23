package wanted.jjsbd.lxpmvc.course.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wanted.jjsbd.lxpmvc.course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	List<Course> findByTitleContainingAndDeletedAtIsNullOrderByCreatedAtDesc(String title);

	@Query("SELECT c FROM Course c " + "LEFT JOIN FETCH c.sections " + "WHERE c.id = :id AND c.deletedAt IS NULL")
	Optional<Course> findByIdWithCurriculum(@Param("id") Long id);
}
