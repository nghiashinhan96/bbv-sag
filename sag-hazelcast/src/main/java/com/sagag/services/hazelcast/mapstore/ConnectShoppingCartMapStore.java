package com.sagag.services.hazelcast.mapstore;

import com.hazelcast.core.MapStore;
import com.sagag.services.hazelcast.domain.ShoppingCartCache;

public abstract class ConnectShoppingCartMapStore implements MapStore<String, ShoppingCartCache> {

}
