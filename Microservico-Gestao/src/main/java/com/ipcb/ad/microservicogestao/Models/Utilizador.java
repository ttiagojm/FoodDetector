package com.ipcb.ad.microservicogestao.Models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Utilizador implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome não pode ser vazio")
    private String nome;

    @NotBlank(message = "Email não pode ser vazio")
    @Column(unique = true)
    @Email(message = "Email não válido", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotBlank(message = "Password não pode ser vazia")
    @Length(min = 8, message = "Password deve conter pelo menos 8 caracteres")
    @Pattern(regexp = ".*\\d.*", message = "Password deve conter pelo menos 1 digito")
    private String password;

    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "utilizador_role",
            joinColumns = @JoinColumn(name = "utilizador_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    private boolean disabled = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Utilizador(Integer id, String nome, String email, String password) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    public Utilizador(String nome, String email, String password, Collection<Role> roles) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = "ROLE_" + (getUsername().equalsIgnoreCase("admin") ? "ADMIN" : "STANDARD");
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getUsername() {
        return this.nome;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !this.disabled;
    }

}