-- Init data for dms offer permission
insert into ESHOP_FUNCTION (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES('DmsToOffer', 'Dms To Offer', '/dms/cart/offer**');
insert into PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES(17, 38);
