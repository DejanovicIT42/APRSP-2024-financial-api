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
		Role requestRole = Role.valueOf(request.getHeader("X-User-Role").substring(5)); //ko je zatrazio request

		if(user.getRole() == Role.ADMIN && requestRole == Role.ADMIN) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		if(Objects.equals(user.getRole(), "OWNER")) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		CustomUser createdUser = repo.save(user);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@PutMapping("/update/{email}")
	public ResponseEntity<CustomUser> updateUser(@RequestBody CustomUser updateUser, @PathVariable String email, HttpServletRequest request) {

		if(!Objects.equals(updateUser.getEmail(), email)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Role requestRole = Role.valueOf(request.getHeader("X-User-Role").substring(5));
		Optional<CustomUser> checkUser = repo.findByEmail(email);

		if(checkUser.isEmpty())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		if(requestRole == Role.ADMIN && updateUser.getRole() != Role.USER){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if(requestRole != Role.OWNER) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		CustomUser updatedUser = repo.save(updateUser);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
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
