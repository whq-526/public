package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject{
	//声明一个保存天空图片的静态对象
		private static BufferedImage image;
		static {
			image = readImage("background0.png");
		}
	//定义背景
	private int step;
	private int y1;//第二张背景图的y坐标
	public Sky() {
		super(World.WIDTH,World.HEIGHT,0,0);
		step = 1;
		y1 = -World.HEIGHT;
	}
	//重写抽象方法的速度
	public void step() {
		y +=step;
		y1 +=step;
		//如果天空移出了窗口 ，将这个天空重置到窗口上方
		if(y>=World.HEIGHT) {
			y = -World.HEIGHT;
		}
		if(y1>=World.HEIGHT) {
			y1 = -World.HEIGHT;
		}
	}
	//图片获取的抽象方法重写
	public BufferedImage getImage() {
		return image;
	}
    //天空对象是一个对象要画两张图，父类中绘制方法只画了一张，在天空中要对父类绘制方法重写
	public void paintObject(Graphics g) {
		//重写的方法要绘制两张图
		g.drawImage(getImage(), x, y, null);
		g.drawImage(getImage(), x, y1,null);
	}
}
