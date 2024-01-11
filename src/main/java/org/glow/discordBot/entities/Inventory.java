package org.glow.discordBot.entities;

import org.glow.discordBot.items.body.Body;
import org.glow.discordBot.items.body.BodyFactory;
import org.glow.discordBot.items.handleft.HandLeft;
import org.glow.discordBot.items.handleft.HandLeftFactory;
import org.glow.discordBot.items.handright.HandRight;
import org.glow.discordBot.items.handright.HandRightFactory;
import org.glow.discordBot.items.head.Head;
import org.glow.discordBot.items.head.HeadFactory;
import org.glow.discordBot.items.legs.Legs;
import org.glow.discordBot.items.legs.LegsFactory;

public class Inventory {

    private Head head;
    private Body body;
    private Legs legs;
    private HandRight handRight;
    private HandLeft handLeft;

    private Bag bag;

    public Inventory() {

        this.head = HeadFactory.INSTANCE.getEmptyHead();
        this.body = BodyFactory.INSTANCE.getEmptyBody();
        this.legs = LegsFactory.INSTANCE.getEmptyLegs();
        this.handRight = HandRightFactory.INSTANCE.getEmptyRightHand();
        this.handLeft = HandLeftFactory.INSTANCE.getEmptyHandLeft();

        this.bag = new Bag(10);

    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Legs getLegs() {
        return legs;
    }

    public void setLegs(Legs legs) {
        this.legs = legs;
    }

    public HandRight getHandRight() {
        return handRight;
    }

    public void setHandRight(HandRight handRight) {
        this.handRight = handRight;
    }

    public HandLeft getHandLeft() {
        return handLeft;
    }

    public void setHandLeft(HandLeft handLeft) {
        this.handLeft = handLeft;
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }
}
