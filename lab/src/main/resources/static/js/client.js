let stompClient = null;
let subMap = new Map();

const toggleConnection = (e, username, password) => {
    let btn = $(e.target)
    if (btn.text() === 'CONNECT') {
        let socket = new SockJS('/notification')
        stompClient = Stomp.over(socket)
        stompClient.connect({"Authorization": "Basic " + btoa(`${username}:${password}`)})
        btn.text('DISCONNECT')
        btn.attr('class', 'btn btn-danger btn-connect')
    } else {
        if (stompClient !== null) {
            stompClient.disconnect()
        }
        btn.text('CONNECT')
        btn.attr('class', 'btn btn-success btn-connect')
    }
}

const toggleSubscription = (e, username) => {
    let btn = $(`#${username}`)
    if (btn.text() === 'SUB') {
        let sub = stompClient.subscribe(`/topic/notification.${username}`, (message) => {

            $('.message-container').append(
                '<span class=\'label label-important\'>' + JSON.parse(JSON.stringify(message.body)) + '</span>' +
                '<br>'
            )
        })
        subMap.set(username, sub)
        btn.attr('class', 'btn btn-danger btn-sub')
        btn.text('UNSUB')
    } else {
        subMap.get(username).unsubscribe()
        subMap.delete(username)
        btn.attr('class', 'btn btn-secondary btn-sub')
        btn.text('SUB')
    }
}

$(() => {
    $('#form_').submit((e) => {
        e.preventDefault()
    })

    $('.btn-connect').on('click', (e) => {
        toggleConnection(e, $('#username').val(), $('#password').val())
    })

    $('.btn-sub').on('click', (e) => {
        toggleSubscription(e, e.target.id)
    })
})