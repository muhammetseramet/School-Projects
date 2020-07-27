import socket
import pickle
import mysql.connector

mydb = mysql.connector.connect(
      host="127.0.0.1",
      database="hotels"
)

mycursor = mydb.cursor()

PORT = 12345
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((socket.gethostname(), PORT))
server.listen()

# TODO complete this
#  Step - 1: DB'den gelen bilgi kontrol edilmeli.
#  Step - 2: no need for procedure, just execute select statement.
while True:
    conn, addr = server.accept()
    message = conn.recv(512).decode()
    # ['GET', '/___', 'HTTP/1.1', 'params:', 'city', 'start_date', 'end_date'] == message.split()
    c = message.split()[1]
    comp_name = c.split(sep='/')[1]
    proc_name = comp_name + "_status"

    city = message.split()[4]
    room_availability_start_date = message.split()[5]
    room_availability_end_date = message.split()[6]

    args = (city, room_availability_start_date, room_availability_end_date)

    mycursor.callproc(proc_name, args)  # args must be tuple
    myresult = mycursor.fetchall()

    # HACK: there should be a better solution
    objs = []
    ok_message = message.split()[2] + " 200 OK"
    objs.append(ok_message)
    objs.append(myresult)

    conn.send(pickle.dumps(objs))


"""
CREATE OR ALTER TABLE caesars_palace(
    city VARCHAR(25) NOT NULL,
    room_availability_start_from VARCHAR(10) NOT NULL,
    room_availability_end VARCHAR(10) NOT NULL
);
"""

"""
CREATE OR ALTER TABLE divan_hotel(
    city VARCHAR(25) NOT NULL,
    room_availability_start VARCHAR(10) NOT NULL,
    room_availability_end VARCHAR(10) NOT NULL
);
"""

" 20 Aralık - 04 Ocak Ceasers Palace "

"""
INSERT INTO ceasers_palace
VALUES ("Adıyaman", "20.12.2019", "27.12.2019");

INSERT INTO ceasers_palace
VALUES ("Bayburt", "27.12.2019", "04.01.2020");

INSERT INTO ceasers_palace
VALUES ("İstanbul", "27.12.2019", "04.01.2020");

INSERT INTO ceasers_palace
VALUES ("Paris", "20.12.2019", "27.12.2019");
"""

" 20 Aralık - 04 Ocak Divan Hotel"

"""
INSERT INTO divan_hotel
VALUES("Adıyaman", "27.12.2019", "04.01.2020");

INSERT INTO divan_hotel
VALUES("Burdur", "20.12.2019", "27.12.2019");

INSERT INTO divan_hotel
VALUES("İstanbul", "20.12.2019", "27.12.2019");

INSERT INTO divan_hotel
VALUES("Paris", "27.12.2019", "04.01.2020");
"""


"""
CREATE PROCEDURE ceasers_palace_status (IN location VARCHAR(25),
                                        IN start_date VARCHAR(10),
                                        IN dept_date VARCHAR(10)) AS
BEGIN
    SELECT *
    FROM ceasers_palace
    WHERE city LIKE location
    AND room_availability_start LIKE start_date
    AND room_availability_end LIKE dept_date;
END
"""

"""
CREATE PROCEDURE divan_hotel_status (IN location VARCHAR(25),
                                     IN start_date VARCHAR(10),
                                     IN dept_date VARCHAR(10)) AS
BEGIN
    SELECT *
    FROM divan_hotel
    WHERE city LIKE location
    AND room_availability_start LIKE start_date
    AND room_availability_end LIKE dept_date;
END
"""
