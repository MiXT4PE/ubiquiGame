package ubiquigame.games.racer.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Stack;

public class GameStateManager {

    private Stack<State> states;

    public GameStateManager(){
        states = new Stack<>();
    }

    /*
     * Pushes the state on the stack to be the active state
     * @param state the state that needs to be active next
     */
    public void push(State state) {
        states.push(state);
    }

    /*
     * Removes the state on the top of the stack
     */
    public void pop(){
        states.pop();
    }

    /*
     * Removes the state on the top of the stack
     */
    public State peek(){
        return states.peek();
    }


    /*
     * Removes the state on the top of the stack and pushes the state onto the stack
     * @param state the state that gets pushed onto the stack
     */
    public void set(State state){
        states.pop();
        states.push(state);
    }

    /*
     * Updates the active state
     * @param dt the time difference in time to the last call of this function
     */
    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb, ShapeRenderer sr){
        states.peek().render(sb, sr);
    }
}
