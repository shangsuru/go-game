import React, { useState, useEffect } from "react";

const Chat = ({ user, socket, player1, player2, customButtons }) => {
  const [inputText, setInputText] = useState("");
  const [chat, setChat] = useState([]);

  useEffect(() => {
    socket.subscribe(`/topic/chat/${player1}/${player2}`, frame => {
      let message = JSON.parse(frame.body);
      setChat([...chat, message]);
    });
  }, [socket, player1, player2, chat]);

  const sendMessage = () => {
    if (inputText.length > 0) {
      socket.send(
        `/app/chat/${player1}/${player2}`,
        {},
        JSON.stringify({ user, text: inputText })
      );
      setInputText("");
    }
  };

  const displayMessage = message => {
    return (
      <div>
        <p className="chat__message">
          <div className="chat__message__user">{message.user}</div>
          <div>{message.text}</div>
        </p>
      </div>
    );
  };

  const handleEnter = e => {
    if (e.key === "Enter") {
      sendMessage();
    }
  };

  return (
    <div
      style={{
        display: "flex",
        flexGrow: 1,
        flexShrink: "1",
        flexDirection: "column",
        alignItems: "center",
        width: "100%",
        margin: "20px",
        minHeight: "300px",
        height: "fit-content",
        overflow: "hidden",
        padding: "20px"
      }}
    >
      <div
        style={{
          display: "flex",
          flexGrow: 1,
          flexDirection: "column",
          alignItems: "center",
          width: "100%",
          borderRadius: "10px",
          height: "100%"
        }}
      >
        <div
          style={{
            backgroundColor: "#433E3C",
            height: "240px",
            width: "100%",
            marginBottom: "0",
            fontSize: "15px",
            color: "white",
            padding: "12px",
            overflow: "scroll"
          }}
        >
          {chat.map(message => {
            return displayMessage(message);
          })}
        </div>
        <div
          style={{
            display: "flex",
            backgroundColor: "grey",
            height: "15%",
            width: "100%",
            padding: "5px"
          }}
        >
          <input
            className="chatInput"
            value={inputText}
            onChange={e => setInputText(e.target.value)}
            onKeyPress={handleEnter}
          />
          {customButtons
            ? customButtons.map(customButton => {
                return (
                  <button
                    key={customButton.label}
                    className="chatboxbutton"
                    style={{ backgroundColor: "#34e346" }}
                    onClick={customButton.onClick}
                  >
                    {customButton.label}
                  </button>
                );
              })
            : null}
          <button className="chatboxbutton" onClick={sendMessage}>
            <div>Send</div>
          </button>
        </div>
      </div>
    </div>
  );
};

export default Chat;
