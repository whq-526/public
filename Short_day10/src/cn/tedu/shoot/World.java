package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Wopld继承JPanel表示这个类成为了一个面板     面板就是现实内容的
public class World extends JPanel{
	//定义窗口的宽和高的常量
	public static final int WIDTH=400;
	public static final int HEIGHT=700;
	//定义状态的常量
	public static final int START=0;//开始
	public static final int RUNNING=1;//运行
	public static final int PAUSE=2;//暂停
	public static final int GAME_OVER=3;//结束
	//定义当前游戏状态
	private int state=START;//默认开始状态（与飞行物的状态无关）
	//定义三个状态下对应的图片
	private static BufferedImage startImg;
	private static BufferedImage pauseImg;
	private static BufferedImage gameoverImg;
	//再类加载时将图片加载到对象
	static {
		startImg=FlyingObject.readImage("start.png");
		pauseImg=FlyingObject.readImage("pause.png");
		gameoverImg=FlyingObject.readImage("gameover.png");
	}
	//分数属性
	private int score=0;
	Hero hero = new Hero();//仅有一架    所有程序都用
	Sky sky = new Sky();
	//定义出现多次的对象的数组**
	private FlyingObject[] enemy = {	};
	private Bullet[] bullets = {	};//因为x和y前面没有给值
	//定义游戏开始的方法   
	public void start() {
		//鼠标移动触发英雄机移动即英雄机随鼠标移动
		//鼠标监听器对象
		MouseAdapter l=new MouseAdapter() {//匿名类
			//重写MouseAdapter类中的鼠标移动方法
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING) {
					int x = e.getX();//获得鼠标x轴位置
					int y = e.getY();//获得鼠标y轴位置
					hero.moveTo(x, y);//让英雄机移动
				}
			}
			//重写鼠标单击时运行的方法
			public void mouseClicked(MouseEvent e) {
				//根据当前状态，切换成另一个状态
				switch(state) {
				//当开始状态时，切换到运行状态
				case START:
					state=RUNNING;
					break;
					//当游戏结束时，切换到开始状态
				case GAME_OVER:
					state=START;
					//游戏重新开始，数据重置
					score=0;
					sky=new Sky();
					hero=new Hero();
					enemy=new FlyingObject[0];
					bullets=new Bullet[0];
					break;
				}
			}
			//重写鼠标移出时运行的方法
			public void mouseExited(MouseEvent e) {
				//如果是运行状态   切换到暂停
				if(state==RUNNING) {
					state=PAUSE;
				}
			}
			//重写鼠标移入时运行的方法
			public void mouseEntered(MouseEvent e) {
				//如果是暂停状态切换到运行状态
				if(state==PAUSE) {
					state=RUNNING;
				}
			}
		};
		//在鼠标移动事件和鼠标滑动事件上注册这个监听器
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		//定义计时器
		Timer timer = new Timer();
		//定义运行间隔（数字越小速度越快）
		int interval = 30;
		//创建定时任务
		TimerTask task = new TimerTask() {
			public void run() {
				if(state==RUNNING) {
					moveAction();//调用移动
					enemyEnterAction();//随机的敌机进
					shootAction();//子弹进场
					outOfBoundsAction();//清除出界和爆炸的敌机 子弹
//					System.out.println(enemy.length);//检测敌机数组的变化   一直变大证明没移除  有大有小证明移除了
					hitAction();//子弹击中敌机
					heroHitAction();//英雄机碰撞
					gameOverAction();//判断英雄机命  ，游戏结束
				}
				repaint();//从新画图
			}
		};
		//启动计时器
		timer.schedule(task, interval,interval);
	}
	//定一个判断游戏结束的方法
	public void gameOverAction() {
		if(hero.getLife()<=0) {//如果英雄机生命降到0或以下  
			state=GAME_OVER;//状态改为游戏结束
		}
	}
	//英雄机和敌机碰撞方法
	public void heroHitAction() {
		//遍历所有敌机
		for(int i=0;i<enemy.length;i++) {
			FlyingObject f = enemy[i];
			if(hero.isLife() && f.isLife() && hero.hit(f)) {
				//如果撞上了  敌机死     英雄机减少命   清空火力值
				f.goDead();//敌机死
				hero.subLife();//减命
				hero.clearFire();//清空火力
			}
		}
	}
	//定义子弹击中敌机的方法
	public void hitAction() {
		//遍历所有子弹
		for(int i=0;i<bullets.length;i++) {
			//获得当前子弹
			Bullet b = bullets[i];
			//遍历敌机
			for(int j=0;j<enemy.length;j++) {
				//获得当前敌机
				FlyingObject f = enemy[j];
				//判断是否相撞（活子弹  撞击  活敌机）
				if(b.isLife() && f.isLife() && b.hit(f)) {
					//子弹和敌机都得死
					b.goDead();
					f.goDead();
					//如果击中的是得分的敌机
					if(f instanceof Score) {
						//按照游戏规则加分              强转f为Score类型
						Score s=(Score)f;
						score +=s.getScore();
					}
					//如果击中的是奖励机
					if(f instanceof Award) {
						Award a=(Award)f;
						//获得奖励机的奖励类型
						int type = a.getAward();
						//根据不同奖励值获得对应的奖励
						switch(type) {
						case Award.DOUBLE_FIRE:
							hero.addDoubleFire();
							break;
						case Award.LIFE:
							hero.addLife();
							break;
						}
					}
					//如果击中了敌机，当前子弹就不需要循环了
					break;
				}
			}
		}
	}
	//定义飞行物出界的方法
	public void outOfBoundsAction() {
		int index=0;//1.复制当前元素，2.新数组长度
		//定义新数组长度
		FlyingObject[] newarr = new FlyingObject[enemy.length];
		//遍历敌机数组
		for(int i=0;i<enemy.length;i++) {
			//获取当前敌机对象
			FlyingObject f= enemy[i];
			//判断是否出界（没出界有操作）
			if(!f.outOfBounds() && !f.isRemove()) {//注意！
				//如果没出界 并且没被移除（爆炸）
				//将当前对象放入新数组中
				newarr[index]=f;
				index++;//移除情况不进if  所以不会++
			}
		}
		//将新数组缩容后赋值给enemy
		enemy=Arrays.copyOf(newarr, index);
		//子弹出界的判断，思路和敌机相同
		index=0;//index重置归零
		//定义新数组长度
		Bullet[] bs = new Bullet[bullets.length];
		//遍历敌机数组
		for(int i=0;i<bullets.length;i++) {
			//获取当前子弹对象
			Bullet b = bullets[i];
			//判断是否出界（没出界有操作）
			if(!b.outOfBounds() && !b.isRemove()) {
				//如果没出界 并且没被移除（爆炸）
				//将当前对象放入新数组中
				bs[index]=b;
				index++;

			}
		}
		//将新数组缩容后赋值给bullets
		bullets=Arrays.copyOf(bs, index);
	}
	//定义子弹进场的方法
	int shootIndex=0;//定义一个标记控制子弹进场频率
	public void shootAction() {
		shootIndex++;
		if(shootIndex%5==0) {//控制子弹频率
			//定义一个数组接收返回值
			Bullet[] bs = hero.shoot();
			//数组扩容bs的长度
			bullets=Arrays.copyOf(bullets,bullets.length+bs.length);
			//将要进场的子弹，追加到扩容数组的尾部
			System.arraycopy(bs, 0, bullets,bullets.length-bs.length, bs.length);
		}
	}
	//定义敌机进场的方法
	int enterIndex=0;//定义一个标记控制敌机进场频率
	public void enemyEnterAction() {
		enterIndex++;
		if(enterIndex%10==0) {//取余的数越小  出现的越快 
			//获得随机产生的敌机
			FlyingObject fly=makeEnemy();
			//将现有的敌机数组扩容1
			enemy=Arrays.copyOf(enemy, enemy.length+1);
			//将生成的fly，存放到扩容后数组的最后一位
			enemy[enemy.length-1]=fly;
		}
	}
	//随机产生敌机的方法
	public FlyingObject makeEnemy() {
		FlyingObject fly=null;
		//随即生成一种敌机，赋给fly
		Random ran = new Random();
		//产生一个0～99的随机数
		int num = ran.nextInt(100);
		if(num<40) {//40%的几率产生小敌机
			fly=new Airplane();
		}else if(num<80) {//40%的几率产生大敌机
			fly=new BigAirplane();
		}else {//剩下20%产生奖励机
			fly=new Bee();
		}
		return fly;
	}
	//定义所有敌机移动的方法
	public void moveAction() {
		sky.step();
		//所有敌机移动
		for(int i=0;i<enemy.length;i++) {
			enemy[i].step();
		}
		//子弹移动
		for(int i=0;i<bullets.length;i++) {
			bullets[i].step();
		}
	}
	//这个方法是重写JPanel的  方法名固定  功能是将World类中所有对象绘制到窗口上
	public void paint(Graphics g) {
		//一定先画背景
		sky.paintObject(g);
		hero.paintObject(g);
		//循环遍历输出子弹和所有敌机
		for(int i=0;i<enemy.length;i++) {
			enemy[i].paintObject(g);
		}
		for(int i=0;i<bullets.length;i++) {
			bullets[i].paintObject(g);
		}
		//将分数和英雄机生命显示在窗体上
		g.drawString("SCORE:"+score, 10, 25);
		g.drawString("LIFE:"+hero.getLife(), 10, 45);
		//根据状态在窗体上显示图片
		switch(state) {
		case START:
			g.drawImage(startImg, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pauseImg, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameoverImg, 0, 0, null);
		}
	}
	public static void main(String[] args) {
		//实例化World类对象w  在使用w.  才可以调用start  因为start没有被static修饰
		World w = new World();
		//创建窗口     面板是在窗口里的
		//实例化窗口，标题为飞机大战
		JFrame frame = new JFrame("飞机大战");
		//将World面板放入frame窗口
		frame.add(w);
		//设定窗口大小
		frame.setSize(World.WIDTH,World.HEIGHT);//400像素，700像素
		//设置窗口关闭时，运行的程序结束
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置窗口的默认位置（居中）
		frame.setLocationRelativeTo(null);
		//显示窗口(会自动调用paint方法)
		frame.setVisible(true);
		//使用w调用start方法
		w.start();
	}

}
