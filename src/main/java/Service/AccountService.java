package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDao;

    public AccountService() {
        accountDao = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDao = accountDAO;
    }

    public Account addAccount(Account account) {
        return accountDao.createAccount(account);
    }

    public boolean loginAttempt(Account account) {
        return accountDao.login(account);
    }
    
}
