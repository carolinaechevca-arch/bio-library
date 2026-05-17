package com.bio_library.loans.application.ports.out;

public interface INotificationPort {
    void notifyBookBorrowed(Long studentId, String studentEmail, String studentPhone, String bookId);
    void notifyBookReturned(Long studentId, String studentEmail, String studentPhone, String bookId);
    void notifyLoanUsageWarning(Long studentId, String studentEmail, String studentPhone, String bookId);
    void notifyLoanUsageRevoked(Long studentId, String studentEmail, String studentPhone, String bookId);
    void notifyLoanExpired(Long studentId, String studentEmail, String studentPhone, String bookId);
}
