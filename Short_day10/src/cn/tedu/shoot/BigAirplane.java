package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class BigAirplane extends FlyingObject implements Score{
	//定义存储大敌机图片的静态数组
	private static BufferedImage[] images;
	//静态初始化块中，为大敌机的数组赋值
	static {
		//初始化数组长度为五
		images = new BufferedImage[5];
		images[0]=readImage("bigairplane0.png");
		for(int i=1;i<images.length;i++) {
			images[i] = readImage("bom"+i+".png");
		}
	}
	//定义大敌机属性
	private int step;
	public BigAirplane() {
		super(66,89);
		step = 2;
	}
	//重写抽象方法的速度
	public void step() {
		y +=step;
	}
	//图片获取的抽象方法重写
	int index=1;
	public BufferedImage getImage() {
		//如果大敌机活着
		if(isLife()) {
			//返回大敌机图片
			return images[0];
		}else if(isDead()) {
			//如果大敌机死了
			BufferedImage img = images[index];
			index++;
			//如果是最后一次爆炸图片
			if(index==images.length) {
				state=REMOVE;
			}
			return img;
		}
		//既不是活着也不是死了返回空
		return null;
	}
	//Score接口中的方法实现
	public int getScore() {
		// 击中大敌机得3分
		return 3;
	}

}
