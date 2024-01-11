package org.glow.discordBot.entities;

import org.glow.discordBot.items.Item;

import java.util.ArrayList;
import java.util.List;

public class Bag {

    private List<Item> items;
    private int size;

    public Bag() {}

    public Bag(int size) {
        items = new ArrayList<>();
        this.size = size;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
