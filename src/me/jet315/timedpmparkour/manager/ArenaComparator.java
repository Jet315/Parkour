package me.jet315.timedpmparkour.manager;

import java.util.Comparator;

/**
 * Created by Jet on 17/12/2017.
 */
public class ArenaComparator implements Comparator<Arena>{

    @Override
    public int compare(Arena o1, Arena o2) {
        return o1.getId().compareTo(o2.getId());
    }
}


