package io.github.alyphen.amethyst.client.panel;

import io.github.alyphen.amethyst.client.AmethystClient;
import io.github.alyphen.amethyst.common.entity.EntityCharacter;
import io.github.alyphen.amethyst.common.tile.Tile;
import io.github.alyphen.amethyst.common.world.World;
import io.github.alyphen.amethyst.common.world.WorldArea;

import javax.swing.*;
import java.awt.*;

public class WorldPanel extends JPanel {

    private AmethystClient client;

    private boolean active;
    private World world;
    private WorldArea area;
    private EntityCharacter playerCharacter;

    public WorldPanel(AmethystClient client) {
        this.client = client;
    }

    public void onTick() {
        world.onTick();
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (getArea() != null && getPlayerCharacter() != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.translate(-getCameraX(), -getCameraY());
            for (int row = 0; row < area.getRows(); row++) {
                for (int col = 0; col < area.getColumns(); col++) {
                    Tile tile = getArea().getTileAt(row, col);
                    int x = col * tile.getSheet().getTileWidth();
                    int y = row * tile.getSheet().getTileHeight();
                    if (x + tile.getSheet().getTileWidth() >= getCameraX()
                            && x <= getCameraX() + getWidth()
                            && y + tile.getSheet().getTileHeight() >= getCameraY()
                            && y <= getCameraY() + getHeight())
                        tile.paint(graphics, x, y);
                }
            }
            getArea().getObjects().stream().filter(object -> object.getX() + object.getBounds().getWidth() >= getCameraX()
                    && object.getX() <= getCameraX() + getWidth()
                    && object.getY() + object.getBounds().getHeight() >= getCameraY()
                    && object.getY() <= getCameraY() + getHeight()).forEach(object -> {
                graphics2D.translate(object.getX(), object.getY());
                object.paint(graphics);
                graphics2D.translate(-object.getX(), -object.getY());
            });
            getArea().getEntities().stream().filter(entity -> entity.getX() + entity.getBounds().getWidth() >= getCameraX()
                    && entity.getX() <= getCameraX() + getWidth()
                    && entity.getY() + entity.getBounds().getHeight() >= getCameraY()
                    && entity.getY() <= getCameraY() + getHeight()).forEach(entity -> {
                graphics2D.translate(entity.getX(), entity.getY());
                entity.paint(graphics);
                graphics2D.translate(-entity.getX(), -entity.getY());
            });
            graphics2D.translate(getCameraX(), getCameraY());
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public WorldArea getArea() {
        return area;
    }

    public void setArea(WorldArea area) {
        world.addArea(area);
        if (this.area != null) world.removeArea(this.area);
        this.area = area;
    }

    public EntityCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(EntityCharacter player) {
        this.playerCharacter = player;
    }

    private int getCameraX() {
        return getPlayerCharacter().getX() - (getWidth() / 2);
    }

    private int getCameraY() {
        return getPlayerCharacter().getY() - (getHeight() / 2);
    }

}
