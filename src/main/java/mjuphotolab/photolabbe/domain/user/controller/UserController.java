package mjuphotolab.photolabbe.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mjuphotolab.photolabbe.domain.user.controller.dto.request.UserSignUpDto;
import mjuphotolab.photolabbe.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/sign-up")
	public void signUp(@Valid @RequestBody UserSignUpDto userSignUpDto) throws Exception {
		userService.signUp(userSignUpDto);
	}

	// @GetMapping("/api/users/{userId}/awards")

	@GetMapping("/jwt-test")
	public String jwtTest() {
		return "jwtTest 요청 성공";
	}

	// TODO: 최종적으로는 admin이 붙어야함
	@PatchMapping("/api/users/{userId}/editRole")
	public Long updateRoleToAdmin(@PathVariable Long userId) {
		return userService.setAdmin(userId);
	}

}
