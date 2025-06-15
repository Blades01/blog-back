package blog_app.demo.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    // --- Getters ---
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public Set<Role> getRoles() { return roles; }
    public String getEmail() { return email; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
    public void setEmail(String email) { this.email = email; }
}
