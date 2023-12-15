package com.picpaySimplificado.services;

import com.picpaySimplificado.domain.user.User;
import com.picpaySimplificado.domain.user.UserType;
import com.picpaySimplificado.dtos.UserDTO;
import com.picpaySimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServices {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal anount) throws Exception {

        //Verifica se o tipo de usuario
        // se ele tem saldo para realizar a transacao
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuário do tipo Logista não está autorizado a realizar transacões");

        }
        if (sender.getBalance().compareTo(anount) < 0) {
            throw new Exception("Nao tem saldo suficiente");

        }
    }

    //caso o usuario nao exista
    public User findUserById(Long id) throws Exception{
        return this.repository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    //Cria um novo usaurio
    public User createUser(UserDTO data){
        User newUser = new User(data);
        this.saveUser(newUser);
        return  newUser;
    }

    //lista os suarios
    public List<User> getAllUsers(){
        return this.repository.findAll();
    }

    public void saveUser(User user){
        this.repository.save(user);
    }

}
