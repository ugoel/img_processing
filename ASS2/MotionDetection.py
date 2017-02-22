#This code is written by Utkarsh Goel for MSU CSCI 442 Computer Vision
import cv2 as cv
import numpy as np
import os

def capture_webcam():
    camera = cv.VideoCapture(0)
    ret_val, tempImg = camera.read()
    tempImg = cv.resize(tempImg, (320,240))
    imgAcw = np.zeros((tempImg.shape), np.float32)

    cv.namedWindow('original', cv.WINDOW_NORMAL)
    cv.resizeWindow('original', 320,240)
    cv.moveWindow('original', 0, 0)

    cv.namedWindow('grayscale', cv.WINDOW_NORMAL)
    cv.resizeWindow('grayscale', 320,240)
    cv.moveWindow('grayscale', 320, 0)

    cv.namedWindow('bright', cv.WINDOW_NORMAL)
    cv.resizeWindow('bright', 320,240)
    cv.moveWindow('bright', 0, 285)

    cv.namedWindow('difference', cv.WINDOW_NORMAL)
    cv.resizeWindow('difference', 320,240)
    cv.moveWindow('difference', 320, 285)

    cv.namedWindow('threshhold', cv.WINDOW_NORMAL)
    cv.resizeWindow('threshhold', 320,240)
    cv.moveWindow('threshhold', 0, 545)

    cv.namedWindow('contour', cv.WINDOW_NORMAL)
    cv.resizeWindow('contour', 320,240)
    cv.moveWindow('contour', 320, 545)
    
    while True:
        ret_val, img = camera.read()
        if ret_val:
            img = cv.flip(img, 1)
            img = cv.resize(img, (320,240))
            cv.imshow('original', img)
            imgBlank = img.copy()

            #printing grayscale image
            grayImg = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
            cv.imshow('grayscale', grayImg)

            #brightening the image
            img_yuv = cv.cvtColor(img, cv.COLOR_BGR2YUV)
            img_yuv[:,:,0] = cv.equalizeHist(img_yuv[:,:,0])
            img_output = cv.cvtColor(img_yuv, cv.COLOR_YUV2BGR)
            #printing the brightened image
            cv.imshow('bright', img_output)
            
            #blurring the brightened image
            blur = cv.GaussianBlur(img_output, (5,5),0)

            cv.accumulateWeighted(blur, imgAcw, 0.32)
            cv.convertScaleAbs(imgAcw, imgBlank, 1.0, 0.0)
            #taking the difference
            diff = cv.absdiff(blur, imgBlank)
            #converting the difference to grayscale
            diffGray =cv.cvtColor(diff, cv.COLOR_BGR2GRAY)

            #printing the difference
            cv.imshow('difference', diffGray)

            #taking threshhold with small value
            ret,thresh = cv.threshold(diffGray,2,255,0)
            #blurring
            blur1 = cv.GaussianBlur(thresh, (5,5), 1.5)
            #taking the threshhold with large value
            ret,thresh1 = cv.threshold(blur1,250,255,0)

            #swapping white pixels with black
            cv.imshow('threshhold', cv.bitwise_not(thresh1))

            #finding contours to moving objects
            im2, contours, hierarchy = cv.findContours(thresh1,cv.RETR_TREE,cv.CHAIN_APPROX_SIMPLE)
            imgg = np.zeros((tempImg.shape), np.float32)
            for contour in contours:
                #getting significant blobs
                if cv.contourArea(contour) >= 5000:
                    (x, y, w, h) = cv.boundingRect(contour)
                    cv.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 1)

            #printing contours on the original image
            cv.imshow('contour', img)
            
        if cv.waitKey(1) == 27:
            break
        
    cv.destroyAllWindows()
    os._exit(0)

def main():
    capture_webcam()

if __name__ == '__main__':
    main()
