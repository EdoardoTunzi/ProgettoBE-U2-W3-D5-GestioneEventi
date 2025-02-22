package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security.services;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Ruolo;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Ruolo ruolo;

    public static UserDetailsImpl costruisciDettagli(Utente user) {
        return new UserDetailsImpl(user.getId(),user.getUsername(),user.getEmail(), user.getPassword(), user.getRuolo());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ruolo.getNome().name()));
    }

    @Override
    public String getPassword() {
        return password;//mancava
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}
