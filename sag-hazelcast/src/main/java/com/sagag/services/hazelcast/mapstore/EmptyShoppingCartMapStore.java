package com.sagag.services.hazelcast.mapstore;

import com.sagag.services.common.profiles.EnableShoppingCartMapStore;
import com.sagag.services.hazelcast.domain.ShoppingCartCache;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
@EnableShoppingCartMapStore(false)
@Slf4j
public class EmptyShoppingCartMapStore extends ConnectShoppingCartMapStore {

  private static final String WARN_MSG = "No support for this System";

  @Override
  public void store(String key, ShoppingCartCache value) {
    log.debug(WARN_MSG);
  }

  @Override
  public void storeAll(Map<String, ShoppingCartCache> map) {
    log.debug(WARN_MSG);
  }

  @Override
  public void delete(String key) {
    log.debug(WARN_MSG);
  }

  @Override
  public void deleteAll(Collection<String> keys) {
    log.debug(WARN_MSG);
  }

  @Override
  public ShoppingCartCache load(String key) {
    log.debug(WARN_MSG);
    return null;
  }

  @Override
  public Map<String, ShoppingCartCache> loadAll(Collection<String> keys) {
    log.debug(WARN_MSG);
    return null;
  }

  @Override
  public Iterable<String> loadAllKeys() {
    log.debug(WARN_MSG);
    return null;
  }
}
