package com.ipcb.ad.microservicogestao.Services;

import com.ipcb.ad.microservicogestao.Models.Role;
import com.ipcb.ad.microservicogestao.Models.Utilizador;
import com.ipcb.ad.microservicogestao.Repositories.RepositorioUtilizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicoDetalhesUtilizador implements UserDetailsService {
    @Autowired
    private RepositorioUtilizador repositorioUtilizador;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utilizador> userOptional = repositorioUtilizador.findByNome(username);

        if (userOptional.isPresent() && userOptional.get().isEnabled()) {
            Utilizador user = userOptional.get();
            Collection<Role> roles = user.getRoles();
            return buildUserForAuthentication(user, roles);
        } else {
            throw new UsernameNotFoundException("Utilizador Inválido");
        }
    }

    private UserDetails buildUserForAuthentication(Utilizador user, Collection<Role> roles) {
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        return new User(
                user.getNome(),
                user.getPassword(),
                authorities
        );
    }
}