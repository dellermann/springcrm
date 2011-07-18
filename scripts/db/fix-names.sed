/^[[:space:]]*<?xml version=/d
/^[[:space:]]*<\/\?pma_xml_export/d
/^[[:space:]]*<\/\?database/d
s/vtiger_account/organization/
s/vtiger_contactdetails/person/
s/vtiger_service/service/
s/vtiger_products/product/
s/vtiger_activity/phone_call/
s/vtiger_quotes/invoicing_transaction/
s/vtiger_salesorder/invoicing_transaction/
s/vtiger_invoice/invoicing_transaction/
s/vtiger_inventoryproductrel/invoicing_item/
