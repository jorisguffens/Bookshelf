package com.gufli.bookshelf.game;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.events.EventManager;
import com.gufli.bookshelf.game.events.PlayerJoinGameTeamEvent;
import com.gufli.bookshelf.game.events.PlayerLeaveGameTeamEvent;
import com.gufli.bookshelf.game.exceptions.InvalidGameTeamException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractTeamGame<T extends GameTeam> extends AbstractGame {

    protected final Set<T> teams = new CopyOnWriteArraySet<>();

    public Set<T> getTeams() {
        return Collections.unmodifiableSet(teams);
    }

    public T getTeam(PlatformPlayer player) {
        return teams.stream().filter(team -> team.contains(player)).findFirst().orElse(null);
    }

    public void setTeam(PlatformPlayer player, T team) {
        if ( !teams.contains(team) ) {
            throw new InvalidGameTeamException("Team is not registred with this game instance.");
        }

        GameTeam previous = getTeam(player);
        if ( previous != null ) {
            previous.removePlayer(player);
            EventManager.dispatch(new PlayerLeaveGameTeamEvent(this, previous, player));
        }

        team.addPlayer(player);
        EventManager.dispatch(new PlayerJoinGameTeamEvent(this, team, player));
    }

    public void setTeam(PlatformPlayer player) {
        T team = teams.stream().min(Comparator.comparing(t -> t.getPlayers().size())).orElse(null);
        if ( team == null ) {
            return;
        }

        setTeam(player, team);
    }

    @Override
    public void removePlayer(PlatformPlayer player) {
        super.removePlayer(player);

        GameTeam team = getTeam(player);
        if ( team != null ) {
            team.removePlayer(player);
        }
    }
}
