from scipy.io import wavfile
from scipy.fftpack import fftfreq
from matplotlib import pyplot as plt
import numpy as np
import soundfile as sf


if __name__ == "__main__":
    fs, data = wavfile.read('allthree.wav')
    fft_out = np.fft.fft(data)
    freq = fftfreq(data.shape[0], 1/fs)
    plt.plot(freq, np.abs(fft_out)) # burada freq domaine geçti ve plot etti. 1.TAMAM
    plt.show()
