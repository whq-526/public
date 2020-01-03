package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Airplane extends FlyingObject implements Score{
	//定义存储小敌机图片的静态数组
	private static BufferedImage[] images;
	//静态初始化块中，为小敌机的数组赋值
	static {
		//初始化数组长度为五
		images = new BufferedImage[5];
		images[0] = readImage("airplane0.png");
//		images[1] = readImage("bom1.png");
		for(int i=1;i<images.length;i++) {
			images[i] = readImage("bom"+i+".png");
		}		
	}
	//定义小敌机的类
	//定义属性
	private int step;//速度   私有
	public Airplane() {
		super(48,50);//调用父类有参
		step = 3;//移动一次动3像素
	}
	//重写抽象方法的速度
	public void step() {
		y +=step;
	}
	//图片获取的抽象方法重写
	int index=1;//记录调用次数
	public BufferedImage getImage() {//getImage()功能类似循环
		//如果小敌机活着
		if(isLife()) {
			//如果小敌机活着返回小敌机图片
			return images[0];
		}else if(isDead()) {
			//如果小敌机死了
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
		//击中小敌机得一分
		return 1;
	}

}
