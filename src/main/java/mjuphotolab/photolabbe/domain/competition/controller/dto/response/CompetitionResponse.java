package mjuphotolab.photolabbe.domain.competition.controller.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import mjuphotolab.photolabbe.domain.competition.entity.Competition;
import mjuphotolab.photolabbe.domain.photo.entity.Photo;

@Getter
public class CompetitionResponse {
	private String title;
	private String content;
	private int awards;
	private List<Photo> photos;

	@Builder
	private CompetitionResponse(final String title, final String content, final int awards, final List<Photo> photos) {
		this.title = title;
		this.content = content;
		this.awards = awards;
		this.photos = photos;
	}

	public static CompetitionResponse of(Competition competition) {
		return CompetitionResponse.builder()
			.title(competition.getTitle())
			.content(competition.getContent())
			.awards(competition.getAwards())
			// TODO: photo를 조회할 때 이런 쿼리를 통해 조회 쿼리 수 줄이기
			.photos(competition.getPhotos())
			.build();
	}
}
