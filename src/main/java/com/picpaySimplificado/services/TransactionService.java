package com.picpaySimplificado.services;

import com.picpaySimplificado.domain.transaction.Transaction;
import com.picpaySimplificado.domain.user.User;
import com.picpaySimplificado.dtos.TransactionDTO;
import com.picpaySimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserServices userServices;
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RestTemplate restTemplate;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        // pegando a o usuario para fazer a validacao
        User sender = this.userServices.findUserById(transaction.senderId());
        User receiver = this.userServices.findUserById(transaction.receiverId());

        //validando usuario
        userServices.validateTransaction(sender, transaction.value());

        //verificar a autorizacao da transacao
        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if(!isAuthorized){
            throw new Exception("Transacao nao autorizada");
        }

        //criando a nova transacao na tabela
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        //atualizar o saldo dos usuarios
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        //salvando na base de dados
        this.repository.save(newTransaction);
        this.userServices.saveUser(sender);
        this.userServices.saveUser(receiver);

        this.notificationService.sendNotification(sender, "Transacao realizada com sucesso!");
        this.notificationService.sendNotification(receiver, "Transacao recebida com sucesso!");
        return newTransaction;
    }

    //metodo para consultar um serviço autorizador externo
    public boolean authorizeTransaction(User sender, BigDecimal value){

       ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);

       if(authorizationResponse.getStatusCode() == HttpStatus.OK ){
           String massage = (String) authorizationResponse.getBody().get("message");
           return "Autorizado".equalsIgnoreCase(massage);
       }else return false;
    }
}
