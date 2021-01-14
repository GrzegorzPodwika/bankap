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

# Procedures
#### Credit repayment
```sql
DELIMITER $$
CREATE PROCEDURE make_installments()
BEGIN
  DECLARE done BOOLEAN DEFAULT FALSE;
  DECLARE account_id_var BIGINT UNSIGNED;
  DECLARE credit_id_var BIGINT UNSIGNED;
  DECLARE cur CURSOR FOR SELECT credit.id, credit.account_id FROM credit JOIN submission ON credit.submission_id = submission.id WHERE submission.is_approved = TRUE;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done := TRUE;

  OPEN cur;

  testLoop: LOOP
    FETCH cur INTO credit_id_var, account_id_var;
    IF done THEN
      LEAVE testLoop;
    END IF;
	SET @max_transaction_id = (SELECT MAX(id) FROM transaction);
	SET @payment = (SELECT monthly_installment FROM credit WHERE credit.id = credit_id_var );
	SET @remaining_installments_var = (SELECT remaining_installments FROM credit WHERE credit.id = credit_id_var);
	SET @current_date_var = (SELECT DATE_FORMAT(NOW(), '%Y-%m-%d'));
	SET @timestamp_var = (SELECT CAST(UNIX_TIMESTAMP(NOW(3)) * 1000 AS unsigned));
	
	IF @remaining_installments_var > 0 THEN
		UPDATE credit SET remaining_installments = remaining_installments - 1 WHERE credit.id = credit_id_var;
		INSERT INTO transaction VALUES(@max_transaction_id + 1, -1.0 * @payment, @current_date_var, "00000000000000000000000000", @timestamp_var, 'Spłata kredytu', account_id_var);
	END IF;
 
  END LOOP testLoop;

  CLOSE cur;
END $$
DELIMITER ;
```

#### Fetch used funds from credit card
```sql
DELIMITER $$
CREATE PROCEDURE fetch_used_funds_from_credit_card()
BEGIN
  DECLARE done BOOLEAN DEFAULT FALSE;
  DECLARE account_id_var BIGINT UNSIGNED;
  DECLARE credit_card_id_var BIGINT UNSIGNED;
  DECLARE cur CURSOR FOR SELECT credit_card.id, credit_card.account_id FROM credit_card JOIN submission ON credit_card.submission_id = submission.id WHERE submission.is_approved = TRUE
									AND DATE(NOW()) = credit_card.return_date;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done := TRUE;

  OPEN cur;

  testLoop: LOOP
    FETCH cur INTO credit_card_id_var, account_id_var;
    IF done THEN
      LEAVE testLoop;
    END IF;
	SET @max_transaction_id = (SELECT MAX(id) FROM transaction);
	SET @payment = (SELECT used_funds FROM credit_card WHERE credit_card.id = credit_card_id_var );
	SET @current_date_var = (SELECT DATE_FORMAT(NOW(), '%Y-%m-%d'));
	SET @timestamp_var = (SELECT CAST(UNIX_TIMESTAMP(NOW(3)) * 1000 AS unsigned));
	
	UPDATE credit_card SET used_funds = 0, return_date = ADDDATE(return_date, 30) WHERE credit_card.id = credit_card_id_var;
	INSERT INTO transaction VALUES(@max_transaction_id + 1, -1.0 * @payment, @current_date_var, "00000000000000000000000000", @timestamp_var, 'Pobranie środków z karty', account_id_var);
 
  END LOOP testLoop;

  CLOSE cur;
END $$
DELIMITER ;
```

# Events
### Remember to turn on events!
```sql
SET GLOBAL event_scheduler = ON;
```

#### Credit repayment event
- ON SCHEDULE EVERY 1 DAY
- ON SCHEDULE EVERY 1 MONTH
```sql
DELIMITER $$
CREATE EVENT trigger_installments_event
ON SCHEDULE EVERY 1 MINUTE
DO 
BEGIN
	CALL make_installments();
END $$

DELIMITER ;
```

#### Fetching user used funds from credit card event
```sql
DELIMITER $$
CREATE EVENT trigger_fetch_used_funds_event
ON SCHEDULE EVERY 1 DAY
DO 
BEGIN
	CALL fetch_used_funds_from_credit_card();
END $$

DELIMITER ;	
```