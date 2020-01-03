package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends FlyingObject implements Award{
	//定义存储奖励机图片的静态数组
	private static BufferedImage[] images;
	//静态初始化块中，为奖励机的数组赋值
	static {
		//初始化数组长度为五
	images = new BufferedImage[5];
	images[0]=readImage("bee0.png");
	for(int i=1;i<images.length;i++) {
		images[i] = readImage("bom"+i+".png");
	}
	}
	//定义奖励机小蜜蜂
	//奖励机会横向移动
	private int xstep;//横向速度（左右）
	private int ystep;//纵向速度（上下）
	public Bee() {
		super(60,51);
		xstep =2;
		ystep =2;
	}
	//重写抽象方法的速度
	public void step() {
		y +=ystep;
		x +=xstep;
		//奖励机碰左壁或右壁    都需要修改x轴运行方法
		if(x>=World.WIDTH-this.width || x<=0) {
			xstep *= -1;
		}
	}
	//图片获取的抽象方法重写
	int index=1;
	public BufferedImage getImage() {
		//如果奖励机活着
		if(isLife()) {
			//返回奖励机图片
			return images[0];
		}else if(isDead()) {
			//如果奖励机死了
			BufferedImage img = images[index];
			index++;
			//如果最后一次爆炸图片
			if(index==images.length) {
				state=REMOVE;
			}
			return img;
		}
		//既不是活着也不是死了返回空
		return null;
	}
	//Award接口中的方法实现
	public int getAward() {
		//随机产生一个0～1的数字
		//0奖励火力值，1奖励生命值
		Random ran = new Random();
		return ran.nextInt(2);
	}

}
