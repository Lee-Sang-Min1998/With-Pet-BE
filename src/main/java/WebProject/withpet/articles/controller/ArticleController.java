package WebProject.withpet.articles.controller;



import WebProject.withpet.articles.dto.ImageDto;
import WebProject.withpet.articles.dto.ViewSpecificArticleResponseDto;
import WebProject.withpet.articles.dto.ViewUserAndArticleResponseDto;
import WebProject.withpet.articles.service.ArticleService;
import WebProject.withpet.articles.dto.ArticleCreateRequestDto;
import WebProject.withpet.articles.validation.ArticleValidator;
import WebProject.withpet.common.auth.PrincipalDetails;
import WebProject.withpet.common.constants.ErrorCode;
import WebProject.withpet.common.constants.ResponseConstants;
import WebProject.withpet.common.constants.ResponseMessages;
import WebProject.withpet.common.dto.ApiResponse;
import WebProject.withpet.common.exception.ArticleCreateException;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
@Validated
public class ArticleController {

    private final ArticleService articleService;

    private final ArticleValidator articleValidator;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createArticle(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody ArticleCreateRequestDto request) {

        Errors errors = new BeanPropertyBindingResult(request, "articleCreateRequestDto");

        //커스텀 검증
        articleValidator.validate(request, errors);

        if (errors.hasErrors()) {
            throw new ArticleCreateException(ErrorCode.INVALID_PARAMETER, errors);
        } else {
            articleService.createArticle(principalDetails.getUser(), request);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseConstants.RESPONSE_SAVE_OK);
    }

    //댓글 구현 후에 추가로 구현해야함
    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ViewSpecificArticleResponseDto>> viewSpecificArticle(
        @PathVariable("articleId") @NotNull(message = "게시글 id를 Url에 담아줘야 합니다.") Long articleId) {

        ApiResponse response = new ApiResponse<>(200, ResponseMessages.VIEW_MESSAGE.getContent(),
                articleService.viewSpecificArticle(articleId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //
}
