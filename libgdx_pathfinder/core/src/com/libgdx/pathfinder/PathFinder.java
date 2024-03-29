package com.libgdx.pathfinder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PathFinder extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    Maze myMaze;

    @Override
    public void create() {
        batch = new SpriteBatch();
        myMaze = new Maze();

        TestComparatorList();


//		img = new Texture("tiny_16x16.png");
    }

    private void TestComparatorList() {
        List<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(10);
        list.add(6);
        list.add(2);

        Collections.sort(list, new IntegerComp());
        System.out.println("LIST ================== " + Arrays.toString(list.toArray()));
    }

    @Override
    public void render() {

        processInput();

        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        batch.begin();
//		batch.draw(img, 0, 0);
        myMaze.render(batch);
        batch.end();

    }

    private void processInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            List<Vector2int> path = (new AStar(myMaze))
                    .FindPath(new Node(myMaze.pointDepart), new Node(myMaze.pointArrivee));

            myMaze.pathFinding = path; // can be null !
        }
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (img != null) img.dispose();
    }
}
