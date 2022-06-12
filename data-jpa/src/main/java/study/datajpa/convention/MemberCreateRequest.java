package study.datajpa.convention;

import lombok.Getter;
import study.datajpa.entity.Member;

@Getter
public class MemberCreateRequest {

    private String username;
    private int age;

    public MemberCreateRequest(String username, int age){
        this.username = username;
        this.age = age;
    }

    public Member toEntity(){
        return Member.builder()
                .username(this.username)
                .age(this.age)
                .team(null)
                .build();
    }
}
