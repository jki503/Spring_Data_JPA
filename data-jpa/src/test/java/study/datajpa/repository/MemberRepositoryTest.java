package study.datajpa.repository;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember(){
        System.out.println(memberRepository.getClass());
        Member member = new Member("memberB",20,null);

        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(savedMember).isEqualTo(findMember);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 5);

        AssertionsForClassTypes.assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findByUsername("aaa");

        AssertionsForClassTypes.assertThat(members.size()).isEqualTo(2);

    }

    @Test
    public void testQuery(){
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findUser("aaa",10);

        AssertionsForClassTypes.assertThat(members.size()).isEqualTo(2);

    }

    @Test
    public void testUsername(){
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        usernameList.forEach(System.out::println);
    }

    @Test
    public void testDto(){

        Team team1 = new Team("teamA");

        Member m1 = new Member("aaa", 10, team1);
        Member m2 = new Member("bbb", 20, team1);

        teamRepository.save(team1);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();

        memberDtos.forEach(System.out::println);
    }

    @Test
    public void findByNames(){
        Team team1 = new Team("teamA");

        Member m1 = new Member("aaa", 10, team1);
        Member m2 = new Member("bbb", 20, team1);

        teamRepository.save(team1);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> memberList = memberRepository.findByNames(Arrays.asList("aaa","bbb"));

        assertThat(memberList.size()).isEqualTo(2);
    }
}