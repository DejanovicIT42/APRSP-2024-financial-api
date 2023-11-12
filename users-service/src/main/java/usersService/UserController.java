package usersService;

import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import usersService.model.CustomUser;

@RestController
public class UserController {

	@Autowired
	private CustomUserRepository repo;
	
	@GetMapping("/users-service/users")
	public List<CustomUser> getAllUsers(){
		return repo.findAll();
	}
	
	@PostMapping("/users-service/users")
	public ResponseEntity<CustomUser> createUser(@RequestBody CustomUser user, HttpServletRequest request) {
		String requestRole = request.getHeader("X-User-Role");
		System.out.println(requestRole);
		System.out.println(user.getRole());
		if((Objects.equals(user.getRole(), "ADMIN") || Objects.equals(user.getRole(), "OWNER")) && Objects.equals(requestRole, "ROLE_ADMIN")) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		CustomUser createdUser = repo.save(user);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}
 }
