package wanted.jjsbd.lxpmvc.enrollment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Enrollment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long enrollmentId;

	// JUnit Test 코드
	public int add(int num1, int num2) {
		return num1 + num2;
	}

}
