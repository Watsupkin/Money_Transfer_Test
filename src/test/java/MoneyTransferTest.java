import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.DashboardPage;
import page.LoginPage;
import data.DataHelper.*;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MoneyTransferTest {


    @BeforeEach
    public void openPage() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferMoneyFromFirstCardToSecondCard() {
        val dashboardPage = new DashboardPage();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = 2_761;

        val transactionPage = dashboardPage.pushSecondCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        val firstCardBalanceResult = firstCardBalanceStart - amount;
        val secondCardBalanceResult = secondCardBalanceStart + amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldTransferMoneyFromSecondCardToFirstCard() {
        val dashboardPage = new DashboardPage();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = 928;

        val transactionPage = dashboardPage.pushFirstCardButton();
        transactionPage.transferMoney(amount, getSecondCardNumber());
        val firstCardBalanceResult = firstCardBalanceStart + amount;
        val secondCardBalanceResult = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldNotTransferMoneyIfAmountExceedsCardBalance() {
        val dashboardPage = new DashboardPage();
        int amount = 28_500;
        val transactionPage = dashboardPage.pushSecondCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        transactionPage.getErrorLimit();
    }

    @Test
    public void shouldBeGetErrorIfSameCard() {
        val dashboardPage = new DashboardPage();
        int amount = 777;
        val transactionPage = dashboardPage.pushFirstCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        transactionPage.getErrorInvalidCard();
    }
}
