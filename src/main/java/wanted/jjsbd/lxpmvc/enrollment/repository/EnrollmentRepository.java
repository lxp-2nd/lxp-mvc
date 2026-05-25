package wanted.jjsbd.lxpmvc.enrollment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	// svae, findById(ID), findAll(), deleteById(ID) 자동 생성

	boolean existsByLearnerIdAndCourseId(Long learnerId, Long courseId);

	// 삭제 여부와 무관하게 특정 회원과 강의에 대한 수강 이력 조회
	Optional<Enrollment> findByLearnerIdAndCourseId(Long learnerId, Long courseId);
}
