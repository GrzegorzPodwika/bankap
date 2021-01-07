# BankAP
 Simple web bank application for studying purposes.

# Triggers

//SET GLOBAL log_bin_trust_function_creators = 1; with admin privileges firstly

DELIMITER $$  
Create TRIGGER trigger_after_insert_transaction AFTER INSERT  
ON transaction  
FOR EACH ROW  
BEGIN  
update account  
set account_balance = account_balance + NEW.amount  
WHERE id = NEW.account_id;  
END$$

DELIMITER ;

DELIMITER $$  
Create TRIGGER trigger_after_delete_transaction AFTER DELETE  
ON transaction  
FOR EACH ROW  
BEGIN  
update account  
set account_balance = account_balance - OLD.amount  
WHERE id = OLD.account_id;  
END$$   