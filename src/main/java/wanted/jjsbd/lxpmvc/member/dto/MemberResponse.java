package wanted.jjsbd.lxpmvc.member.dto;

import wanted.jjsbd.lxpmvc.member.domain.Member;

public record MemberResponse(String nickname, String email) {

	public static MemberResponse from(Member member) {
		return new MemberResponse(member.getNickname(), member.getEmail());
	}
}
