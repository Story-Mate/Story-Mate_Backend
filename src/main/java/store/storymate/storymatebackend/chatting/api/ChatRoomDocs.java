package store.storymate.storymatebackend.chatting.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import store.storymate.storymatebackend.auth.api.dto.response.IdTokenResponseDto;
import store.storymate.storymatebackend.chatting.api.dto.request.ChatRoomReqDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResList;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatRoomResDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatRoomResList;
import store.storymate.storymatebackend.global.annotation.CurrentMemberEmail;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;


@Tag(name = "[채팅방 API]", description = "채팅방 관련 API")
public interface ChatRoomDocs {

    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "채팅방 생성 성공",
                            content = @Content(schema = @Schema(implementation = ChatRoomResDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<ChatRoomResDto> createChatRoom(
            @Parameter(description = "로그인한 사용자 이메일 정보 (토큰에 담겨서 전송됨)", required = true) String email,
            @Parameter(description = "채팅방 생성 요청 데이터", required = true) ChatRoomReqDto request
    );

    @Operation(summary = "채팅방 조회", description = "로그인한 사용자의 채팅방을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "채팅방 조회 성공",
                            content = @Content(schema = @Schema(implementation = ChatRoomResList.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<ChatRoomResList> getChatRooms(
            @Parameter(description = "로그인한 사용자 이메일 정보 (토큰에 담겨서 전송됨)", required = true) String email,
            @Parameter(description = "페이지 번호", required = true) int page,
            @Parameter(description = "요청할 개수", required = true) int size
    );
}
