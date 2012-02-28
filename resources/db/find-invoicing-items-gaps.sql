--
-- find-invoicing-items-gaps.sql
--
-- Script to find gaps in the list of invoicing items, that is, invoicing
-- items with items_idx columns numbered for example 0, 1, 2, 4, 5.  Here
-- index 3 is missing.
SELECT * FROM invoicing_item AS ii1 
  WHERE ii1.items_idx > 0 
    AND (
	    SELECT COUNT(*) FROM invoicing_item AS ii2
	      WHERE ii2.invoicing_transaction_id = ii1.invoicing_transaction_id 
	        AND ii2.items_idx = ii1.items_idx - 1
    ) = 0;
