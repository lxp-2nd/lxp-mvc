package wanted.jjsbd.lxpmvc.course.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wanted.jjsbd.lxpmvc.course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	/// 비로그인 사용자
	List<Course> findByTitleContainingAndDeletedAtIsNullOrderByCreatedAtDesc(String title);

	/// 로그인 사용자: 자신이 수강 중인 강의를 제외(Not IN)하고 가져오는 쿼리
	@Query("SELECT c FROM Course c " + "WHERE c.title LIKE CONCAT('%', :title, '%') " + "AND c.deletedAt IS NULL "
		+ "AND c.id NOT IN (SELECT e.course.id FROM enrollment e WHERE e.learner.id = :memberId) "
		+ "ORDER BY c.createdAt DESC")
	List<Course> findAvailableCoursesForMember(@Param("title") String title, @Param("memberId") Long memberId);

	@Query("SELECT c FROM Course c " + "LEFT JOIN FETCH c.sections " + "WHERE c.id = :id AND c.deletedAt IS NULL")
	Optional<Course> findByIdWithCurriculum(@Param("id") Long id);
}
