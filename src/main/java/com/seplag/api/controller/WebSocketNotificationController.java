package com.seplag.api.controller;

import com.seplag.api.domain.album.Album;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketNotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketNotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Este método será chamado para enviar a notificação para todos os clientes conectados
    public void notifyNewAlbum(Album album) {
        // Enviar a mensagem para o tópico "/topic/album" que os clientes vão assinar
        System.out.println("📢 Enviando álbum via WebSocket: " + album.getNomeAlbum());
        messagingTemplate.convertAndSend("/topic/novo-album", album);
    }
}
