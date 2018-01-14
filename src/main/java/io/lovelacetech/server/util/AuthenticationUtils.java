package io.lovelacetech.server.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;

import java.util.Date;

public class AuthenticationUtils {

  public static final String JWT_SIGNATURE_KEY = "lovelace!ass!etmanager***jan4changes";

  public static String jwtSign(ApiUser user) {
    return Jwts.builder()
        .claim("user", user)
        .setIssuedAt(new Date())
        .signWith(SignatureAlgorithm.HS256, AuthenticationUtils.JWT_SIGNATURE_KEY)
        .compact();
  }

  public static boolean userIsAtLeast(ApiUser user, AccessLevel accessLevel) {
    return user.getAccessLevel().toInt() >= accessLevel.toInt();
  }

  public static boolean userIsSuper(ApiUser user) {
    return user.getAccessLevel() == AccessLevel.SUPER;
  }

  public static boolean userIsAdmin(ApiUser user) {
    return user.getAccessLevel() == AccessLevel.ADMIN;
  }
}
