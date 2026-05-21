package wanted.jjsbd.lxpmvc.common.localdev;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Profile("local")
@Component
@RequiredArgsConstructor
public class DataInitializer {

	@PostConstruct
	public void init() {
		// memberRepository.save();
		// cartRepository.save();
		// courseRepository.save();
		// ...
		// 이런 식으로 원하는 데이터 추가
	}
}
