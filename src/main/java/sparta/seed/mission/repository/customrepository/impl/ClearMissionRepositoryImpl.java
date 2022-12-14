package sparta.seed.mission.repository.customrepository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sparta.seed.mission.domain.dto.requestdto.MissionSearchCondition;
import sparta.seed.mission.domain.dto.responsedto.ClearMissionResponseDto;
import sparta.seed.mission.domain.dto.responsedto.QClearMissionResponseDto;
import sparta.seed.mission.repository.customrepository.ClearMissionRepositoryCustom;

import java.util.List;

import static sparta.seed.mission.domain.QClearMission.clearMission;


@RequiredArgsConstructor
public class ClearMissionRepositoryImpl implements ClearMissionRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<ClearMissionResponseDto> dailyMissionStats(MissionSearchCondition condition, Long memberId) {
    return queryFactory.select(new QClearMissionResponseDto(clearMission.clearTime, clearMission.count()))
            .from(clearMission)
            .where(clearMission.clearTime.between(condition.getStartDate(), condition.getEndDate()), clearMission.memberId.eq(memberId))
            .groupBy(clearMission.clearTime)
            .fetch();
  }

  @Override
  public List<Long> WeekMissionStats(MissionSearchCondition condition, Long memberId) {
    return null;
  }
}
