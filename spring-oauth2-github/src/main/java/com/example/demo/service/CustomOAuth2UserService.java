package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.controller.repo.UserRepository;
import com.example.demo.entity.User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("id") != null ? 
                           oAuth2User.getAttribute("id").toString() : 
                           oAuth2User.getName();
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String login = oAuth2User.getAttribute("login");
        String avatarUrl = oAuth2User.getAttribute("avatar_url");

        // Try to find user by providerId first, then by email
        User user = userRepository.findByProviderId(providerId)
                .orElseGet(() -> userRepository.findByEmail(email).orElse(new User()));

        user.setName(name != null ? name : login);
        user.setEmail(email);
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setAvatarUrl(avatarUrl);

        userRepository.save(user);

        return oAuth2User;
    }
}
