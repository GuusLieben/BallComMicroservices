'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;
var topic = null;
var anonymous = false;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

window.onbeforeunload = function(){
   if (stompClient) {
        stompClient.send("/app/chat.send/" + topic,
                {},
                JSON.stringify({
                    sender: username,
                    type: 'LEAVE',
                    topic: topic
                })
            )
   }
}

function connect(event) {
    username = document.querySelector('#name').value.trim();
    topic = document.querySelector('#topic').value.trim();
    anonymous = document.querySelector('#anonymous').checked;

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        // TODO: Make socket destination dynamic
        var socket = new SockJS('http://gateway/websocket');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/' + topic, onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.register/" + topic,
        {},
        JSON.stringify({
            sender: username,
            type: 'JOIN',
            topic: topic,
            meta: {
                anonymous: anonymous
            }
        })
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not start a conversation, please try again later';
    connectingElement.style.color = 'red';
}

function send(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var socketMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            topic: topic
        };

        stompClient.send("/app/chat.send/" + topic, {}, JSON.stringify(socketMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = (message.sender ?? 'Unknown person') + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = (message.sender ?? 'Unknown person') + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode((message.sender ?? 'Unknown person')[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor((message.sender ?? 'Unknown person'));

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode((message.sender ?? 'Unknown person'));
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', send, true)
