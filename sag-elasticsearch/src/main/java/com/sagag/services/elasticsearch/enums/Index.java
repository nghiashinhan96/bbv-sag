package com.sagag.services.elasticsearch.enums;

import com.sagag.services.elasticsearch.utils.ElasticsearchConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Fields declaration interface for Elasticsearch query.
 */
public interface Index {

  /**
   * Path enumeration for indexes.
   */
  @Getter
  @AllArgsConstructor
  enum Path {
    //@formatter:off
    CUST_CONTACTS("contacts"),
    CUST_ADDRESSES("addresses"),
    REFS("refs"),
    ID("id"),
    CODE("code"),
    CRITERIA("criteria"),
    INFO("info"),
    PARTS("parts"),
    PARTS_EXT("parts_ext");
    //@formatter:on
    private final String value;
  }

  /**
   * Vehicle document fields path declaration for search query.
   */
  @AllArgsConstructor
  enum Vehicle implements IAttributePath {
    //@formatter:off
    ID(Path.ID.value, Path.ID.value),
    ID_MAKE(Path.REFS.value, "id_make"),
    ID_MODEL(Path.REFS.value, "id_model"),
    VEHICLE_CLASS(Path.REFS.value, "vehicle_class"),
    VEHICLE_SUB_CLASS(Path.REFS.value, "vehicle_sub_class"),
    VEHICLE_NAME(Path.REFS.value, "vehicle_name"),
    VEHICLE_BUILT_YEAR_FROM(Path.REFS.value, "vehicle_built_year_from"),
    VEHICLE_BUILT_YEAR_TIL(Path.REFS.value, "vehicle_built_year_till"),
    VEHICLE_ENGINE_CODE(Path.REFS.value, "vehicle_engine_code"),
    VEHICLE_ENGINE_CODE_RAW(Path.REFS.value, "vehicle_engine_code.raw"),
    VEHICLE_ENGINE_CODE_FULL(Path.REFS.value, "vehicle_engine_code_full"),
    VEHICLE_FUEL_TYPE(Path.REFS.value, "vehicle_fuel_type"),
    VEHICLE_FUEL_TYPE_RAW(Path.REFS.value, "vehicle_fuel_type.raw"),
    VEHICLE_ZYLINDER(Path.REFS.value, "vehicle_zylinder"),
    VEHICLE_ZYLINDER_SORT(Path.REFS.value, "vehicle_zylinder_sort"),
    VEHICLE_ENGINE(Path.REFS.value, "vehicle_engine"),
    VEHICLE_POWER_KW(Path.REFS.value, "vehicle_power_kw"),
    VEHICLE_POWER_KW_SORT(Path.REFS.value, "vehicle_power_kw_sort"),
    VEHICLE_POWER_HP(Path.REFS.value, "vehicle_power_hp"),
    VEHICLE_BODY_TYPE(Path.REFS.value, "vehicle_body_type"),
    VEHICLE_BODY_TYPE_RAW(Path.REFS.value, "vehicle_body_type.raw"),
    VEHICLE_DRIVE_TYPE(Path.REFS.value, "vehicle_drive_type"),
    VEHICLE_DRIVE_TYPE_RAW(Path.REFS.value, "vehicle_drive_type.raw"),
    VEHICLE_BRAND(Path.REFS.value, "vehicle_brand"),
    VEHICLE_BRAND_RAW(Path.REFS.value, "vehicle_brand.raw"),
    VEHICLE_MODEL(Path.REFS.value, "vehicle_model"),
    VEHICLE_MODEL_RAW(Path.REFS.value, "vehicle_model.raw"),
    CODE(Path.CODE.value, Path.CODE.value),
    VEHICLE_CAPACITY_CC_TECH(Path.REFS.value, "vehicle_capacity_cc_tech"),
    GT_UMC("gt_links", "gt_umc"),
    GT_MOD(GT_UMC.path, "gt_mod"),
    GT_ENG(GT_UMC.path, "gt_eng"),
    GT_DRV(GT_UMC.path, "gt_drv"),
    GT_MOD_ALT(GT_UMC.path, "gt_mod_alt"),
    VEH_CODE("codes", "codes"),
    VEH_CODE_VALUE(VEH_CODE.path, "veh_code_value"),
    VEH_CODE_TYPE(VEH_CODE.path, "veh_code_type"),
    VEH_CODE_ATTR(VEH_CODE.path, "veh_code_attr"),
    ID_VEH_CODE(VEH_CODE_VALUE.path, "id_veh_code"),
    VEH_ID(StringUtils.EMPTY, ElasticsearchConstants.VEH_ID_ATTR),
    KTYPE(StringUtils.EMPTY, "ktype"),
    SEARCH_BUILT_YEAR_FROM(Path.REFS.value, "search_built_year_from"),
    SEARCH_BUILT_YEAR_TILL(Path.REFS.value, "search_built_year_till"),
    VEHICLE_MODEL_YEAR(Path.REFS.value, "vehicle_model_year"),
    VEHICLE_FULL_NAME(StringUtils.EMPTY, "vehicle_full_name"),
    VEHICLE_FULL_NAME_RAW(StringUtils.EMPTY, "vehicle_full_name.raw"),
    VEHICLE_ADVANCE_NAME_RAW(StringUtils.EMPTY, "vehicle_name.raw"),
    NAME_ALT(StringUtils.EMPTY, "name_alt"),
    MODEL(StringUtils.EMPTY, "model");
    //@formatter:on

