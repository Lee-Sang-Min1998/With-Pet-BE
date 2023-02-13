package WebProject.withpet.users.controller;

import WebProject.withpet.common.auth.PrincipalDetails;
import WebProject.withpet.common.auth.application.JwtTokenProvider;
import WebProject.withpet.common.constants.ResponseConstants;
import WebProject.withpet.common.dto.ApiResponse;
import WebProject.withpet.users.dto.SocialLoginResponseDto;
import WebProject.withpet.users.dto.UserChangeInfoRequestDto;
import WebProject.withpet.users.dto.UserRequestDto;
import WebProject.withpet.users.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody UserRequestDto user) {
        userService.register(user);
        return ResponseEntity.ok(ResponseConstants.RESPONSE_SAVE_OK);
    }

    @PostMapping("kakao/login")
    public ResponseEntity<ApiResponse<SocialLoginResponseDto>> socialLogin(@RequestParam(name = "code") String code)
            throws JSONException {

        ApiResponse<SocialLoginResponseDto> socialLongResponse = new ApiResponse<>(200, "카카오 로그인 성공",
                userService.socialLogin(code));

        return ResponseEntity.status(HttpStatus.OK).body(socialLongResponse);

    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> changeUserInfo(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @Valid @RequestBody UserChangeInfoRequestDto userChangeInfoRequestDto){

        userService.changeUserInfo(principalDetails.getUser(),userChangeInfoRequestDto);


        return ResponseEntity.status(HttpStatus.OK).body(ResponseConstants.RESPONSE_UPDATE_OK);
    }

}
