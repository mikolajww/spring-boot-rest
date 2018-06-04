package pl.dmcs.mww.model.response;

import pl.dmcs.mww.model.Role;
import pl.dmcs.mww.model.RoleName;

import java.util.HashSet;
import java.util.Set;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Set<RoleName> roles = new HashSet<>();

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
        this.roles.add(RoleName.ROLE_STUDENT);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Set<RoleName> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleName> roles) {
        this.roles = roles;
    }
}
