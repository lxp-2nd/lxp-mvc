package wanted.jjsbd.lxpmvc.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import wanted.jjsbd.lxpmvc.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmailAndDeletedAtIsNull(String email);
}
