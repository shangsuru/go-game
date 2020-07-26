package server.api.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import server.api.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MultiplayerController {

  List<String> playingUsers = new ArrayList<>();

  @MessageMapping("/joinGame/{player1}/{player2}")
  @SendTo("/topic/system/{player1}/{player2}")
  public String joinGame(@DestinationVariable String player1, @DestinationVariable String player2, @RequestBody String user) {
    playingUsers.add(user);
    if (playingUsers.contains(player1) && playingUsers.contains(player2)) {
      return "CONNECTION_ESTABLISHED";
    }

    return "JOINED";
  }

  @MessageMapping("/chat/{player1}/{player2}")
  @SendTo("/topic/chat/{player1}/{player2}")
  public ChatMessage sendChatMessage(@DestinationVariable String player1, @DestinationVariable String player2, @RequestBody ChatMessage message) {
    return message;
  }
}
