package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject{
	//声明一个保存子弹图片的静态对象
	private static BufferedImage image;
	static {
		//静态块中加载子弹图
		image = readImage("bullet.png");
	}
	//定义子弹
	private int step;
	public Bullet(int x,int y) {
		super(8,20,x,y);
		step = 4;
	}
	//重写抽象方法的速度
	public void step() {
		y -= step;
	}
	//图片获取的抽象方法重写
	public BufferedImage getImage() {
		//子弹如果活着为
		if(isLife()) {
			//返回图片
			return image;
		}else if(isDead()) {
			//如果死了直接消失
			state =REMOVE;
		}
		//消失就返回null
		return null;
	}
	//重写父类中编写的出界方法
	public  boolean outOfBounds() {
		//返回真表示真出界了
		//x轴大余子弹的负高度   即子弹完全移出窗体上方
		return y<-this.height;//this可写可不写
	}

}
