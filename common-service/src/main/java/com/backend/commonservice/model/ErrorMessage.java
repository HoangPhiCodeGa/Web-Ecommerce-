package com.backend.commonservice.model;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/22/2025 7:38 PM
 */

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorMessage {
    USER_NOT_FOUND("User not found", 404, HttpStatus.NOT_FOUND),
    ITEM_NOT_FOUND("Item not found", 404, HttpStatus.NOT_FOUND),
    UNAUTHORIZED("Bạn không có quyền truy cập vào tài nguyên này", 403, HttpStatus.FORBIDDEN),
    ACCESS_DENIED("Bạn không có quyền thực hiện hành động này", 403, HttpStatus.FORBIDDEN),
    UNAUTHENTICATED("Bạn cần đăng nhập để truy cập", 401, HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST("Invalid Request", 400, HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_CREDENTIAL("Invalid Credential", 400, HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("Token không hợp lệ hoặc đã hết hạn", 401, HttpStatus.UNAUTHORIZED),
    BAD_REQUEST("Bad Request", 400, HttpStatus.BAD_REQUEST),
    INVALID_DATA("Invalid Data", 400, HttpStatus.BAD_REQUEST),
    DUPLICATE_DATA("Duplicate Data", 400, HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("Invalid Parameter", 400, HttpStatus.BAD_REQUEST)
    ;
    String message;
    int code;
    HttpStatus httpStatus;


}
