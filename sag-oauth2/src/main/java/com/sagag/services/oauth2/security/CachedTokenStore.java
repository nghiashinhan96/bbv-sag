package com.sagag.services.oauth2.security;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.hazelcast.utils.TokenExpiry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of token services that stores tokens in Hazelcast cache map.
 * <p>
 * This class was copied the implementation from the Spring OAuth2 <code>InMemoryTokenStore</code>,
 * version 2.3.1. The only difference between this class and <code>InMemoryTokenStore</code> is that
 * all cache related to the token was saved to Hazelcast memory section, used for load balancing.
 *
 * @see {@link org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore}
 */
@Component("tokenStore")
@Primary
public class CachedTokenStore implements TokenStore {

  private static final int DEFAULT_FLUSH_INTERVAL = 1000;

  private IMap<String, OAuth2AccessToken> accessTokenStore = null;

  private IMap<String, OAuth2AccessToken> authenticationToAccessTokenStore = null;

  private IMap<String, Collection<OAuth2AccessToken>> userNameToAccessTokenStore = null;

  private IMap<String, Collection<OAuth2AccessToken>> clientIdToAccessTokenStore = null;

  private IMap<String, OAuth2RefreshToken> refreshTokenStore = null;

  private IMap<String, String> accessTokenToRefreshTokenStore = null;

  private IMap<String, OAuth2Authentication> authenticationStore = null;

  private IMap<String, OAuth2Authentication> refreshTokenAuthenticationStore = null;

  private IMap<String, String> refreshTokenToAccessTokenStore = null;

  private IMap<String, TokenExpiry> expiryMap = null;

  private final DelayQueue<TokenExpiry> expiryQueue = new DelayQueue<>();

  private int flushInterval = DEFAULT_FLUSH_INTERVAL;

  private AuthenticationKeyGenerator authenticationKeyGenerator =
      new DefaultAuthenticationKeyGenerator();

  private AtomicInteger flushCounter = new AtomicInteger(0);

  private final HazelcastInstance hazelcastInstance;

  @Autowired
  public CachedTokenStore(HazelcastInstance hazelcastInstance) {
    this.hazelcastInstance = hazelcastInstance;
  }

  /**
   * The number of tokens to store before flushing expired tokens. Defaults to 1000.
   *
   * @param flushInterval the interval to set
   */
  public void setFlushInterval(int flushInterval) {
    this.flushInterval = flushInterval;
  }

  /**
   * The interval (count of token inserts) between flushing expired tokens.
   *
   * @return the flushInterval the flush interval
   */
  public int getFlushInterval() {
    return flushInterval;
  }

  /**
   * Convenience method for super admin users to remove all tokens (useful for testing, not really
   * in production)
   */
  public void clear() {
    accessTokenStore.clear();
    authenticationToAccessTokenStore.clear();
    clientIdToAccessTokenStore.clear();
    refreshTokenStore.clear();
    accessTokenToRefreshTokenStore.clear();
    authenticationStore.clear();
    refreshTokenAuthenticationStore.clear();
    refreshTokenToAccessTokenStore.clear();
    expiryQueue.clear();
  }

