package com.example.korbinian.strengthtester;

import java.util.*;

/**
 * Created by korbinian on 09.01.18.
 */

public class UserScoreList implements Iterable<UserScore> {

    private final List<UserScore> bList = new ArrayList<UserScore>();

    @Override
    public Iterator<UserScore> iterator() {
        return bList.iterator();
    }
}
