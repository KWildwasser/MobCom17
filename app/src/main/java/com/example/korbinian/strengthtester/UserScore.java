package com.example.korbinian.strengthtester;

/**
 * Created by korbinian on 09.01.18.
 */


public class UserScore implements Comparable<UserScore> {



        public String userid;
        public String username;
        public String score;


    public UserScore() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public UserScore(String userid, String username, String score) {
            this.userid = userid;
            this.username = username;
            this.score = score;

        }

        public int compareTo(UserScore other) {
            if(Float.parseFloat(this.score) > Float.parseFloat(other.score)) return -1;
            if(Float.parseFloat(this.score) == Float.parseFloat(other.score)) return 0;
            return 1;

        }



}
