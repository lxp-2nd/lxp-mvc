package wanted.jjsbd.lxpmvc.course.config;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DummyDataLoader {

	// 분리한 트랜잭션 컴포넌트를 주입받습니다.
	private final DummyDataInitService dummyDataInitService;

	@PostConstruct
	public void init() {
		// 외부 빈(Bean)의 메서드를 호출하므로 정상적으로 트랜잭션이 작동합니다!
		dummyDataInitService.initData();

		System.out.println("✅ 로컬 테스트용 더미 데이터 세팅 완료");
	}
}
