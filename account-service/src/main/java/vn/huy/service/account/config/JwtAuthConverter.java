package vn.huy.service.account.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import vn.huy.service.account.util.ConstantUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtAuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Map<String, Collection<String>> realmAccess = jwt.getClaim(ConstantUtil.CLAIM_REALM_ACCESS);
        if (realmAccess != null && !realmAccess.isEmpty()) {
            Collection<String> roles = realmAccess.get(ConstantUtil.CLAIM_ROLES);
            if (roles != null && !roles.isEmpty()) {
                Collection<GrantedAuthority> realmRoles = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(ConstantUtil.PREFIX_ROLE + role))
                        .collect(Collectors.toList());
                grantedAuthorities.addAll(realmRoles);
            }
        }
        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim(ConstantUtil.CLAIM_RESOURCE_ACCESS);
        if (resourceAccess != null && !resourceAccess.isEmpty()) {
            resourceAccess.forEach((resource, resourceClaims) -> {
                resourceClaims.get(ConstantUtil.CLAIM_ROLES).forEach(
                        role -> grantedAuthorities.add(new SimpleGrantedAuthority(ConstantUtil.PREFIX_ROLE + role))
                );
            });
        }
        return grantedAuthorities;
    }
}
