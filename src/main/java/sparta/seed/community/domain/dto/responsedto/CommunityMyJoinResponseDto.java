package sparta.seed.community.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommunityMyJoinResponseDto {
	private Long communityId;
	private boolean writer;
	private String img;
	private String title;
	private int currentPercent;
	private int successPercent;
	private String startDate;
	private String endDate;
	private String dateStatus;

	@Builder
	public CommunityMyJoinResponseDto(Long communityId, boolean writer, String title, String img, double currentPercent, double successPercent, String startDate, String endDate, String dateStatus) {
		this.communityId = communityId;
		this.title = title;
		this.img = img;
		this.writer = writer;
		this.currentPercent = (int)currentPercent;
		this.successPercent = (int)successPercent;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dateStatus = dateStatus;
	}
}