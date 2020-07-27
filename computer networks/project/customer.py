import socket
import sys
import tkinter
from threading import Thread

# TODO-1 when server sends new info, display them
#  display için burada airplane ve hotels listbox kullan
def receive():
    """ Handles receiving messages from server """
    while True:
        try:
            message = sckt.recv(512).decode()
            print(message)
            #hotels_box.insert(tkinter.END, message)
        except OSError:  # client has left
            break
    on_closing()

# send all information to the server
def send():
    """ Sends messages to server"""
    start_get = start_entry.get()
    return_get = return_entry.get()
    nop_get = spinner.get()
    pref_hotel_get = preferred_hotels.get()
    pref_airline_get = preferred_airlines.get()
    location_from_get = flight_from.get()
    location_to_get = flight_to.get()
    sckt.send((start_get + " " + return_get + " " + nop_get + " " +
               pref_hotel_get + " " + pref_airline_get + " " +
               location_from_get + " " + location_to_get)
              .encode())


""" Gui exiting.. """
def on_closing():
    sckt.close()
    window.quit()
    sys.exit()


""" Gui specification """
window = tkinter.Tk()
window.title("Trip Advisor")
window.maxsize(900, 600)

# first frame
left_frame = tkinter.Frame(window)
left_frame.pack(side="left")

# centered second frame
button_frame = tkinter.Frame(window)
button_frame.pack(side="left")

# slightly right sided, third frame
right_frame1 = tkinter.Frame(window)
right_frame1.pack(side="left", padx=5, pady=10)

# right sided fourth frame
right_frame2 = tkinter.Frame(window)
right_frame2.pack(side="left", padx=5, pady=10)

# -- Label -- <> Start Date : -- Entry --
tkinter.Label(left_frame, text="Start Date (dd.mm.yyyy): ").pack()
start_entry = tkinter.Entry(left_frame)
start_entry.pack()

# -- Label -- <> Return Date : -- Entry --
tkinter.Label(left_frame, text="Return Date (dd.mm.yyyy): ").pack()
return_entry = tkinter.Entry(left_frame)
return_entry.pack()

# -- Label -- <> Number of people : -- Spinner --
tkinter.Label(left_frame, text="Number of people:").pack()
spinner = tkinter.StringVar(left_frame)
tkinter.Spinbox(left_frame, from_=1, to=10, textvariable=spinner).pack()

# -- Label -- <> Departure From : -- Entry --
tkinter.Label(left_frame, text="Departure From: ").pack()
flight_from = tkinter.Entry(left_frame)
flight_from.pack()

# -- Label -- <> To : -- Entry --
tkinter.Label(left_frame, text="To: ").pack()
flight_to = tkinter.Entry(left_frame)
flight_to.pack()

# -- Label -- <> Preferred hotels : -- Entry --
tkinter.Label(left_frame, text="Preferred hotels: ").pack()
preferred_hotels = tkinter.Entry(left_frame)
preferred_hotels.pack()

# -- Label -- <> Preferred airlines : -- Entry --
tkinter.Label(left_frame, text="Preferred airlines: ").pack()
preferred_airlines = tkinter.Entry(left_frame)
preferred_airlines.pack()

# -- Button -- <> Search
tkinter.Button(button_frame, text="Search", command=send, width=10).pack(side=tkinter.TOP)

# -- Label -- <> Available hotels : -- Entry --
tkinter.Label(right_frame1, text="Available hotels: ").pack()
hotels_box = tkinter.Listbox(right_frame1, selectmode='SINGLE')
hotels_box.pack(fill="y")

# -- Label -- <> Available flights : -- Entry --
tkinter.Label(right_frame2, text="Available flights: ").pack()
airlines_box = tkinter.Listbox(right_frame2, selectmode='SINGLE')
airlines_box.pack(fill="y")

# close all connections
window.protocol("WM_DELETE_WINDOW", on_closing)

""" Now, socket part """
PORT = 6000

sckt = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sckt.connect((socket.gethostname(), PORT))

receive_thread = Thread(target=receive).start()
tkinter.mainloop()  # starts gui
