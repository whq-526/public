package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	//声明一个保存英雄机图片的静态数组
	private static BufferedImage[] images;
	static {
		images=new BufferedImage[2];
		images[0] = readImage("hero0.png");
		images[1] = readImage("hero1.png");
	}
	//定义英雄机     速度靠鼠标
	private int life;//生命值
	private int doubleFire;//火力值（双排炮)
	public Hero() {
		super(97,139,World.WIDTH/2-97/2,420);
		life = 3;//英雄机初始命数
		doubleFire = 0;//英雄机初始没有火力值
	}
	//英雄机随鼠标移动，step方法作空实现即可
	public void step() {		
	}
	//英雄机
	//图片获取的抽象方法重写
	int index=0;
	public BufferedImage getImage() {//getImage()这个方法是隔0.几秒就运行一次，相当于循环
		int i = index%2;
		index++;
		return images[i];//return images[index++%2];加加取余2
	}
	//英雄机开炮的方法
	public Bullet[] shoot() {
		Bullet[] bs=null;
		//定义一个英雄机的四分之一宽，，方便子弹的x轴定位
		int tx=this.width/4-2;
		int ty=20;//子弹的高度
		//根据英雄机的火力值开炮
		if(doubleFire>0) {
			//双排炮
			bs=new Bullet[2];//定义长度为2的数组
			bs[0]=new Bullet(this.x+tx, this.y-ty);//左侧子弹
			bs[1]=new Bullet(this.x+3*tx, this.y-ty);//右侧子弹
			doubleFire--;//减少火力值
		}else {
			//单排炮
			bs=new Bullet[1];//长度为一的数组 ，-----因为返回值是数组
			bs[0]=new Bullet(this.x+2*tx, this.y-ty);//单发子弹位置
		}
		return bs;
	}
	//英雄机移动的方法
	public void moveTo(int x,int y) {
		//将x看作是鼠标的x轴
		//将y看作是鼠标的y轴
		//将鼠标位置调整到英雄机的中心
		this.x=x-this.width/2;
		this.y=y-this.height/2;
	}
	//加命方法
	public void addLife() {
		life++;
	}
	//加火力方法
	public void addDoubleFire() {
		doubleFire +=40;
	}
	//返回命方法
	public int getLife() {
		return life;
	}
	//减命方法
	public void subLife() {
		life--;
	}
	//清空火力值
	public void clearFire() {
		doubleFire=0;
	}

}
