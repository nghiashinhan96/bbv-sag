package com.sagag.services.hazelcast.api;


public interface DmsExportCacheService {

  void add(String fileContent, String userKey);

  String getFileContent(String userKey);

  void clearCache(String userKey);
}
