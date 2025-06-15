package blog_app.demo.model;

import jakarta.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Getter
    public Long getId() { return id; }
    public String getName() { return name; } // <-- This is what was missing

    // Setter
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}
