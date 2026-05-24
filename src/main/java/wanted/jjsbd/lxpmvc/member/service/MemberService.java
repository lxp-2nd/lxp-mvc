package wanted.jjsbd.lxpmvc.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
import wanted.jjsbd.lxpmvc.member.domain.AuthInfo;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.dto.LoginRequest;
import wanted.jjsbd.lxpmvc.member.dto.MemberCreateRequest;
import wanted.jjsbd.lxpmvc.member.dto.MemberResponse;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthInfo login(LoginRequest request) {
		String normalizedEmail = request.getNormalizedEmail();
		Member member = memberRepository.findByEmailAndDeletedAtIsNull(normalizedEmail)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_INVALID_CREDENTIALS));
		if (!passwordEncoder.matches(request.password(), member.getPasswordHash())) {
			throw new CustomException(ErrorCode.MEMBER_INVALID_CREDENTIALS);
		}
		return AuthInfo.from(member);
	}

	public MemberResponse getProfile(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.filter(existingMember -> !existingMember.isDeleted())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_ALREADY_WITHDRAWN));
		return MemberResponse.from(member);
	}

	@Transactional
	public AuthInfo signup(MemberCreateRequest request) {
		String normalizedEmail = request.getNormalizedEmail();
		if (memberRepository.existsByEmail(normalizedEmail)) {
			throw new CustomException(ErrorCode.MEMBER_DUPLICATE_EMAIL);
		}
		String passwordHash = passwordEncoder.encode(request.password());
		Member newMember = Member.createBasicMember(
			request.nickname(),
			normalizedEmail,
			passwordHash
		);
		Member savedMember = memberRepository.save(newMember);
		return AuthInfo.from(savedMember);
	}
}
