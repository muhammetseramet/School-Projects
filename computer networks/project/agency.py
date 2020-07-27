import socket
import pickle
from threading import Thread

# NOTE = Sections where a code reviewer found something that needs
#  discussion or further investigation.

# XXX = warning about possible pitfalls, can be used as NOTE:XXX

# HACK = not very well written or malformed code to circumvent problem/
#  bug should be used as HACK:FIXME:

# FIXME = this works, sort of, but it could be done better.

# BUG = there is a problem here.

# TODO = no problem, but additional code needs to be written,
#  usually when you are skipping something

# NOTE - Burada airline server tarafına HTTP GET mesajı atıyoruz.
def client_accept_airline_conn(company_name):

    message = "GET /{}/flights HTTP/1.1" + " " \
                                           "params: {} {} {} {}"\
        .format(company_name, start_date, flight_from, flight_to)

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((socket.gethostname(), 12344))
    client.send(message.encode())

    client_receive_airline(client)

# NOTE:XXX - Burada serverdan gelen query sonucunu alcak ve
#  gelen sonucu gui tarafına gönderecek.
def client_receive_airline(sock):
    """ Handles receiving messages from server """
    try:
        all_message = sock.recv(512)
        message_list = pickle.loads(all_message)
        print(message_list[0])  # HTTP/1.1 OK

        for item in message_list[1]:
            print(item[0])

    except OSError:  # client has left
        print(OSError)

# NOTE - Burada hotel server tarafına HTTP GET mesajı atıyoruz.
def client_accept_hotel_conn(hotel_name):
    message = "GET /{}/available_rooms HTTP/1.1" + " " \
                                           "params: {} {} {} {}" \
        .format(hotel_name, flight_to, start_date, return_date)

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((socket.gethostname(), 12345))
    client.send(message.encode())

    client_receive_hotels(client)

# NOTE:XXX - Burada serverdan gelen query sonucunu alcak ve
#  gelen sonucu gui tarafına gönderecek.
def client_receive_hotels(sock):
    """ Handles receiving messages from server """
    try:
        all_message = sock.recv(512)
        message_list = pickle.loads(all_message)
        print(message_list[0])  # HTTP/1.1 OK

        for item in message_list[1]:
            print(item[0])

    except OSError:  # client has left
        print(OSError)


# NOTE - Burası gelen client isteklerini ilk karşıladığımız yer.
addresses = {}
def server_accept_conn():
    while True:
        conn, addr = server.accept()
        print("{} has connected.".format(addr))
        addresses[conn] = addr
        Thread(target=server_handle_client, args=(conn, addr)).start()

# NOTE - Client tarafından gelen mesajları dinlediğimiz yer.
def server_handle_client(conn, address):
    while True:
        try:
            message = conn.recv(512).decode()  # Clienttan mesajı aldık
            start_date = message.split()[0]
            return_date = message.split()[1]
            number_of_people = message.split()[2]
            pref_hotels = message.split()[3]
            pref_airline = message.split()[4]
            flight_from = message.split()[5]
            flight_to = message.split()[6]

            client_accept_airline_conn("turkish_airlines")
            client_accept_airline_conn("pegasus_airlines")
            client_accept_hotel_conn("ceasers_palace")
            client_accept_hotel_conn("divan_hotel")
            conn.send(message.encode())
            #client = socket.socket()
        except OSError:
            print("{} has left.".format(address))
            break
    conn.close()


PORT = 6000
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((socket.gethostname(), PORT))
server.listen()
start_date = ""; return_date = ""; number_of_people = ""
pref_hotels = ""; pref_airline = ""
flight_from = ""; flight_to = ""
server_thread = Thread(target=server_accept_conn).start()
