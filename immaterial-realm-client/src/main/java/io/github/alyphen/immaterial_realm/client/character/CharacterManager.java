package io.github.alyphen.immaterial_realm.client.character;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;
import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CharacterManager {

    private ImmaterialRealmClient client;
    private Map<Long, Character> characters = new HashMap<>();

    public CharacterManager(ImmaterialRealmClient client) {
        this.client = client;
        try {
            createCharactersTable();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createCharactersTable() throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS characters (" +
                        "player_id INTEGER," +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "gender TEXT," +
                        "race TEXT," +
                        "description TEXT," +
                        "dead BOOLEAN," +
                        "active BOOLEAN," +
                        "area_name TEXT," +
                        "x INTEGER," +
                        "y INTEGER" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Sprite getWalkUpSprite(Character character) {
        File walkUpSpriteFile = new File("./characters/" + client.getNetworkManager().getServerAddress() + "/" + character.getId() + "/walk_up.png");
        if (walkUpSpriteFile.exists()) {
            try {
                return Sprite.load(walkUpSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public Sprite getWalkDownSprite(Character character) {
        File walkDownSpriteFile = new File("./characters/" + client.getNetworkManager().getServerAddress() + "/" + character.getId() + "/walk_down.png");
        if (walkDownSpriteFile.exists()) {
            try {
                return Sprite.load(walkDownSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public Sprite getWalkLeftSprite(Character character) {
        File walkLeftSpriteFile = new File("./characters/" + client.getNetworkManager().getServerAddress() + "/" + character.getId() + "/walk_left.png");
        if (walkLeftSpriteFile.exists()) {
            try {
                return Sprite.load(walkLeftSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public Sprite getWalkRightSprite(Character character) {
        File walkRightSpriteFile = new File("./characters/" + client.getNetworkManager().getServerAddress() + "/" + character.getId() + "/walk_right.png");
        if (walkRightSpriteFile.exists()) {
            try {
                return Sprite.load(walkRightSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public Character getCharacter(long id) throws SQLException {
        Character character = characters.get(id);
        if (character != null) {
            return character;
        } else {
            Connection connection = client.getDatabaseManager().getConnection();
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM characters WHERE id = ?")) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    character = new Character(
                            resultSet.getLong("player_id"),
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("gender"),
                            resultSet.getString("race"),
                            resultSet.getString("description"),
                            resultSet.getBoolean("dead"),
                            resultSet.getBoolean("active"),
                            resultSet.getString("area_name"),
                            resultSet.getInt("x"),
                            resultSet.getInt("y")
                    );
                    characters.put(id, character);
                    return character;
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public void addCharacter(Character character) throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO characters (player_id, name, gender, race, description, dead, active, area_name, x, y) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setLong(1, character.getPlayerId());
            statement.setString(2, character.getName());
            statement.setString(3, character.getGender());
            statement.setString(4, character.getRace());
            statement.setString(5, character.getDescription());
            statement.setBoolean(6, character.isDead());
            statement.setBoolean(7, character.isActive());
            statement.setString(8, character.getAreaName());
            statement.setInt(9, character.getX());
            statement.setInt(10, character.getY());
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updateCharacter(Character character) throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE characters SET player_id = ?, name = ?, gender = ?, race = ?, description = ?, dead = ?, active = ?, area_name = ?, x = ?, y = ? WHERE id = ?")) {
            statement.setLong(1, character.getPlayerId());
            statement.setString(2, character.getName());
            statement.setString(3, character.getGender());
            statement.setString(4, character.getRace());
            statement.setString(5, character.getDescription());
            statement.setBoolean(6, character.isDead());
            statement.setBoolean(7, character.isActive());
            statement.setString(8, character.getAreaName());
            statement.setInt(9, character.getX());
            statement.setInt(10, character.getY());
            statement.setLong(11, character.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
