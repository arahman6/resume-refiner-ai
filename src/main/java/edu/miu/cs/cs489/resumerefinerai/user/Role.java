package edu.miu.cs.cs489.resumerefinerai.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(Permission.ADMIN_WRITE, Permission.ADMIN_READ)
    ),
    MEMBER(
            Set.of(Permission.MEMBER_WRITE, Permission.MEMBER_READ)
    );

    @Getter
    private final Set<Permission> permissions;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+name()));
        return authorities;
    }
}
