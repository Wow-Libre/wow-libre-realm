package com.wow.libre.controller;

import com.wow.libre.entity.AccountEntity;
import com.wow.libre.repository.AccountRepository;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
public class RegisterController {


    private AccountRepository accountRepository;

    @Autowired
    public RegisterController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/hola5")
    public String test() throws NoSuchAlgorithmException, DecoderException {
        String username = "300868cb7c86cc35164ee5243601004baa59436a861a6292f4a4a5189e45307b";
        String password = "1741850eaf2b991b4686760c429288f358a8bfecb104ff2449e6fb9b42fe782e";


        // Convierte la cadena hexadecimal en un arreglo de bytes
        byte[] byteArray = Hex.decodeHex(username);

        byte[] byteArray2 = Hex.decodeHex(password);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(20);
        accountEntity.setSalt(byteArray);
        accountEntity.setVerifier(byteArray2);
        accountEntity.setUsername("test".toUpperCase());

        accountRepository.save(accountEntity);

        return "sebas 5";
    }


}
