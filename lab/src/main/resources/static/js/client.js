let stompClient = null;
let subMap = new Map();

const connect = () => {
    let socket = new SockJS('/notification')
    stompClient = Stomp.over(socket)
    stompClient.connect({"Authorization": "Basic bG1hbzpsbWFv"})
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
}

const toggleSubscription = (username) => {
    let btn = $(event.target)
    if (btn.text() === 'SUB') {
        let sub = stompClient.subscribe(`/topic/notification.${username}`, (message) => {
            $('.message-container').append(
                '<span class=\'label label-important\'>' + JSON.parse(JSON.stringify(message.body)) + '</span>' +
                '<br>'
            )
        })
        subMap.set(username, sub)

        btn.attr('class', 'btn btn-danger btn-subscribe')
        btn.text('UNSUB')
    } else {
        subMap.get(username).unsubscribe()
        btn.attr('class', 'btn btn-secondary btn-subscribe')
        btn.text('UNSUB')
    }
}

const btnInitialisation = () => {
    $('.btn-connect').on('click', () => {
        let btn = $('.btn-connect')
        if (btn.text() === 'CONNECT') {
            connect()
            btn.text('DISCONNECT')
            btn.attr('class', 'btn btn-danger btn-connect')
        } else {
            disconnect()
            btn.text('CONNECT')
            btn.attr('class', 'btn btn-success btn-connect')
        }
    })


}

// init
$(() => {
    btnInitialisation()

});
