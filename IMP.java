/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class.
 */

/*
This code is modified by Utkarsh Goel
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.lang.*;
import java.util.Arrays;
import java.awt.image.BufferedImage;

class IMP implements MouseListener{
    JFrame frame;
    JPanel mp;
    JButton start;
    JScrollPane scroll;
    JMenuItem openItem, exitItem, resetItem;
    Toolkit toolkit;
    File pic;
    ImageIcon img;
    int colorX, colorY;
    int [] pixels;
    int [] results;
    //Instance Fields you will be using below

    //This will be your height and width of your 2d array
    int height=0, width=0;

    //your 2D array of pixels
    int picture[][];
    int rotateCount = 0;
    /*
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown
     * menu is how you will open an image to manipulate.
     */
    IMP()
    {
        toolkit = Toolkit.getDefaultToolkit();
        frame = new JFrame("Image Processing Software by Utkarsh");
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu functions = getFunctions();
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent ev){quit();}
        });
        openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){ handleOpen(); }
        });
        resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){ reset(); }
        });
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){ quit(); }
        });
        file.add(openItem);
        file.add(resetItem);
        file.add(exitItem);
        bar.add(file);
        bar.add(functions);
        frame.setSize(600, 600);
        mp = new JPanel();
        mp.setBackground(new Color(0, 0, 0));
        scroll = new JScrollPane(mp);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        JPanel butPanel = new JPanel();
        butPanel.setBackground(Color.black);
        start = new JButton("start");
        start.setEnabled(false);

        butPanel.add(start);
        frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(bar);
        frame.setVisible(true);
    }

   /*
    * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods
    * for handling the choice, fun1, fun2, fun3, fun4, etc. etc.
    */

    private JMenu getFunctions()
    {
        JMenu fun = new JMenu("Functions");
        JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
        JMenuItem secondItem = new JMenuItem("Rotate 90 Degrees");
        JMenuItem thirdItem = new JMenuItem("Grayscale - Average");
        JMenuItem fourthItem = new JMenuItem("Grayscale - Lightness");
        JMenuItem fifthItem = new JMenuItem("Grayscale - Luminosity");
        JMenuItem sixthItem = new JMenuItem("Equalize");
        JMenuItem seventhItem = new JMenuItem("Detect Orange");
        JMenuItem eigthItem = new JMenuItem("Blur");
        JMenuItem ninethItem = new JMenuItem("Edge Detection 3x3 Mask");
        JMenuItem tenthItem = new JMenuItem("Edge Detection 5x5 Mask");
        JMenuItem eleventhItem = new JMenuItem("Draw Histogram");

        firstItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){fun1();}
        });

        secondItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){rotate90();}
        });

        thirdItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){grayscale("average");}
        });

        fourthItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){grayscale("lightness");}
        });

        fifthItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){grayscale("luminosity");}
        });

        sixthItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){equalize();}
        });

        seventhItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){detectOrange();}
        });
        eigthItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){blur();}
        });
        ninethItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){edgeDetection3by3();}
        });
        tenthItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){edgeDetection5by5();}
        });
        eleventhItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){DrawHistograms();}
        });




        fun.add(firstItem);
        fun.add(secondItem);
        fun.add(thirdItem);
        fun.add(fourthItem);
        fun.add(fifthItem);
        fun.add(sixthItem);
        fun.add(seventhItem);
        fun.add(eigthItem);
        fun.add(ninethItem);
        fun.add(tenthItem);
        fun.add(eleventhItem);
        return fun;

    }

    /*
     * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame.
     * You don't need to worry about this method.
     */
    private void handleOpen()
    {
        img = new ImageIcon();
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(frame);
        if(option == JFileChooser.APPROVE_OPTION) {
            pic = chooser.getSelectedFile();
            img = new ImageIcon(pic.getPath());
            clear();
            rotateCount=0;
            width = img.getIconWidth();
            height = img.getIconHeight();
            JLabel label = new JLabel(img);
            label.addMouseListener(this);
            pixels = new int[width * height];

            results = new int[width * height];


            Image image = img.getImage();

            PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
            try {
                pg.grabPixels();
            } catch (InterruptedException e) {
                System.err.println("Interrupted waiting for pixels");
                return;
            }

            for (int i = 0; i < width * height; i++)
                results[i] = pixels[i];
            turnTwoDimensional();
            mp.removeAll();
            mp.add(label);

            mp.revalidate();
        }
    }

    /*
     * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
     * So this method changes the one dimensional array to a two-dimensional.
     */
    private void turnTwoDimensional()
    {
        picture = new int[height][width];
        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
                picture[i][j] = pixels[i*width+j];
    }
    /*
     *  This method takes the picture back to the original picture
     */
    private void reset()
    {
        for(int i = 0; i<width*height; i++)
            pixels[i] = results[i];
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);
        mp.revalidate();
        turnTwoDimensional();
        rotateCount=0;
    }
    /*
     * This method is called to redraw the screen with the new image.
     */
    private void resetPicture()
    {
        Image img2;
        if(rotateCount % 2 == 1) {
            for(int i=0; i<width; i++)
                for(int j=0; j<height; j++)
                    pixels[i*height+j] = picture[i][j];
            img2 = toolkit.createImage(new MemoryImageSource(height, width, pixels, 0, height));
        }
        else {
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++)
                    pixels[i*width+j] = picture[i][j];
            img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));
        }
        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);
        mp.revalidate();
    }

    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
    private int [] getPixelArray(int pixel)
    {
        int temp[] = new int[4];
        temp[0] = (pixel >> 24) & 0xff;
        temp[1]   = (pixel >> 16) & 0xff;
        temp[2] = (pixel >>  8) & 0xff;
        temp[3]  = (pixel) & 0xff;
        return temp;

    }
    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer.
     */
    private int getPixels(int rgb[])
    {
        int alpha = 0;
        int rgba = (rgb[0] << 24) | (rgb[1] <<16) | (rgb[2] << 8) | rgb[3];
        return rgba;
    }

    private int getPixelsGray(int rgb[])
    {
        int alpha = 0;
        int rgba = (rgb[0] << 24) | (rgb[1] );
        return rgba;
    }

    public void getValue()
    {
        int pix = picture[colorY][colorX];
        int temp[] = getPixelArray(pix);
        System.out.println("Color value " + temp[0] + " " + temp[1] + " "+ temp[2] + " " + temp[3]);
    }

    /**************************************************************************************************
     * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is
     * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will
     * have a 2D array called picture that is holding each pixel from your picture.
     *************************************************************************************************/
   /*
    * Example function that just removes all red values from the picture.
    * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array
    * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
    * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B.
    * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
    * integer value so you can give it back to the program and display the new picture.
    */
    private void fun1()
    {
        int outerLoop = width;
        int innerLoop = height;
        if(rotateCount % 2 == 0) {
            outerLoop = height;
            innerLoop = width;
        }
        for(int i=0; i<outerLoop; i++)
            for(int j=0; j<innerLoop; j++)
            {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                rgbArray[1] = 0;
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);

            }
        resetPicture();
    }

    /*
     * fun2
     * This is where you will write your STACK
     * All the pixels are in picture[][]
     * Look at above fun1() to see how to get the RGB out of the int (getPixelArray)
     * and then put the RGB back to an int (getPixels)
     */

    private void clear() {
        mp.removeAll();
        mp.revalidate();
        mp.updateUI();
    }


    private void grayscale(String type) {
        int outerLoop = width;
        int innerLoop = height;
        if(rotateCount % 2 == 0) {
            outerLoop = height;
            innerLoop = width;
        }
        for(int i=0; i<outerLoop; i++)
            for(int j=0; j<innerLoop; j++)
            {
                int rgbArray[] = new int[4];
                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);
                if (type == "average") {
                    rgbArray[1] = rgbArray[2] = rgbArray[3] = (int) (rgbArray[1] + rgbArray[2] + rgbArray[3])/3;
                }
                else if (type == "luminosity") {
                    rgbArray[1] = rgbArray[2] = rgbArray[3] = (int) (0.21*rgbArray[1] + 0.72*rgbArray[2] + 0.07*rgbArray[3]);
                }
                else if (type == "lightness") {
                    rgbArray[1] = rgbArray[2] = rgbArray[3] = (Math.max(Math.max(rgbArray[1], rgbArray[2]), rgbArray[3]) + Math.min(Math.min(rgbArray[1], rgbArray[2]), rgbArray[3]))/2;
                }
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    private void equalize() {
        int rArray[] = new int[width*height];
        int gArray[] = new int[width*height];
        int bArray[] = new int[width*height];
        int outerLoop = width;
        int innerLoop = height;
        if(rotateCount % 2 == 0) {
            outerLoop = height;
            innerLoop = width;
        }
        for(int i=0; i<outerLoop; i++)
            for(int j=0; j<innerLoop; j++)
            {
                int rgbArray[] = new int[4];
                rgbArray = getPixelArray(picture[i][j]);
                rArray[i*innerLoop+j] = rgbArray[1];
                gArray[i*innerLoop+j] = rgbArray[2];
                bArray[i*innerLoop+j] = rgbArray[3];
            }
        Arrays.sort(rArray);
        Arrays.sort(gArray);
        Arrays.sort(bArray);

        for(int i=0; i<outerLoop; i++)
            for(int j=0; j<innerLoop; j++)
            {
                int rgbArray[] = new int[4];
                rgbArray = getPixelArray(picture[i][j]);

                if((rArray[width*height -1] - rArray[0]) == 0) {
                    rgbArray[1] = (((rgbArray[1] - rArray[0])*(255 - 0))/(rArray[width*height -1] - rArray[0]) + 1) + 0;
                }
                else {
                    rgbArray[1] = (((rgbArray[1] - rArray[0])*(255 - 0))/(rArray[width*height -1] - rArray[0])) + 0;
                }
                if((gArray[width*height -1] - gArray[0]) == 0) {
                    rgbArray[2] = (((rgbArray[2] - gArray[0])*(255 - 0))/(gArray[width*height -1] - gArray[0]) + 1) + 0;
                }
                else {
                    rgbArray[2] = (((rgbArray[2] - gArray[0])*(255 - 0))/(gArray[width*height -1] - gArray[0])) + 0;
                }
                if((bArray[width*height -1] - bArray[0]) == 0) {
                    rgbArray[3] = (((rgbArray[3] - bArray[0]) * (255 - 0)) / (bArray[width * height - 1] - bArray[0]) + 1) + 0;
                }
                else {
                    rgbArray[3] = (((rgbArray[3] - bArray[0]) * (255 - 0)) / (bArray[width * height - 1] - bArray[0])) + 0;
                }
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    private void rotate90() {
        int tempPicture[][] = picture;
        int outerLoop = width;
        int innerLoop = height;
        if(rotateCount % 2 == 0) {
            outerLoop = height;
            innerLoop = width;
        }
        picture = new int[innerLoop][outerLoop];
        for (int i = 0; i < outerLoop; i++) {
            for (int j = 0; j < innerLoop; j++) {
                picture[innerLoop - j - 1][i] = tempPicture[i][j];
            }
        }
        rotateCount+=1;
        clear();
        resetPicture();
    }

    private void detectOrange(){
        int outerLoop = width;
        int innerLoop = height;
        if(rotateCount % 2 == 0) {
            outerLoop = height;
            innerLoop = width;
        }
            for (int i = 0; i < outerLoop; i++) {
                for (int j = 0; j < innerLoop; j++) {
                    int rgbArray[] = new int[4];
                    //get three ints for R, G and B
                    rgbArray = getPixelArray(picture[i][j]);
                    if (rgbArray[1] >= 204 && rgbArray[1] <= 255 && rgbArray[2] >= 102 && rgbArray[2] <= 229 && rgbArray[3] >= 0 && rgbArray[3] <= 204) {
                        rgbArray[1] = 255;
                        rgbArray[2] = 255;
                        rgbArray[3] = 255;
                    } else {
                        rgbArray[1] = 0;
                        rgbArray[2] = 0;
                        rgbArray[3] = 0;
                    }
                    picture[i][j] = getPixels(rgbArray);
                }
            }
        resetPicture();
    }

    private void blur(){
        int tempPicture[][] = picture;
        int outerLoop = width;
        int innerLoop = height;
        if(rotateCount % 2 == 0) {
            outerLoop = height;
            innerLoop = width;
        }
        picture = new int[outerLoop][innerLoop];
        for (int i = 0; i < outerLoop; i++) {
            for (int j = 0; j < innerLoop; j++) {
                if (i == 0 || i == outerLoop - 1 || j == 0 || j == innerLoop - 1) {
                    picture[i][j] = tempPicture[i][j];
                }
                else {
                    int rgbArray[] = new int[4];
                    rgbArray[0] = 0;
                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;
                    for (int outLoop = -1; outLoop <= 1; outLoop++) {
                        for(int inLoop = -1; inLoop <= 1; inLoop++) {
                            rgbArray[0] += (getPixelArray(tempPicture[i+outLoop][j+inLoop]))[0];
                            rgbArray[1] += (getPixelArray(tempPicture[i+outLoop][j+inLoop]))[1];
                            rgbArray[2] += (getPixelArray(tempPicture[i+outLoop][j+inLoop]))[2];
                            rgbArray[3] += (getPixelArray(tempPicture[i+outLoop][j+inLoop]))[3];
                        }
                    }
                    rgbArray[0] = rgbArray[0]/9;
                    rgbArray[1] = rgbArray[1]/9;
                    rgbArray[2] = rgbArray[2]/9;
                    rgbArray[3] = rgbArray[3]/9;
                    picture[i][j] = getPixels(rgbArray);
                }
            }
        }
        resetPicture();
    }

    private void edgeDetection3by3(){
        grayscale("luminosity");
        int tempPicture[][] = picture;
        int outerLoop = width;
        int innerLoop = height;
        if(rotateCount % 2 == 0) {
            outerLoop = height;
            innerLoop = width;
        }
        picture = new int[outerLoop][innerLoop];
        for (int i = 0; i < outerLoop; i++) {
            for (int j = 0; j < innerLoop; j++) {
                if (i == 0 || i == outerLoop - 1 || j == 0 || j == innerLoop - 1) {
                    picture[i][j] = tempPicture[i][j];
                }
                else {
                    int rgbArray[] = new int[4];
                    rgbArray[0] = 0;
                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;
                    for (int outLoop = -1; outLoop <= 1; outLoop++) {
                        for (int inLoop = -1; inLoop <= 1; inLoop++) {
                            if (outLoop == 0 && inLoop == 0) {
                                //rgbArray[0] -= 4 * (getPixelArray(tempPicture[i][j]))[0];
                                rgbArray[1] += 8 * (getPixelArray(tempPicture[i][j]))[1];
                                rgbArray[2] += 8 * (getPixelArray(tempPicture[i][j]))[2];
                                rgbArray[3] += 8 * (getPixelArray(tempPicture[i][j]))[3];
                            } else {
                                //rgbArray[0] += (getPixelArray(tempPicture[i + outLoop][j + inLoop]))[0];
                                rgbArray[1] -= (getPixelArray(tempPicture[i + outLoop][j + inLoop]))[1];
                                rgbArray[2] -= (getPixelArray(tempPicture[i + outLoop][j + inLoop]))[2];
                                rgbArray[3] -= (getPixelArray(tempPicture[i + outLoop][j + inLoop]))[3];
                            }
                        }
                    }
                    picture[i][j] = getPixels(rgbArray);
                }
            }
        }
        resetPicture();
    }

    private void edgeDetection5by5(){
        grayscale("luminosity");
        int tempPicture[][] = picture;
        int outerLoop = width;
        int innerLoop = height;
        if(rotateCount % 2 == 0) {
            outerLoop = height;
            innerLoop = width;
        }
        picture = new int[outerLoop][innerLoop];
        for (int i = 0; i < outerLoop; i++) {
            for (int j = 0; j < innerLoop; j++) {
                if (i == 0 || i == 1 || i == outerLoop - 1 || i == outerLoop - 2 || j == 0 || j == 1 || j == innerLoop - 1 || j == innerLoop - 2) {
                    picture[i][j] = tempPicture[i][j];
                }
                else {
                    int rgbArray[] = new int[4];
                    rgbArray[0] = 0;
                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;
                    for (int outLoop = -2; outLoop <= 2; outLoop++) {
                        for (int inLoop = -2; inLoop <= 2; inLoop++) {
                            if (outLoop == 0 && inLoop == 0) {
                                //rgbArray[0] += 16 * (getPixelArray(tempPicture[i][j]))[0];
                                rgbArray[1] += 16 * (getPixelArray(tempPicture[i][j]))[1];
                                rgbArray[2] += 16 * (getPixelArray(tempPicture[i][j]))[2];
                                rgbArray[3] += 16 * (getPixelArray(tempPicture[i][j]))[3];
                            } else if (outLoop == -2 || inLoop == -2) {
                                //rgbArray[0] -= (getPixelArray(tempPicture[i + outLoop][j + inLoop]))[0];
                                rgbArray[1] -= (getPixelArray(tempPicture[i + outLoop][j + inLoop]))[1];
                                rgbArray[2] -= (getPixelArray(tempPicture[i + outLoop][j + inLoop]))[2];
                                rgbArray[3] -= (getPixelArray(tempPicture[i + outLoop][j + inLoop]))[3];
                            }
                        }
                    }
                    picture[i][j] = getPixels(rgbArray);
                }
            }
        }
        resetPicture();
    }

    private void DrawHistograms() {
        JFrame redFrame = new JFrame("Red");
        redFrame.setSize(256, 780);
        redFrame.setLocation(600, 0);
        JFrame greenFrame = new JFrame("Green");
        greenFrame.setSize(256, 780);
        greenFrame.setLocation(600 + 256 + 1, 0);
        JFrame blueFrame = new JFrame("blue");
        blueFrame.setSize(256, 780);
        blueFrame.setLocation(600 + 256 + 1 + 256 + 1, 0);

        MyPanel redPanel = new MyPanel();
        MyPanel greenPanel = new MyPanel();
        MyPanel bluePanel = new MyPanel();
        redFrame.getContentPane().add(redPanel, BorderLayout.CENTER);
        redFrame.setVisible(true);
        greenFrame.getContentPane().add(greenPanel, BorderLayout.CENTER);
        greenFrame.setVisible(true);
        blueFrame.getContentPane().add(bluePanel, BorderLayout.CENTER);
        blueFrame.setVisible(true);

        start.setEnabled(true);

        start.addActionListener(evt -> {
            int outerLoop = width;
            int innerLoop = height;
            if(rotateCount % 2 == 0) {
                outerLoop = height;
                innerLoop = width;
            }
            int rArray[] = new int[256];
            int gArray[] = new int[256];
            int bArray[] = new int[256];
            for (int i = 0; i < outerLoop; i++) {
                for (int j = 0; j < innerLoop; j++) {
                    int rgbArray[] = new int[4];
                    rgbArray = getPixelArray(picture[i][j]);
                    rArray[rgbArray[1]] += 1;
                    gArray[rgbArray[2]] += 1;
                    bArray[rgbArray[3]] += 1;
                }
            }

            redPanel.loadData(rArray, Color.red);
            redPanel.repaint();
            greenPanel.loadData(gArray, Color.green);
            greenPanel.repaint();
            bluePanel.loadData(bArray, Color.blue);
            bluePanel.repaint();
        });



    }


    private void quit()
    {
        System.exit(0);
    }

    @Override
    public void mouseEntered(MouseEvent m){}
    @Override
    public void mouseExited(MouseEvent m){}
    @Override
    public void mouseClicked(MouseEvent m){
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }
    @Override
    public void mousePressed(MouseEvent m){}
    @Override
    public void mouseReleased(MouseEvent m){}

    public static void main(String [] args)
    {
        IMP imp = new IMP();
    }

}

class MyPanel extends JPanel {
    BufferedImage grid;
    Graphics2D gc;
    int data[];
    int tempData[];
    Color col;

    void loadData(int[] data, Color col) {
        this.data = data;
        this.col = col;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        if(grid == null){
            grid = (BufferedImage)(this.createImage(getWidth(),getHeight()));
            gc = grid.createGraphics();
        }
        g2.drawImage(grid, null, 0, 0);
        g2.setColor(col);
        if (data != null) {
            tempData = data.clone();
            Arrays.sort(tempData);
            int scaling = tempData[255];
            for (int i = 0; i <= 255; i++) {
                g2.fillRect(i, getHeight()-data[i]*780/scaling, 1, data[i]*780/scaling);
            }
        }
    }
}