package bankAccount.dtos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service")
public interface UserProxy {
    @GetMapping("users-service/users/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email);
}
