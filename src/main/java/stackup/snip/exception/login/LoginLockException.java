package stackup.snip.exception.login;

import lombok.Getter;

@Getter
public class LoginLockException extends RuntimeException {

    private final long remainSeconds;

    public LoginLockException(long remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
}
