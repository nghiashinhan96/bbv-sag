package com.sagag.services.article.api.token;

public interface ErpAuthenService extends ErpTokenScheduledTasks {

  String getAxToken();

  String refreshAccessToken();
}
