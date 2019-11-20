import cv2 as cv
import numpy as np

# calculate the correleation coefficient between two arrays
def find_correlation_coeff(test1, test2):
    meanA = np.mean(test1)
    meanB = np.mean(test2)
    stdA = np.std(test1)
    stdB = np.std(test2)

    arr1 = test1 - meanA
    arr2 = test2 - meanB
    arr1 = arr1 / stdA
    arr2 = arr2 / stdB

    correlation_coeff = np.mean(arr1 * arr2)
    return correlation_coeff * 100

# search faces in the given image
# TODO complete this
def search_faces(image, face):
    [i, j] = image.shape
    [m, n] = face.shape

    array = np.zeros([i, j])
    dict = {'max' : 0, 'x' : 0, 'y' : 0}
    max = 0
    for x in range(0, i-m+1):
        for y in range(0, j-n+1):
            value = find_correlation_coeff(image[x:x+m, y:y+n], face)
            array[x, y] = value
            sum = np.sum(array[x:x+m, y:y+n])
            if sum > dict['max']:
                dict['x'] = x
                dict['y'] = y
                dict['max'] = sum

    if dict['max'] == 0:
        exit()

    cv.rectangle(image, (dict['x'],dict['y']), (dict['x']+m, dict['y']+n), (255, 255, 255), 2)
    cv.imshow('Face Detection',image)
    cv.waitKey(0)
    cv.destroyAllWindows()

def find_min(x, y, z):
    min = x
    if min > y:
        min = y
    if min > z:
        min = z
    return min

def average_faces(face1, face2, face3):
    [i, j] = face1.shape
    [k, l] = face2.shape
    [m, n] = face3.shape

    minx = find_min(i, k, m)
    miny = find_min(j, l, n)

    average = np.zeros([minx, miny])
    for x in range(0, minx):
        for y in range(0, miny):
            result = (int(face1[x, y]) + int(face2[x, y]) + int(face3[x, y])) / 3
            average[x, y] = int(result)
    return average

# main
if __name__ == "__main__":
    #image = cv.imread("family2.jpg", 0)
    face1 = cv.imread("kid.jpg", 0)
    #face2 = cv.imread("woman.jpg", 0)
    face3 = cv.imread("man.jpg", 0)

    #average = average_faces(face1, face2, face3)
    #search_faces(image, average)
    result = find_correlation_coeff(face1, face3)
    x = np.corrcoef(face1, face3)
    print(x==result)
