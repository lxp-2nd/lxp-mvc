package wanted.jjsbd.lxpmvc.enrollment.config;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EnrollmentDummyDataLoader {

	private final EnrollmentDummyDataInitService initService;

	@PostConstruct
	public void init() {
		initService.initData();
		System.out.println("✅ [Enrollment] 회원, 강의, 수강신청 통합 더미 데이터 세팅 완료");
	}
}
