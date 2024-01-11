package org.glow.discordBot.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.glow.discordBot.items.body.Body;
import org.glow.discordBot.items.handleft.HandLeft;
import org.glow.discordBot.items.handright.HandRight;
import org.glow.discordBot.items.head.Head;
import org.glow.discordBot.items.legs.Legs;
import org.glow.discordBot.items.skill.skills.Dawn;
import org.glow.discordBot.items.skill.skills.ShiningMiracle;
import org.glow.discordBot.items.usableitem.TeyvatFriedEgg;

import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Body.class, name = "Body"),
        @JsonSubTypes.Type(value = HandLeft.class, name = "HandLeft"),
        @JsonSubTypes.Type(value = HandRight.class, name = "HandRight"),
        @JsonSubTypes.Type(value = Head.class, name = "Head"),
        @JsonSubTypes.Type(value = Legs.class, name = "Legs"),

        @JsonSubTypes.Type(value = ShiningMiracle.class, name = "ShiningMiracle"),
        @JsonSubTypes.Type(value = Dawn.class, name = "Dawn"),

        @JsonSubTypes.Type(value = TeyvatFriedEgg.class, name = "TeyvatFriedEgg")
})
public abstract class Item {

    private String name;
    private int price;

    public Item() {
    }

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (price != item.price) return false;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + price;
        return result;
    }
}
