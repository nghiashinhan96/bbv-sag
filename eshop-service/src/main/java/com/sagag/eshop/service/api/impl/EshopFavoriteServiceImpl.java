package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopFavoriteRepository;
import com.sagag.eshop.repo.entity.EshopFavorite;
import com.sagag.eshop.repo.enums.EshopFavoriteType;
import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.EshopFavoriteException;
import com.sagag.eshop.service.validator.criteria.EshopFavoriteValidateCriteria;
import com.sagag.eshop.service.validator.favorite.EshopFavoriteValidator;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteLeafKeyDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteRequestDto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
@Transactional
public class EshopFavoriteServiceImpl implements EshopFavoriteService {

  private static final Integer TOP_LATEST_COMMENTS = 5;

  @Autowired
  private EshopFavoriteRepository favoriteRepository;

  @Autowired
  private List<EshopFavoriteValidator> favoriteTypeValidators;

  private EshopFavoriteValidator getValidator(EshopFavoriteDto favoriteItem) {
    Assert.notNull(favoriteItem, "The given favoriteItem must not be null");
    Assert.hasText(favoriteItem.getType(), "The given favorite type must not be empty");

    EshopFavoriteType favoriteType = EshopFavoriteType.valueOf(favoriteItem.getType());

    return favoriteTypeValidators.stream()
        .filter(typeProcessor -> typeProcessor.supportMode(favoriteType)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No support context with this"));
  }

  @Override
  public void processFavoriteItem(UserInfo userInfo, EshopFavoriteDto favoriteItem, Boolean isAdd)
      throws ValidationException {
    EshopFavoriteValidator validator = getValidator(favoriteItem);
    validator.validate(EshopFavoriteValidateCriteria.builder().favoriteItem(favoriteItem).add(isAdd)
        .userId(userInfo.getOriginalUserId()).build());

    if (BooleanUtils.isTrue(isAdd)) {
      addFavoriteItem(favoriteItem, userInfo.getOriginalUserId());
    } else {
      deleteFavoriteItem(validator.getFavoriteItemValid());
    }
  }

  private void addFavoriteItem(EshopFavoriteDto item, Long userId) {
    EshopFavorite entity = SagBeanUtils.map(item, EshopFavorite.class);
    entity.setUserId(userId);
    entity.setType(EshopFavoriteType.valueOf(item.getType()));
    entity.setCreatedTime(Calendar.getInstance().getTime());
    entity.setLastUpdate(Calendar.getInstance().getTime());
    favoriteRepository.save(entity);
  }

  private void deleteFavoriteItem(Optional<EshopFavorite> itemValid) {
    if (itemValid.isPresent()) {
      favoriteRepository.delete(itemValid.get());
    }
  }

  @Override
  public List<EshopFavoriteDto> getFavoriteItemList(UserInfo userInfo) {
    return favoriteRepository.findByUserIdOrderByCreatedTimeDesc(userInfo.getOriginalUserId())
        .stream().map(item -> SagBeanUtils.map(item, EshopFavoriteDto.class))
        .collect(Collectors.toList());
  }

  @Override
  public EshopFavoriteDto updateFavoriteItem(UserInfo userInfo, EshopFavoriteDto favoriteItem)
      throws ValidationException {
    final EshopFavoriteValidator eshopFavValidator = getValidator(favoriteItem);
    eshopFavValidator.validate(EshopFavoriteValidateCriteria.builder().favoriteItem(favoriteItem)
        .add(false).userId(userInfo.getOriginalUserId()).build());

    final EshopFavorite validatedEshopFavorite = eshopFavValidator.getFavoriteItemValid()
        .orElseThrow(() -> new EshopFavoriteException("Eshop Favorite Item has error"));

    validatedEshopFavorite.setTitle(favoriteItem.getTitle());
    validatedEshopFavorite.setComment(favoriteItem.getComment());
    validatedEshopFavorite.setLastUpdate(Calendar.getInstance().getTime());
    final EshopFavorite updatedEshopFavorite = favoriteRepository.save(validatedEshopFavorite);

    return SagBeanUtils.map(updatedEshopFavorite, EshopFavoriteDto.class);
  }

  @Override
  public Page<EshopFavoriteDto> getLatestFavoriteItemList(UserInfo userInfo, Pageable pageable) {

    return favoriteRepository.findLatestFavoriteByUserId(userInfo.getOriginalUserId(), pageable)
        .map(item -> SagBeanUtils.map(item, EshopFavoriteDto.class));
  }

  private Predicate<EshopFavoriteDto> isVehicleItem() {
    return dto -> StringUtils.equals(dto.getType(), EshopFavoriteType.VEHICLE.name())
        && !StringUtils.isEmpty(dto.getVehicleId());
  }

  private Predicate<EshopFavoriteDto> isArticleItem() {
    return dto -> StringUtils.equals(dto.getType(), EshopFavoriteType.ARTICLE.name())
        && !StringUtils.isEmpty(dto.getArticleId());
  }

  private Predicate<EshopFavoriteDto> isLeafNodeItem() {
    return dto -> StringUtils.equals(dto.getType(), EshopFavoriteType.LEAF_NODE.name())
        && !StringUtils.isEmpty(dto.getTreeId()) && !StringUtils.isEmpty(dto.getLeafId());
  }

  @Override
  public List<EshopFavoriteDto> getInfoItemsByList(UserInfo userInfo,
      List<EshopFavoriteDto> items) {
    List<String> vehicleIds = items.stream().filter(isVehicleItem())
        .map(EshopFavoriteDto::getVehicleId).collect(Collectors.toList());

    // Prevent case query In empty list
    vehicleIds.add(StringUtils.EMPTY);

    List<String> artIds = items.stream().filter(isArticleItem()).map(EshopFavoriteDto::getArticleId)
        .collect(Collectors.toList());
    // Prevent case query In empty list
    artIds.add(StringUtils.EMPTY);

    List<String> leafNodeKeys = buildLeafNodeKey(items);

    return favoriteRepository
        .findFavoriteItems(userInfo.getOriginalUserId(), artIds, vehicleIds, leafNodeKeys).stream()
        .map(item -> SagBeanUtils.map(item, EshopFavoriteDto.class)).collect(Collectors.toList());
  }

  private List<String> buildLeafNodeKey(List<EshopFavoriteDto> favoriteList) {
    Map<EshopFavoriteLeafKeyDto, List<EshopFavoriteDto>> mapByLeafKey =
        favoriteList.stream().filter(isLeafNodeItem()).collect(Collectors
            .groupingBy(dto -> new EshopFavoriteLeafKeyDto(dto.getTreeId(), dto.getLeafId())));

    if (mapByLeafKey.isEmpty()) {
      return Collections.emptyList();
    }

    List<String> keys = new LinkedList<>();

    mapByLeafKey.forEach((key, value) -> value.stream().map(EshopFavoriteDto::getGaId).forEach(gaId -> {
      String keyStr = new StringBuilder(StringUtils.trimToEmpty(key.getTreeId()))
          .append(SagConstants.UNDERSCORE).append(StringUtils.trimToEmpty(key.getLeafId()))
          .append(SagConstants.UNDERSCORE).append(StringUtils.trimToEmpty(gaId)).toString();
      keys.add(keyStr);
    }));
    return keys;
  }

  @Override
  public Page<EshopFavoriteDto> searchFavoriteItem(UserInfo userInfo,
      EshopFavoriteRequestDto requestDto) {
    Assert.notNull(requestDto, "The given requestDto must not be null");

    final EshopFavoriteType favoriteType = EshopFavoriteType.valueOfSafely(requestDto.getType());
    final String key = StringUtils.trimToEmpty(requestDto.getKeySearch());

    return favoriteRepository
        .searchFavoriteItems(userInfo.getOriginalUserId(), favoriteType, key,
            PageRequest.of(requestDto.getPage(), requestDto.getSize()))
        .map(item -> SagBeanUtils.map(item, EshopFavoriteDto.class));
  }

  @Override
  public void updateFavoriteFlagVehicle(UserInfo userInfo, List<VehicleDto> vehicles) {
    List<String> vehicleIds = vehicles.stream().filter(v -> !StringUtils.isEmpty(v.getId()))
        .map(VehicleDto::getId).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(vehicleIds)) {
      return;
    }

    final Map<String, String> favoriteIdsMap = favoriteRepository
        .findFavoriteItemsByVehicleIds(userInfo.getOriginalUserId(), vehicleIds).stream()
        .collect(Collectors.toMap(EshopFavorite::getVehicleId, EshopFavorite::getComment));

    vehicles.stream().filter(v -> favoriteIdsMap.containsKey((v.getId()))).forEach(v -> {
      v.setFavorite(true);
      v.setFavoriteComment(favoriteIdsMap.get(v.getId()));
    });
  }

  @Override
  public void updateFavoriteFlagArticles(UserInfo userInfo, List<ArticleDocDto> articles) {
    List<String> articleIds = articles.stream().filter(a -> !StringUtils.isEmpty(a.getArtid()))
        .map(ArticleDocDto::getArtid).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(articleIds)) {
      return;
    }

    final Map<String, String> favoriteIdsMap = favoriteRepository
        .findFavoriteItemsByArticleIds(userInfo.getOriginalUserId(), articleIds).stream()
        .collect(Collectors.toMap(EshopFavorite::getArticleId, EshopFavorite::getComment));

    articles.stream().filter(a -> favoriteIdsMap.containsKey(a.getArtid())).forEach(a -> {
      a.setFavorite(true);
      a.setFavoriteComment(favoriteIdsMap.get(a.getArtid()));
    });
  }

  @Override
  public List<String> searchComments(String text, UserInfo userInfo) {
    Assert.notNull(userInfo, "The UserInfo must be not null");
    return favoriteRepository.findLatestUniqueComments(userInfo.getOriginalUserId(),
        StringUtils.trimToEmpty(text), PageUtils.defaultPageable(TOP_LATEST_COMMENTS));
  }

}
