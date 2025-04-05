package com.backend.commonservice.model;

import lombok.Getter;

/**
 * Exception xử lý trường hợp người dùng không có quyền truy cập
 * @author: Pham Kim Khuong
 * @version: 1.0
 */
@Getter
public class AccessDeniedException extends AppException {
    public AccessDeniedException() {
        super(ErrorMessage.UNAUTHORIZED);
    }
    
    public AccessDeniedException(String message) {
        super(ErrorMessage.UNAUTHORIZED);
    }
} 