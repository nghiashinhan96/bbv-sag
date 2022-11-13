############## Insert missing payment method ##############

##### PAYMENT_METHOD 
INSERT INTO dbo.PAYMENT_METHOD (PAY_METHOD,DESC_CODE,DESCRIPTION,ORDER_DISPLAY) VALUES 
('Kartenzahlung','CARD','Kartenzahlung',4)
,('Sofortrechnung','DIRECT_INVOICE','Sofortrechnung',3)
;

UPDATE dbo.PAYMENT_METHOD SET ORDER_DISPLAY = 1 WHERE DESC_CODE = 'CREDIT';
UPDATE dbo.PAYMENT_METHOD SET ORDER_DISPLAY = 2 WHERE DESC_CODE = 'CASH';

##### PAYMENT_METHOD 
INSERT INTO dbo.PAYMENT_TYPE (CODE,DESCRIPTION) VALUES 
('Bar','Barzahlung, petty cash')
,('Betreibung','Betreibung, operation')
,('Fin','Finanzierung, financing')
,('Karte','Kartenzahlung, card payment')
,('Keine','Keine, none')
,('Sak','Schuldanerkennung, acknowledgment of debt')
,('Selbstzahl','Selbstzahler, self-payer')
,('Sepa B Fin','Sepa B Finanzierung, financing')
,('Sepa B Sak','Sepa B Schuldanerkennung, acknowledgment of debt')
,('Sepa B2B','Sepa B2B')
;
INSERT INTO dbo.PAYMENT_TYPE (CODE,DESCRIPTION) VALUES 
('Sepa C Fin','Sepa C Finanzierung, financing')
,('Sepa C Sak','Sepa C Schuldanerkennung, acknowledgment of debt')
,('Sepa Core','Sepa Core (inkl. Cor1)')
,('Sofort','Sofort fällige Rechnung, immediate due invoice')
,('Bar','Barzahlung, petty cash')
,('Betreibung','Betreibung, operation')
,('Fin','Finanzierung, financing')
,('Karte','Kartenzahlung, card payment')
,('Keine','Keine, none')
,('Sak','Schuldanerkennung, acknowledgment of debt')
;
INSERT INTO dbo.PAYMENT_TYPE (CODE,DESCRIPTION) VALUES 
('Selbstzahl','Selbstzahler, self-payer')
,('Sepa B Fin','Sepa B Finanzierung, financing')
,('Sepa B Sak','Sepa B Schuldanerkennung, acknowledgment of debt')
,('Sepa B2B','Sepa B2B')
,('Sepa C Fin','Sepa C Finanzierung, financing')
,('Sepa C Sak','Sepa C Schuldanerkennung, acknowledgment of debt')
,('Sepa Core','Sepa Core (inkl. Cor1)')
,('Sofort','Sofort fällige Rechnung, immediate due invoice')
;

##### REMOVE_LANGUAGE_NL
UPDATE dbo.ESHOP_USER
SET [LANGUAGE] = (SELECT id from dbo.LANGUAGES WHERE LANGCODE='LANG_DE' AND LANGISO='DE' AND DESCRIPTION='German')
WHERE [LANGUAGE]= (SELECT id FROM dbo.LANGUAGES WHERE LANGCODE='LANG_NL' AND LANGISO='NL' AND DESCRIPTION='Dutch')
;
DELETE FROM dbo.LANGUAGES WHERE LANGCODE='LANG_NL' AND LANGISO='NL' AND DESCRIPTION='Dutch';

##### UPDATE CURRENCY FORMAT
DECLARE @dd_ch INT = (SELECT ID FROM ORGANISATION WHERE SHORTNAME='derendinger-ch');
INSERT INTO ORGANISATION_SETTINGS(NAME,ORGANISATION_ID,SETTING_KEY,SETTING_VALUE) VALUES('Derendinger-Switzerland',@dd_ch,'setting_locale','de_CH');

DECLARE @technomag INT = (SELECT ID FROM ORGANISATION WHERE SHORTNAME='technomag');
INSERT INTO ORGANISATION_SETTINGS(NAME,ORGANISATION_ID,SETTING_KEY,SETTING_VALUE) VALUES('Technomag-Switzerland',@technomag,'setting_locale','de_CH');

DECLARE @matik_ch INT = (SELECT ID FROM ORGANISATION WHERE SHORTNAME='matik-ch');
INSERT INTO ORGANISATION_SETTINGS(NAME,ORGANISATION_ID,SETTING_KEY,SETTING_VALUE) VALUES('Matik-Switzerland',@matik_ch,'setting_locale','de_CH');

DECLARE @wbb INT = (SELECT ID FROM ORGANISATION WHERE SHORTNAME='wbb');
INSERT INTO ORGANISATION_SETTINGS(NAME,ORGANISATION_ID,SETTING_KEY,SETTING_VALUE) VALUES('Walchli-Bollier-Bulach',@wbb,'setting_locale','de_CH');

DECLARE @rbe INT = (SELECT ID FROM ORGANISATION WHERE SHORTNAME='rbe');
INSERT INTO ORGANISATION_SETTINGS(NAME,ORGANISATION_ID,SETTING_KEY,SETTING_VALUE) VALUES('Remco-Belgium',@rbe,'setting_locale','de_CH');
