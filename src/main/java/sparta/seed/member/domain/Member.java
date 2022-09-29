package sparta.seed.member.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import sparta.seed.member.domain.dto.requestdto.NicknameRequestDto;
import sparta.seed.util.BaseEntity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String password;

  private String nickname;

  private String socialId;

  @Enumerated(EnumType.STRING)
  private Authority authority;

  @Enumerated(EnumType.STRING)
  private LoginType loginType;

  private String profileImage;

  private int exp;

  private int level;



  @ElementCollection
  private Map<String,Boolean> dailyMission = new HashMap<>(6,1);

  private boolean isSecret;

  @Builder
  public Member(Long id, String username, String password, String nickname, String socialId, Authority authority, String profileImage,int exp,int level,LoginType loginType) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.socialId = socialId;
    this.authority = authority;
    this.profileImage = profileImage;
    this.exp = exp;
    this.level = level;
    this.loginType = loginType;
  }

  public void updateNickname(NicknameRequestDto requestDto) {
    nickname = requestDto.getNickname();
  }

  public void updateIsSecret(boolean isSecret) {
    this.isSecret = isSecret;
  }

  public void addExp(int exp) {
    this.exp += exp;
  }

  public void initExp() {
    this.exp = 0;
  }

  public void minusExp(int exp) {
    this.exp -= exp;
  }
  public void levelUp() {
    this.level += 1;
  }
}