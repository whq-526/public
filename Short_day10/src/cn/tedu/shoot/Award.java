package cn.tedu.shoot;

public interface Award {
	//奖励接口 
	//表示击中该单位会获得奖励
	//定义奖励对应的数字
	int LIFE = 1;//公有静态常量
	int DOUBLE_FIRE=0;
	//定义返回奖励的方法
	int getAward();
}
