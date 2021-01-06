package com.bank.application.ui.views.user.credit;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Credit;
import com.bank.application.backend.entity.Submission;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.service.CreditService;
import com.bank.application.backend.service.SubmissionService;
import com.bank.application.backend.service.UserService;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Route(value = "credits", layout = MainView.class)
@PageTitle("Credits | BankAP")
@CssImport("./styles/views/credits/credits-view.css")
public class CreditView extends Div {

    private User activeUser;
    private final CreditService creditService;
    private final UserService userService;
    private final SubmissionService submissionService;


    public CreditView(CreditService creditService, UserService userService, SubmissionService submissionService) {
        this.submissionService = submissionService;
        this.userService = userService;
        this.creditService = creditService;

        setId("credit-view");

        fetchActiveUser();

        createGrid();
    }


    public void createGrid() {
        GridCrud<Credit> crud = new GridCrud<>(Credit.class);

        crud.getGrid().setColumns("amount", "numberOfInstallments", "begin", "end", "submissionApproved");

        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD, "amount", "numberOfInstallments",
                "begin", "end");

        crud.setFindAllOperation(() -> creditService.findAllByAccount(activeUser.getAccount()));

        crud.setAddOperation(creditForm -> {
            Credit credit = new Credit();
            Account account = activeUser.getAccount();
            Submission submission = new Submission();
            submission.setAccount(account);

            credit.setAmount(creditForm.getAmount());
            credit.setNumberOfInstallments(creditForm.getNumberOfInstallments());
            credit.setBegin(creditForm.getBegin());
            credit.setEnd(creditForm.getEnd());
            credit.setAccount(account);
            credit.setSubmission(submission);

            if (credit.getBegin() == null || credit.getEnd() == null) {
                Notification.show("Begin and end date cannot be empty!");
                return null;
            }

            Pattern compiledPattern = Pattern.compile("^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$");
            Matcher matcherBegin = compiledPattern.matcher(credit.getBegin());
            Matcher matcherEnd = compiledPattern.matcher(credit.getEnd());

            if(! matcherBegin.matches() || ! matcherEnd.matches()) {
                Notification.show("Incorrect date format! Enter date like yyyy-mm-dd");
                return null;
            }


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            try {
                Date beginDate = simpleDateFormat.parse(credit.getBegin());
                Date endDate = simpleDateFormat.parse(credit.getEnd());

                if(computeDiff(beginDate, endDate).get(TimeUnit.DAYS) < credit.getNumberOfInstallments() * 30) {
                    Notification.show("The time interval between start date and end date must be at least equal " +
                            "(number of installments> = number of months)");

                    System.out.println(credit.getBegin() + " " + credit.getEnd());
                    System.out.println(computeDiff(beginDate, endDate).get(TimeUnit.DAYS) + " " + credit.getNumberOfInstallments() * 30);
                    return null;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            submissionService.save(submission);
            creditService.save(credit);

            Notification.show("Pomyślnie utworzono kredyt!", 3000, Notification.Position.MIDDLE);
            return credit;
        });

        crud.setShowNotifications(false);

        crud.setFindAllOperationVisible(false);
        crud.setUpdateOperationVisible(false);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.DELETE, "amount", "numberOfInstallments",
                "begin", "end");
        crud.setDeleteOperation(creditService::delete);

        setSizeFull();
        add(crud);
    }

    public static Map<TimeUnit, Long> computeDiff(Date date1, Date date2) {

        long diffInMillies = date2.getTime() - date1.getTime();

        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        // create the result map of TimeUnit and difference
        Map<TimeUnit, Long> result = new LinkedHashMap<TimeUnit, Long>();
        long milliesRest = diffInMillies;

        for (TimeUnit unit : units) {

            // calculate difference in millisecond
            long diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;

            // put the result in the map
            result.put(unit, diff);
        }

        return result;
    }

    private void fetchActiveUser() {
        try {
            fetchUserById();
        } catch (com.bank.application.ui.views.user.cards.CardsView.UserNotFoundException e) {
            System.out.println("User has not been found!");
        }
    }

    private void fetchUserById() throws com.bank.application.ui.views.user.cards.CardsView.UserNotFoundException {
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute(Constants.USER_ID);
        Optional<User> fetchedUpdatedUser = userService.findUserById(userId);
        if (fetchedUpdatedUser.isPresent()) {
            activeUser = fetchedUpdatedUser.get();
        } else {
            throw new com.bank.application.ui.views.user.cards.CardsView.UserNotFoundException();
        }
    }

    public static class UserNotFoundException extends Exception {

    }
}
