package com.project.commerce.domain.payment;

public enum PaymentStatus {
    READY,      // 결제 시도
    SUCCESS,    // 결제 성공
    FAIL,        // 결제 실패
    CANCELLED   // 결제 취소
}
