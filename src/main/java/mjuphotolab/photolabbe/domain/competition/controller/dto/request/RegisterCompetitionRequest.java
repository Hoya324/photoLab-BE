package mjuphotolab.photolabbe.domain.competition.controller.dto.request;

import java.util.List;

import lombok.Getter;
import mjuphotolab.photolabbe.domain.competition.entity.Competition;
import mjuphotolab.photolabbe.domain.user.entity.User;

@Getter
public class RegisterCompetitionRequest {

	private User user;
	private String competitionTitle;
	private String competitionContent;
	private int awardCount;
	private List<CompetitionPhotoDto> competitionPhotoDtos;

	public Competition toEntity(User user) {
		return Competition.builder()
			.user(user)
			.title(competitionTitle)
			.content(competitionContent)
			.awardCount(awardCount)
			.build();
	}
}
