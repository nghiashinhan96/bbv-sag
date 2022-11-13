package com.sagag.services.service.user.cache.impl;

import com.sagag.services.common.enums.country.UserLoaderMode;
import com.sagag.services.service.profiles.UserLoaderModeProfile;

import org.springframework.stereotype.Component;

@Component
@UserLoaderModeProfile(UserLoaderMode.DEFAULT)
public class DefaultSyncUserLoaderImpl extends AbstractSyncUserLoader {

}
