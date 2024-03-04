package usersService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usersService.proxy.BankAccountProxy;
import usersService.miloradEror.CustomExceptions;
import usersService.model.CustomUser;
import usersService.proxy.CryptoWalletProxy;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users-service/users")
public class UserController {

    @Autowired
    private CustomUserRepository repo;

    @Autowired
    private BankAccountProxy bankAccountProxy;

    @Autowired
    private CryptoWalletProxy walletProxy;

    @GetMapping("/{email}")
    private ResponseEntity<CustomUser> getByEmail(@PathVariable String email) throws Exception {
        Optional<CustomUser> user = repo.findByEmail(email);
        if (user.isEmpty()) {
            throw new CustomExceptions.NoContentFoundException("No user found");
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping()
    public List<CustomUser> getAllUsers() {
        return repo.findAll();
    }

    @PostMapping()
    public ResponseEntity<CustomUser> createUser(@RequestBody CustomUser user, HttpServletRequest request) throws Exception {
        Role requestRole;
        try {
            requestRole = Role.valueOf(request.getHeader("X-User-Role").substring(5)); //ko je zatrazio request
        } catch (Exception w) {
            throw new CustomExceptions.UnauthorizedAccountException("User is not authorized");
        }

        if (requestRole == Role.ADMIN && user.getRole() != Role.USER) {
            throw new CustomExceptions.UnauthorizedAccountException("ADMIN can create only USER roles");
        }

        if (user.getRole() == Role.OWNER) {
            throw new CustomExceptions.OwnerAlreadyExistsException("There can only be one OWNER role in the system.");
        }

        CustomUser createdUser = repo.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<CustomUser> updateUser(@RequestBody CustomUser updateUser, @PathVariable String email, HttpServletRequest request) throws Exception {

        if (!Objects.equals(updateUser.getEmail(), email)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Role requestRole = Role.valueOf(request.getHeader("X-User-Role").substring(5));
        Optional<CustomUser> checkUser = repo.findByEmail(email);

        if (checkUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (requestRole == Role.ADMIN && updateUser.getRole() != Role.USER) {
            throw new CustomExceptions.UnauthorizedAccountException("ADMIN can update only USER roles.");
        }

        if (requestRole == Role.USER) {
            throw new CustomExceptions.UnauthorizedAccountException("Only OWNER can update all other users.");
        }

        CustomUser updatedUser = repo.save(updateUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email, HttpServletRequest request) throws Exception {
        Role requestRole = Role.valueOf(request.getHeader("X-User-Role").substring(5));
        Optional<CustomUser> deleteUser = repo.findByEmail(email);

        if (requestRole != Role.OWNER) {
            throw new CustomExceptions.UnauthorizedAccountException("Only OWNER can delete other users.");
        }

        if (deleteUser.isEmpty()) {
            throw new CustomExceptions.UserDoesntExistException("This user does not exist.");
        }

        if (deleteUser.get().getRole() == Role.USER) {
            bankAccountProxy.deleteBankAccount(email);
        }

        if(deleteUser.get().getRole() == Role.USER){
            walletProxy.deleteCryptoWallet(email);
        }

        repo.deleteByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
