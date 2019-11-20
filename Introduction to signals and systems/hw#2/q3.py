import wave
from scipy.io import wavfile
import matplotlib.pyplot as plt
from numpy import *
from numpy.fft import *
from scipy.fftpack import fftfreq, irfft, rfft
import winsound

mySound = wave.open('Funny-04.wav' , 'r') #load wav file into mySound
samplerate , data = wavfile.read('Funny-04.wav')
bitRate = str((mySound.getframerate() * mySound.getsampwidth() * 8 * mySound.getnchannels()) / 1000)

# frequency before trimmed
#plt.plot(data)
#plt.xlabel("Time")
#plt.ylabel("Amplitude")
#plt.show()

#freq. after taking ftt
samples = data.shape[0]
datafft = fft(data)
fftabs = abs(datafft)  #Get the absolute value of real and complex component
freqs = fftfreq(samples,1/samplerate)
plt.xlim( [10, samplerate/2] )
plt.xscale( 'log' )
plt.grid( True )
#winsound.PlaySound('Funny-04.wav', winsound.SND_FILENAME)

#freq. after ifft
samples_2 = datafft.shape[0]
dataifft = ifft(datafft)
ifftabs = abs(dataifft)  #Get the absolute value of real and complex component
i_freqs = fftfreq(samples_2,1/samplerate)
plt.xlim( [10, samplerate/2] )
plt.xscale( 'log' )
plt.grid( True )
plt.plot(freqs[:int(freqs.size/2)],ifftabs[:int(i_freqs.size/2)])
plt.show()
