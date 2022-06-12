package study.datajpa.convention;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import study.datajpa.entity.Member;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCreateResponse {

    private Long id;
    private String username;
    private String teamName;

    private MemberCreateResponse(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public static MemberCreateResponse of(Member member){
        return new MemberCreateResponse(
                member.getId(),
                member.getUsername(),
                member.getTeam().getName()
        );
    }
}
