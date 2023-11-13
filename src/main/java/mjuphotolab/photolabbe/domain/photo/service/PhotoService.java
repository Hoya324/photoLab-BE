package mjuphotolab.photolabbe.domain.photo.service;

import static mjuphotolab.photolabbe.aws.PhotoDomainType.*;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import mjuphotolab.photolabbe.aws.PhotoDomainType;
import mjuphotolab.photolabbe.aws.service.AwsS3Service;
import mjuphotolab.photolabbe.domain.competition.entity.Competition;
import mjuphotolab.photolabbe.domain.competition.repository.CompetitionRepository;
import mjuphotolab.photolabbe.domain.photo.controller.dto.request.PhotoRequest;
import mjuphotolab.photolabbe.domain.empathy.service.EmpathyService;
import mjuphotolab.photolabbe.domain.photo.controller.dto.response.BestPhotoDto;
import mjuphotolab.photolabbe.domain.photo.controller.dto.response.PhotoDto;
import mjuphotolab.photolabbe.domain.photo.entity.Photo;
import mjuphotolab.photolabbe.domain.photo.repository.PhotoRepository;
import mjuphotolab.photolabbe.domain.user.entity.User;
import mjuphotolab.photolabbe.domain.user.service.UserService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {

	private final PhotoRepository photoRepository;
	private final CompetitionRepository competitionRepository;
	private final EmpathyService empathyService;
	private final UserService userService;
	private final AwsS3Service awsS3Service;

	@Transactional(readOnly = true)
	public PhotoDto findPhoto(Long photoId, User user) {
		Photo photo = photoRepository.findById(photoId)
			.orElseThrow(() -> new IllegalArgumentException("[Error] 사진을 찾을 수 없습니다."));

		return PhotoDto.from(photo, userService.findByStudentNumber(photo.getStudentNumber()),
			empathyService.isLiked(user, photo));
	}

	public void deletePhoto(Long photoId) {
		Photo photo = photoRepository.findById(photoId)
			.orElseThrow(() -> new IllegalArgumentException("[Error] 사진을 찾을 수 없습니다."));
		photoRepository.delete(photo);
	}

	@Transactional(readOnly = true)
	public List<BestPhotoDto> findBestPhotos() {
		Pageable pageable = PageRequest.of(0, 4);
		List<Photo> photos = photoRepository.getBestPhotos(pageable);
		return photos.stream()
			.map(photo -> BestPhotoDto.from(photo, getNickname(photo)))
			.collect(Collectors.toList());
	}

	public Long updatePhoto(Long photoId, PhotoRequest photoRequest) {
		Photo photo = photoRepository.findById(photoId)
			.orElseThrow(() -> new IllegalArgumentException("[Error] 사진을 찾을 수 없습니다."));
		photo.updateInfo(photoRequest);
		return photoId;
	}

	public void registerCompetitionPhoto(MultipartFile multipartFile, PhotoRequest photoRequest, User user) {
		String imagePath = awsS3Service.uploadImage(COMPETITION.name(), multipartFile);
		Competition competition = competitionRepository.findById(photoRequest.getParentId())
			.orElseThrow(() -> new IllegalArgumentException("[Error] 해당 공모전을 찾을 수 없습니다."));
		Photo photo = photoRequest.toEntity(competition, imagePath, user);
		photoRepository.save(photo);
	}

	private String getNickname(final Photo photo) {
		return userService.findByStudentNumber(photo.getStudentNumber()).getNickname();
	}
}
