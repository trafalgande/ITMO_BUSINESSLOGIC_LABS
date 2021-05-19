const SockJS = require('sockjs-client')
const Stomp = require('stompjs')

let stompClient = null;

function connect() {
    let socket = new SockJS('/notification')
    stompClient = Stomp.overWS(socket)
    stompClient.connect({"Authorization": "Basic bG1hbzpsbWFv"}, function (frame) {
        console.log('connected ' + frame)
        stompClient.subscribe('/topic/notification', function (message) {
            console.log(message)
        })
    })
}
