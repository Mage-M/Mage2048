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
		setColumnCount(4); 		//这个方法将布局规定为4列，就会生成一个4*4的正方形
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
				for(int x1 = x + 1;x1 < 4;x1 ++) {	//从当前的位置向右去遍历
					if(cardsMap[x1][y].getNum() > 0) { //如果右边某个位置有数
						
						if(cardsMap[x][y].getNum() <= 0) { //如果当前位置没有数，左移后清除右边的数
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							
							x --;
							merge = true;
						} else if(cardsMap[x][y].equals(cardsMap[x1][y])) { //如果当前位置有数且和右边的数相同，合并后清除右边的数
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
				for(int x1 = x - 1;x1 >= 0;x1 --) {	//从当前的位置向左去遍历
					if(cardsMap[x1][y].getNum() > 0) { //如果左边某个位置有数
						
						if(cardsMap[x][y].getNum() <= 0) { //如果当前位置没有数，右移后清除左边的数
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							
							x ++;
							merge = true;
						} else if(cardsMap[x][y].equals(cardsMap[x1][y])) { //如果当前位置有数且和左边的数相同，合并后清除左边的数
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
				for(int y1 = y + 1;y1 < 4;y1 ++) {	//从当前的位置向下去遍历
					if(cardsMap[x][y1].getNum() > 0) { //如果下边某个位置有数
						
						if(cardsMap[x][y].getNum() <= 0) { //如果当前位置没有数，上移后清除下边的数
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							
							y --;
							merge = true;
						} else if(cardsMap[x][y].equals(cardsMap[x][y1])) { //如果当前位置有数且和下边的数相同，合并后清除下边的数
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
				for(int y1 = y - 1;y1 >= 0;y1 --) {	//从当前的位置向上去遍历
					if(cardsMap[x][y1].getNum() > 0) { //如果上边某个位置有数
						
						if(cardsMap[x][y].getNum() <= 0) { //如果当前位置没有数，下移后清除上边的数
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							
							y ++;
							merge = true;
						} else if(cardsMap[x][y].equals(cardsMap[x][y1])) { //如果当前位置有数且和上边的数相同，合并后清除上边的数
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
