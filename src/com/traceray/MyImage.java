package com.traceray;

import java.io.IOException;
import java.io.OutputStream;

public class MyImage {
	private OutputStream out;
	public int w;
	public int h;
	double[][] brightness;
	MyColor[][] color;
	public MyImage(int w, int h, OutputStream out){
		this.w=w;
		this.h=h;
		this.brightness=new double[w][h];
		this.color=new MyColor[w][h];
		this.out=out;
	}
	public MyImage setBrightness(int l, int t, double b){
		this.brightness[l-1][t-1]=b;
		return setColor(l, t, new MyColor(b, b, b));
	}
	public MyImage setColor(int l, int t, MyColor c){
		this.color[l-1][t-1]=c;
		return this;
	}
	public void write() throws IOException{
		this.out.write(new byte[]{'e', 'c', 'f'});
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				this.out.write(color[i][j].toByteArray());
			}
		}
	}
}