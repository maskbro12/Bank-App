//package com.ofss.controller;
//steps to create UI
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller // NOT @RestController! This is important.
//public class ViewController {
//    
//    @GetMapping("/accounts-page") // URL you want to map
//    public String accountsPage(Model model) {
//        // Add any objects you want Thymeleaf to use in your HTML
//        // Example: model.addAttribute("accounts", yourAccountList);
//
//        return "accounts"; // WITHOUT the .html extension
//    }
//}
package com.ofss.controller;

import com.ofss.model.Account;
import com.ofss.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts-page")
    public String accountsPage(Model model) {
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);  // send to UI
        return "accounts";
    }
}