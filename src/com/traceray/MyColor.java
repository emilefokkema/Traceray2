package com.traceray;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MyColor {
	private static Pattern colorStringPattern=Pattern.compile("\\d{1,3},\\s?\\d{1,3},\\s?\\d{1,3}t?");
	public double r;
	public double g;
	public double b;
	public boolean reflectable;
	private MyColor(double r, double g, double b, boolean reflectable){
		this(r,g,b);
		this.reflectable=reflectable;
	}
	private String valueString(int v){
		String s=Integer.toHexString(v);
		if(s.length()==1){s="0"+s;}
		return s;
	}
	public String toString(){
		//return Integer.toHexString(intR())+Integer.toHexString(intG())+Integer.toHexString(intB());
		return valueString(intR())+valueString(intG())+valueString(intB());
	}
	public byte[] toByteArray(){
		String s=toString();
		int l=s.length();
		byte[] b=new byte[l];
		for(int i=0;i<l;i++){
			b[i]=(byte)s.charAt(i);
		}
		return b;
	}
	public int intR(){
		return (int)Math.floor(Math.max(Math.min(this.r,255), 0));
	}
	public int intG(){
		return (int)Math.floor(Math.max(Math.min(this.g,255), 0));
	}
	public int intB(){
		return (int)Math.floor(Math.max(Math.min(this.b,255), 0));
	}
	public MyColor(double r, double g, double b){
		this.r=r;
		this.g=g;
		this.b=b;
	}
	public MyColor(double brightness){
		this(brightness, brightness, brightness);
	}
	public MyColor(String colorString){
		Matcher m=colorStringPattern.matcher(colorString);
		if(m.find()){
			String[] vals=colorString.split(",");
			this.r=Double.parseDouble(vals[0].trim());
			this.g=Double.parseDouble(vals[1].trim());
			if(colorString.endsWith("t")){
				String trimmed=vals[2].trim();
				this.b=Double.parseDouble(trimmed.substring(0,trimmed.length()-1));
				this.reflectable=true;
			}
			else{
				this.b=Double.parseDouble(vals[2].trim());
			}
		}else{
			this.r=this.g=this.b=0;
		}
	}
	private MyColor(double brightness, boolean reflectable){
		this(brightness, brightness, brightness, reflectable);
		
	}
    public MyColor limit(double l){
        if(Math.min(this.r, Math.min(this.g, this.b))<l){return new MyColor(0);}
        return this;
    }
    public MyColor limit(){
    	if(this.reflectable){return this;}else{return new MyColor(0);}
    }
	public MyColor add(MyColor c){
		return new MyColor(this.r+c.r, this.g+c.g, this.b+c.b, this.reflectable||c.reflectable);
	}
	public MyColor multiply(MyColor c){
		return new MyColor(this.r*c.r, this.g*c.g, this.b*c.b);
	}
	public MyColor scale(double s){
		return new MyColor(this.r*s, this.g*s, this.b*s, this.reflectable);
	}
	public MyColor setReflectable(){
		this.reflectable=true;
		return this;
	}
}
