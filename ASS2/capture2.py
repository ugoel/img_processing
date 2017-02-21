#This code is written by Utkarsh Goel for MSU CSCI 442 Computer Vision
import cv2 as cv
import numpy as np
import os

def print_hsv_val(event, x, y, flags, param):
    if(event==cv.EVENT_LBUTTONDOWN):
        selectedPix = param[y, x]
        print("H: ", selectedPix[0], ", S: ", selectedPix[1], ", V: ", selectedPix[2])

def nothing(x):
    pass

def capture_webcam():
    isHsvEnabled = True
    isRegularEnabled = True
    camera = cv.VideoCapture(0)
    
    cv.namedWindow('slider', cv.WINDOW_NORMAL)
    cv.resizeWindow('slider', 320,60)
    cv.moveWindow('slider', 320, 445)
    cv.createTrackbar('H min','slider',5,255,nothing)
    cv.createTrackbar('H max','slider',20,255,nothing)
    cv.createTrackbar('S min','slider',50,255,nothing)
    cv.createTrackbar('S max','slider',140,255,nothing)
    cv.createTrackbar('V min','slider',90,255,nothing)
    cv.createTrackbar('V max','slider',170,255,nothing)
    
    while True:
        ret_val, img = camera.read()
        if ret_val:
            imgReg = cv.flip(img, 1)
            if isRegularEnabled:
                cv.namedWindow('regular', cv.WINDOW_NORMAL)
                cv.resizeWindow('regular', 320,240)
                cv.moveWindow('regular', 0, 0)
                imgReg = cv.resize(imgReg, (320,240))
                cv.imshow('regular', imgReg)
            if isHsvEnabled:
                cv.namedWindow('hsv', cv.WINDOW_NORMAL)
                cv.resizeWindow('hsv', 320,240)
                cv.moveWindow('hsv', 320, 0)
                imgHsv = cv.cvtColor(imgReg, cv.COLOR_BGR2HSV)
                cv.imshow('hsv', imgHsv)

        # get current positions of four trackbars
        hMin = cv.getTrackbarPos('H min','slider')
        hMax = cv.getTrackbarPos('H max','slider')
        sMin = cv.getTrackbarPos('S min','slider')
        sMax = cv.getTrackbarPos('S max','slider')
        vMin = cv.getTrackbarPos('V min','slider')
        vMax = cv.getTrackbarPos('V max','slider')

        HSVLOW = np.array([hMin,sMin,vMin])
        HSVHIGH = np.array([hMax,sMax,vMax])
        #print(HSVLOW, HSVHIGH)
        mask = cv.inRange(imgHsv, HSVLOW, HSVHIGH)
        cv.namedWindow('mask', cv.WINDOW_NORMAL)
        cv.resizeWindow('mask', 320,240)
        cv.moveWindow('mask', 0, 265)
        cv.imshow('mask', mask)

        #erode the grayscale image
        kernel = np.ones((3,3),np.uint8)
        erode = cv.erode(mask, kernel, iterations = 4)
        #dilate the grayscale image
        dilation = cv.dilate(mask, kernel, iterations = 2)
        cv.namedWindow('erode and dilation', cv.WINDOW_NORMAL)
        cv.resizeWindow('erode and dilation', 320,240)
        cv.moveWindow('erode and dilation', 0, 505)
        cv.imshow('erode and dilation', dilation)

        if cv.waitKey(1) == 114:
            isRegularEnabled = False
            cv.destroyWindow("regular")
        elif cv.waitKey(1) == 104:
            isHsvEnabled = False
            cv.destroyWindow("hsv")
        elif cv.waitKey(1) == 27:
            break
        if (isRegularEnabled == False and isHsvEnabled == False):
            break
        cv.setMouseCallback('hsv', print_hsv_val, imgHsv)
        
    cv.destroyAllWindows()
    os._exit(0)

def main():
    print("Press r to close the regular webcam window\nPress h to close the hsv window")
    capture_webcam()

if __name__ == '__main__':
    main()
