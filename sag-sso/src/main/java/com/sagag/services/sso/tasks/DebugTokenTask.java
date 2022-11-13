package com.sagag.services.sso.tasks;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class DebugTokenTask {
  public static String debug(String token) {
    StringBuilder sb = new StringBuilder("");

    DecodedJWT jwt = JWT.decode(token);
    String type = jwt.getType();

    sb.append("Algorithm:\t").append(jwt.getAlgorithm());
    sb.append("\r\nAudience:");
    jwt.getAudience().forEach(m -> {
      sb.append("\t").append(m).append("\r\n");
    });

    sb.append("Claims:\r\n");
    jwt.getClaims().forEach((k, v) -> {
      sb.append("\t").append(k).append(":\t").append(v.asString()).append("\r\n");
    });

    sb.append("Expiration Date:\t").append(jwt.getExpiresAt());
    sb.append("\r\nIssuing Date:\t\t").append(jwt.getIssuedAt());
    sb.append("\r\nIssuer:\t\t\t").append(jwt.getIssuer());
    sb.append("\r\nKID:\t\t\t").append(jwt.getKeyId());
    sb.append("\r\nNote Before Date:\t").append(jwt.getNotBefore());
    sb.append("\r\nSignature\t\t").append(jwt.getSignature());
    sb.append("\r\nSubject\t\t\t").append(jwt.getSubject());
    sb.append("\r\nType\t\t\t").append(type);

    return sb.toString();
  }
}
