package wanted.jjsbd.lxpmvc.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	// svae, findById(ID), findAll(), deleteById(ID) 자동 생성

	boolean existsByLearnerIdAndCourseId(Long learnerId, Long courseId);
}
