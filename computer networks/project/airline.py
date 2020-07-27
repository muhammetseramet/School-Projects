import socket
import pickle
import mysql.connector

mydb = mysql.connector.connect(
      host="127.0.0.1",
      database="airlines"
)

mycursor = mydb.cursor()

PORT = 12344
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((socket.gethostname(), PORT))
server.listen()

# TODO complete this
#  Step - 1: DB'den gelen bilgi kontrol edilmeli.
#  Step - 2: no need for procedure, just execute select statement.
while True:
    conn, addr = server.accept()
    message = conn.recv(512).decode()
    # ['GET', '/___', 'HTTP/1.1', 'params:', 'flight_time', 'flight_from', 'flight_to'] == message.split()
    c = message.split()[1]
    comp_name = c.split(sep='/')[1]
    proc_name = "check_flights_" + comp_name

    flight_time = message.split()[4]
    flight_from = message.split()[5]
    flight_to = message.split()[6]

    args = (flight_time, flight_from, flight_to)

    mycursor.callproc(proc_name, args)  # args must be tuple
    myresult = mycursor.fetchall()

    # HACK: there should be a better solution
    objs = []
    ok_message = message.split()[2] + " 200 OK"
    objs.append(ok_message)
    objs.append(myresult)

    conn.send(pickle.dumps(objs))

"""
CREATE OR ALTER TABLE turkish_airlines(
    flight_date VARCHAR(10) NOT NULL,
    flight_dept VARCHAR(25) NOT NULL,
    flight_loc VARCHAR(25) NOT NULL
);
"""

"""
CREATE OR ALTER TABLE pegasus_airlines(
    flight_date VARCHAR(10) NOT NULL,
    flight_dept VARCHAR(25) NOT NULL,
    flight_loc VARCHAR(25) NOT NULL
);
"""

" 20 - 24 Aralık Adıyaman -> Bayburt Seferleri "
" 24 - 27 Aralık İstanbul -> Paris Seferleri"
" 27 - 31 Aralık Bayburt -> Adıyaman Seferleri "
" 01 - 04 Ocak 2020 Paris -> İstanbul Seferleri "

"""
INSERT INTO turkish_airlines
VALUES('20.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO turkish_airlines
VALUES('21.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO turkish_airlines
VALUES('22.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO turkish_airlines
VALUES('23.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO turkish_airlines
VALUES('24.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO turkish_airlines
VALUES('24.12.2019', 'İstanbul', 'Paris');

INSERT INTO turkish_airlines
VALUES('25.12.2019', 'İstanbul', 'Paris');

INSERT INTO turkish_airlines
VALUES('26.12.2019', 'İstanbul', 'Paris');

INSERT INTO turkish_airlines
VALUES('27.12.2019', 'İstanbul', 'Paris');

INSERT INTO turkish_airlines
VALUES('27.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO turkish_airlines
VALUES('28.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO turkish_airlines
VALUES('29.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO turkish_airlines
VALUES('30.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO turkish_airlines
VALUES('31.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO turkish_airlines
VALUES('01.01.2020', 'Paris', 'İstanbul');

INSERT INTO turkish_airlines
VALUES('02.01.2020', 'Paris', 'İstanbul');

INSERT INTO turkish_airlines
VALUES('03.01.2020', 'Paris', 'İstanbul');

INSERT INTO turkish_airlines
VALUES('04.01.2020', 'Paris', 'İstanbul');

"""

" 20 - 24 Aralık Bayburt -> Adıyaman Seferleri "
" 24 - 27 Aralık İstanbul -> Paris Seferleri"
" 27 - 31 Aralık Adıyaman -> Bayburt Seferleri "
" 01 - 04 Ocak 2020 Paris -> İstanbul Seferleri "

"""
INSERT INTO pegasus_airlines
VALUES('20.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO pegasus_airlines
VALUES('21.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO pegasus_airlines
VALUES('22.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO pegasus_airlines
VALUES('23.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO pegasus_airlines
VALUES('24.12.2019', 'Bayburt', 'Adıyaman');

INSERT INTO pegasus_airlines
VALUES('24.12.2019', 'İstanbul', 'Paris');

INSERT INTO pegasus_airlines
VALUES('25.12.2019', 'İstanbul', 'Paris');

INSERT INTO pegasus_airlines
VALUES('26.12.2019', 'İstanbul', 'Paris');

INSERT INTO pegasus_airlines
VALUES('27.12.2019', 'İstanbul', 'Paris');

INSERT INTO pegasus_airlines
VALUES('27.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO pegasus_airlines
VALUES('28.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO pegasus_airlines
VALUES('29.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO pegasus_airlines
VALUES('30.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO pegasus_airlines
VALUES('31.12.2019', 'Adıyaman', 'Bayburt');

INSERT INTO pegasus_airlines
VALUES('01.01.2020', 'Paris', 'İstanbul');

INSERT INTO pegasus_airlines
VALUES('02.01.2020', 'Paris', 'İstanbul');

INSERT INTO pegasus_airlines
VALUES('03.01.2020', 'Paris', 'İstanbul');

INSERT INTO pegasus_airlines
VALUES('04.01.2020', 'Paris', 'İstanbul');
"""

""" 
CREATE PROCEDURE check_flights_turkish_airlines (IN flight_time VARCHAR(10),
								                IN flight_from VARCHAR(25),
								                IN flight_to VARCHAR(25)) AS
BEGIN 
	SELECT *
	FROM turkish_airlines
	WHERE flight_date LIKE flight_time
	AND
	flight_dept LIKE flight_from
	AND
	flight_loc LIKE flight_to;
END
"""

""" 
CREATE PROCEDURE check_flights_pegasus_airlines (IN flight_time VARCHAR(10),
								                IN flight_from VARCHAR(25),
								                IN flight_to VARCHAR(25)) AS
BEGIN 
	SELECT *
	FROM pegasus_airlines
	WHERE flight_date LIKE flight_time
	AND
	flight_dept LIKE flight_from
	AND
	flight_loc LIKE flight_to;
END
"""
