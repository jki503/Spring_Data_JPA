# 쿼리 메소드 기능

## 메소드 이름으로 쿼리 생성

</br>

- 쿼리 메소드 기능
  - 메소드 이름으로 쿼리 생성
  - 메소드 이름으로 JPA NamedQueyr 호출
  - @Query 어노테이션을 사용해서 리포지토리 인터페이스에 쿼리 직접정의

</br>

- 실제 순수 JPA를 통해 만든 메서드

```java
public List<Member> findByUsernameAndAgeGreaterThen(String username, int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username",username)
                .setParameter("age",age)
                .getResultList();
    }
```

```java
@Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThen("aaa", 5);

        assertThat(members.size()).isEqualTo(2);
    }

```

> 그런데 Spring Data Jpa에서 쿼리를 만들기 위해 이렇게  
> 상속 받은 후 메서드를 만들 수 있는가?

</br>

```java
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
```

</br>

> Spring data JPA가 프로퍼티에 맞춰서 JPQL을 만들어준다.

</br>

- 조회: find...By ,read...By ,query...By get...By,
- COUNT: count...By 반환타입 long
- EXISTS: exists...By 반환타입 boolean
- 삭제: delete...By, remove...By 반환타입 long
- DISTINCT: findDistinct, findMemberDistinctBy
- LIMIT: findFirst3, findFirst, findTop, findTop3

## JPA NamedQuery

</br>

```java
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member {
}
```

```java
// @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
```

</br>

> 실제로 Repository에서 정의된 Named query를 이렇게 사용하면 된다.  
> 쿼리 어노테이션이 없어도 동작을 하는 이유는  
> 메서드 명을 통해서 NamedQuery를 먼저 찾기 때문이다.  
> `즉, 명칭을 같게 만들면 안써도 동작한다!`

</br>

## @Query, 레포지토리 메소드에 쿼리 정의하기

</br>

```java
@Query("select m from Member m where m.username = :username and m.age = :age")
public List<Member> findUser(@Param("username")String username, @Param("age") int age);

```

```java
@Test
    public void testQuery(){
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findUser("aaa",10);

        AssertionsForClassTypes.assertThat(members.size()).isEqualTo(2);

    }
```

</br>

- 실행할 메서드에 정적 쿼리를 직접 작성하여 이름 없는 NamedQuery라 할 수 있다.
- JPA Named 쿼리 처럼 애플리케이션 실행 시점에 문법 오류를 발견 할 수 있다.
- 메서드 이름을 스프링이 지원해준다 하여도 `파라미터가 증가할 수록 이름이 길어진다`
  - @Query 좋아!

</br>

## @Query, 값, DTO 조회하기

</br>

```java
 @Query("select m.username from Member m")
    public List<String> findUsernameList();

@Test
    public void testUsername(){
        Member m1 = new Member("aaa", 10, null);
        Member m2 = new Member("aaa", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        usernameList.forEach(System.out::println);
    }
```

</br>

- 그러면 DTO는?

```java
@Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name)from Member m join m.team t")
List<MemberDto> findMemberDto();
```

</br>

> package 정보를 모두 입력해야하는 번거로움..

</br>

## 파라미터 바인딩

</br>

- 위치기반(나도 안씀.)
- 이름기반

</br>

- 컬렉션 파라미터 바인딩
  - 실제로 where in 절을 사용할 때 사용할 일이 있지않을까~

```java
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);
```

```java
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

```

</br>

## 반환 타입

</br>

</br>

## 순수 JPA 페이징과 정렬

</br>

</br>

## 스프링 데이터 JPA 페이징과 정렬

</br>

</br>

## 벌크성 수정 쿼리

</br>

</br>

## @EntityGraph

</br>

</br>

## JPA Hint & Lock
