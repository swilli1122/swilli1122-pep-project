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
        if (account.getUsername() != "" && account.getUsername() != null) {
            if (account.getPassword().length() >= 4) {
                return accountDao.createAccount(account);
            }
        } 
        return null;
    }

    public Account loginAttempt(Account account) {
        if (account.getUsername() != null && account.getPassword() != null) {
            return accountDao.login(account);
        } 
        return null;        
    }
    
}
