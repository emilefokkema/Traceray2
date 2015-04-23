package com.traceray;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import com.google.appengine.api.ThreadManager;

public class SceneImageWriter {
	int w,h, d;
	Scene s;
	Viewport v;
	OutputStream out;
	public SceneImageWriter(Scene s, Viewport v, OutputStream imageOutput, int recursionDepth){
		this.w=v.w;
		this.h=v.h;
		this.s=s;
		this.v=v;
		this.d=recursionDepth;
		this.out=imageOutput;
	}
	public void write() throws IOException{
		MyImage i=new MyImage(w, h, this.out);
		ThreadFactory factory= ThreadManager.currentRequestThreadFactory();
		ExecutorService executor = Executors.newFixedThreadPool(4, factory);
		for(int l=1;l<=w;l++){
			for(int t=1;t<=h;t++){
				Runnable pixelCalculator=new PixelCalculator(l, t, i, v, s, d);
				executor.execute(pixelCalculator);
			}
		}
		executor.shutdown();
		while(!executor.isTerminated()){}
		System.out.println("SceneImageWriter: image calculated.");
		System.out.println(""+w+", "+h);
		i.write();
	}
}
