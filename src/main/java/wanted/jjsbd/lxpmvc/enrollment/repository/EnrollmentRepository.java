package wanted.jjsbd.lxpmvc.enrollment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	// save, findById(ID), findAll(), deleteById(ID) 자동 생성

	boolean existsByLearnerIdAndCourseId(Long learnerId, Long courseId);

	// 삭제 여부와 무관하게 특정 회원과 강의에 대한 수강 이력 조회
	Optional<Enrollment> findByLearnerIdAndCourseId(Long learnerId, Long courseId);

	// 특정 회원의 수강 목록 조회 (Course를 Fetch Join으로 한 번에 가져옴) - JPQL
	@Query("SELECT e FROM enrollment e JOIN FETCH e.course c WHERE e.learner.id = :learnerId")
	List<Enrollment> findAllByLearnerId(@Param("learnerId") Long learnerId);
}
