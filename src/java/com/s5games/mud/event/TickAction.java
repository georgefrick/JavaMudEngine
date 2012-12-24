package com.s5games.mud.event;

import com.s5games.mud.command.Script;
import com.s5games.mud.beans.Actor;

import java.util.Collection;

/**
 * Represents a timed action, given to the game, these actions will occur as specified.
 * The action occurs one more times after a specified number of seconds, it can also be
 * created to simply run.
 * User: George Frick
 * Date: Feb 15, 2008
 * Time: 11:08:32 PM
 */
public class TickAction {

    private Script script;
    private Actor target;
    private Collection<Actor> group;
    private int actionCount;
    private boolean repeat;
    private int seconds;
    private int tick;

    public TickAction() {
        repeat = false;
        seconds = 0;
        actionCount = 0;
        target = null;
        script = null;
        tick = -1;
    }

    public TickAction(Script script, Actor target, int actionCount, boolean repeat, int seconds) {
        this.script = script;
        this.target = target;
        this.actionCount = actionCount;
        this.repeat = repeat;
        this.seconds = seconds;
        tick = -1;
    }

    /**
     * The script to run.
     *
     * @return
     */
    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    /**
     * A target to pass to the script.
     *
     * @return
     */
    public Actor getTarget() {
        return target;
    }

    public void setTarget(Actor target) {
        this.target = target;
    }

    /**
     * How many times to repeat this action.
     * The action will expire when this is 0 or less.
     *
     * @return
     */
    public int getActionCount() {
        return actionCount;
    }

    public void setActionCount(int actionCount) {
        this.actionCount = actionCount;
    }

    /**
     * If this action should repeat indefinately. Will not expire.
     *
     * @return
     */
    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * How many seconds to wait before running this script.
     *
     * @return
     */
    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
     * An optional group of actors to pass to the script (group, etc)
     *
     * @return
     */
    public Collection<Actor> getGroup() {
        return group;
    }

    public void setGroup(Collection<Actor> group) {
        this.group = group;
    }

    /**
     * This method will reset the action (Start over the timer)
     */
    public void reset() {
        if (actionCount > 0) {
            actionCount--;
            tick = seconds;
        } else if( repeat ) {
            tick = seconds;
        }
    }

    /**
     * This method will start the action, until it is called, ticking
     * the action will not do anything.
     */
    public void start() {
        reset();
    }

    public boolean tick() {
        if (--tick == 0) {
            reset();
            return true;
        }
        return false;
    }

    public boolean done() {
        return (actionCount == 0 && repeat == false && tick <= 0);
    }
}
