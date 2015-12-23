package com.example;

public class Joker {

    public Joker() {

    }

    public String getJoke() {
        switch(randomWithRange(0, 4)) {
            case 0:
                return "Knock Knock. Olive. Olive who? Olive you!";
            case 1:
                return "A horse walks into a bar. The bartender says, “Hey.”\n" +
                        "\n" +
                        "The horse says, “You read my mind, buddy.”";
            case 2:
                return "When Chuck Norris calls 911, it’s to ask if everything is okay.";
            case 3:
                return "The worst place to have a heart attack is during a gama of cherades. " +
                        "…Especially if the people you are playing with, are really bad guessers.";
            case 4:
                return "Knock knock! Who’s there? Dwayne. Dwayne who? Dwayne the bathtub, I’m drowning.";
        }
        return "";
    }

    //http://stackoverflow.com/questions/7961788/math-random-explained
    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
}
