from matplotlib import pyplot as plt
import cv2 as cv
import os
import numpy as np
import math

def convolve(img, blur):
    [i, j] = img.shape
    [k, m] = blur.shape

    [x, y] = [i+2*(k-1), j+2*(m-1)]
    rep = np.pad(img, blur.shape, 'constant', constant_values=0)

    rotate = np.rot90(blur, 2)
    array = np.array(rotate)
    out = np.zeros([i+k-1, j+m-1])

    for t in range(0, x-k+1):
        for q in range(0, y-m+1):
            temp = rep[t:t+k, q:q+m]
            out[t, q] = (temp * array).sum()

    out[out<0] = 0
    out[out>255] = 255
    return out

def extend_border(img, blur):
    [i, j] = img.shape
    [k, m] = blur.shape

    [x, y] = [i+2*(k-1), j+2*(m-1)]
    rep = np.pad(img, blur.shape, 'wrap')

    rotate = np.rot90(blur, 2)
    array = np.array(rotate)
    out = np.zeros([i+k-1, j+m-1])

    for t in range(0, x-k+1):
        for q in range(0, y-m+1):
            temp = rep[t:t+k, q:q+m]
            out[t, q] = (temp * array).sum()

    out[out<0] = 0
    out[out>255] = 255
    return out

if __name__ == "__main__":
    image = cv.imread('cameraman.tif', 0)
    gauss_array = np.array(([[0.036883, 0.039164, 0.039955, 0.039164, 0.036883],
                                 [0.039164, 0.041586, 0.042426, 0.041586, 0.039164],
                                 [0.039955, 0.042426, 0.043283, 0.042426, 0.039955],
                                 [0.039164, 0.041586, 0.042426, 0.041586, 0.039164],
                                 [0.036883, 0.039164, 0.039955, 0.039164, 0.036883]]))
    laplace_arr = np.array([[0.3333, 0.3333, 0.3333],
                            [0.3333, -2.6667, 0.3333],
                            [0.3333, 0.3333, 0.3333]])
    sobel_array = np.array([[1, 2, 1],
                            [0, 0, 0],
                            [-1, -2, -1]])
    kernel_motion_blur = np.zeros([29, 29], dtype='float32')
    for x in range(0, 29):
        for y in range(0, 29):
            if (x == 0 and y == 0) or (x == 28 and y == 28):
                kernel_motion_blur[x, y] = 0.0156461363493650
            elif x == y:
                kernel_motion_blur[x, y] = 0.0223194155857750
            elif x == y-1:
                kernel_motion_blur[x, y] = 0.0065372054729530

    Laplacian = convolve(image, laplace_arr)
    GaussianBlur = convolve(image, gauss_array)
    Sobel = convolve(image, sobel_array)
    MotionBlur = convolve(image, kernel_motion_blur)

    extendLaplacian = extend_border(image, laplace_arr)
    extendGaussianBlur = extend_border(image, gauss_array)
    extendSobel = extend_border(image, sobel_array)
    extendMotionBlur = extend_border(image, kernel_motion_blur)

    plt.subplot(2, 4, 1), plt.imshow(Laplacian, cmap = 'gray')
    plt.ylabel("Zero Padding")
    plt.title('Laplacian Blur'), plt.xticks([]), plt.yticks([])

    plt.subplot(2, 4, 2), plt.imshow(GaussianBlur, cmap='gray')
    plt.title('Gaussian Blur'), plt.xticks([]), plt.yticks([])

    plt.subplot(2, 4, 3), plt.imshow(Sobel, cmap='gray')
    plt.title('Sobel Blur'), plt.xticks([]), plt.yticks([])

    plt.subplot(2, 4, 4), plt.imshow(MotionBlur, cmap='gray')
    plt.title('Motion Blur'), plt.xticks([]), plt.yticks([])

    plt.subplot(2, 4, 5), plt.imshow(extendLaplacian, cmap='gray')
    plt.ylabel("Border Replication")
    plt.title('Laplacian Blur'), plt.xticks([]), plt.yticks([])

    plt.subplot(2, 4, 6), plt.imshow(extendGaussianBlur, cmap='gray')
    plt.title('Gaussian Blur'), plt.xticks([]), plt.yticks([])

    plt.subplot(2, 4, 7), plt.imshow(extendSobel, cmap='gray')
    plt.title('Sobel Blur'), plt.xticks([]), plt.yticks([])

    plt.subplot(2, 4, 8), plt.imshow(extendMotionBlur, cmap='gray')
    plt.title('Motion Blur'), plt.xticks([]), plt.yticks([])

    plt.show()
