package Entities;

import java.util.ArrayList;
import java.util.List;
import Entities.Observer;


public class ScoreSubject {
    private int score;
    private List<Observer> observers;

    public ScoreSubject() {
        observers = new ArrayList<>();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        notifyObservers();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.onScoreUpdate(score);
        }
    }
}