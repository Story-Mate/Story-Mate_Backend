package store.storymate.storymatebackend.global.template;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseTemplate<T> {
    private final int statusCode;
    private String message;
    private T data;
    private final boolean success;

    private ApiResponseTemplate(HttpStatus httpStatus) {
        this.statusCode = httpStatus.value();
        this.success = httpStatus.is2xxSuccessful();
    }

    public static <T> ApiResponseTemplate<T> ok() {
        return new ApiResponseTemplate<>(HttpStatus.OK);
    }

    public static <T> ApiResponseTemplate<T> created() {
        return new ApiResponseTemplate<>(HttpStatus.CREATED);
    }

    public static <T> ApiResponseTemplate<T> status(HttpStatus httpStatus) {
        return new ApiResponseTemplate<>(httpStatus);
    }

    public ApiResponseTemplate<T> message(String message) {
        this.message = message;
        return this;
    }

    public ApiResponseTemplate<T> data(T data) {
        this.data = data;
        return this;
    }

    public static <T> ApiResponseTemplate<T> ok(T data) {
        ApiResponseTemplate<T> response = new ApiResponseTemplate<>(HttpStatus.OK);
        return response.data(data);
    }

    public static <T> ApiResponseTemplate<T> ok(String message) {
        ApiResponseTemplate<T> response = new ApiResponseTemplate<>(HttpStatus.OK);
        return response.message(message);
    }

    public static <T> ApiResponseTemplate<T> ok(String message, T data) {
        ApiResponseTemplate<T> response = new ApiResponseTemplate<>(HttpStatus.OK);
        return response.message(message).data(data);
    }

    public static <T> ApiResponseTemplate<T> created(T data) {
        ApiResponseTemplate<T> response = new ApiResponseTemplate<>(HttpStatus.CREATED);
        return response.data(data);
    }

    public static <T> ApiResponseTemplate<T> created(String message, T data) {
        ApiResponseTemplate<T> response = new ApiResponseTemplate<>(HttpStatus.CREATED);
        return response.message(message).data(data);
    }
}
