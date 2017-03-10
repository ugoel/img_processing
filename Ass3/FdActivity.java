//This code is written by Utkarsh Goel for MSU CSCI 442 Computer Vision Ass#3

package org.opencv.samples.facedetect;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FdActivity extends Activity implements CvCameraViewListener2 {

    private static final String    TAG                 = "CSCI442Ass3";
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    private static final Scalar    EYE_RECT_COLOR     = new Scalar(255, 0, 0, 255);
    private Mat                    mRgba;
    private Mat                    mGray;
    private float                  mRelativeFaceSize   = 0.5f;
    private int                    mAbsoluteFaceSize   = 0;
    private float                  mRelativeEyeSize   = 0.1f;
    private int                    mAbsoluteEyeSize   = 0;

    private CascadeClassifier      mJavaDetector;
    private CascadeClassifier      mJavaDetectorEye;
    private CameraBridgeViewBase   mOpenCvCameraView;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    File mCascadeFile = loadCascade("haarcascade_frontalface_default");
                    mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                    mJavaDetector.load(mCascadeFile.getAbsolutePath());
                    if (mJavaDetector.empty()) {
                        Log.e(TAG, "Failed to load cascade classifier");
                        mJavaDetector = null;
                    }
                    else {
                        Log.i(TAG, "Loaded cascade face classifier from " + mCascadeFile.getAbsolutePath());
                    }

                    File mCascadeFileEye = loadCascade("haarcascade_eye");
                    mJavaDetectorEye = new CascadeClassifier(mCascadeFileEye.getAbsolutePath());
                    mJavaDetectorEye.load(mCascadeFileEye.getAbsolutePath());
                    if (mJavaDetectorEye.empty()) {
                        Log.e(TAG, "Failed to load cascade eye classifier");
                        mJavaDetectorEye = null;
                    }
                    else {
                        Log.i(TAG, "Loaded cascade eye classifier from " + mCascadeFileEye.getAbsolutePath());
                    }

                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public File loadCascade(String harFile) {
        File mCascadeFile = null;
        File cascadeDir = null;
        try {
            InputStream is = getResources().openRawResource(getResources().getIdentifier(harFile, "raw", getApplicationContext().getPackageName()));
            cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            mCascadeFile = new File(cascadeDir, harFile + ".xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to load cascade eye. Exception thrown: " + e);
        }
        cascadeDir.delete();
        return mCascadeFile;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.face_detect_surface_view);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }
        MatOfRect faces = new MatOfRect();
        mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        Rect[] facesArray = faces.toArray();

        if (mAbsoluteEyeSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeEyeSize) > 0) {
                mAbsoluteEyeSize = Math.round(height * mRelativeEyeSize);
            }
        }

        for (int i = 0; i < facesArray.length; i++) {
            Mat mRgbaROI = mRgba.submat(facesArray[i]);
            Mat mGrayROI = mGray.submat(facesArray[i]);
            //tl is top-left and br is bottom right
            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);

            MatOfRect eyes = new MatOfRect();
            mJavaDetectorEye.detectMultiScale(mGrayROI, eyes, 1.1, 2, 2, new Size(mAbsoluteEyeSize, mAbsoluteEyeSize), new Size());
            Rect[] eyesArray = eyes.toArray();
            for (int j = 0; j < eyesArray.length; j++)
            {
                //logic to draw rectangles (around eyes) only in the upper half of the face.
                // This should prevent opencv from drawing rectangles around nostrils or other places
                double halfFace = (facesArray[i].tl().y + facesArray[i].br().y) / 2;
                if (eyesArray[j].br().y < halfFace && eyesArray[j].tl().y < halfFace)
                {
                    Imgproc.rectangle(mRgbaROI, eyesArray[j].tl(), eyesArray[j].br(), EYE_RECT_COLOR, 3);
                }
            }
        }
        return mRgba;
    }


}