  public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
    this.authenticationKeyGenerator = authenticationKeyGenerator;
  }

  public int getAccessTokenCount() {
    Assert.state(
        accessTokenStore.isEmpty()
            || accessTokenStore.size() >= accessTokenToRefreshTokenStore.size(),
        "Too many refresh tokens");
    Assert.state(accessTokenStore.size() == authenticationToAccessTokenStore.size(),
        "Inconsistent token store state");
    Assert.state(accessTokenStore.size() <= authenticationStore.size(),
        "Inconsistent authentication store state");
    return accessTokenStore.size();
  }

  public int getRefreshTokenCount() {
    Assert.state(refreshTokenStore.size() == refreshTokenToAccessTokenStore.size(),
        "Inconsistent refresh token store state");
    return accessTokenStore.size();
  }

  public int getExpiryTokenCount() {
    return expiryQueue.size();
  }

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
    String key = authenticationKeyGenerator.extractKey(authentication);
    OAuth2AccessToken accessToken = authenticationToAccessTokenStore.get(key);
    if (accessToken == null) {
      return null;
    }
    final OAuth2Authentication currentAuthentication = readAuthentication(accessToken.getValue());
    if (currentAuthentication == null
        || !key.equals(authenticationKeyGenerator.extractKey(currentAuthentication))) {
      // Keep the stores consistent (maybe the same user is represented by this authentication but
      // the details
      // have changed)
      storeAccessToken(accessToken, authentication);
    }
    return accessToken;
  }

  @Override
  public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
    return readAuthentication(token.getValue());
  }

  @Override
  public OAuth2Authentication readAuthentication(String token) {
    return this.authenticationStore.get(token);
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
    return readAuthenticationForRefreshToken(token.getValue());
  }

  public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
    return this.refreshTokenAuthenticationStore.get(token);
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
    if (this.flushCounter.incrementAndGet() >= this.flushInterval) {
      flush();
      this.flushCounter.set(0);
    }
    this.accessTokenStore.put(token.getValue(), token);
    this.authenticationStore.put(token.getValue(), authentication);
    this.authenticationToAccessTokenStore.put(authenticationKeyGenerator.extractKey(authentication),
        token);
    if (!authentication.isClientOnly()) {
      addToCollection(this.userNameToAccessTokenStore, getApprovalKey(authentication), token);
    }
    addToCollection(this.clientIdToAccessTokenStore,
        authentication.getOAuth2Request().getClientId(), token);
    if (token.getExpiration() != null) {
      TokenExpiry expiry = new TokenExpiry(token.getValue(), token.getExpiration());
      expiryQueue.remove(expiryMap.put(token.getValue(), expiry));
      this.expiryQueue.put(expiry);
    }
    if (token.getRefreshToken() != null && token.getRefreshToken().getValue() != null) {
      this.refreshTokenToAccessTokenStore.put(token.getRefreshToken().getValue(), token.getValue());
      this.accessTokenToRefreshTokenStore.put(token.getValue(), token.getRefreshToken().getValue());
    }
  }

  private String getApprovalKey(OAuth2Authentication authentication) {
    String userName = authentication.getUserAuthentication() == null ? ""
        : authentication.getUserAuthentication().getName();
    return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
  }

  private String getApprovalKey(String clientId, String userName) {
    return clientId + (userName == null ? "" : ":" + userName);
  }

  private void addToCollection(IMap<String, Collection<OAuth2AccessToken>> store, String key,
      OAuth2AccessToken token) {
    if (!store.containsKey(key)) {
      synchronized (store) {
        if (!store.containsKey(key)) {
          store.put(key, new HashSet<OAuth2AccessToken>());
        }
      }
    }
    store.get(key).add(token);
  }

  @Override
  public void removeAccessToken(OAuth2AccessToken accessToken) {
    removeAccessToken(accessToken.getValue());
  }

  @Override
  public OAuth2AccessToken readAccessToken(String tokenValue) {
    return this.accessTokenStore.get(tokenValue);
  }

  public void removeAccessToken(String tokenValue) {
    OAuth2AccessToken removed = this.accessTokenStore.remove(tokenValue);
    this.accessTokenToRefreshTokenStore.remove(tokenValue);
    // Don't remove the refresh token - it's up to the caller to do that
    OAuth2Authentication authentication = this.authenticationStore.remove(tokenValue);
    if (authentication != null) {
      this.authenticationToAccessTokenStore
          .remove(authenticationKeyGenerator.extractKey(authentication));
      Collection<OAuth2AccessToken> tokens;
      String clientId = authentication.getOAuth2Request().getClientId();
      tokens =
          this.userNameToAccessTokenStore.get(getApprovalKey(clientId, authentication.getName()));
      if (tokens != null) {
        tokens.remove(removed);
      }
      tokens = this.clientIdToAccessTokenStore.get(clientId);
      if (tokens != null) {
        tokens.remove(removed);
      }
      this.authenticationToAccessTokenStore
          .remove(authenticationKeyGenerator.extractKey(authentication));
    }
  }

  @Override
  public void storeRefreshToken(OAuth2RefreshToken refreshToken,
      OAuth2Authentication authentication) {
    this.refreshTokenStore.put(refreshToken.getValue(), refreshToken);
    this.refreshTokenAuthenticationStore.put(refreshToken.getValue(), authentication);
  }

  @Override
  public OAuth2RefreshToken readRefreshToken(String tokenValue) {
    return this.refreshTokenStore.get(tokenValue);
  }

  @Override
  public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
    removeRefreshToken(refreshToken.getValue());
  }

  public void removeRefreshToken(String tokenValue) {
    this.refreshTokenStore.remove(tokenValue);
    this.refreshTokenAuthenticationStore.remove(tokenValue);
    this.refreshTokenToAccessTokenStore.remove(tokenValue);
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
    removeAccessTokenUsingRefreshToken(refreshToken.getValue());
  }

  private void removeAccessTokenUsingRefreshToken(String refreshToken) {
    String accessToken = this.refreshTokenToAccessTokenStore.remove(refreshToken);
    if (accessToken != null) {
      removeAccessToken(accessToken);
    }
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId,
      String userName) {
    Collection<OAuth2AccessToken> result =
        userNameToAccessTokenStore.get(getApprovalKey(clientId, userName));
    return result != null ? Collections.unmodifiableCollection(result) : Collections.emptySet();
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
    Collection<OAuth2AccessToken> result = clientIdToAccessTokenStore.get(clientId);
    return result != null ? Collections.unmodifiableCollection(result) : Collections.emptySet();
  }

  private void flush() {
    TokenExpiry expiry = expiryQueue.poll();
    while (expiry != null) {
      removeAccessToken(expiry.getValue());
      expiry = expiryQueue.poll();
    }
  }

  @PostConstruct
  public void initMaps() {
    accessTokenStore = hazelcastInstance.getMap(HazelcastMaps.ACCESS_TOKEN_STORE.name());
    authenticationToAccessTokenStore =
        hazelcastInstance.getMap(HazelcastMaps.AUTHENTICATION_TO_ACCESS_TOKEN_STORE.name());
    userNameToAccessTokenStore =
        hazelcastInstance.getMap(HazelcastMaps.USER_NAME_TO_ACCESS_TOKEN_STORE.name());
    clientIdToAccessTokenStore =
        hazelcastInstance.getMap(HazelcastMaps.CLIENT_ID_TO_ACCESS_TOKEN_STORE.name());
    refreshTokenStore =
        hazelcastInstance.getMap(HazelcastMaps.CLIENT_ID_TO_ACCESS_TOKEN_STORE.name());
    accessTokenToRefreshTokenStore =
        hazelcastInstance.getMap(HazelcastMaps.ACCESS_TOKEN_TO_REFRESH_TOKEN_STORE.name());
    authenticationStore = hazelcastInstance.getMap(HazelcastMaps.AUTHENTICATION_STORE.name());
    refreshTokenAuthenticationStore =
        hazelcastInstance.getMap(HazelcastMaps.REFRESH_TOKEN_AUTHENTICATION_STORE.name());
    refreshTokenToAccessTokenStore =
        hazelcastInstance.getMap(HazelcastMaps.REFRESH_TOKEN_TO_ACCESS_TOKEN_STORE.name());
    expiryMap = hazelcastInstance.getMap(HazelcastMaps.EXPIRY_MAP.name());
  }

}
