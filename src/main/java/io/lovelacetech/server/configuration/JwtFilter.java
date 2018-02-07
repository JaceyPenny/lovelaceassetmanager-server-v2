package io.lovelacetech.server.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.Messages;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;

public class JwtFilter extends GenericFilterBean {

  private UserRepository userRepository;

  public JwtFilter setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
    return this;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) req;
    final HttpServletResponse response = (HttpServletResponse) res;
    final String authHeader = request.getHeader("Authorization");

    if ("OPTIONS".equals(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);

      chain.doFilter(req, res);
    } else {

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        throw new ServletException("Missing or invalid Authorization header");
      }

      final String token = authHeader.substring(7);

      try {
        Claims claims = Jwts.parser()
            .setSigningKey(AuthenticationUtils.JWT_SIGNATURE_KEY)
            .parseClaimsJws(token)
            .getBody();

        ApiUser user = ApiUser.fromClaims((LinkedHashMap<String, Object>) claims.get("user"));
        User loadedUser = userRepository.findOne(user.getId());
        if (loadedUser == null) {
          throw new ServletException(Messages.NO_USER_FOUND_BY_ID);
        }

        request.setAttribute("authenticatedUser", loadedUser.toApi());
      } catch (final SignatureException e) {
        throw new ServletException("Invalid token");
      }

      chain.doFilter(req, res);
    }
  }
}
