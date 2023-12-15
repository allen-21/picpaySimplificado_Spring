package com.picpaySimplificado.services;

import com.picpaySimplificado.domain.user.User;
import com.picpaySimplificado.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;// para fazer requisicao http

    public void sendNotification(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDTO notificationResquest = new NotificationDTO(email, message);
   /*    ResponseEntity<String> notificationReponse = restTemplate.postForEntity("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6",notificationResquest, String.class);

       if(!(notificationReponse.getStatusCode() == HttpStatus.OK)){
           System.out.println("erro ao enviar notificacao");
           throw new Exception("Servico de notificacao esta fora do ar");

       }*/
        System.out.println("Notificacao enviada para o Usuario");
    }
}
