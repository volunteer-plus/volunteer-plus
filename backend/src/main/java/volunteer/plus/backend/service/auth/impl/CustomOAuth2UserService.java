package volunteer.plus.backend.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.UserService;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends OidcUserService {

    private final UserService userService;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);
        String email = oauthUser.getAttribute("email");

        User user = (User) userService.loadUserByUsername(email);

        if (user == null) {
            user = User
                    .builder()
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .email(email)
                    .firstName(oauthUser.getAttribute("given_name"))
                    .lastName(oauthUser.getAttribute("family_name"))
                    .build();
        } else {
            user.setFirstName(oauthUser.getAttribute("given_name"));
            user.setLastName(oauthUser.getAttribute("family_name"));
        }

        userRepository.save(user);
        return user;
    }

}
