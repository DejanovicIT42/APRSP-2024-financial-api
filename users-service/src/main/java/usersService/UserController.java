package usersService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import usersService.model.CustomUser;

@RestController
@RequestMapping("/users-service/users")
public class UserController {

	@Autowired
	private CustomUserRepository repo;
	
	@GetMapping()
	public List<CustomUser> getAllUsers(){
		return repo.findAll();
	}
	
	@PostMapping()
	public ResponseEntity<CustomUser> createUser(@RequestBody CustomUser user, HttpServletRequest request) {
		String requestRole = request.getHeader("X-User-Role");

		if(Objects.equals(user.getRole(), "ADMIN") && Objects.equals(requestRole, "ROLE_ADMIN")) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		if(Objects.equals(user.getRole(), "OWNER")) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		CustomUser createdUser = repo.save(user);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{email}")
	public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email, HttpServletRequest request){
			Optional<CustomUser> deleteUser = repo.findByEmail(email);

			if(deleteUser.isEmpty())
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			repo.deleteByEmail(email);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
 }
