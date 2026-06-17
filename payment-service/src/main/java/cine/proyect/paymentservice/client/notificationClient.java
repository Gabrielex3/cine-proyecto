package cine.proyect.paymentservice.client;

import cine.proyect.paymentservice.dto.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface notificationClient {
    @PostMapping("/api/v2/cine/notification/send")
    void enviarNotificacion(@RequestBody NotificationRequestDTO dto);

}
