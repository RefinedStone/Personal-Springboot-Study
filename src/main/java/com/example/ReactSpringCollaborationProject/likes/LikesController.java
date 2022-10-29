package com.example.ReactSpringCollaborationProject.likes;

import com.example.ReactSpringCollaborationProject.global.dto.ResponseDto;
import com.example.ReactSpringCollaborationProject.security.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class LikesController {

    private final LikesService likesService;

    @Autowired
    LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    // 좋아요 등록
    @PutMapping("/likes/{postId}")
    public ResponseDto<?> createLikes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long postId,
            @RequestBody LikesRequestDto likesRequestDto) {

        return ResponseDto.success(likesService.createLikes(userDetails.getAccount(), postId, likesRequestDto));
    }


}
