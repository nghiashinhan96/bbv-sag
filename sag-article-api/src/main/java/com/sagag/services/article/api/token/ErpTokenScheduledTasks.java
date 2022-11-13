package com.sagag.services.article.api.token;

import com.sagag.services.common.schedule.ScheduledTask;

public interface ErpTokenScheduledTasks extends ScheduledTask {

  void retryRefreshErpAccessToken();
}
