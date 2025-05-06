package com.purwafest.purwafest.auth.infrastructure.security.filters;

import com.purwafest.purwafest.auth.application.BlackListToken;
import com.purwafest.purwafest.auth.infrastructure.security.ExtractTokenHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BlackListTokenFilter extends OncePerRequestFilter {
    private final BlackListToken blackListToken;

    public BlackListTokenFilter(BlackListToken blackListToken){
        this.blackListToken = blackListToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = ExtractTokenHelper.getTokenFromRequest(request);
        if(token != null && blackListToken.isBlackListed(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request,response);
    }
}
