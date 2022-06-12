package study.datajpa.convention;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private MemberRepository memberRepository;

    @Transactional
    public MemberCreateResponse createMember(MemberCreateRequest memberCreateRequest){
        Member savedMember = memberRepository.save(memberCreateRequest.toEntity());
        return MemberCreateResponse.of(savedMember);
    }
}
