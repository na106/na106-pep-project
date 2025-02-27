package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account register(Account account){
        if(account.getUsername().isEmpty() || account.getPassword().length() < 4){
            return null;
        }
        Account acc = accountDAO.getAccByUsername(account.getUsername());
        if(acc == null){
            return this.accountDAO.register(account);
        }
        return null;
    }

    public Account AccLogin(Account account){
        Account a = accountDAO.loginAccount(account.getUsername(), account.getPassword());
        if (a != null){
            return a;
        } else{
            return null;
        }
    }

}
