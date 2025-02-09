package store.storymate.storymatebackend.chatting.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResList;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;


@Tag(name = "[채팅 메세지 API]", description = "채팅 메세지 관련 API")
public interface ChatMessageDocs {

    @Operation(summary = "채팅 메세지 조회", description = "채팅방 아이디(roomId)를 통해 채팅 메세지 리스트를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "채팅 메세지 리스트 반환 성공",
                            content = @Content(schema = @Schema(implementation = ChatMessageResList.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<ChatMessageResList> getChatMessages(
            @Parameter(description = "채팅방 아이디 (roomId)", required = true) Long roomId,
            @Parameter(description = "페이지 번호", required = true) int page,
            @Parameter(description = "요청할 개수", required = true) int size
    );
}
