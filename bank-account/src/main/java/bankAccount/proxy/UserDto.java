package bankAccount.proxy;

import bankAccount.Role;

public class UserDto {
    private long id;

    private String email;

    private String password;

    private Role role;

    private String environment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public UserDto(long id, String email, String password, Role role, String environment) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.environment = environment;
    }
}
