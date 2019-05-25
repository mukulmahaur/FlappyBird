package com.example.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameover;
	BitmapFont font;

	int score = 0;
	int scoringtube = 0;

	Texture[] birds;
	int flapState = 0;
	int counter = 0;
	float birdy = 0;
	float velocity = 0;
    Circle birdcircle;
    Rectangle[] toptubesrectangles;
    Rectangle[] bottomtubesrectangles;
//    ShapeRenderer shapeRenderer;

	int gameState = 0;
    Texture toptube;
    Texture bottomtube;
    float gap = 400;
    float maxtubeoffset;
    Random randomgenerator;
    int numberoftubes = 4;
    float[] tubeoffset = new float[numberoftubes];
    float tubevelocity = 4;
    float[] tubex = new float[numberoftubes];
    float distancebwtubes;

    public void startgame(){

        birdy = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

        for(int i=0;i<numberoftubes;i++) {
            tubeoffset[i] = (randomgenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubex[i] = Gdx.graphics.getWidth() - toptube.getWidth() / 2 + i * distancebwtubes;
            toptubesrectangles[i] = new Rectangle();
            bottomtubesrectangles[i] = new Rectangle();

        }
    }

	@Override
	public void create () {
		batch = new SpriteBatch();
	    background = new Texture("bg.png");

	    gameover = new Texture("gameover.png");
//        shapeRenderer = new ShapeRenderer();
        birdcircle = new Circle();
	    birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");


		toptube = new Texture("toptube.png");
        bottomtube = new Texture("bottomtube.png");
        maxtubeoffset = Gdx.graphics.getHeight()/2 - gap /2 -100;
        randomgenerator = new Random();
        distancebwtubes = Gdx.graphics.getWidth()*3/4;
        toptubesrectangles = new Rectangle[numberoftubes];
        bottomtubesrectangles = new Rectangle[numberoftubes];

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        startgame();

	}

	@Override
	public void render () {

        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState == 1){

            if(tubex[scoringtube]<Gdx.graphics.getWidth()/2){
                score++;
                Gdx.app.log("score",String.valueOf(score));
                if(scoringtube < numberoftubes-1){
                    scoringtube++;
                }
                else{
                    scoringtube = 0;
                }
            }

            if(Gdx.input.justTouched()){
//                Gdx.app.log("touched","touched");
                velocity = -20;

            };

            for(int i=0;i<numberoftubes;i++){
                if(tubex[i]<0-toptube.getWidth()){
                    tubex[i] += numberoftubes*distancebwtubes;
                    tubeoffset[i] = (randomgenerator.nextFloat()-0.5f)* (Gdx.graphics.getHeight() - gap -200);
                }else{
                    tubex[i] -= tubevelocity;
                }
                batch.draw(toptube,tubex[i],Gdx.graphics.getHeight()/2+gap/2 + tubeoffset[i]);
                batch.draw(bottomtube,tubex[i],Gdx.graphics.getHeight()/2-gap/2-bottomtube.getHeight() + tubeoffset[i]);

                toptubesrectangles[i] = new Rectangle(tubex[i],Gdx.graphics.getHeight()/2+gap/2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
                bottomtubesrectangles[i] = new Rectangle(tubex[i],Gdx.graphics.getHeight()/2-gap/2-bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());

            }

            if( birdy>0 ){
                velocity++;
                birdy -= velocity;
            }else{
                gameState = 2;
            }
		}else if(gameState == 0){
			if(Gdx.input.justTouched()){
				gameState = 1;
			};
		}else if(gameState == 2){
		    batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
		    birdy = Gdx.graphics.getHeight()/2 + 200;

		    if(Gdx.input.justTouched()){
                gameState = 1;
                startgame();
                score = 0;
                scoringtube = 0;
                velocity = 0;
            };
        }

		if(counter>=10){
			counter = 0;
		}

		if(counter < 5){
			flapState = 0;
			counter++;
		}
		else{
			flapState = 1;
			counter++;
		}


		batch.draw(birds[flapState],Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2,birdy);
        font.draw(batch,String.valueOf(score),100,200);

		batch.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
        birdcircle.set(Gdx.graphics.getWidth()/2 , birdy + birds[flapState].getHeight()/2, birds[flapState].getWidth()/2);
//        shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);
        for(int i=0;i<numberoftubes;i++){
//            shapeRenderer.rect(toptubesrectangles[i].x,toptubesrectangles[i].y,toptubesrectangles[i].width,toptubesrectangles[i].height);
//            shapeRenderer.rect(bottomtubesrectangles[i].x,bottomtubesrectangles[i].y,bottomtubesrectangles[i].width,bottomtubesrectangles[i].height);
            if(Intersector.overlaps(birdcircle,toptubesrectangles[i]) || Intersector.overlaps(birdcircle,bottomtubesrectangles[i])){
                Gdx.app.log("asdasda","asdasd");
                gameState = 2;
            }
        }
//        shapeRenderer.end();
	}
}
