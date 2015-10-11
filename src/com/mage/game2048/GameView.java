package com.mage.game2048;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout{

	
	
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initGameView();
	}

	public GameView(Context context) {
		super(context);
		
		initGameView();
	}
	
	private void initGameView() {
		setColumnCount(4); 		//������������ֹ涨Ϊ4�У��ͻ�����һ��4*4��������
		setBackgroundColor(0xffbbada0);
		
		setOnTouchListener(new View.OnTouchListener() {

			private float startX,startY,offsetX,offsetY;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;
					
					if(Math.abs(offsetX) > Math.abs(offsetY)) {
						if(offsetX < -5) {
							swipeLeft();
						} else if(offsetX > 5) {
							swipeRight();
						}
					} else {
						if(offsetY < -5) {
							swipeUp();
						} else if(offsetY > 5) {
							swipeDown();
						}
					}
					break;
				default:
					break;
				}
				
				
				return true;
			}
			
		});
	}
	
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		int cardWidth = (Math.min(w, h) - 10) / 4;
		
		addCard(cardWidth,cardWidth);
		
		startGame();
	}
	
	private void addCard(int cardWidth, int cardHeight) {
		
		Card c;
		
		for(int y = 0;y < 4;y ++) {
			for(int x = 0;x <4;x ++) {
				c = new Card(getContext());
				c.setNum(0);
				addView(c, cardWidth, cardHeight);
				
				cardsMap[x][y] = c;
			}
		}
	}
	
	
	private void startGame() {
		
		MainActivity.getMainActivity().clearScore();
		
		for(int y = 0;y < 4;y ++) {
			for(int x = 0;x < 4;x ++) {
				cardsMap[x][y].setNum(0);
			}
		}
		
		addRandomNum();
		addRandomNum();
		
	}
	
	
	private void addRandomNum() {
		
		emptyPoints.clear();
		
		for(int y = 0;y < 4;y ++) {
			for(int x = 0;x < 4;x ++) {
				if(cardsMap[x][y].getNum() <= 0) {
					emptyPoints.add(new Point(x,y));
				}
			}
		}
		
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
		
		cardsMap[p.x][p.y].setNum(Math.random() > 0 ? 2 : 4);
	}

	private void swipeLeft() {
		
		boolean merge = false;
		
		for(int y = 0;y < 4;y ++) {
			for(int x = 0;x < 4;x ++) {	
				for(int x1 = x + 1;x1 < 4;x1 ++) {	//�ӵ�ǰ��λ������ȥ����
					if(cardsMap[x1][y].getNum() > 0) { //����ұ�ĳ��λ������
						
						if(cardsMap[x][y].getNum() <= 0) { //�����ǰλ��û���������ƺ�����ұߵ���
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							
							x --;
							merge = true;
						} else if(cardsMap[x][y].equals(cardsMap[x1][y])) { //�����ǰλ�������Һ��ұߵ�����ͬ���ϲ�������ұߵ���
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x1][y].setNum(0);
							
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					} 
				}
			}
		}
		
		if(merge) {
			addRandomNum();
		}
	}
	
	private void swipeRight() {
		
		boolean merge = false;
		
		for(int y = 0;y < 4;y ++) {
			for(int x = 3;x >= 0;x --) {	
				for(int x1 = x - 1;x1 >= 0;x1 --) {	//�ӵ�ǰ��λ������ȥ����
					if(cardsMap[x1][y].getNum() > 0) { //������ĳ��λ������
						
						if(cardsMap[x][y].getNum() <= 0) { //�����ǰλ��û���������ƺ������ߵ���
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							
							x ++;
							merge = true;
						} else if(cardsMap[x][y].equals(cardsMap[x1][y])) { //�����ǰλ�������Һ���ߵ�����ͬ���ϲ��������ߵ���
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x1][y].setNum(0);
							
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					} 
				}
			}
		}
		
		if(merge) {
			addRandomNum();
		}
	}
	
	private void swipeUp() {
		
		boolean merge = false;
		
		for(int x = 0;x < 4;x ++) {
			for(int y = 0;y < 4;y ++) {	
				for(int y1 = y + 1;y1 < 4;y1 ++) {	//�ӵ�ǰ��λ������ȥ����
					if(cardsMap[x][y1].getNum() > 0) { //����±�ĳ��λ������
						
						if(cardsMap[x][y].getNum() <= 0) { //�����ǰλ��û���������ƺ�����±ߵ���
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							
							y --;
							merge = true;
						} else if(cardsMap[x][y].equals(cardsMap[x][y1])) { //�����ǰλ�������Һ��±ߵ�����ͬ���ϲ�������±ߵ���
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x][y1].setNum(0);
							
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					} 
				}
			}
		}
		
		if(merge) {
			addRandomNum();
		}
		
	}
	
	private void swipeDown() {
		
		boolean merge = false;
		
		for(int x = 0;x < 4;x ++) {
			for(int y = 3;y >= 0;y --) {	
				for(int y1 = y - 1;y1 >= 0;y1 --) {	//�ӵ�ǰ��λ������ȥ����
					if(cardsMap[x][y1].getNum() > 0) { //����ϱ�ĳ��λ������
						
						if(cardsMap[x][y].getNum() <= 0) { //�����ǰλ��û���������ƺ�����ϱߵ���
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							
							y ++;
							merge = true;
						} else if(cardsMap[x][y].equals(cardsMap[x][y1])) { //�����ǰλ�������Һ��ϱߵ�����ͬ���ϲ�������ϱߵ���
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x][y1].setNum(0);
							
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					} 
				}
			}
		}
		
		if(merge) {
			addRandomNum();
		}
	}
	
	
	private Card[][] cardsMap = new Card[4][4];

	private List<Point> emptyPoints = new ArrayList<Point>();
}
