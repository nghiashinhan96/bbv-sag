-- NOTE: on behalf admin transformation from offline users. real users data got email address,
-- Per the current context, Ms Phuong confirmed the correctness of this SQL
-- Phuong's and Simon's email were created for testing and will be transformed into virtual users
UPDATE eshop_user SET eshop_user.username = 'Agent-' + usr.username,
eshop_user.email = null,
eshop_user.type = 'ON_BEHALF_ADMIN'
FROM eshop_user usr
INNER JOIN GROUP_USER gu ON usr.id = gu.user_id
INNER JOIN organisation_group og ON og.group_id = gu.group_id
INNER JOIN organisation org ON org.id = og.organisation_id
WHERE usr.username = org.org_code
AND (usr.email = 'phuong.nguyen@bbv.vn' OR usr.email = 'simon.kendall@sag-ag.ch' OR usr.email = 'connect4sales@sag-ag.ch' OR usr.email is NULL);

--- ALTER ACTIVE USER PROFILE VIEW
-- for backoffice admin can not view on behalf admin in management
ALTER VIEW V_ACTIVE_USER AS
SELECT
    U.ID,
    U.USERNAME,
    U.EMAIL,
    CONCAT(U.FIRST_NAME, ' ', U.LAST_NAME) AS FULL_NAME,
    U.PHONE,
    R.NAME AS ROLE_NAME,
    G.NAME AS GROUP_NAME,
    OG.ORGANISATION_ID AS ORG_ID,
    O.PARENT_ID AS ORG_PARENT_ID,
    O.ORG_CODE AS ORG_CODE,
    OP.SHORTNAME AS AFFILIATE,
    O.NAME AS ORG_NAME
FROM
    ESHOP_USER U
    JOIN GROUP_USER GU ON U.ID = GU.USER_ID
    JOIN ESHOP_GROUP G ON GU.GROUP_ID = G.ID
    JOIN GROUP_ROLE GR ON GR.GROUP_ID = G.ID
    JOIN ESHOP_ROLE R  ON GR.ROLE_ID = R.ID
    JOIN ORGANISATION_GROUP OG ON G.ID = OG.GROUP_ID
    JOIN ORGANISATION O ON O.ID = OG.ORGANISATION_ID
    JOIN LOGIN L ON L.USER_ID = U.ID
    JOIN ORGANISATION OP ON O.PARENT_ID = OP.ID
WHERE L.IS_USER_ACTIVE = 1 AND (U.TYPE IS NULL OR (U.TYPE <> 'VIRTUAL' AND U.TYPE <> 'ON_BEHALF_ADMIN'));

--- added column user type in user detail view
ALTER VIEW V_USER_DETAIL AS
  SELECT DISTINCT
    ROW_NUMBER() OVER (ORDER BY U.ID) AS ID,
    U.ID AS USER_ID,
    U.USERNAME AS USER_NAME,
    U.EMAIL AS USER_EMAIL,
    U.FIRST_NAME AS FIRST_NAME,
    U.LAST_NAME AS LAST_NAME,
    U.PHONE AS TELEPHONE,
    US.ID AS USER_SETTING_ID,
    U.TYPE AS USER_TYPE,
    US.SALE_ON_BEHALF_OF AS SALE_ON_BEHALF_OF, -- deprecated
    R.ID AS ROLE_ID,
    R.NAME AS ROLE_NAME,
    G.ID AS GROUP_ID,
    G.NAME AS GROUP_NAME,
    OG.ORGANISATION_ID AS ORG_ID,
    O.PARENT_ID AS ORG_PARENT_ID,
    O.ORG_CODE AS ORG_CODE,
    O.SHORTNAME AS ORG_SHORT_NAME,
    O.NAME AS ORG_NAME,
    O.ORDER_SETTINGS_ID AS ORG_SETTINGS_ID,
    SL.CODE AS SALUT_CODE,
    L.FIRST_LOGIN_DATE AS FIRST_LOGIN_DATE,
    U.SIGN_IN_DATE AS LAST_LOGIN_DATE
  from
  	ESHOP_USER U
    JOIN GROUP_USER GU ON U.ID = GU.USER_ID
    JOIN ESHOP_GROUP G ON GU.GROUP_ID = G.ID
    JOIN GROUP_ROLE GR ON GR.GROUP_ID = G.ID
    JOIN ESHOP_ROLE R  ON GR.ROLE_ID = R.ID
    JOIN ORGANISATION_GROUP OG ON G.ID = OG.GROUP_ID
    JOIN ORGANISATION O ON O.ID = OG.ORGANISATION_ID
    JOIN LOGIN L ON L.USER_ID = U.ID
    JOIN USER_SETTINGS US ON US.ID = U.SETTING
    JOIN SALUTATION SL ON SL.ID = U.SALUTATION
  WHERE L.IS_USER_ACTIVE = 1;
