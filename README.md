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

DELIMITER $$
Create TRIGGER trigger_after_insert_credit AFTER INSERT
ON credit
FOR EACH ROW
BEGIN
update account
set number_of_credits = number_of_credits + 1
WHERE id = NEW.account_id;
END$$

DELIMITER $$  
Create TRIGGER trigger_after_insert_credit_card AFTER INSERT  
ON credit_card  
FOR EACH ROW  
BEGIN  
update account  
set number_of_credit_cards = number_of_credit_cards + 1  
WHERE id = NEW.account_id;  
END$$  

DELIMITER $$  
Create TRIGGER trigger_after_delete_credit AFTER DELETE  
ON credit  
FOR EACH ROW  
BEGIN  
update account  
set number_of_credits = number_of_credits - 1  
WHERE id = OLD.account_id;  
END$$  

DELIMITER $$  
Create TRIGGER trigger_after_delete_credit_card AFTER DELETE  
ON credit_card  
FOR EACH ROW  
BEGIN  
update account  
set number_of_credit_cards = number_of_credit_cards - 1  
WHERE id = OLD.account_id;  
END$$   