package sparta.seed.domain.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sparta.seed.domain.Community;
import sparta.seed.domain.Img;

import java.util.List;

@Getter
@Setter
public class CommunityResponseDto {

  private Long communityId;
  private List<Img> imgList;
  private String title;
  //모집여부
  private boolean isRecruitment;
  private List<Community> participantsList;
  //달성도
  private long participantsPer;
  //참가인원
  private Integer participantsCnt;
  //사용자 닉네임
  private String nickname;
  //모임 시작날짜
  private String startDate;
  //모임 종료날짜
  private String endDate;
  //비밀글 여부
  private boolean isSecret;
  //패스워드
  private String password;
  //내용
  private String content;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
  private String createAt;
  private boolean isWriter;



  @QueryProjection
  @Builder
  public CommunityResponseDto(Long communityId, List<Img> imgList, String title, boolean isRecruitment, List<Community> participantsList, long participantsPer, Integer participantsCnt, String nickname, String startDate, String endDate, boolean isSecret, String password,String createAt,String content,boolean isWriter) {
    this.communityId = communityId;
    this.imgList = imgList;
    this.title = title;
    this.isRecruitment = isRecruitment;
    this.participantsList = participantsList;
    this.participantsPer = participantsPer;
    this.participantsCnt = participantsCnt;
    this.nickname = nickname;
    this.startDate = startDate;
    this.endDate = endDate;
    this.isSecret = isSecret;
    this.password = password;
    this.createAt = createAt;
    this.content = content;
    this.isWriter = isWriter;
  }
}
