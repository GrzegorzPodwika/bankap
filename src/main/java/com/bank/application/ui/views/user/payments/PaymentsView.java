package com.bank.application.ui.views.user.payments;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Transaction;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.service.AccountService;
import com.bank.application.backend.service.TransactionService;
import com.bank.application.backend.service.UserService;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.home.HomeView.UserNotFoundException;
import com.bank.application.ui.views.main.MainView;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Route(value = "payments", layout = MainView.class)
@PageTitle("Payments")
public class PaymentsView extends VerticalLayout {

    private User activeUser;
    private final AccountService accountService;
    private final UserService userService;
    private final TransactionService transactionService;
    private List<Transaction> allTransactionsByUser;


    public PaymentsView(TransactionService transactionService, AccountService accountService, UserService userService) {
        setId("payments-view");
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;

        fetchActiveUser();

        GridCrud<Transaction> crud = new GridCrud<>(Transaction.class);

        crud.getGrid().setColumns("transactionTitle", "amount", "receiverAccountNumber", "date");
        crud.getGrid().setColumnReorderingAllowed(true);
        crud.getCrudFormFactory().setUseBeanValidation(true);

        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD,
                "transactionTitle", "amount", "receiverAccountNumber");


        allTransactionsByUser = transactionService.findAllByAccount(activeUser.getAccount());
        crud.setFindAllOperation(() -> transactionService.findAllByAccount(activeUser.getAccount()));

        crud.setAddOperation(transaction -> {
            if (hasUserEnoughMoney(transaction) && isTransactionCorrect(transaction)) {
                Optional<Account> searchReceiver = accountService.existAccountByAccountNumber(transaction.getReceiverAccountNumber());

                if (searchReceiver.isPresent()) {
                    Account senderAccount = activeUser.getAccount();
                    Account receiverAccount = searchReceiver.get();

                    double transferAmountPositive = transaction.getAmount();
                    if (transaction.getAmount() < 0.0) {
                        transferAmountPositive *= -1.0;
                    }

                    transaction.setAmount(transferAmountPositive * -1.0);
                    transaction.setAccount(senderAccount);

                    Transaction receiverTransaction = new Transaction();
                    receiverTransaction.setTransactionTitle(transaction.getTransactionTitle());
                    receiverTransaction.setAmount(transferAmountPositive);
                    receiverTransaction.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
                    receiverTransaction.setAccount(receiverAccount);

                    updateBalanceInSenderAndReceiverAccounts(senderAccount, receiverAccount, transferAmountPositive);
                    transactionService.add(transaction, receiverTransaction);

                    fetchTransactions();

                    Notification.show("Pomyślnie wysłano przelew!", 3000, Notification.Position.MIDDLE);
                    return transaction;
                } else {
                    Notification.show("Nie znaleziono klienta o podanym numerze konta!");
                    return null;
                }
            } else {
                Notification.show("Nie masz wystarczająco pieniędzy na koncie!");
                return null;
            }
        });

        crud.getGrid().getColumns().forEach(col -> col.setAutoWidth(true));

        crud.setFindAllOperationVisible(false);
        crud.setUpdateOperationVisible(false);
        crud.setDeleteOperationVisible(false);
        crud.setDeleteOperation(transactionService::delete);

        setSizeFull();

        Button buttonGenerateStatement = new Button("Generuj wyciąg");
        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
                new StreamResource("statement.pdf", () -> {
                    try {
                        generateAndExposeStatement();
                    } catch (FileNotFoundException | DocumentException e) {
                        e.printStackTrace();
                    }

                    try {
                        return new FileInputStream("statement.pdf");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }));
        buttonWrapper.wrapComponent(buttonGenerateStatement);
        add(buttonWrapper);


        add(crud);
    }


    private void fetchActiveUser() {
        try {
            fetchUserById();
        } catch (UserNotFoundException e) {
            System.out.println("User has not been found!");
        }
    }

    private void fetchUserById() throws UserNotFoundException {
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute(Constants.USER_ID);
        Optional<User> fetchedUpdatedUser = userService.findUserById(userId);
        if (fetchedUpdatedUser.isPresent()) {
            activeUser = fetchedUpdatedUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    private void fetchTransactions() {
        allTransactionsByUser = transactionService.findAllByAccount(activeUser.getAccount());
    }

    private void generateAndExposeStatement() throws FileNotFoundException, DocumentException {
        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream("statement.pdf"));
        document.open();

/*        Font font = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);
        Chunk chunk = new Chunk("Hello World", font);
        document.add(chunk);*/

        PdfPTable pdfPTable = new PdfPTable(5);
        pdfPTable.setWidthPercentage(100f);
        addTableHeader(pdfPTable);
        addRows(pdfPTable);

        document.add(pdfPTable);
        document.close();
    }

    private void addTableHeader(PdfPTable pdfPTable) {
        Stream.of("Konto", "Konto odbiorcy", "Tytuł przelewu", "Kwota", "Data")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2f);
                    header.setPhrase(new Phrase(columnTitle));
                    pdfPTable.addCell(header);
                });
    }

    private void addRows(PdfPTable pdfPTable) {
        for (Transaction trans: allTransactionsByUser) {
            pdfPTable.addCell(trans.getAccount().getAccountNumber());
            pdfPTable.addCell(trans.getReceiverAccountNumber());
            pdfPTable.addCell(trans.getTransactionTitle());
            pdfPTable.addCell(String.valueOf(trans.getAmount()));
            pdfPTable.addCell(trans.getDate());
        }
    }

    private boolean hasUserEnoughMoney(Transaction transaction) {
        return Double.parseDouble(activeUser.getAccount().getAccountBalance()) >= transaction.getAmount();
    }

    private boolean isTransactionCorrect(Transaction transaction) {
        return transaction.getTransactionTitle() != null && !transaction.getTransactionTitle().isEmpty() && transaction.getAmount() != 0.0;
    }

    private void updateBalanceInSenderAndReceiverAccounts(Account senderAccount, Account receiverAccount, double transferAmount) {

        double newBalanceSender = Double.parseDouble(senderAccount.getAccountBalance()) - transferAmount;
        newBalanceSender = Math.round(newBalanceSender * 100) / 100.0;
        senderAccount.setAccountBalance(Double.toString(newBalanceSender));

        double newBalanceReceiver = Double.parseDouble(receiverAccount.getAccountBalance()) + transferAmount;
        newBalanceReceiver = Math.round(newBalanceReceiver * 100) / 100.0;
        receiverAccount.setAccountBalance(Double.toString(newBalanceReceiver));

        accountService.update(senderAccount);
        accountService.update(receiverAccount);
    }


}
