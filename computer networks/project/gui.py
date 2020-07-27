from tkinter import *
from customer_client_process import client_process

class Application(Frame):
	def __init__(self, parent):
		super(Application, self).__init__(parent)
		self.grid()
		self.create_main()

	def create_main(self):
		#scrolledtext.ScrolledText(window,
		#					text="In this program, we will help you to find"+
		#					" the dream vacation that you always wanted to go" +
		#					", but never have the chance to take it.")
		self.start_label = Label(self, text="Start Date (dd.mm.yyyy): ").grid(row=0)
		self.return_label = Label(self, text="Return Date (dd.mm.yyyy): ").grid(row=1)
		self.hotel_label = Label(self, text="Available hotels: ").grid(row=0, column=3)
		self.airline_label = Label(self, text="Available flights: ").grid(row=0, column=4)
		self.start_entry = Entry(master=self)
		self.return_entry = Entry(master=self)
		self.hotels_entry = Entry(master=self)
		self.airlines_entry = Entry(master=self)

		self.start_entry.grid(row=0, column=1)
		self.return_entry.grid(row=1, column=1)
		self.hotels_entry.grid(row=1, column=3)
		self.airlines_entry.grid(row=1, column=4)

		#regex
		#mached = re.match("[0-9]{2}\.[0-9]{2}\.[0-9]{4}", entry.get())

		self.spinner = StringVar()
		self.nop_label = Label(self, text="Number of people:").grid(row=2)
		Spinbox(self, from_ =1, to=10, textvariable=self.spinner).grid(row=2, column=1)

		self.btn = Button(self, text="Search", command=self.clicked, width=10).grid(column=2, row=1)

	def clicked(self):
		start_get = self.start_entry.get()
		return_get = self.return_entry.get()
		nop_get = self.spinner.get()
		client_process(start_get, return_get, nop_get)

window = Tk()
window.title("Trip Advisor")
windowWidth = window.winfo_reqwidth()
windowHeight = window.winfo_reqheight()
positionRight = int(window.winfo_screenwidth()/2 - windowWidth/2)
positionDown = int(window.winfo_screenheight()/2 - windowHeight/2)
window.geometry("{}x{}+{}+{}".format(650, 100,
							positionRight, positionDown))
#window.resizable(0, 0)
app = Application(window)
window.mainloop()
