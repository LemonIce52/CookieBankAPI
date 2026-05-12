package com.example.cookieBank.controller;

import com.example.cookieBank.dto.account.AccountDTO;
import com.example.cookieBank.dto.account.UpdatePinDTO;
import com.example.cookieBank.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@Validated
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccount() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(
            @PathVariable("id") @PositiveOrZero(message = "id can't must be less zero!") Long id
    ) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @PutMapping("/updatePin")
    public void updatePin(
            @Valid @RequestBody UpdatePinDTO updatePin
    ) {
        accountService.updatePin(updatePin);
    }
}