    private final String path;
    private final String field;

    @Override
    public String field() {
      return this.field;
    }

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }

    @Override
    public boolean isNested() {
      return !Path.REFS.value.equals(this.path) && IAttributePath.super.isNested();
    }
  }

  /**
   * Enumeration to define the customer field.
   */
  @AllArgsConstructor
  enum Unitree implements IAttributePath {
    //@formatter:off
    NODES(StringUtils.EMPTY, "nodes"),
    NODE_NAME(NODES.field, "node_name"),
    LEAF_ID(NODES.field, "nodes.leaf_id");
    //@formatter:on

    private final String path;
    private final String field;

    @Override
    public String field() {
      return this.field;
    }

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }
  }

  /**
   * Enumeration to define the customer field.
   */
  @AllArgsConstructor
  enum Customer implements IAttributePath {
    //@formatter:off
    CUSTOMER_ACCOUNT_NR(StringUtils.EMPTY, "customer_account_nr"),
    PRIMARY_PHONE_CC(StringUtils.EMPTY, "primary_phone_cc"),
    PRIMARY_PHONE(StringUtils.EMPTY, "primary_phone"),
    NAME(StringUtils.EMPTY, "customer_name"),
    ALIAS(StringUtils.EMPTY, "name_alias"),
    PEMAIL(StringUtils.EMPTY, "primary_email"),
    PFAX(StringUtils.EMPTY, "primary_fax"),
    TAX_NR(StringUtils.EMPTY, "tax_nr"),
    TAX_EX_CODE(StringUtils.EMPTY, "tax_exempt_code"),
    OFFICAL_REG_CODE(StringUtils.EMPTY, "offical_registration_code"),
    MANDANT(StringUtils.EMPTY, "mandant"),
    MANDANT_CODE(StringUtils.EMPTY, "mandant_code"),
    CONTACT_FNAME(Path.CUST_CONTACTS.value, "first_name"),
    CONTACT_LNAME(Path.CUST_CONTACTS.value, "last_name"),
    ADDR_ID_LOCATION(Path.CUST_ADDRESSES.value, "id_location"),
    ADDR_BUILDING_COMP(Path.CUST_ADDRESSES.value, "building_comp"),
    ADDR_DESC(Path.CUST_ADDRESSES.value, "address_desc"),
    ADDR_STREET(Path.CUST_ADDRESSES.value, "address_street"),
    ADDR_CITY(Path.CUST_ADDRESSES.value, "city"),
    ADDR_ZIP(Path.CUST_ADDRESSES.value, "zip"),
    ADDR_IS_PRIMARY(Path.CUST_ADDRESSES.value, "is_primary");
    //@formatter:on

    private final String path;
    private final String field;

    @Override
    public String field() {
      return this.field;
    }

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }

  }

  /**
   * Branch enumeration for indexes.
   */
  @Getter
  @AllArgsConstructor
  enum Branch {
    //@formatter:off
    ID("id"),
    ZIP("zip"),
    ADDRESS_DESC("address_desc"),
    ADDRESS_COUNTRY("address_country"),
    ADDRESS_STREET("address_street"),
    REGION_ID("region_id"),
    PRIMARY_EMAIL("primary_email"),
    BRANCH_NR("branch_nr"),
    PRIMARY_URL("primary_url"),
    PRIMARY_FAX("primary_fax"),
    ADDRESS_CITY("address_city"),
    PRIMARY_PHONE("primary_phone"),
    ORG_ID("org_id");
    //@formatter:on

    private final String value;

  }

  /**
   * Enumeration to define the customer field.
   */
  @AllArgsConstructor
  enum Article implements IAttributePath {

    //@formatter:off
    ARTID(Path.REFS.value, "artid"),
    NAME(Path.REFS.value, "name"),
    ID_PIM(Path.REFS.value, "id_pim"),
    ID_AUTONET(Path.REFS.value, "id_autonet"),
    ARTNR(Path.REFS.value, "artnr"),
    ID_DLNR(Path.REFS.value, "id_dlnr"),
    SUPPLIER(Path.REFS.value, "supplier"),
    PRODUCT_ADDON(Path.REFS.value, "product_addon"),
    PRODUCT_BRAND(Path.REFS.value, "product_brand"),
    CN(Path.CRITERIA.value, "cn"),
    CVP(Path.CRITERIA.value, "cvp"),
    INFO_TXT(Path.INFO.value, "info_txt"),
    PNRN(Path.PARTS.value, "pnrn"),
    PARTS_EX_PNRN(Path.PARTS_EXT.value, "pnrn"),
    PRODUCT_INFO_TEXT(Path.REFS.value, "product_info_text"),
    PRODUCT_VALUES(Path.REFS.value, "product_values"),
    ARTICLES_NN(Path.REFS.value, "artilces_nn"),
    GOLDEN_RECORD_ID(Path.REFS.value, "id_gr"),
    PARTS_TYPE(Path.PARTS.value, "ptype"),
    PARTS_BRAND_ID(Path.PARTS.value, "brandid");
    //@formatter:on

    private final String path;
    private final String field;

    @Override
    public String field() {
      return this.field;
    }

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }
  }

  /**
   * Enumeration to define the fitment field.
   */
  @AllArgsConstructor
  enum Fitment implements IAttributePath {
    ID("id", "_id"),
    VEHID(ElasticsearchConstants.VEH_ID_ATTR, ElasticsearchConstants.VEH_ID_ATTR),
    GENART("genart", "genart"),
    ARTICLE_ID("articles", ElasticsearchConstants.REF_ARTICLE_ID_IN_FITMENT),
    ID_AUTONET("articles", "articles.id_autonet");

    private final String code;
    private final String value;

    @Override
    public String field() {
      return this.value;
    }

    @Override
    public String path() {
      return this.code;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }
  }

  /**
   * Enumeration to define the format ga field.
   */
  @AllArgsConstructor
  enum FormatGa implements IAttributePath {
    ID("id", "id"),
    GEN_ARTS("genarts", "genarts"),
    GAID("gaid", "genarts.gaid"),
    ELEMENTS("elements", "elements"),
    CID("cid", "elements.cid");

    private final String code;
    private final String value;

    @Override
    public String field() {
      return this.value;
    }

    @Override
    public String path() {
      return this.code;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }
  }

  /**
   * Enumeration to define the article vehicles field.
   */
  @AllArgsConstructor
  enum ArticleVehicles implements IAttributePath {
    ID("id", "_id"),
    ARTICLE_ARTID("article", "article.artid"),
    VEHICLES_SORT("vehicles", "vehicles.sort");

    private final String path;
    private final String value;

    @Override
    public String field() {
      return this.value;
    }

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }
  }


  /**
   * Enumeration to define the UnitreeDoc field.
   */
  @AllArgsConstructor
  enum UnitreeDoc implements IAttributePath {
    TREE_ID("tree_id", "id"),
    TREE_SORT("tree_sort", "treeSort"),
    TREE_IMAGE("tree_image", "treeImage"),
    TREE_NAME("tree_name", "treeName"),
    TREE_EXTERNAL_SERVICE("tree_external_service", "treeExternalService"),
    EXTERNAL_SERVICE_ATTRIBUTE("tree_external_service_attribute", "treeExternalServiceAttribute");

    private final String path;
    private final String value;

    @Override
    public String field() {
      return this.value;
    }

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }
  }

  @AllArgsConstructor
  public enum Category implements IAttributePath {
    //@formatter:off
    LEAFTXT(Path.REFS.value, "leaftxt"),
    NODE_KEYWORDS(Path.REFS.value, "node_keywords");
    //@formatter:on

    private final String path;
    private final String value;

    @Override
    public String field() {
      return this.value;
    }

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }
  }

  @AllArgsConstructor
  public enum WssArticleGroup implements IAttributePath {
    //@formatter:off
    ID("id", "_id"),
    ARTICLE_GROUP_ARTGRP("artgrp_tree", "artgrp.keyword"),
    ARTICLE_GROUP_ARTGRP_DESC("designations", "text"),
    ARTICLE_GROUP_LEAFID("artgrp_tree", "leafid.keyword"),
    ARTICLE_GROUP_PARENTID("artgrp_tree", "parentid.keyword");
    //@formatter:on

    private final String path;
    private final String value;

    @Override
    public String field() {
      return this.value;
    }

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }
  }

  @AllArgsConstructor
  public enum ExternalParts implements IAttributePath {
    PRNR(Path.REFS.value, "prnr"),
    ID_PIM(Path.REFS.value, "id_pim");

    private final String path;
    private final String field;

    @Override
    public String field() {
      return this.field;
    }

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String aggTerms() {
      return this.name();
    }
  }
}

