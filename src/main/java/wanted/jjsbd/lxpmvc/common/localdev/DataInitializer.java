package wanted.jjsbd.lxpmvc.common.localdev;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@Profile("local")
@Component
@RequiredArgsConstructor
public class DataInitializer {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final CourseInitializer courseInitializer;

	@PostConstruct
	public void init() {
		initMember();
		courseInitializer.initData();
	}

	private void initMember() {
		String hashed = passwordEncoder.encode("qwer1234!@#$");
		String hashed2 = passwordEncoder.encode("test1!");
		// 강사 생성 (id 1~5)
		memberRepository.save(Member.createInstructorMember("instructor1", "instructor1@email.com", hashed));
		memberRepository.save(Member.createInstructorMember("instructor2", "instructor2@email.com", hashed));
		memberRepository.save(Member.createInstructorMember("instructor3", "instructor3@email.com", hashed));
		memberRepository.save(Member.createInstructorMember("instructor4", "instructor4@email.com", hashed));
		memberRepository.save(Member.createInstructorMember("instructor5", "instructor5@email.com", hashed));
		// 일반 유저 생성
		memberRepository.save(Member.createBasicMember("user", "user@email.com", hashed));
		memberRepository.save(Member.createBasicMember("test", "test@test.test", hashed2));
	}
}
