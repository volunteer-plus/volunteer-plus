package volunteer.plus.backend.domain.enums;

public enum PaymentStatus {
    PENDING,    // створено, чекає обробки
    SUCCESS,    // оплачено
    FAILED,     // не вдалося провести платіж
    CANCELLED   // відмінено користувачем
}
