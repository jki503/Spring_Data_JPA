package study.datajpa.dto;

import lombok.Getter;
import lombok.ToString;
import study.datajpa.entity.Member;

@Getter
@ToString
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public static MemberDto of(Member member){
        return new MemberDto(
                member.getId(),
                member.getUsername(),
                member.getTeam().getName()
        );
    }
}
