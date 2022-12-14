package sparta.seed.campaign.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CampaignResponseDto {
	private Long campaignId;
	private String title;
	private String thumbnail;
	private String thumbnailUrl;

	@Builder
	public CampaignResponseDto(Long campaignId, String thumbnail, String title, String thumbnailUrl) {
		this.campaignId = campaignId;
		this.title = title;
		this.thumbnail = thumbnail;
		this.thumbnailUrl = thumbnailUrl;
	}
}
