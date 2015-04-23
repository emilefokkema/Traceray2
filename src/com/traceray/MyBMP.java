package com.traceray;

import java.io.IOException;
import java.io.OutputStream;

public class MyBMP{
	private byte [] intToDWord (int parValue) {
	     byte retValue [] = new byte [4];
	     retValue [0] = (byte) (parValue & 0x00FF);
	     retValue [1] = (byte) ((parValue >>  8) & 0x000000FF);
	     retValue [2] = (byte) ((parValue >>  16) & 0x000000FF);
	     retValue [3] = (byte) ((parValue >>  24) & 0x000000FF);
	     return (retValue);
	  }
	private byte [] intToWord (int parValue) {
	     byte retValue [] = new byte [2];
	     retValue [0] = (byte) (parValue & 0x00FF);
	     retValue [1] = (byte) ((parValue >>  8) & 0x00FF);
	     return (retValue);
	  }
	private final static int BITMAPFILEHEADER_SIZE = 14;
	  private final static int BITMAPINFOHEADER_SIZE = 40;
	  //--- Private variable declaration
	  //--- Bitmap file header
	  private byte bitmapFileHeader [] = new byte [14];
	  private byte bfType [] = {'B', 'M'};
	  private int bfSize = 0;
	  private int bfReserved1 = 0;
	  private int bfReserved2 = 0;
	  private int bfOffBits = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE;
	  //--- Bitmap info header
	  private byte bitmapInfoHeader [] = new byte [40];
	  private int biSize = BITMAPINFOHEADER_SIZE;
	  private int biWidth = 0;
	  private int biHeight = 0;
	  private int biPlanes = 1;
	  private int biBitCount = 24;
	  private int biCompression = 0;
	  private int biSizeImage = 0x030000;
	  private int biXPelsPerMeter = 0x0;
	  private int biYPelsPerMeter = 0x0;
	  private int biClrUsed = 0;
	  private int biClrImportant = 0;
	  //--- Bitmap raw data
	  private int bitmap [];
	private OutputStream out;
	private MyColor[][] pixels;
	private void writeFileHeader(){
		try {
			out.write (bfType);
	        out.write (intToDWord (bfSize));
	        out.write (intToWord (bfReserved1));
	        out.write (intToWord (bfReserved2));
	        out.write (intToDWord (bfOffBits));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void writeInfoHeader(){
		try{
			out.write (intToDWord (biSize));
	        out.write (intToDWord (biWidth));
	        out.write (intToDWord (biHeight));
	        out.write (intToWord (biPlanes));
	        out.write (intToWord (biBitCount));
	        out.write (intToDWord (biCompression));
	        out.write (intToDWord (biSizeImage));
	        out.write (intToDWord (biXPelsPerMeter));
	        out.write (intToDWord (biYPelsPerMeter));
	        out.write (intToDWord (biClrUsed));
	        out.write (intToDWord (biClrImportant));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void writeBitmap () {
	      int size;
	      int value;
	      int j;
	      int i;
	      int rowCount;
	      int rowIndex;
	      int lastRowIndex;
	      int pad;
	      int padCount;
	      byte rgb [] = new byte [3];
	      size = (biWidth * biHeight) - 1;
	      pad = 4 - ((biWidth * 3) % 4);
	      if (pad == 4)   // <==== Bug correction
	         pad = 0;     // <==== Bug correction
	      rowCount = 1;
	      padCount = 0;
	      rowIndex = size - biWidth;
	      lastRowIndex = rowIndex;
	      try {
	         for (j = 0; j < size; j++) {
	            value = bitmap [rowIndex];
	            rgb [0] = (byte) (value & 0xFF);
	            rgb [1] = (byte) ((value >> 8) & 0xFF);
	            rgb [2] = (byte) ((value >>  16) & 0xFF);
	            out.write (rgb);
	            if (rowCount == biWidth) {
	               padCount += pad;
	               for (i = 1; i <= pad; i++) {
	                  out.write (0x00);
	               }
	               rowCount = 1;
	               rowIndex = lastRowIndex - biWidth;
	               lastRowIndex = rowIndex;
	            }
	            else
	               rowCount++;
	            rowIndex++;
	         }
	         //--- Update the size of the file
	         bfSize += padCount - pad;
	         biSizeImage += padCount - pad;
	      }
	      catch (Exception wb) {
	         wb.printStackTrace ();
	      }
	   }  
	private boolean convertImage(){
		int pad;
		int parWidth=pixels.length;
		int parHeight=pixels[0].length;
		bitmap = new int [parWidth * parHeight];
//		PixelGrabber pg = new PixelGrabber (parImage, 0, 0, parWidth, parHeight,
//                bitmap, 0, parWidth);
//try {
//pg.grabPixels ();
//}
//catch (InterruptedException e) {
//e.printStackTrace ();
//return (false);
//}
		 pad = (4 - ((parWidth * 3) % 4)) * parHeight;
	     biSizeImage = ((parWidth * parHeight) * 3) + pad;
	     bfSize = biSizeImage + BITMAPFILEHEADER_SIZE +
	BITMAPINFOHEADER_SIZE;
	     biWidth = parWidth;
	     biHeight = parHeight;
	     return true;
	}
	public MyBMP(MyColor[][] pixels, OutputStream out){
		this.pixels=new MyColor[2][];
		this.pixels[0]=new MyColor[2];
			this.pixels[0][0]=new MyColor(255,0,0);
			this.pixels[0][1]=new MyColor(0,255,0);
		this.pixels[1]=new MyColor[2];
			this.pixels[1][0]=new MyColor(0,0,255);
			this.pixels[1][1]=new MyColor(0,0,0);
		this.out=out;
	}
	public void write(){
		convertImage();
		writeFileHeader();
		writeInfoHeader();
		writeBitmap();
	}
	
}

