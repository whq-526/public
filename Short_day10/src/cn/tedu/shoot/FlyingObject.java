package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public abstract class FlyingObject {//改为抽象类
	//定义飞行物状态的三个**常量**
	public static final int LIFE=0;//活着的
	public static final int DEAD=1;//死了
	public static final int REMOVE=2;//消失了
	//定义当前对象的状态的属性
	protected int state=LIFE;//默认开始是活着的
	//定义所有子类共有的属性//保护类型
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	//小敌机，大敌机，奖励机使用的构造
	//原因是这三类对象的x和y轴是不需要参数的
	public FlyingObject(int width,int height) {
		Random ran = new Random();
		this.width=width;
		this.height=height;
		x=ran.nextInt(World.WIDTH-width);
		y=-height;//决定图片出现在窗口里还是窗口外，一般定为负height即窗外为初始位置
	}
	//天空，子弹，英雄机构造
	public FlyingObject(int width,int height,int x,int y) {
		this.width=width;
		this.height=height;
		this.x=x;
		this.y=y;
	} 
	//将程序中需要的图片读取到内存中的方法
	public static BufferedImage readImage(String fileName) {
		//读取图片到对象
		BufferedImage img;
		try {
			img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	//判断当前对象是活着的
	public boolean isLife() {
		return state==LIFE;
	}
	//判断当前对象是死了的
	public boolean isDead() {
		return state==DEAD;
	}
	//判断当前对象是消失的
	public boolean isRemove() {
		return state==REMOVE;
	}
	//父类中编写抽象方法step
	//所有的飞行物都会移动：移动方法
	public abstract void step();
	//这个方法时对象获得自己图片的方法
	public abstract BufferedImage getImage();
	//将一个图片绘制到窗体
	public void paintObject(Graphics g) {//---单词意思----paintObject绘制对象（画板 g）
		//在花板上绘制指定图片
		g.drawImage(getImage(),x,y,null);
	}
	//定义飞行物出界的抽象方法
	//默认定义小敌机大敌机 奖励机的出界方法
	public  boolean outOfBounds() {
		//返回真表示真出界了
		//y轴大余了窗体的高，即出界
		return y>World.HEIGHT;
	}
	//定义飞行物碰撞的方法
	//this: 子弹\英雄机       other：敌机
	public boolean hit(FlyingObject other) {
		//定义碰撞范围的四个点
		int x1=other.x-this.width;
		int x2=other.x+other.width;
		int y1=other.y-this.height;
		int y2=other.y+other.height;
		//子弹或英雄机在这四个点之内时候返回真
		return this.x>x1 && this.x<x2 && this.y>y1 && this.y<y2;
	}
	//定义一个状态修改为DEAD的方法
	public void goDead() {
		this.state=DEAD;
	}
}
