package sparta.seed.campaign.repository.custom.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import sparta.seed.campaign.repository.custom.AqRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static sparta.seed.campaign.domain.QAqApiData.*;

public class AqRepositoryImpl implements AqRepositoryCustom {

  @PersistenceContext
  EntityManager em;

  public Double airQualityData(String category,String datetime) {
    Double average = 0.0;
    Double averageResult = 0.0 ;
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    List<Double> fetch = queryFactory.select(aqApiData.amount)
            .from(aqApiData)
            .where(aqApiData.category.eq(category), aqApiData.datetime.eq(datetime))
            .fetch();

    for (Double amount : fetch) {
      average += amount;
    }
    averageResult = average / 17;

    return averageResult;
  }
}