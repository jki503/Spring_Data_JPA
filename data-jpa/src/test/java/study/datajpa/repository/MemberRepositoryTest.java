package study.datajpa.repository;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        System.out.println(memberRepository.getClass());
        Member member = new Member("memberB", 20, null);

        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(savedMember).isEqualTo(findMember);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 5);

        AssertionsForClassTypes.assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findByUsername("aaa");

        AssertionsForClassTypes.assertThat(members.size()).isEqualTo(2);

    }

    @Test
    public void testQuery() {
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findUser("aaa", 10);

        AssertionsForClassTypes.assertThat(members.size()).isEqualTo(2);

    }

    @Test
    public void testUsername() {
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        usernameList.forEach(System.out::println);
    }

    @Test
    public void testDto() {

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
    public void findByNames() {
        Team team1 = new Team("teamA");

        Member m1 = new Member("aaa", 10, team1);
        Member m2 = new Member("bbb", 20, team1);

        teamRepository.save(team1);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> memberList = memberRepository.findByNames(Arrays.asList("aaa", "bbb"));

        assertThat(memberList.size()).isEqualTo(2);
    }

    @Test
    public void returnType() {
        Team team1 = new Team("teamA");

        Member m1 = new Member("aaa", 10, team1);
        Member m2 = new Member("aaa", 20, team1);

        teamRepository.save(team1);

        memberRepository.save(m1);
        memberRepository.save(m2);


    }

    @Test
    public void paging() throws Exception {

        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //page에 있는 실제 content
        List<Member> content = page.getContent();

        long totalElements = page.getTotalElements();

        content.forEach(System.out::println);

        System.out.println(totalElements);

        assertThat(content.size()).isEqualTo(3); // 실제 컨텐트 개수
        assertThat(page.getTotalElements()).isEqualTo(5); // 총 컨텐트의 개수
        assertThat(page.getNumber()).isEqualTo(0); // 페이지가 몇번인지
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지가 2인지. -> 5개를 사이즈가 3인페이지니까 3 + 2 2개 나온다
        assertThat(page.isFirst()).isTrue(); // 첫번째인지.
        assertThat(page.hasNext()).isTrue(); // 다음페이지가 있는지
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 19, null));
        memberRepository.save(new Member("member3", 20, null));
        memberRepository.save(new Member("member4", 21, null));
        memberRepository.save(new Member("member5", 40, null));

        int resultCount = memberRepository.bulkAgePlus(20);

        System.out.println(memberRepository.findByUsername("member5").get(0).getAge());

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamA));

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
    public void findFetch() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamA));

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findMemberFetchJoin();

        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
    public void queryHint() {
        Member member1 = new Member("member5", 20, null);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock() {
        Member member1 = new Member("member5", 20, null);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> members = memberRepository.findLockByUsername(member1.getUsername());
    }

    @Test
    public void callCustom(){
        memberRepository.findMemberCustom();
    }

    @Test
    public void testBaseEntity(){
        Member member1 = new Member("member5", 20, null);

        memberRepository.save(member1);
        member1.setUsername("updateMember");
    }

    @Test
    public void projections(){
        Team teamA = new Team("teamA");

        teamRepository.save(teamA);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamA));

        em.flush();
        em.clear();

        //when
        List<UsernameOnlyDto> result = memberRepository.findProjectionsByUsername("member1");

        result.forEach(System.out::println);
    }
}