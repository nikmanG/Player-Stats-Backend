package io.github.nikmang.playerinfo.config.filters;

import io.github.nikmang.playerinfo.config.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private Constants constants;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, Constants constants) {
        this.authenticationManager = authenticationManager;
        this.constants = constants;
        setFilterProcessesUrl(Constants.AUTH_LOGIN_URL);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<String> authorities = user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] secretKey = constants.getJwtSecret();

        String token = Jwts
                .builder()
                .signWith(Keys.hmacShaKeyFor(secretKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", constants.getTokenType())
                .setIssuer(constants.getTokenIssuer())
                .setAudience(constants.getTokenAudience())
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 360000000L))
                .claim("rol", authorities)
                .compact();

        response.addHeader(constants.getTokenHeader(), constants.getTokenPrefix() + token);
    }
}
