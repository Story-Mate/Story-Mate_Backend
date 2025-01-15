package store.storymate.storymatebackend.global.template;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private final int statusCode;
    private String message;
    private T data;
    private final boolean success;

    private ApiResponse(HttpStatus httpStatus) {
        this.statusCode = httpStatus.value();
        this.success = httpStatus.is2xxSuccessful();
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(HttpStatus.OK);
    }

    public static <T> ApiResponse<T> created() {
        return new ApiResponse<>(HttpStatus.CREATED);
    }

    public static <T> ApiResponse<T> status(HttpStatus httpStatus) {
        return new ApiResponse<>(httpStatus);
    }

    public ApiResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    public ApiResponse<T> data(T data) {
        this.data = data;
        return this;
    }

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.OK);
        return response.data(data);
    }

    public static <T> ApiResponse<T> ok(String message) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.OK);
        return response.message(message);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.OK);
        return response.message(message).data(data);
    }

    public static <T> ApiResponse<T> created(T data) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.CREATED);
        return response.data(data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.CREATED);
        return response.message(message).data(data);
    }
}
